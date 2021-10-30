package ru.gb.antonov.j710.order.services;

import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j710.monolith.entities.dtos.OrderItemDto;
import ru.gb.antonov.j710.order.dtos.OrderDto;
import ru.gb.antonov.j710.order.entities.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.BRAND_NAME_ENG;

@Service
@RequiredArgsConstructor
public class PayPalService
{
    private final OrderService orderService;

/** Создаём запрос и прикрепляем к нему список заказов. Наш список будет состоять из одного заказа, но, теоретически, можем подшить к запросу несколько заказов. */
    @Transactional
    public OrderRequest createOrderRequest (Long orderId)
    {
        Order order = orderService.findById (orderId)
                                  .orElseThrow(()->new ResourceNotFoundException ("Заказ не найден: id: " + orderId));

    //Контекст (но не спринговый, а пэйпаловский):
        ApplicationContext applicationContext = new ApplicationContext()
                .brandName (BRAND_NAME_ENG)
                .landingPage ("BILLING")
                .shippingPreference ("SET_PROVIDED_ADDRESS");
    //Список заказов:
        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        purchaseUnitRequests.add (newPurchaseUnitRequest (order));

    //Создаём и формируем запрос:
        return new OrderRequest().checkoutPaymentIntent ("CAPTURE")
                                 .applicationContext (applicationContext)
                                 .purchaseUnits (purchaseUnitRequests);
    }

    private PurchaseUnitRequest newPurchaseUnitRequest (Order order)
    {
        String userName = orderService.userNameByUserId (order.getOuruserId());
        OrderDto odto = orderService.orderToDto (order);

        return new PurchaseUnitRequest()
                .referenceId (order.getId().toString())
                .description (BRAND_NAME_ENG + " order")
                .amountWithBreakdown (newAmountWithBreakdown (odto.getCost().doubleValue()))
                .items (oitemsToItems (odto.getOitems()))
                .shippingDetail (newShippingDetail (userName));
    }

/** (Из комментария к AmountWithBreakdown.) Общая сумма заказа с дополнительной разбивкой, которая предоставляет подробную информацию, такую как общая сумма товара, общая сумма налога, доставка, обработка, страхование и скидки, если таковые имеются. Если вы укажете {@code amount.breakdown}, то сумма будет равна<br>
{@code item_total}<br>плюс {@code tax_total}<br>плюс {@code shipping (доставка)}<br>плюс {@code handling (обработка)}<br>плюс {@code insurance (страховка)}<br>минус {@code shipping_discount (скидка на доставку)}<br>минус {@code discount (скидка)}.<br>
Сумма должна быть положительным числом. Список поддерживаемых валют и десятичной точности см. в разделе PayPal REST APIs <a href="/docs/integration/direct/rest/currency-codes/">Currency Codes</a> */
    private static AmountWithBreakdown newAmountWithBreakdown (double orderCost)
    {
        return new AmountWithBreakdown()
                    .currencyCode ("RUB")
                    .value (String.valueOf (orderCost))
                    .amountBreakdown (new AmountBreakdown().itemTotal (newMoneyRubOfSum (orderCost)));
    }

/** Перевод пунктов заказа из нашего формата в формат PayPal. */
    private static List<Item> oitemsToItems (List<OrderItemDto> oitems)
    {
        return oitems.stream()
                     .map (oitem->new Item().name (oitem.getTitle())
                                            .unitAmount (newMoneyRubOfSum (oitem.getPrice().doubleValue()))
                                            .quantity (String.valueOf (oitem.getQuantity())))
                     .collect (Collectors.toList());
    }

    private static Money newMoneyRubOfSum (double sum)
    {   return new Money().currencyCode ("RUB").value (String.valueOf (sum));
    }

/**
{@code address_line_1 }  ({@code length: ?-300}) — улица и номер дома      <p>
{@code address_line_2 }  ({@code length: ?-300}) — номер квартиры, офиса, апартоментов<p>
{@code admin_area_1   }  ({@code length: ?-300}) — регион по ISO 3166-2    <p>
{@code admin_area_2   }  ({@code length: ?-120}) — населённый пункт в регионе<p>
{@code postal_code    }  ({@code length: ?-60 }) — почтовый индекс (post code)<p>
{@code country_code   }  ({@code length: 2    }) — код страны              <p>
Ссылки: <br>
• <a href="https://developer.paypal.com/docs/api/orders/v2">https://developer.paypal.com/docs/api/orders/v2</a> — описания параметров для составления адресов;<br>
• <a href="https://ru.wikipedia.org/wiki/ISO_3166-2">https://ru.wikipedia.org/wiki/ISO_3166-2</a> — деление на регионы в различных странах;<br>
• <a href="https://ru.wikipedia.org/wiki/ISO_3166-2:RU">https://ru.wikipedia.org/wiki/ISO_3166-2:RU</a> — деление на регионы в РФ;<br>
• <a href="https://developer.paypal.com/docs/api/reference/country-codes/">https://developer.paypal.com/docs/api/reference/country-codes/</a> — двухбуквенные коды стран.<br>
*/
    private static ShippingDetail newShippingDetail (String userName)
    {
        return new ShippingDetail()
            .name (new Name().fullName (userName))
            .addressPortable (new AddressPortable()
                    .addressLine1 ("18 Dmitrovskaya St")
                    .addressLine2 ("216")
                    .adminArea2 ("Moscow")
                    .adminArea1 ("RU-MOW")
                    .postalCode ("125540")
                    .countryCode ("RU"));
    }
}
