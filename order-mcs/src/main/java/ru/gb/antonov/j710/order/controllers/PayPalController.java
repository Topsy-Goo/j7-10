package ru.gb.antonov.j710.order.controllers;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCaptureRequest;
import com.paypal.orders.OrdersCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.gb.antonov.j710.order.services.OrderService;
import ru.gb.antonov.j710.order.services.PayPalService;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/paypal")
@RequiredArgsConstructor
//@CrossOrigin ("*")
public class PayPalController {

    private final PayPalHttpClient payPalClient;
    private final OrderService     orderService;
    private final PayPalService    payPalService;

/** Платёж подготавливается. */
    @PostMapping ("/create/{orderId}")
    public ResponseEntity<?> createOrder (@PathVariable Long orderId) throws IOException  {

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer ("return=representation");
        request.requestBody (payPalService.createOrderRequest (orderId));
        HttpResponse<Order> response = payPalClient.execute (request);
        return new ResponseEntity<> (response.result().id(), HttpStatus.valueOf (response.statusCode()));
    }

/** Платёж подтверждается. */
    @PostMapping ("/capture/{payPalId}")
    public ResponseEntity<?> captureOrder (@PathVariable String payPalId) throws IOException {

        OrdersCaptureRequest request = new OrdersCaptureRequest (payPalId);
        request.requestBody (new OrderRequest());

        HttpResponse<Order> response = payPalClient.execute (request);
        Order payPalOrder = response.result();
        if ("COMPLETED".equals (payPalOrder.status())) {

            long orderId = Long.parseLong (payPalOrder.purchaseUnits().get(0).referenceId());
            orderService.setOrderStateToPayed (orderId);
            return new ResponseEntity<>("Заказ оплачен!", HttpStatus.valueOf (response.statusCode()));
        }
        return new ResponseEntity<>(payPalOrder, HttpStatus.valueOf (response.statusCode()));
    }
}