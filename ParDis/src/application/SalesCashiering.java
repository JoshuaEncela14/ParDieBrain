package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SalesCashiering extends Application {

    Stage salesStage;
    TableView<SalesItem> salesTable;
    ObservableList<SalesItem> salesData;
    private ObservableList<DeliveryItems> inventoryData;
	private Deliveryy deliveryy;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        salesStage = primaryStage;
        salesStage.setTitle("Sales Cashiering");

        // GridPane
        GridPane salesGrid = new GridPane();
        salesGrid.setPadding(new Insets(20, 10, 10, 20));
        salesGrid.setVgap(5);
        salesGrid.setHgap(10);

        // Item Label
        Label itemLabel = new Label("Item: ");
        GridPane.setConstraints(itemLabel, 0, 0);

        // Item Input
        TextField itemInput = new TextField();
        itemInput.setPromptText("Enter item number");
        GridPane.setConstraints(itemInput, 0, 1);
        itemInput.setPrefHeight(35);
        itemInput.setPrefWidth(250);

        // Quantity Label
        Label qtyLabel = new Label("Quantity: ");
        GridPane.setConstraints(qtyLabel, 0, 2);

        // Quantity Input
        TextField qtyInput = new TextField();
        qtyInput.setPromptText("Enter quantity");
        GridPane.setConstraints(qtyInput, 0, 3);
        qtyInput.setPrefHeight(35);
        qtyInput.setPrefWidth(250);

        // Add to Sales Button
        Button addToSalesButton = new Button("Add to Sales");
        GridPane.setConstraints(addToSalesButton, 0, 8);

        // Inventory Button
        Button inventoryButton = new Button("Inventory");
        GridPane.setConstraints(inventoryButton, 0, 9);
        inventoryButton.getStyleClass().add("button-light");

        // TableView
        salesTable = new TableView<>();
        salesTable.setPrefHeight(250);
        salesTable.setPrefWidth(500);
        salesData = FXCollections.observableArrayList();

        TableColumn<SalesItem, String> itemColumn = new TableColumn<>("Item");
        itemColumn.setMinWidth(165);
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("item"));

        TableColumn<SalesItem, String> qtyColumn = new TableColumn<>("Quantity");
        qtyColumn.setMinWidth(165);
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        TableColumn<SalesItem, String> totalColumn = new TableColumn<>("Total");
        totalColumn.setMinWidth(165);
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));

        salesTable.getColumns().addAll(itemColumn, qtyColumn, totalColumn);
        GridPane.setConstraints(salesTable, 2, 0, 1, 10);

        addToSalesButton.setOnAction(event -> {
            String item = itemInput.getText();
            String quantityStr = qtyInput.getText();
            
            try {
                int quantity = Integer.parseInt(quantityStr);

                // Get price based on the item number
                String price = getPriceFromItemNumber(item);
                double priceValue = Double.parseDouble(price);

                // Compute total
                double totalValue = priceValue * quantity;
                String total = String.valueOf(totalValue);

                // Update sales data
                boolean itemExists = false;
                for (SalesItem salesItem : salesData) {
                    if (salesItem.getItem().equals(item)) {
                        int newQuantity = Integer.parseInt(salesItem.getQuantity()) + quantity;
                        double newTotal = newQuantity * priceValue;
                        salesItem.setQuantity(String.valueOf(newQuantity));
                        salesItem.setTotal(String.valueOf(newTotal));
                        itemExists = true;
                        break;
                    }
                }
                if (!itemExists) {
                    salesData.add(new SalesItem(item, quantityStr, total));
                }
                salesTable.setItems(salesData);

                // Update inventory data
                for (DeliveryItems deliveryItem : inventoryData) {
                    if (deliveryItem.getItem().equals(item)) {
                        int newInventoryQuantity = Integer.parseInt(deliveryItem.getQuantity()) - quantity;
                        deliveryItem.setQuantity(String.valueOf(newInventoryQuantity));
                        break;
                    }
                }
                // Reflect the updated inventory data back to Deliveryy class
                deliveryy.setInventoryData(inventoryData);
            } catch (NumberFormatException e) {
                // Handle invalid input
                System.err.println("Invalid quantity or price format");
            }
        });



     // Inventory button action
        inventoryButton.setOnAction(e -> {
            Thread inventoryThread = new Thread(() -> {
                Platform.runLater(() -> {
                    StringBuilder inventoryDetails = new StringBuilder();
                    double grandTotal = 0.0;
                    if (inventoryData != null) {
                        for (DeliveryItems item : inventoryData) {
                            double deliveryQty = Double.parseDouble(item.getQuantity());
                            double deliveryPrice = Double.parseDouble(item.getPrice());
                            double deliveryTotal = deliveryQty * deliveryPrice;

                            double salesQty = 0.0;
                            double salesTotal = 0.0;
                            for (SalesItem salesItem : salesData) {
                                if (salesItem.getItem().equals(item.getItem())) {
                                    salesQty += Double.parseDouble(salesItem.getQuantity());
                                    salesTotal += Double.parseDouble(salesItem.getTotal());
                                }
                            }

                            double remainingQty = deliveryQty - salesQty;
                            double remainingTotal = deliveryTotal - salesTotal;

                            // Append required details to StringBuilder
                            inventoryDetails.append(String.format("Code: %s, Item: %s, Overall Quantity: %.2f, Overall Total: %.2f\n",
                                    item.getItem(), item.getItem(), remainingQty, remainingTotal));

                            grandTotal += remainingTotal;
                        }
                    }

                    // Append inventory grand total
                    inventoryDetails.append("\nInventory Grand Total: ").append(grandTotal);
                    System.out.println(inventoryDetails.toString());
                });
            });
            inventoryThread.setDaemon(true);
            inventoryThread.start();
        });





        salesGrid.getChildren().addAll(itemLabel, itemInput, qtyLabel, qtyInput, addToSalesButton, inventoryButton, salesTable);

        Scene salesScene = new Scene(salesGrid, 780, 450);
        salesScene.getStylesheets().add("design.css");
        salesStage.setScene(salesScene);
        salesStage.show();
    }

    public void setInventoryData(ObservableList<DeliveryItems> inventoryData) {
        this.inventoryData = inventoryData;
    }

    public String getPriceFromItemNumber(String itemNumber) {
        return String.valueOf(Deliveryy.getPriceForItem(itemNumber));
    }

    public void setDeliveryData(Deliveryy deliveryy) {
        this.deliveryy = deliveryy;
    }

    public void show() {
        salesStage.show(); 
    }
}
