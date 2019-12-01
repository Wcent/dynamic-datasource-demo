package org.cent.dynamicdatasourcedemo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.cent.dynamicdatasourcedemo.annotation.UseDataSource;
import org.cent.dynamicdatasourcedemo.constant.DataSourceType;
import org.cent.dynamicdatasourcedemo.model.Order;

@Mapper
public interface OrderMapper {

    @UseDataSource(type = DataSourceType.SLAVE)
    Order getOrder(String oid);

    @UseDataSource(type = DataSourceType.SLAVE)
    int addOrder(Order order);

    @UseDataSource(type = DataSourceType.SLAVE)
    int updateOrder(@Param("order") Order order, @Param("orderTemp") Order orderTemp);
}
