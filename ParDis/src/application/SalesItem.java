package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SalesItem {

    private final StringProperty item;
    private final StringProperty quantity;
    private final StringProperty total;

    public SalesItem(String item, String quantity, String total) {
        this.item = new SimpleStringProperty(item);
        this.quantity = new SimpleStringProperty(quantity);
        this.total = new SimpleStringProperty(total);
    }

    public String getItem() {
        return item.get();
    }

    public StringProperty itemProperty() {
        return item;
    }

    public String getQuantity() {
        return quantity.get();
    }

    public StringProperty quantityProperty() {
        return quantity;
    }

    public String getTotal() {
        return total.get();
    }

    public StringProperty totalProperty() {
        return total;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public void setTotal(String total) {
        this.total.set(total);
    }
}
