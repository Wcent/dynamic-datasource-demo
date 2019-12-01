package org.cent.dynamicdatasourcedemo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * redis计数器工具类
 * 自增计数器，可用于生成订单号
 * @author Vincent
 * @version 1.0 2019/11/24
 */
@Component
public class RedisCounterUtil {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public String getId(String name) {

        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
        String key = (name == null ? ("sid:"+ date) : (name + ":" + date));
        // key带日期，新的一天没有value时返回1，每天重置
        long id = stringRedisTemplate.opsForValue().increment(key);
        // 新key-value，设置1天有效期，过去自动清理
        if (id <= 1) {
            stringRedisTemplate.expire(key, 1, TimeUnit.DAYS);
            if (id == 0) {
                id = stringRedisTemplate.opsForValue().increment(key);
            }
        }
        // 日期+时间+计数值组成，计数值10位长，不足补前导0
        return date + time +String.format("%010d", id);
    }
}
