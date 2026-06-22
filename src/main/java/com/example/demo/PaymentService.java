package com.example.demo;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final Tracer tracer =
            GlobalOpenTelemetry.getTracer("order-service");

    public void callPaymentManually(long amount) {

        Span span = tracer.spanBuilder(
                "dependency.payment-service.charge"
        ).startSpan();

        try (Scope scope = span.makeCurrent()) {

            span.setAttribute("peer.service", "payment-service");
            span.setAttribute("payment.amount", amount);

            // gọi payment service

        } catch (Exception e) {

            span.recordException(e);
            span.setStatus(StatusCode.ERROR);

            throw e;

        } finally {
            span.end();
        }
    }
}
