package org.cent.dynamicdatasourcedemo.model;

import java.math.BigDecimal;

/**
 * @author Vincent
 * @version 1.0 2019/11/24
 */
public class Order {
    private int id;
    private String oid;
    private BigDecimal amount;
    private String status;
    private String date;
    private String time;
    private String mntDate;
    private String mntTime;
    private int version;

    public Order() {
    }

    public Order(Order order) {
        this.id = order.id;
        this.oid = order.oid;
        this.amount = order.amount;
        this.status = order.status;
        this.date = order.date;
        this.time = order.time;
        this.mntDate = order.mntDate;
        this.mntTime = order.mntTime;
        this.version = order.version;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMntDate() {
        return mntDate;
    }

    public void setMntDate(String mntDate) {
        this.mntDate = mntDate;
    }

    public String getMntTime() {
        return mntTime;
    }

    public void setMntTime(String mntTime) {
        this.mntTime = mntTime;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", oid='" + oid + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", time='" + time + '\'' +
                ", mntDate='" + mntDate + '\'' +
                ", mntTime='" + mntTime + '\'' +
                ", version=" + version +
                '}';
    }
}
