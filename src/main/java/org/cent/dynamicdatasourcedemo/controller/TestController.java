package org.cent.dynamicdatasourcedemo.controller;

import org.cent.dynamicdatasourcedemo.model.Order;
import org.cent.dynamicdatasourcedemo.service.CounterService;
import org.cent.dynamicdatasourcedemo.service.OrderService;
import org.cent.dynamicdatasourcedemo.util.RedisCounterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author Vincent
 * @version 1.0 2019/11/23
 */
@RestController
public class TestController {

    @Autowired
    private RedisCounterUtil redisCounterUtil;

    @Autowired
    private CounterService counterService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/sid/{name}")
    String getSequenceId(@PathVariable(name = "name") String name) {
        return redisCounterUtil.getId(name);
    }

    @GetMapping("/oid/{name}")
    String getOrderId(@PathVariable(name = "name") String name) {
        return counterService.getId(name);
    }

    @GetMapping("/order-info/{oid}")
    Order getOrder(@PathVariable(name = "oid") String oid) {
        return orderService.getOrder(oid);
    }

    @GetMapping("/order/{amount}")
    String order(@PathVariable(name = "amount") BigDecimal amount) {
        return orderService.order(amount);
    }

    @GetMapping("/order/{oid}/{status}")
    String pay(@PathVariable(name = "oid") String oid, @PathVariable(name = "status") String status) {
        switch (status) {
            case "1":
                return orderService.paying(oid);
            case "2":
                return orderService.paySuccess(oid);
            case "3":
                return orderService.payFailure(oid);
            case "4":
                return orderService.refund(oid);
            case "5":
                return orderService.refundSuccess(oid);
            case "6":
                return orderService.refundFailure(oid);
            default:
                throw new RuntimeException("测试状态值错误，1：下单，2：付款成功，3：付款失败，4：退款，5：退款成功，6：退款失败");
        }
    }
}
