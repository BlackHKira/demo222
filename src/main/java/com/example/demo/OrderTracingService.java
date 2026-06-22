package com.example.demo;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.annotations.SpanAttribute;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OrderTracingService {

    // Xóa hoàn toàn RestClient vì chúng ta sẽ giả lập (mock) mọi thứ trong demo
    public OrderTracingService() {
    }

    @WithSpan("order.validate")
    public void validate(@SpanAttribute("order.customer_id") String customerId) {
        if (customerId == null || customerId.isBlank()) {
            throw new IllegalArgumentException("customerId is required");
        }
    }

    @WithSpan("dependency.customer-service.get-customer")
    public void getCustomer(String customerId) {
        // Lấy Span hiện tại do annotation @WithSpan tự động tạo ra
        Span span = Span.current();

        try {
            // Giả lập độ trễ mạng gọi API Customer (100ms)
            Thread.sleep(100);

            // Gắn thêm thông tin động vào Trace để lên Grafana dễ tìm kiếm
            span.setAttribute("customer.id", customerId);
            span.setAttribute("customer.found", true);

        } catch (Exception e) {
            span.recordException(e);
        }
    }

    @WithSpan("dependency.inventory-service.reserve-stock")
    public Map<String, Object> reserveStock(@SpanAttribute("order.item_id") String itemId) {
        try {
            // Giả lập độ trễ gọi Inventory Service (50ms)
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Span.current().recordException(e);
        }

        // Trả về dữ liệu ảo thành công
        return Map.of("itemId", itemId, "status", "RESERVED");
    }

    @WithSpan("dependency.payment-service.charge")
    public Map<String, Object> chargePayment(
            @SpanAttribute("payment.amount") long amount,
            @SpanAttribute("payment.currency") String currency
    ) {
        try {
            // Giả lập độ trễ gọi Payment Service (200ms)
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Span.current().recordException(e);
        }

        // Trả về dữ liệu ảo thành công
        return Map.of("transactionId", "TXN-9999", "status", "SUCCESS");
    }

    @WithSpan("order.persist")
    public String persistOrder(
            @SpanAttribute("order.total_amount") long amount,
            @SpanAttribute("db.system") String dbSystem
    ) {
        try {
            // Giả lập thời gian lưu xuống Database (20ms)
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Span.current().recordException(e);
        }
        return "ORD-001";
    }

    @WithSpan("order.publish-created-event")
    public void publishCreatedEvent(@SpanAttribute("messaging.destination.name") String topic) {
        try {
            // Giả lập thời gian đẩy message vào Kafka (10ms)
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Span.current().recordException(e);
        }
    }
}