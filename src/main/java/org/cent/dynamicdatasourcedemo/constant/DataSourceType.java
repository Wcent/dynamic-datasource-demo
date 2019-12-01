package org.cent.dynamicdatasourcedemo.constant;

/**
 * 数据源枚举类，指定数据源类型
 * @author Vincent
 * @version 1.0 2019/12/1
 */
public enum DataSourceType {
    MASTER("master"),
    SLAVE("slave");

    private String type;

    DataSourceType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "DataSourceType{" +
                "type='" + type + '\'' +
                '}';
    }
}
