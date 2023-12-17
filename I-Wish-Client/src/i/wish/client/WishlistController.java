package i.wish.client;

import DTO.AddItemDTO;
import DTO.ContributorInfoDTO;
import DTO.ItemDTO;
import DTO.UserDataDTO;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class WishlistController extends AnchorPane {

    UserDataDTO userDTO;
    int userId;
    Socket socket;
    DataInputStream dis;
    PrintStream ps;
    protected final Label label;
    protected final TableView<ItemDTO> tableView;
    protected final TableColumn<ItemDTO, byte[]> image;
    protected final TableColumn<ItemDTO, String> item_name;
    protected final TableColumn<ItemDTO, String> price;
    protected final TableColumn<ItemDTO, String> reprice;
    protected final TableColumn<ItemDTO, Void> cont;
    protected final TableColumn<ItemDTO, String> id;
    protected final Button btnAddItem;
    protected final Button btnUpdate;
    protected final Button btnDeleteItem;
    ObservableList<ItemDTO> wishlist;

    public WishlistController(UserDataDTO userDTO) {
        this.userDTO = userDTO;
        //this.userId = userId;
        wishlist = FXCollections.observableArrayList(userDTO.getWishlist());

        label = new Label();
        tableView = new TableView();
        image = new TableColumn();
        item_name = new TableColumn();
        price = new TableColumn();
        reprice = new TableColumn();
        cont = new TableColumn();
        id = new TableColumn();
        btnAddItem = new Button();
        btnUpdate = new Button();
        btnDeleteItem = new Button();
        
        String imagePath = "WishlistBG.png"; // Change this to the actual path of your image
        Image backgroundImage = new Image(getClass().getResource(imagePath).toExternalForm());
        
        // Set the BackgroundSize to 100% width and height without preserving the aspect ratio
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(background));

        setId("AnchorPane");
        setPrefHeight(500.0);
        setPrefWidth(596.0);

        label.setLayoutX(251.0);
        label.setLayoutY(25.0);
        label.setText("Wishlist");
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setFont(new Font("System Bold", 25.0));

        tableView.setLayoutX(18.0);
        tableView.setLayoutY(92.0);
        tableView.setPrefHeight(311.0);
        tableView.setPrefWidth(563.0);
        tableView.getStylesheets().add(getClass().getResource("header.css").toExternalForm());
        

        image.setPrefWidth(110.0);
        image.setText("Image");

        item_name.setPrefWidth(110.0);
        item_name.setText("Name");

        price.setPrefWidth(110.0);
        price.setText("Price");

        reprice.setPrefWidth(110.0);
        reprice.setText("Collected");

        cont.setPrefWidth(121.0);
        cont.setText("Contributers");

        id.setPrefWidth(52.0);
        id.setText("id");
        id.setVisible(false);
        image.setCellValueFactory(new PropertyValueFactory<>("image"));
        image.setCellFactory(column -> new ImageTableCell());

        item_name.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        reprice.setCellValueFactory(new PropertyValueFactory<>("remaining"));
        //cont.setCellValueFactory(new PropertyValueFactory<>("contributers"));
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        cont.setCellFactory(column -> new TableCell<ItemDTO, Void>() {
            private final Button showContributorsButton = new Button("Show Contributors");

            {
                showContributorsButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #3d6394;");
                showContributorsButton.setOnAction(event -> {
                    ItemDTO selectedItem = getTableView().getItems().get(getIndex());
                    showContributorsPopup(selectedItem.getItemName(), selectedItem.getContributors());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(showContributorsButton);
                }
            }
        });

        btnAddItem.setLayoutX(33.0);
        btnAddItem.setLayoutY(431.0);
        btnAddItem.setMnemonicParsing(false);
        btnAddItem.setPrefHeight(36.0);
        btnAddItem.setPrefWidth(131.0);
        btnAddItem.setStyle("-fx-background-radius: 15; -fx-border-color: #79a9e7; -fx-border-radius: 15; -fx-background-color: transparent;");
        btnAddItem.setText("Add Item");
        btnAddItem.setTextFill(javafx.scene.paint.Color.valueOf("#587ea9"));

        btnUpdate.setLayoutX(233.0);
        btnUpdate.setLayoutY(431.0);
        btnUpdate.setMnemonicParsing(false);
        btnUpdate.setPrefHeight(36.0);
        btnUpdate.setPrefWidth(131.0);
        btnUpdate.setStyle("-fx-background-radius: 15; -fx-border-color: #79a9e7; -fx-border-radius: 15; -fx-background-color: transparent;");
        btnUpdate.setText("Refresh");
        btnUpdate.setTextFill(javafx.scene.paint.Color.valueOf("#587ea9"));

        btnDeleteItem.setLayoutX(435.0);
        btnDeleteItem.setLayoutY(431.0);
        btnDeleteItem.setMnemonicParsing(false);
        btnDeleteItem.setPrefHeight(36.0);
        btnDeleteItem.setPrefWidth(131.0);
        btnDeleteItem.setStyle("-fx-background-radius: 15; -fx-border-color: #79a9e7; -fx-border-radius: 15; -fx-background-color: transparent;");
        btnDeleteItem.setText("Delete Item");
        btnDeleteItem.setTextFill(javafx.scene.paint.Color.valueOf("#587ea9"));

        getChildren().add(label);
        tableView.getColumns().add(image);
        tableView.getColumns().add(item_name);
        tableView.getColumns().add(price);
        tableView.getColumns().add(reprice);
        tableView.getColumns().add(cont);
        tableView.getColumns().add(id);
        image.setStyle("-fx-alignment: CENTER;");
        item_name.setStyle("-fx-alignment: CENTER;");
        price.setStyle("-fx-alignment: CENTER;");
        reprice.setStyle("-fx-alignment: CENTER;");
        cont.setStyle("-fx-alignment: CENTER;");
        getChildren().add(tableView);
        getChildren().add(btnAddItem);
        getChildren().add(btnUpdate);
        getChildren().add(btnDeleteItem);
        tableView.setItems(wishlist);
        try {
            socket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        btnDeleteItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ItemDTO selectedItem = tableView.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    System.out.println("Selected Item Details:");
                    System.out.println("ID: " + selectedItem.getId());
                    System.out.println("Name: " + selectedItem.getItemName());
                    System.out.println("Price: " + selectedItem.getPrice());
                    System.out.println("Remaining Price: " + selectedItem.getRemaining());
                    int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete item?", "Delete Item Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        try {
                            // Retrieve the attributes and print them
                            String itemName = selectedItem.getItemName();
                            int price = selectedItem.getPrice();
                            int remainingPrice = selectedItem.getRemaining();
                            int itemId = selectedItem.getId();
                            JSONObject deletedItem = new JSONObject();
                            deletedItem.put("HEADER", "fnDeletItem");
                            deletedItem.put("REMAININGPRICE", remainingPrice);
                            deletedItem.put("ITEMID", itemId);
                            deletedItem.put("USERID", userDTO.getId());
                            ps.println(deletedItem.toString());
                            String response = dis.readLine();
                            if (response.equals("Item Deleted")) {
                                tableView.setItems(FXCollections.observableArrayList(userDTO.getWishlist()));
                                JOptionPane.showMessageDialog(null, "Item Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                                userDTO.getWishlist().remove(selectedItem);
                                int selectedId = tableView.getSelectionModel().getSelectedIndex();
                                tableView.getItems().remove(selectedId);
                                tableView.getSelectionModel().clearSelection();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error could't delete", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Choose item first", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }
        });

        btnAddItem.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Instantiate the AddItemHandler class and call the handle method
                AddItemHandler addItemHandler = new AddItemHandler(dis, ps, userDTO, tableView);
                addItemHandler.handle();
            }
        });

        btnUpdate.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    JSONObject userData = new JSONObject();
                    userData.put("HEADER", "fnUserWishlist");
                    userData.put("USERID", userDTO.getId());
                    ps.println(userData.toString());

                    String response = dis.readLine();

                    JSONObject userWishlist = new JSONObject(response);
                    userDTO.setWishlist(parseItemsJson(userWishlist.getJSONArray("wishlist")));
                    wishlist.clear();
                    wishlist.addAll(userDTO.getWishlist());
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    public ArrayList<ItemDTO> parseItemsJson(JSONArray itemsArray) {
        ArrayList<ItemDTO> items = new ArrayList<>();

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemJson = itemsArray.getJSONObject(i);
            String itemId = itemJson.getString("item_id");
            String imageString = itemJson.getString("image");
            byte[] image = java.util.Base64.getDecoder().decode(imageString);
            String itemName = itemJson.getString("item_name");
            String collected = itemJson.getString("paid");
            String price = itemJson.getString("price");

            // Handle contributors and their details
            JSONArray contributorsArray = itemJson.optJSONArray("contributors_details");
            ArrayList<ContributorInfoDTO> contributorsDetails = new ArrayList<>();
            if (contributorsArray != null) {
                for (int j = 0; j < contributorsArray.length(); j++) {
                    JSONObject contributor = contributorsArray.getJSONObject(j);
                    String contributorName = contributor.getString("name");
                    int contributionAmount = contributor.getInt("contribution_amount");
                    contributorsDetails.add(new ContributorInfoDTO(contributorName, contributionAmount));
                }
            }

            // Create an ItemDTO object and add it to the list
            ItemDTO item = new ItemDTO(Integer.parseInt(itemId), Integer.parseInt(price), itemName, Integer.parseInt(collected), image);
            item.setContributors(contributorsDetails);
            items.add(item);
        }

        return items;
    }

    private class ImageTableCell extends TableCell<ItemDTO, byte[]> {

        private final ImageView imageView = new ImageView();
        private final int imageSize = 50;

        public ImageTableCell() {
            setGraphic(imageView);
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
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

    private void showContributorsPopup(String itemName, List<ContributorInfoDTO> contributors) {
        // Create a pop-up dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Contributors for " + itemName);

        // Create a table to display contributor name and total contribution
        TableView<ContributorInfoDTO> contributorsTable = new TableView<>();
        contributorsTable.getStylesheets().add(getClass().getResource("header.css").toExternalForm());

        contributorsTable.setPrefHeight(150);
        contributorsTable.setPrefWidth(350);

        TableColumn<ContributorInfoDTO, String> contributorNameColumn = new TableColumn<>("Contributor");
        contributorNameColumn.setCellValueFactory(new PropertyValueFactory<>("contributorName"));
        contributorNameColumn.setPrefWidth(165);

        TableColumn<ContributorInfoDTO, Double> contributionAmountColumn = new TableColumn<>("Contribution Amount");
        contributionAmountColumn.setCellValueFactory(new PropertyValueFactory<>("contributionAmount"));
        contributionAmountColumn.setPrefWidth(165);

        contributorsTable.getColumns().addAll(contributorNameColumn, contributionAmountColumn);

        // Clear existing items in the table
        contributorsTable.getItems().clear();

        // Add contributors for the selected item to the table
        contributorsTable.getItems().addAll(contributors);

        // Set content for the dialog
        dialog.getDialogPane().setContent(contributorsTable);

        // Add a close button
        Button closeButton = new Button("Close");
        closeButton.setOnAction(event -> dialog.close());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        // Show the dialog
        dialog.showAndWait();
    }

// Create a class to represent contributor information
}
