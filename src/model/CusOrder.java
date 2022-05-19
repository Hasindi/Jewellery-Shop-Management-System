package model;

public class CusOrder {
    private String orderId;
    private String orderDate;
    private String cid;

    public CusOrder() {
    }

    public CusOrder(String orderId, String orderDate, String cid) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.cid = cid;
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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    @Override
    public String toString() {
        return "CusOrder{" +
                "orderId='" + orderId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", cid='" + cid + '\'' +
                '}';
    }
}
