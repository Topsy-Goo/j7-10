package ru.gb.antonov.j710.order.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.entities.dtos.*;
import ru.gb.antonov.j710.order.entities.Order;
import ru.gb.antonov.j710.order.entities.OrderItem;
import ru.gb.antonov.j710.order.entities.OrderState;
import ru.gb.antonov.j710.order.integration.OrderToCartCallService;
import ru.gb.antonov.j710.order.integration.OrderToOurUserCallService;
import ru.gb.antonov.j710.order.integration.OrderToProductCallService;
import ru.gb.antonov.j710.order.repositos.OrderItemRepo;
import ru.gb.antonov.j710.order.repositos.OrdersRepo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.orderCreationTimeToString;

@Service
@RequiredArgsConstructor
public class OrderService
{
    private final OrdersRepo                ordersRepo;
    private final OrderToOurUserCallService orderToOurUserCallService;
    private final OrderToProductCallService orderToProductCallService;
    private final OrderToCartCallService    orderToCartCallService;
    private final OrderStatesService        orderStatesService;
    private final OrderItemRepo             orderItemRepo;

//---------------------------------------------------------------------------------------
    @Transactional
    public OrderDetalesDto getOrderDetales (String login)
    {
        CartDto dryCartDto = orderToCartCallService.getDryCartDto (login);
        OrderDetalesDto odt = new OrderDetalesDto();
        odt.setCartDto (dryCartDto);
        return odt;
    }

/** <p>Вызывается из {@code OrderController} при получении запроса на оформление заказа (юзер ознакомился
    со сфорсированным для него списком товарных позиций, заполнил
    форму для оформления заказа и нажал кнопку подтверждения). </p>

    <p>Всё время на странице заказа показываем юзеру товары, которые он заказал. Мы должны вернуть в
    {@code OrderDetalesDto} ту же {@code OrderDetalesDto}, но с дозаполненными полями, чтобы показать
    юзеру детали оформленного заказа.</p>

    @param detales содержит всю необходимую информацию для оформления заказа, включая «сухую» корзину.

    @return та же {@code OrderDetalesDto}, но с дозаполненными полями.
*/
    @Transactional
    public OrderDetalesDto applyOrderDetails (@NotNull OrderDetalesDto detales, String username)
    {
        CartDto cartDto   = detales.getCartDto();
        Long ouruserId    = orderToOurUserCallService.userIdByLogin (username);
        OrderState oState = orderStatesService.getOrderStatePending();

        Order o = new Order();
        o.setState (oState);
        o.setOuruserId (ouruserId);
        o.setPhone (detales.getPhone());
        o.setAddress (detales.getAddress());
        o.setOrderItems (cartDto.getOitems()
                                .stream()
                                .map ((dto)->orderItemFromDto (o, dto))
                                .collect (Collectors.toList()));
        o.setCost (cartDto.getCost());
        ordersRepo.save (o);

        detales.setOrderNumber (o.getId());
        detales.setOrderState (oState.getFriendlyName());
        detales.setOrderCreationTime (orderCreationTimeToString (o.getCreatedAt()));
        //detales.setDeliveryType ("Самовывоз");  TODO: сделать ниспадающий список на стр.оформления заказа.
        //detales.setDeliveryCost (0.0);        //TODO: поменять на чтение стоимости выбранной доставки.
        detales.setOverallCost (o.getCost() + detales.getDeliveryCost());

        //(Оставляем dryCart в OrderDetalesDto, чтобы юзер мог на неё посмотреть перед уходом со
        // страницы заказа.)
        //Удаляем из корзины юзера НЕпустые позиции, — они были перенесены в dryCart. Пустые —
        // оставляем, чтобы юзер их сам удалил, если захочет:
        orderToCartCallService./*clearCart*/removeNonEmptyItems (username);
        return detales;
    }

    private OrderItem orderItemFromDto (Order o, OrderItemDto dto)
    {
        if (o == null || dto == null)
            throw new BadCreationParameterException ("orderItemFromDto(): не удалось сформировать строку заказа.");

        OrderItem oi = new OrderItem();
        oi.setOrder(o);
        oi.setProductId (dto.getProductId());
        oi.setBuyingPrice (dto.getPrice());
        oi.setQuantity (dto.getQuantity());
        return oi;
    }

    @Transactional
    public Collection<OrderDto> getUserOrdersAsOrderDtos (String login)
    {
        Long ouruserId = orderToOurUserCallService.userIdByLogin (login);
        List<Order> orders = ordersRepo.findAllByOuruserId (ouruserId);
        Collection<OrderDto> list = new ArrayList<>((orders != null) ? orders.size() : 0);

        if (orders != null)
        for (Order o : orders)
        {
            list.add (orderToDto (o));
        }
        return list;
    }

    private OrderDto orderToDto (Order o)
    {
        if (o == null)
            throw new BadCreationParameterException ("Не удалось прочитать инф-цию о заказе.");

        OrderDto odto = new OrderDto();
        int[] oitemLoad = {0};  //< накопительный счётчик товаров в заказе

        odto.setOrderNumber (o.getId());
        odto.setState       (o.getState().getFriendlyName());
        odto.setAddress     (o.getAddress());
        odto.setPhone       (o.getPhone());
        odto.setCost        (o.getCost());
        odto.setOitems      (o.getOrderItems()
                              .stream()
                              .map ((oi)->orderItemToDto (oi, oitemLoad))
                              .collect (Collectors.toList()));
        odto.setLoad        (oitemLoad[0]);
        return odto;
    }

    private OrderItemDto orderItemToDto (OrderItem oi, int[] oitemLoad)
    {
        if (oi == null || oitemLoad == null || oitemLoad.length == 0)
            throw new BadCreationParameterException ("Не удалось прочитать инф-цию об элементе заказа.");

        OrderItemDto oidto      = new OrderItemDto();
        double       price      = oi.getBuyingPrice();
        int          quantity   = oi.getQuantity();
        ProductDto   productDto = orderToProductCallService.getProductById(oi.getProductId());

        oidto.setProductId (productDto.getProductId());
        oidto.setCategory  (productDto.getCategory());
        oidto.setTitle     (productDto.getTitle());
        oidto.setQuantity  (quantity);
        oidto.setPrice     (price);
        oidto.setCost      (price * quantity);
        oitemLoad[0] += quantity;
        return oidto;
    }

    public List<OrderItem> userOrderItemsByProductId (Long uid, Long pid, Integer stateId)
    {   return orderItemRepo.userOrderItemsByProductId (uid, pid, stateId);
    }
}
