package org.cent.dynamicdatasourcedemo.service.impl;

import org.cent.dynamicdatasourcedemo.mapper.OrderMapper;
import org.cent.dynamicdatasourcedemo.model.Order;
import org.cent.dynamicdatasourcedemo.service.CounterService;
import org.cent.dynamicdatasourcedemo.service.OrderService;
import org.cent.dynamicdatasourcedemo.util.RedisCounterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Vincent
 * @version 1.0 2019/11/24
 */
@Repository
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RedisCounterUtil redisCounterUtil;

    @Autowired
    private CounterService counterService;

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public Order getOrder(String oid) {

        Order order = orderMapper.getOrder(oid);
        if (order == null) {
            throw new RuntimeException(String.format("订单[%s]不存在", oid));
        }
        return order;
    }

    @Override
    public String order(BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) == 0 || amount.compareTo(BigDecimal.ZERO)<0) {
            throw new RuntimeException("订单金额必须大于0");
        }

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String oid = redisCounterUtil.getId("order_id");
//        String oid = counterService.getId("order_id");
        Order order = new Order();
        order.setOid(oid);
        order.setAmount(amount);
        // 设置订单发起初始状态，0：待付款
        order.setStatus("0");
        order.setDate(date);
        order.setTime(time);
        order.setMntDate(date);
        order.setMntTime(time);
        // 下单处理
        if (orderMapper.addOrder(order) <= 0) {
            throw new RuntimeException("下单失败");
        }
        return String.format("下单成功，订单号：%s", oid);
    }

    @Override
    public String paying(String oid) {

        // 订单付款状态流转，0：待付款 -> 1：付款中
        if (!update(oid, "0", "1")) {
            throw new RuntimeException(String.format("订单[%s]付款失败，状态已变，请查证后处理", oid));
        }
        return String.format("订单[%s]付款处理中", oid);
    }

    @Override
    public String paySuccess(String oid) {

        // 订单付款成功状态流转，1：付款中 -> 2：付款成功
        if (!update(oid, "1", "2")) {
            throw new RuntimeException(String.format("订单[%s]付款失败，状态已变，请查证后处理", oid));
        }
        return String.format("订单[%s]付款成功", oid);
    }

    @Override
    public String payFailure(String oid) {

        // 订单付款失败状态流转，1：付款中 -> 3：付款失败
        if (!update(oid, "1", "3")) {
            throw new RuntimeException(String.format("订单[%s]付款失败，状态已变，请查证后处理", oid));
        }
        return String.format("订单[%s]付款失败", oid);
    }

    @Override
    public String refund(String oid) {

        // 订单退款状态流转，2：付款成功 -> 4：退款中
        if (!update(oid, "2", "4")) {
            throw new RuntimeException(String.format("订单[%s]退款失败，状态已变，请查证后处理", oid));
        }
        return String.format("订单[%s]退款处理中", oid);
    }

    @Override
    public String refundSuccess(String oid) {

        // 订单退款成功状态流转，4：退款中 -> 5：退款成功
        if (!update(oid, "4", "5")) {
            throw new RuntimeException(String.format("订单[%s]退款失败，状态已变，请查证后处理", oid));
        }
        return String.format("订单[%s]退款成功", oid);
    }

    @Override
    public String refundFailure(String oid) {

        // 订单退款失败状态流转，4：退款中 -> 2：付款成功，注意退款失败状态恢复为付款成功
        if (!update(oid, "4", "2")) {
            throw new RuntimeException(String.format("订单[%s]退款失败，状态已变，请查证后处理", oid));
        }
        return String.format("订单[%s]退款失败", oid);
    }

    private boolean update(String oid, String statusBefore, String statusAfter) {

        // 当前日期时间
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        // 查找订单信息
        Order order = orderMapper.getOrder(oid);
        if (order == null) {
            throw new RuntimeException(String.format("订单[%s]不存在", oid));
        } else if (!order.getStatus().equals(statusBefore)) {
            throw new RuntimeException(String.format("订单[%s]状态不对，请查证后处理", oid));
        }
        // 暂存更新前订单信息
        Order orderBefore = new Order(order);
        // 更新订单处理状态，0：待付款，1：付款中，2：付款成功，3：付款失败，4：退款中，5：退款成功
        order.setStatus(statusAfter);
        order.setMntDate(date);
        order.setMntTime(time);
        order.setVersion(order.getVersion()+1);
        // 带条件sql更新语句作并发控制，条件不满足未找到记录更新，数据库不会抛出异常
        return orderMapper.updateOrder(order, orderBefore) > 0;
    }
}
