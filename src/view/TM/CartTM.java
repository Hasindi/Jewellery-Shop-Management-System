package view.TM;

import javafx.scene.control.Button;

public class CartTM {
    private String id;
    private String stockItem;
    private String description;
    private int qty;
    private double unitPrice;
    private double totalCost;
    private Button btn;

    public CartTM() {
    }

    public CartTM(String id, String stockItem, String description, int qty, double unitPrice, double totalCost, Button btn) {
        this.id = id;
        this.stockItem = stockItem;
        this.description = description;
        this.qty = qty;
        this.unitPrice = unitPrice;
        this.totalCost = totalCost;
        this.btn = btn;
    }

    public String getId() {
        return id;
    }

    public void setId(String sId) {
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

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "CartTM{" +
                "id='" + id + '\'' +
                ", stockItem='" + stockItem + '\'' +
                ", description='" + description + '\'' +
                ", qty=" + qty +
                ", unitPrice=" + unitPrice +
                ", totalCost=" + totalCost +
                ", btn=" + btn +
                '}';
    }
}
