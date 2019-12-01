package org.cent.dynamicdatasourcedemo.service;

import org.cent.dynamicdatasourcedemo.model.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface OrderService {

    Order getOrder(String oid);
    String order(BigDecimal amount);
    String paying(String oid);
    String paySuccess(String oid);
    String payFailure(String oid);
    String refund(String oid);
    String refundSuccess(String oid);
    String refundFailure(String oid);
}
