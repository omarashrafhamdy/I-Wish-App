package i.wish.client;

import DTO.AddItemDTO;
import DTO.FriendsDTO;
import DTO.ItemDTO;
import DTO.UserDataDTO;
import DTO.WishListDTO;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.swing.JOptionPane;
import org.json.JSONObject;

public class FriendListController extends AnchorPane {

    protected final TableView<FriendsDTO> tableView;
    protected final TableColumn<FriendsDTO, String> friends;
    protected final TableColumn<FriendsDTO, String> id;
    protected final TableView<ItemDTO> tableView0;
    protected final TableColumn<ItemDTO, byte[]> image;
    protected final TableColumn<ItemDTO, String> itemName;
    protected final TableColumn<ItemDTO, String> price;
    protected final TableColumn<ItemDTO, String> collected;
    protected final TableColumn<ItemDTO, Void> contribute;
    protected final Button btnRemoveFriend;
    ArrayList<FriendsDTO> friendsDto;
    Socket socket;
    DataInputStream dis;
    PrintStream ps;
    private int userId;
    String fname;

    public FriendListController(ArrayList<FriendsDTO> friendsDto, int userId, String fname) {
        ObservableList<FriendsDTO> friendData = FXCollections.observableArrayList(friendsDto);
        this.fname = fname;

        this.friendsDto = friendsDto;
        this.userId = userId;
        tableView = new TableView();
        friends = new TableColumn();
        id = new TableColumn();
        tableView0 = new TableView();
        image = new TableColumn();
        itemName = new TableColumn();
        price = new TableColumn();
        collected = new TableColumn();
        contribute = new TableColumn();
        btnRemoveFriend = new Button();

        setId("AnchorPane");
        setPrefHeight(500.0);
        setPrefWidth(596.0);

        tableView.setLayoutX(14.0);
        tableView.setLayoutY(42.0);
        tableView.setPrefHeight(410.0);
        tableView.setPrefWidth(146.0);
        tableView.setStyle("-fx-background-radius: 0;");
        tableView.getStylesheets().add(getClass().getResource("header.css").toExternalForm());

        friends.setEditable(false);
        friends.setPrefWidth(144.0);
        friends.setSortable(false);
        friends.setText("Friends");

        id.setEditable(false);
        id.setPrefWidth(145.0);
        id.setSortable(false);
        id.setText("id");
        id.setVisible(false);

        tableView0.setLayoutX(184.0);
        tableView0.setLayoutY(42.0);
        tableView0.setPrefHeight(444.0);
        tableView0.setPrefWidth(401.0);
        tableView0.getStylesheets().add(getClass().getResource("header.css").toExternalForm());

        image.setEditable(false);
        image.setPrefWidth(80.0);
        image.setSortable(false);
        image.setText("Image");

        itemName.setEditable(false);
        itemName.setPrefWidth(80.0);
        itemName.setSortable(false);
        itemName.setText("Name");

        price.setEditable(false);
        price.setPrefWidth(80.0);
        price.setSortable(false);
        price.setText("Price");

        collected.setEditable(false);
        collected.setPrefWidth(80.0);
        collected.setSortable(false);
        collected.setText("Collected");

        contribute.setEditable(false);
        contribute.setPrefWidth(79.0);
        contribute.setSortable(false);
        contribute.setText("Contribute");

        btnRemoveFriend.setLayoutX(14.0);
        btnRemoveFriend.setLayoutY(461.0);
        btnRemoveFriend.setMnemonicParsing(false);
        btnRemoveFriend.setPrefHeight(25.0);
        btnRemoveFriend.setPrefWidth(146.0);
        btnRemoveFriend.setStyle("-fx-background-radius: 15; -fx-border-color: #79a9e7; -fx-border-radius: 15; -fx-background-color: transparent; -fx-text-fill: #587ea9;");
        btnRemoveFriend.setText("Remove Friend");

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        friends.setCellValueFactory(new PropertyValueFactory<>("userName"));

        tableView.getColumns().add(friends);
        tableView.getColumns().add(id);
        getChildren().add(tableView);
        tableView0.getColumns().add(image);
        tableView0.getColumns().add(itemName);
        tableView0.getColumns().add(price);
        tableView0.getColumns().add(collected);
        tableView0.getColumns().add(contribute);
        image.setStyle("-fx-alignment: CENTER;");
        itemName.setStyle("-fx-alignment: CENTER;");
        price.setStyle("-fx-alignment: CENTER;");
        collected.setStyle("-fx-alignment: CENTER;");
        contribute.setStyle("-fx-alignment: CENTER;");
        getChildren().add(tableView0);
        getChildren().add(btnRemoveFriend);

        //add friends data inside table view
        tableView.setItems(friendData);

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                FriendsDTO selectedFriend = newSelection;
                // Get the items belonging to the selected friend
                ArrayList<ItemDTO> friendItems = selectedFriend.getWishlist();
                ObservableList<ItemDTO> friendItemsData = FXCollections.observableArrayList(friendItems);

                // Set items directly to the tableView0
                tableView0.setItems(friendItemsData);

                // Add button functionality to the existing contribute column
                contribute.setCellFactory(column -> new TableCell<ItemDTO, Void>() {
                    private final Button contributeBtn = new Button("Contribute");

                    {
                        contributeBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3d6394;");
                        contributeBtn.setOnAction(event -> {
                            ItemDTO item = getTableView().getItems().get(getIndex());

                            // Prompt user for contribution amount
                            String contributionAmountStr = JOptionPane.showInputDialog("Enter contribution amount");

                            try {
                                int contributionAmount = Integer.parseInt(contributionAmountStr);
                                WishListDTO wishListDTO = new WishListDTO(userId, item.getId(), contributionAmount, item.getPrice());

                                System.out.println("User Balance: " + UserDataDTO.getBalance());
                                int userBalance = UserDataDTO.getBalance();
                                if (contributionAmount > userBalance) {
                                    JOptionPane.showMessageDialog(null, "Insufficient balance", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                } else if (contributionAmount <= 0) {
                                    JOptionPane.showMessageDialog(null, "Invalid contribution amount", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                } else if (contributionAmount > item.getPrice()) {
                                    JOptionPane.showMessageDialog(null, "Amount is bigger than item price", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                } else if ((item.getPrice() < (item.getRemaining() + contributionAmount))) {
                                    JOptionPane.showMessageDialog(null, "Amount is bigger than what's left to contribute", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                } //int totalContribution = 
                                else if (contributionAmount > 0 && item.getRemaining() == item.getPrice()) {
                                    JOptionPane.showMessageDialog(null, "Item is fully contributed", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                } else {
                                    JSONObject contributeData = new JSONObject();
                                    item.setRemaining((item.getRemaining() + contributionAmount));
                                    contributeData.put("HEADER", "fnContribute");
                                    contributeData.put("USER_ID", userId);
                                    contributeData.put("FRIEND_ID", newSelection.getId());
                                    contributeData.put("ITEM_ID", item.getId());
                                    contributeData.put("FNAME", fname);
                                    contributeData.put("friend_name", newSelection.getUserName());
                                    contributeData.put("itemName", item.getItemName());
                                    contributeData.put("CONTRIBUTION_AMOUNT", contributionAmount);

                                    ps.println(contributeData.toString());

                                    System.out.println(item.getRemaining());
                                }

                                String contributeResponse = dis.readLine();
                                System.out.println(contributeResponse);

                                if (contributeResponse.equals("Contribution successful")) {
                                    // Update the UI or perform any additional actions if needed
                                    UserDataDTO.setBalance((UserDataDTO.getBalance() - contributionAmount));
                                    System.out.println(UserDataDTO.getBalance());
                                    MainPageController.changeBalanceLabel(UserDataDTO.getBalance());
                                    tableView0.refresh();
                                    JOptionPane.showMessageDialog(null, "Contribution successful", "Success", JOptionPane.INFORMATION_MESSAGE);
                                } else {
                                    JOptionPane.showMessageDialog(null, "Contribution failed", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            } catch (IOException ex) {
                                JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                            } catch (NumberFormatException e) {
                                JOptionPane.showMessageDialog(null, "Invalid contribution amount format", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(contributeBtn);
                        }
                    }
                });
                image.setCellValueFactory(new PropertyValueFactory<>("image"));
                image.setCellFactory(column -> new ImageTableCell());
                itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
                price.setCellValueFactory(new PropertyValueFactory<>("price"));
                collected.setCellValueFactory(new PropertyValueFactory<>("remaining"));
            }
        });
        try {
            socket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
        btnRemoveFriend.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FriendsDTO selectedFriend = tableView.getSelectionModel().getSelectedItem();
                if (selectedFriend != null) {
                    int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete friend?", "Delete Friend Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {

                        try {
                            // Retrieve the attributes and print them
                            String userName = selectedFriend.getUserName();
                            int friendId = selectedFriend.getId();

                            JSONObject deletedFriend = new JSONObject();
                            deletedFriend.put("HEADER", "fnDeleteFriend");
                            deletedFriend.put("USERID", userId);
                            deletedFriend.put("FRIENDID", friendId);
                            ps.println(deletedFriend.toString());
                            String response = dis.readLine();
                            System.out.println(response);
                            if (response.equals("Friend Deleted")) {
                                JOptionPane.showMessageDialog(null, "Friend Deleted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);

                                int selectedId = tableView.getSelectionModel().getSelectedIndex();
                                tableView.getItems().remove(selectedId);
                                tableView.getSelectionModel().clearSelection();
                                tableView0.getItems().clear();
                            } else {
                                JOptionPane.showMessageDialog(null, "Error could't delete", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (IOException ex) {
                            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Choose friend first", "Error", JOptionPane.ERROR_MESSAGE);

                }

            }
        }
        );

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

}
