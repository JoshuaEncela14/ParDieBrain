package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class InventoryItem {
    private final StringProperty item;
    private final StringProperty deliveredQuantity;
    private final StringProperty soldQuantity;
    private final StringProperty remainingQuantity;
    private final StringProperty total;

    public InventoryItem(String item, String deliveredQuantity, String soldQuantity, String remainingQuantity, String total) {
        this.item = new SimpleStringProperty(item);
        this.deliveredQuantity = new SimpleStringProperty(deliveredQuantity);
        this.soldQuantity = new SimpleStringProperty(soldQuantity);
        this.remainingQuantity = new SimpleStringProperty(remainingQuantity);
        this.total = new SimpleStringProperty(total);
    }

    public String getItem() {
        return item.get();
    }

    public String getDeliveredQuantity() {
        return deliveredQuantity.get();
    }

    public String getSoldQuantity() {
        return soldQuantity.get();
    }

    public String getRemainingQuantity() {
        return remainingQuantity.get();
    }

    public String getTotal() {
        return total.get();
    }

    public void setItem(String item) {
        this.item.set(item);
    }

    public void setDeliveredQuantity(String deliveredQuantity) {
        this.deliveredQuantity.set(deliveredQuantity);
    }

    public void setSoldQuantity(String soldQuantity) {
        this.soldQuantity.set(soldQuantity);
    }

    public void setRemainingQuantity(String remainingQuantity) {
        this.remainingQuantity.set(remainingQuantity);
    }

    public void setTotal(String total) {
        this.total.set(total);
    }
}
