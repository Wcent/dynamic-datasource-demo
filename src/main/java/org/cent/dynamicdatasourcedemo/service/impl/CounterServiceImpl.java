package org.cent.dynamicdatasourcedemo.service.impl;

import org.cent.dynamicdatasourcedemo.annotation.UseDataSource;
import org.cent.dynamicdatasourcedemo.constant.DataSourceType;
import org.cent.dynamicdatasourcedemo.mapper.CounterMapper;
import org.cent.dynamicdatasourcedemo.model.Counter;
import org.cent.dynamicdatasourcedemo.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Vincent
 * @version 1.0 2019/11/23
 */
@Repository
public class CounterServiceImpl implements CounterService {

    @Autowired
    private CounterMapper counterMapper;

    /**
     * 计数器生成24位id号
     * 日期+时间+计数值，计数值每日累计，切日重置
     * @param name 计数器名称
     * @return 24位长的id号
     */
    @Override
    public String getId(String name) {
        String id = null;
        LocalDateTime now;
        String date;
        String time;
        int rows;

        for (int i = 0; i < 10; i++) {
            // 当前日期、时间
            now = LocalDateTime.now();
            date = now.toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            time = now.toLocalTime().format(DateTimeFormatter.ofPattern("HHmmss"));

            // 当前计数值
            Counter counter = counterMapper.getCounter(name);
            if (counter == null) {
                throw new RuntimeException(String.format("Counter named '%s' not exist!", name));
            }

            // 同日累计，切日重置
            if (date.equals(counter.getDate())) {
                // sql条件自增
                rows = counterMapper.count(counter);
                // 自增更新成功时，前值可用, sql条件更新失败不会抛异常，更新记录数0
                if (rows > 0) {
                    id = date + time + String.format("%010d", counter.getValue());
                    return id;
                }
            } else {
                // 切日，sql条件更新日期及计数值重置2
                counter.setDate(date);
                rows = counterMapper.recount(counter);
                // 重置2成功，则前值1可用, sql条件更新失败不会抛异常，更新记录数0
                if (rows > 0) {
                    id = date + time + String.format("%010d", 1);
                    return id;
                }
            }
        }
        return id;
    }
}
