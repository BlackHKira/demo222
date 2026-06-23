package com.example.demo;

import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @WithSpan("orders.validate-request")
    public void validate(
            @SpanAttribute("order.customer_id")
            String customerId) {

        // validate input
    }

    @WithSpan("orders.calculate-price")
    public long calculatePrice(
            @SpanAttribute("order.item_count")
            int itemCount) {

        return itemCount * 60000L;
    }

    @WithSpan("orders.persist")
    public String persist(
            @SpanAttribute("order.total_amount")
            long amount) {

        return "ORD-001";
    }

    @Transactional
    public Order createOrder(OrderRequest request) {

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());

        return order;
    }
}