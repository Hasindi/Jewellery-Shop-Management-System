package model;

public class SupOrder {
    private String orderId;
    private String orderDate;
    private String sid;

    public SupOrder() {
    }

    public SupOrder(String orderId, String orderDate, String sid) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.sid = sid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "SupOrder{" +
                "orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}
