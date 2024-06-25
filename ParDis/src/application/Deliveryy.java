package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Deliveryy extends Application {

    Stage window;
    TableView<DeliveryItems> table;
    ObservableList<DeliveryItems> data;
    String[] itemNumbers;
    static double[] itemPrices;
    String[] items;
    String[] quantities;
    String[] units;
    String[] prices;
    double[] totals;
    int currentIndex = 0;
    int codeIndex = 101;

    public static void main(String[] args) {
        launch(args);
    }

    private final Object uiLock = new Object();
	private ObservableList<DeliveryItems> inventoryData;

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("DELIVERIES FOR ABC COMPANY");

        // Initialize arrays
        itemNumbers = new String[100];
        itemPrices = new double[100];
        items = new String[100];
        quantities = new String[100];
        units = new String[100];
        prices = new String[100];
        totals = new double[100];

        // GridPane
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        // Create UI elements in a separate thread
        Thread uiThread = new Thread(() -> {
            synchronized (uiLock) {
                Platform.runLater(() -> createUI(grid));
            }
        });
        uiThread.setDaemon(true);
        uiThread.start();

        Scene scene = new Scene(grid, 1200, 750);
        scene.getStylesheets().add("design.css");
        window.setScene(scene);
        window.show();
    }

    private void createUI(GridPane grid) {
        // Code Label
        Label codeLabel = new Label("Code: ");
        GridPane.setConstraints(codeLabel, 0, 0);

        // Code Input
        TextField codeInput = new TextField();
        codeInput.setText(String.valueOf(codeIndex));
        codeInput.setEditable(false);
        GridPane.setConstraints(codeInput, 0, 1);
        codeInput.setPrefHeight(35);
        codeInput.setPrefWidth(250);
        codeInput.getStyleClass().add("textfield-code");

        // Item Label
        Label item = new Label("Item: ");
        GridPane.setConstraints(item, 0, 2);

        // Item Input
        TextField itemInput = new TextField();
        itemInput.setPromptText("Enter Item");
        GridPane.setConstraints(itemInput, 0, 3);
        itemInput.setPrefHeight(35);
        itemInput.setPrefWidth(250);

        // Quantity Label
        Label qty = new Label("Quantity: ");
        GridPane.setConstraints(qty, 0, 4);

        // Quantity Input
        TextField qtyInput = new TextField();
        qtyInput.setPromptText("Enter Quantity");
        GridPane.setConstraints(qtyInput, 0, 5);
        qtyInput.setPrefHeight(35);
        qtyInput.setPrefWidth(250);

        // Unit Label
        Label unit = new Label("Unit: ");
        GridPane.setConstraints(unit, 0, 6);

        // Unit Input
        TextField unitInput = new TextField();
        unitInput.setPromptText("Enter Unit");
        GridPane.setConstraints(unitInput, 0, 7);
        unitInput.setPrefHeight(35);
        unitInput.setPrefWidth(250);

        // Price Label
        Label price = new Label("Price: ");
        GridPane.setConstraints(price, 0, 8);

        // Price Input
        TextField priceInput = new TextField();
        priceInput.setPromptText("Enter Price");
        GridPane.setConstraints(priceInput, 0, 9);
        priceInput.setPrefHeight(35);
        priceInput.setPrefWidth(250);

        // Add to Inventory
        Button addButton = new Button("Add");
        GridPane.setConstraints(addButton, 0, 15);

        Button saleButton = new Button("Sales");
        GridPane.setConstraints(saleButton, 0, 16);
        saleButton.getStyleClass().add("button-light");
        saleButton.setVisible(false);

        // TableView
        table = new TableView<>();
        table.setPrefHeight(350);
        data = FXCollections.observableArrayList();

        data.addListener((ListChangeListener<DeliveryItems>) change -> {
            while (change.next()) {
                if (data.size() >= 3) {
                    saleButton.setVisible(true);
                } else {
                    saleButton.setVisible(false);
                }
            }
        });

        TableColumn<DeliveryItems, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setMinWidth(90);
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<DeliveryItems, String> qtyColumn = new TableColumn<>("Quantity");
        qtyColumn.setMinWidth(90);
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<DeliveryItems, String> unitColumn = new TableColumn<>("Unit");
        unitColumn.setMinWidth(90);
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));

        TableColumn<DeliveryItems, String> priceColumn = new TableColumn<>("Price");
        priceColumn.setMinWidth(90);
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        TableColumn<DeliveryItems, String> amountColumn = new TableColumn<>("Total Amount");
        amountColumn.setMinWidth(100);
        amountColumn.setCellValueFactory(cellData -> {
            DeliveryItems itemData = cellData.getValue();
            try {
                double quantityValue = Double.parseDouble(itemData.getQuantity());
                double priceValue = Double.parseDouble(itemData.getPrice());
                double total = quantityValue * priceValue;
                return new SimpleStringProperty(String.format("%.2f", total));
            } catch (NumberFormatException e) {
                return new SimpleStringProperty("0.00");
            }
        });

        table.getColumns().addAll(itemColumn, qtyColumn, unitColumn, priceColumn, amountColumn);
        GridPane.setConstraints(table, 2, 0, 1, 18); // Spanning multiple rows

        // Add button action
        addButton.setOnAction(e -> {
            itemNumbers[currentIndex] = String.valueOf(codeIndex);
            itemPrices[currentIndex] = Double.parseDouble(priceInput.getText());
            items[currentIndex] = itemInput.getText();
            quantities[currentIndex] = qtyInput.getText();
            units[currentIndex] = unitInput.getText();
            prices[currentIndex] = priceInput.getText();
            totals[currentIndex] = Double.parseDouble(priceInput.getText()) * Double.parseDouble(qtyInput.getText());
            currentIndex++;
            codeIndex++;
            codeInput.setText(String.valueOf(codeIndex));

            DeliveryItems newItem = new DeliveryItems(
                    itemInput.getText(),
                    qtyInput.getText(),
                    unitInput.getText(),
                    priceInput.getText()
            );
            data.add(newItem);
            table.setItems(data);

            itemInput.clear();
            qtyInput.clear();
            unitInput.clear();
            priceInput.clear();
        });

        saleButton.setOnAction(e -> {
            SalesCashiering salesCashiering = new SalesCashiering();
            salesCashiering.setInventoryData(data); // Pass inventory data to SalesCashiering
            salesCashiering.setDeliveryData(this); // Pass the Deliveryy instance to SalesCashiering
            try {
                salesCashiering.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add everything to grid
        grid.getChildren().addAll(codeLabel, codeInput, item, itemInput, qty, qtyInput, unit, unitInput, price, priceInput, addButton, saleButton, table);
    }

    public static double getPriceForItem(String itemNumber) {
        try {
            int index = Integer.parseInt(itemNumber) - 101; // Adjust the index
            if (index >= 0 && index < 100) {
                return itemPrices[index];
            } else {
                return 0.0; // Return 0.0 if the index is out of bounds
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    public String[] getItemNumbers() {
        return itemNumbers;
    }

    public double[] getItemPrices() {
        return itemPrices;
    }

    public String[] getItems() {
        return items;
    }

    public String[] getQuantities() {
        return quantities;
    }

    public double[] getTotals() {
        return totals;
    }

    public static DeliveryItems[] getDeliveryData() {
        // TODO Auto-generated method stub
        return null;
    }

    public void setInventoryData(ObservableList<DeliveryItems> inventoryData) {
        this.inventoryData = inventoryData;
    }
}
