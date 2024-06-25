package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeliveryItems {

    private final StringProperty item;
    private final StringProperty quantity;
    private final StringProperty unit;
    private final StringProperty price;

    public DeliveryItems(String item, String quantity, String unit, String price) {
        this.item = new SimpleStringProperty(item);
        this.quantity = new SimpleStringProperty(quantity);
        this.unit = new SimpleStringProperty(unit);
        this.price = new SimpleStringProperty(price);
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

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public String getPrice() {
        return price.get();
    }

    public StringProperty priceProperty() {
        return price;
    }
	
	public void setQuantity(String newValue) {
	    quantity.set(newValue);
	}
	
}
