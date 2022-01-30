package ru.gb.antonov.j710.order.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.monolith.entities.dtos.CartDto;
import ru.gb.antonov.j710.monolith.entities.dtos.OrderItemDto;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;
import ru.gb.antonov.j710.order.dtos.OrderDetalesDto;
import ru.gb.antonov.j710.order.dtos.OrderDto;
import ru.gb.antonov.j710.order.dtos.ShippingInfoDto;
import ru.gb.antonov.j710.order.entities.*;
import ru.gb.antonov.j710.order.integration.OrderToCartCallService;
import ru.gb.antonov.j710.order.integration.OrderToOurUserCallService;
import ru.gb.antonov.j710.order.integration.OrderToProductCallService;
import ru.gb.antonov.j710.order.repositos.OrderItemRepo;
import ru.gb.antonov.j710.order.repositos.OrdersRepo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.ORDER_IS_EMPTY;
import static ru.gb.antonov.j710.monolith.Factory.orderCreationTimeToString;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrdersRepo       ordersRepo;
    private final OrderItemRepo    orderItemRepo;
    private final OrderToOurUserCallService orderToOurUserCallService;
    private final OrderToProductCallService orderToProductCallService;
    private final OrderToCartCallService    orderToCartCallService;
    private final OrderStatesService        orderStatesService;
//---------------------------------------------------------------------------------------
    @Transactional
    public OrderDetalesDto getOrderDetales (String login) {

        CartDto dryCartDto = orderToCartCallService.getDryCartDto (login);

        if (dryCartDto.getTitlesCount() <= 0)
            throw new BadCreationParameterException (ORDER_IS_EMPTY);

        OrderDetalesDto odt = new OrderDetalesDto();
        odt.setCartDto (dryCartDto);
        odt.setShippingInfoDto (new ShippingInfoDto());
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
    public OrderDetalesDto applyOrderDetails (@NotNull OrderDetalesDto detales, String username)  {

        CartDto cartDto   = detales.getCartDto();
        if (cartDto.getTitlesCount() <= 0)
            throw new BadCreationParameterException (ORDER_IS_EMPTY);

        Long ouruserId    = orderToOurUserCallService.userIdByLogin (username);
        OrderState oState = orderStatesService.getOrderStatePending();
        ShippingInfo shippingInfo = ShippingInfo.fromShippingInfoDto (detales.getShippingInfoDto()).adjust();

        Order o = new Order();
        List<OrderItem> oitems = cartDto.getOitems()
                                .stream()
                                .map ((dto)->orderItemFromDto (o, dto))
                                .collect (Collectors.toList());
        o.setOrderItems (oitems);
        o.setAllItemsCost (cartDto.getCost());
        o.setOuruserId (ouruserId);
        o.setState (oState);
        o.setShippingInfo (shippingInfo);
        ordersRepo.save (o);

        detales.setOrderNumber (o.getId());
        detales.setOrderState (oState.getFriendlyName());
        detales.setOrderCreationTime (orderCreationTimeToString (o.getCreatedAt()));

    //(Оставляем dryCart в OrderDetalesDto, чтобы юзер мог на неё посмотреть перед уходом со страницы заказа.)
    //Удаляем из корзины юзера НЕпустые позиции, — они были перенесены в dryCart. Пустые — оставляем, чтобы юзер их сам удалил, если захочет.
        orderToCartCallService.removeNonEmptyItems (username);
        return detales;
    }

    private OrderItem orderItemFromDto (Order o, OrderItemDto dto)  {

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
    public Collection<OrderDto> getUserOrdersAsOrderDtos (String login) {

        Long ouruserId = orderToOurUserCallService.userIdByLogin (login);
        List<Order> orders = ordersRepo.findAllByOuruserId (ouruserId);
        Collection<OrderDto> list = new ArrayList<>((orders != null) ? orders.size() : 0);

        if (orders != null)
        for (Order o : orders)
            list.add (orderToDto (o));
        return list;
    }

/** Составляем DTO-шку для сделанного ранее заказа. Используется в лином кабинете пользователя
 для демонстрации пользователю списка его заказов. */
    public OrderDto orderToDto (Order o) {

        if (o == null)
            throw new BadCreationParameterException ("Не удалось прочитать инф-цию о заказе.");

        OrderDto odto = new OrderDto();
        int[] oitemLoad = {0};  //< накопительный счётчик товаров в заказе

        odto.setOrderNumber (o.getId());
        odto.setState       (o.getState().getFriendlyName());
        odto.setAddress     (o.getShippingInfo().getAddress());
        odto.setPhone       (o.getShippingInfo().getPhone());
        odto.setCost        (o.getAllItemsCost());
        odto.setOitems      (o.getOrderItems()
                              .stream()
                              .map ((oi)->orderItemToDto (oi, oitemLoad))
                              .collect (Collectors.toList()));
        odto.setLoad        (oitemLoad[0]);
        return odto;
    }

    private OrderItemDto orderItemToDto (OrderItem oi, int[] oitemLoad) {

        if (oi == null || oitemLoad == null || oitemLoad.length == 0)
            throw new BadCreationParameterException ("Не удалось прочитать инф-цию об элементе заказа.");

        OrderItemDto oidto    = new OrderItemDto();
        BigDecimal   price    = oi.getBuyingPrice();
        int          quantity = oi.getQuantity();
        ProductDto   productDto = orderToProductCallService.getProductById (oi.getProductId());

        oidto.setProductId (productDto.getProductId());
        oidto.setCategory  (productDto.getCategory());
        oidto.setTitle     (productDto.getTitle());
        oidto.setQuantity  (quantity);
        oidto.setPrice     (price);
        oidto.setCost      (price.multiply (BigDecimal.valueOf(quantity)));
        oidto.setMeasure   (productDto.getMeasure());
        oitemLoad[0] += quantity;
        return oidto;
    }

    public List<OrderItem> userOrderItemsByProductId (Long uid, Long pid, Integer stateId) {
        return orderItemRepo.userOrderItemsByProductId (uid, pid, stateId);
    }
//------------------ Для PayPal ---------------------------------------------------------

    public Optional<Order> findById (@NotNull Long orderId) {
        return ordersRepo.findById (orderId);
    }

    public String userNameByUserId (@NotNull Long uid) {
        return orderToOurUserCallService.userNameByUserId (uid);
    }

    @Transactional
    public void setOrderStateToPayed (@NotNull Long orderId) {
        findById (orderId).ifPresent (o->o.setState (orderStatesService.getOrderStatePayed()));
    }
}