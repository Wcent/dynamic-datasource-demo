package org.cent.dynamicdatasourcedemo.model;

/**
 * @author Vincent
 * @version 1.0 2019/11/23
 */
public class Counter {
    private int id;
    private String name;
    private int value;
    private String date;
    private int version;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", value=" + value +
                ", date='" + date + '\'' +
                ", version=" + version +
                '}';
    }
}
