package model;

public class Stock {
    private String id;
    private String stockItem;
    private String description ;
    private int qty;
    private double unitPrice;

    public Stock() {
    }

    public Stock(String id, String stockItem, String description, int qty, double unitPrice) {
        this.id = id;
        this.stockItem = stockItem;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStockItem() {
        return stockItem;
    }

    public void setStockItem(String stockItem) {
        this.stockItem = stockItem;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id='" + id + '\'' +
                ", stockItem='" + stockItem + '\'' +
                ", description='" + description + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
