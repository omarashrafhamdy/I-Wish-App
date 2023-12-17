package i.wish.client;

import DTO.AddItemDTO;
import DTO.ItemDTO;
import DTO.UserDataDTO;
import java.io.ByteArrayInputStream;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.JOptionPane;

public class AddItemHandler {

    private final DataInputStream dis;
    private final PrintStream ps;
    private final UserDataDTO userDTO;
    private final TableView<ItemDTO> tableView;

    public AddItemHandler(DataInputStream dis, PrintStream ps, UserDataDTO userDTO, TableView<ItemDTO> tableView) {
        this.dis = dis;
        this.ps = ps;
        this.userDTO = userDTO;
        this.tableView = tableView;
    }

    public void handle() {
        try {
            // Request the list of items from the server
            JSONObject request = new JSONObject();
            request.put("HEADER", "fnGetItems");
            ps.println(request.toString());

            // Receive the list of items from the server
            String response3 = dis.readLine();
            JSONArray itemsArray = new JSONArray(response3);

            // Convert the JSON array to a list of AddItemDTO objects
            List<AddItemDTO> itemsList = new ArrayList<>();
            for (int i = 0; i < itemsArray.length(); i++) {
                JSONObject itemJson = itemsArray.getJSONObject(i);
                String imageString = itemJson.getString("image");
                byte[] image = java.util.Base64.getDecoder().decode(imageString);
                AddItemDTO item = new AddItemDTO(
                        Integer.parseInt(itemJson.getString("id")),
                        itemJson.getString("name"),
                        Integer.parseInt(itemJson.getString("price")),
                        itemJson.getString("category"),
                        image
                );
                itemsList.add(item);
            }

            // Check if any items are available
            if (itemsList.isEmpty()) {
                Platform.runLater(() -> {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("No items available in the database.");
                    errorAlert.showAndWait();
                });
                return;
            }

            // Create a new TableView for the item selection popup
            TableView<AddItemDTO> itemSelectionTableView = new TableView<>();
            TableColumn<AddItemDTO, String> itemNameColumn = new TableColumn<>("Name");
            TableColumn<AddItemDTO, Integer> priceColumn = new TableColumn<>("Price");
            TableColumn<AddItemDTO, String> categoryColumn = new TableColumn<>("Category");
            TableColumn<AddItemDTO, byte[]> itemImageColumn = new TableColumn<>("image");

            itemNameColumn.setCellFactory(column -> new AddItemHandler.TextTableCell());
            priceColumn.setCellFactory(column -> new AddItemHandler.TextTableCell2());
            categoryColumn.setCellFactory(column -> new AddItemHandler.TextTableCell());
            itemImageColumn.setCellFactory(column -> new AddItemHandler.ImageTableCell());

            itemNameColumn.setPrefWidth(115);
            priceColumn.setPrefWidth(115);
            categoryColumn.setPrefWidth(115);
            itemImageColumn.setPrefWidth(120);

            itemNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
            categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
            itemImageColumn.setCellValueFactory(new PropertyValueFactory<>("image"));

            itemSelectionTableView.getColumns().addAll(itemImageColumn, itemNameColumn, priceColumn, categoryColumn);
            itemSelectionTableView.setItems(FXCollections.observableArrayList(itemsList));
            itemSelectionTableView.getStylesheets().add(getClass().getResource("header.css").toExternalForm());

            // Create a dialog to display the list of items as a table
            DialogPane dialogPane = new DialogPane();
            dialogPane.setHeaderText("Select an item to add to your wishlist:");
            dialogPane.setContent(itemSelectionTableView);

            // Set preferred size for the dialog pane
            dialogPane.setPrefWidth(500);
            dialogPane.setPrefHeight(300);

            // Add a custom "Add" button to the dialog
            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialogPane.getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            Dialog<ButtonType> itemDialog = new Dialog<>();
            itemDialog.setTitle("Choose an Item");
            itemDialog.setDialogPane(dialogPane);

            // Show the dialog and wait for the user's choice
            Optional<ButtonType> result = itemDialog.showAndWait();

            // Process the user's choice
            result.ifPresent(buttonType -> {
                if (buttonType == addButton) {
                    AddItemDTO selectedItem = itemSelectionTableView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        // Check if the item is already in the wishlist
                        if (isItemInWishlist(selectedItem)) {
                            Platform.runLater(() -> {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                alert.setTitle("Warning");
                                alert.setHeaderText(null);
                                alert.setContentText("Item is already in your wishlist.");
                                alert.showAndWait();
                            });
                        } else {
                            try {
                                // Send a request to the server to add the item to the wishlist
                                JSONObject addItemRequest = new JSONObject();
                                addItemRequest.put("HEADER", "fnAddItemToWishlist");
                                addItemRequest.put("USER_ID", userDTO.getId());
                                addItemRequest.put("ITEM_ID", selectedItem.getId());
                                //addItemRequest.put("image", selectedItem.getImage());
                                addItemRequest.put("PAID", selectedItem.getPaid());
                                addItemRequest.put("ITEM_PRICE", selectedItem.getPrice());

                                // Send the request to the server
                                ps.println(addItemRequest.toString());

                                // Receive the response from the server
                                String addItemResponse = dis.readLine();
                                if (addItemResponse.equals("Item added successfully")) {
                                    // Update the local wishlist and table view
                                    ItemDTO newItem = new ItemDTO(
                                            selectedItem.getId(),
                                            selectedItem.getPrice(),
                                            selectedItem.getName(),// You can set a default value for remaining, adjust accordingly
                                            0,
                                            selectedItem.getImage()
                                    );
                                    userDTO.getWishlist().add(newItem);

                                    Platform.runLater(() -> {
                                        tableView.setItems(FXCollections.observableArrayList(userDTO.getWishlist()));
                                        // Inform the user about the successful addition
                                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                        successAlert.setTitle("Success");
                                        successAlert.setHeaderText(null);
                                        successAlert.setContentText("Item added to your wishlist successfully!");
                                        successAlert.showAndWait();
                                    });
                                } else {
                                    // Display an error message if the server failed to add the item
                                    Platform.runLater(() -> {
                                        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                                        errorAlert.setTitle("Error");
                                        errorAlert.setHeaderText(null);
                                        errorAlert.setContentText("Failed to add item to the wishlist.");
                                        errorAlert.showAndWait();
                                    });
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }
                }
            });
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private boolean isItemInWishlist(AddItemDTO selectedItem) {
        // Check if the item is already in the wishlist based on your logic
        for (ItemDTO item : userDTO.getWishlist()) {
            if (item.getId() == selectedItem.getId()) {
                return true;
            }
        }
        return false;
    }

    private class ImageTableCell extends TableCell<AddItemDTO, byte[]> {

        private final ImageView imageView = new ImageView();
        private final int imageSize = 50;

        public ImageTableCell() {
            setGraphic(imageView);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(byte[] item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                imageView.setImage(null);
            } else {
                Image image = new Image(new ByteArrayInputStream(item));
                imageView.setFitWidth(imageSize);
                imageView.setFitHeight(imageSize);
                imageView.setImage(image);
            }
        }
    }

    private class TextTableCell extends TableCell<AddItemDTO, String> {

        public TextTableCell() {
            setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
                setAlignment(Pos.CENTER);
            } else {
                setText(item);
                setAlignment(Pos.CENTER);
            }
        }
    }

    private class TextTableCell2 extends TableCell<AddItemDTO, Integer> {

        public TextTableCell2() {
            setAlignment(Pos.CENTER);
        }

        @Override
        protected void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setText(null);
            } else {
                setText(String.valueOf(item));
                setAlignment(Pos.CENTER);
            }
        }
    }
}
