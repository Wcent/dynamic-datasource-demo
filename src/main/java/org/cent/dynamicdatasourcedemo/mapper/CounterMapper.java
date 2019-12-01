package org.cent.dynamicdatasourcedemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.cent.dynamicdatasourcedemo.annotation.UseDataSource;
import org.cent.dynamicdatasourcedemo.constant.DataSourceType;
import org.cent.dynamicdatasourcedemo.model.Counter;

@Mapper
public interface CounterMapper {
    @UseDataSource(type = DataSourceType.MASTER)
    Counter getCounter(String name);

    @UseDataSource
    int count(Counter counter);

    @UseDataSource
    int recount(Counter counter);
}
