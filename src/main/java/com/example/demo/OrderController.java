package com.example.demo;

import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderTracingService tracingService;
    private final OrderService orderService;

    public OrderController(
            OrderTracingService tracingService,
            OrderService orderService) {

        this.tracingService = tracingService;
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public Order createOrder(@RequestBody OrderRequest request) {
        return orderService.createOrder(request);
    }

    @PostMapping
    public Map<String, Object> create(
            @RequestBody Map<String, Object> body) {

        String customerId = String.valueOf(body.get("customerId"));
        String itemId = String.valueOf(body.get("itemId"));

        orderService.validate(customerId);

        long amount = orderService.calculatePrice(2);

        String orderId = orderService.persist(amount);

        tracingService.getCustomer(customerId);
        tracingService.reserveStock(itemId);
        tracingService.chargePayment(amount, "VND");
        tracingService.publishCreatedEvent("order.created");

        return Map.of(
                "orderId", orderId,
                "amount", amount,
                "status", "created"
        );
    }
}