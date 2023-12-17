package i.wish.client;

import DTO.ContributorInfoDTO;
import DTO.FriendsDTO;
import DTO.ItemDTO;
import DTO.NotificationsDTO;
import DTO.SearchedRequstedDTO;
import DTO.UserDataDTO;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class MainPageController extends BorderPane {

    UserDataDTO userDto;
    Socket socket;
    DataInputStream dis;
    PrintStream ps;
    WishlistController wishlistController;
    FriendListController friendListController;
    FriendsDTO friendsDTO;
    AddFriendRequestController addFriendRequestController;
    NotificationsController notificationsController;

    protected final VBox vBox;
    protected final AnchorPane anchorPane;
    protected final ImageView imageView;
    protected final Label labelName;
    protected static Label labelBalance;
    protected final ImageView imageView0;
    protected final Pane pane;
    protected final HBox hBox;
    protected final Pane pane0;
    protected final Button btnWishlist;
    protected final HBox hBox0;
    protected final HBox hBox1;
    protected final Pane pane1;
    protected final Button btnFriendlist;
    protected final HBox hBox2;
    protected final HBox hBox3;
    protected final Pane pane2;
    protected final Button btnAddFriend;
    protected final HBox hBox4;
    protected final HBox hBox5;
    protected final Pane pane3;
    protected final Button btnAddBalance;
    protected final HBox hBox6;
    protected final HBox hBox7;
    protected final Pane pane4;
    protected final Button btnNotfication;
    protected final HBox hBox8;
    protected final HBox hBox9;
    protected final Pane pane5;
    protected final Button btnLogout;
    protected final AnchorPane anchorPane0;

    public MainPageController(UserDataDTO userDto) {
        this.userDto = userDto;
        //wishlistController = new WishlistController(userDto);
        //notificationsController = new NotificationsController(userDto);
        //addFriendRequestController= new AddFriendRequestController(userDto);

        vBox = new VBox();
        anchorPane = new AnchorPane();
        imageView = new ImageView();
        labelName = new Label();
        labelBalance = new Label();
        imageView0 = new ImageView();
        pane = new Pane();
        hBox = new HBox();
        pane0 = new Pane();
        btnWishlist = new Button();
        hBox0 = new HBox();
        hBox1 = new HBox();
        pane1 = new Pane();
        btnFriendlist = new Button();
        hBox2 = new HBox();
        hBox3 = new HBox();
        pane2 = new Pane();
        btnAddFriend = new Button();
        hBox4 = new HBox();
        hBox5 = new HBox();
        pane3 = new Pane();
        btnAddBalance = new Button();
        hBox6 = new HBox();
        hBox7 = new HBox();
        pane4 = new Pane();
        btnNotfication = new Button();
        hBox8 = new HBox();
        hBox9 = new HBox();
        pane5 = new Pane();
        btnLogout = new Button();
        anchorPane0 = new AnchorPane();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(500.0);
        setPrefWidth(800.0);
        
        String imagePath = "MainPageBG.png";
        Image backgroundImage = new Image(getClass().getResource(imagePath).toExternalForm());
        
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        vBox.setBackground(new Background(background));

        BorderPane.setAlignment(vBox, javafx.geometry.Pos.CENTER);
        vBox.setPrefHeight(500.0);
        vBox.setPrefWidth(204.0);
        //vBox.setStyle("-fx-background-color: #CAD5DB;");

        anchorPane.setPrefHeight(183.0);
        anchorPane.setPrefWidth(199.0);

        imageView.setFitHeight(115.0);
        imageView.setFitWidth(176.0);
        imageView.setLayoutX(48.0);
        imageView.setLayoutY(14.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(getClass().getResource("logo2.png").toExternalForm()));

        labelName.setAlignment(javafx.geometry.Pos.CENTER);
        labelName.setLayoutY(131.0);
        labelName.setPrefHeight(25.0);
        labelName.setPrefWidth(204.0);
        labelName.setText(userDto.getFullName());
        labelName.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        labelName.setTextFill(javafx.scene.paint.Color.valueOf("#0d2c51"));
        labelName.setFont(new Font("System Bold", 16.0));

        labelBalance.setLayoutX(88.0);
        labelBalance.setLayoutY(158.0);
        labelBalance.setText(String.valueOf(UserDataDTO.getBalance()));
        labelBalance.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        labelBalance.setTextFill(javafx.scene.paint.Color.valueOf("#0d2c51"));
        labelBalance.setFont(new Font(16.0));
        
        imageView0.setFitHeight(16.0);
        imageView0.setFitWidth(16.0);
        imageView0.setLayoutX(69.0);
        imageView0.setLayoutY(163.0);
        imageView0.setPickOnBounds(true);
        imageView0.setPreserveRatio(true);
        imageView0.setImage(new Image(getClass().getResource("coin.png").toExternalForm()));

        pane.setPrefHeight(15.0);
        pane.setPrefWidth(199.0);

        hBox.setPrefHeight(30.0);
        hBox.setPrefWidth(199.0);

        pane0.setPrefHeight(30.0);
        pane0.setPrefWidth(14.0);

        btnWishlist.setMnemonicParsing(false);
        btnWishlist.setPrefHeight(30.0);
        btnWishlist.setPrefWidth(176.0);
        btnWishlist.setStyle("-fx-background-color: #79a9e7; -fx-background-radius: 15;");
        btnWishlist.setText("Wishlist");
        btnWishlist.setTextFill(javafx.scene.paint.Color.WHITE);
        btnWishlist.setCursor(Cursor.HAND);

        hBox0.setPrefHeight(14.0);
        hBox0.setPrefWidth(204.0);

        hBox1.setPrefHeight(30.0);
        hBox1.setPrefWidth(199.0);

        pane1.setPrefHeight(30.0);
        pane1.setPrefWidth(14.0);

        btnFriendlist.setMnemonicParsing(false);
        btnFriendlist.setPrefHeight(30.0);
        btnFriendlist.setPrefWidth(176.0);
        btnFriendlist.setStyle("-fx-background-color: #79a9e7; -fx-background-radius: 15;");
        btnFriendlist.setText("Friendlist");
        btnFriendlist.setTextFill(javafx.scene.paint.Color.WHITE);
        btnFriendlist.setCursor(Cursor.HAND);

        hBox2.setPrefHeight(14.0);
        hBox2.setPrefWidth(204.0);

        hBox3.setPrefHeight(30.0);
        hBox3.setPrefWidth(199.0);

        pane2.setPrefHeight(30.0);
        pane2.setPrefWidth(14.0);

        btnAddFriend.setMnemonicParsing(false);
        btnAddFriend.setPrefHeight(30.0);
        btnAddFriend.setPrefWidth(176.0);
        btnAddFriend.setStyle("-fx-background-color: #79a9e7; -fx-background-radius: 15;");
        btnAddFriend.setText("Add friend / Request");
        btnAddFriend.setTextFill(javafx.scene.paint.Color.WHITE);
        btnAddFriend.setCursor(Cursor.HAND);

        hBox4.setPrefHeight(14.0);
        hBox4.setPrefWidth(204.0);

        hBox5.setPrefHeight(30.0);
        hBox5.setPrefWidth(199.0);

        pane3.setPrefHeight(30.0);
        pane3.setPrefWidth(14.0);

        btnAddBalance.setMnemonicParsing(false);
        btnAddBalance.setPrefHeight(30.0);
        btnAddBalance.setPrefWidth(176.0);
        btnAddBalance.setStyle("-fx-background-color: #79a9e7; -fx-background-radius: 15;");
        btnAddBalance.setText("Add Balance");
        btnAddBalance.setTextFill(javafx.scene.paint.Color.WHITE);
        btnAddBalance.setCursor(Cursor.HAND);

        hBox6.setPrefHeight(14.0);
        hBox6.setPrefWidth(204.0);

        hBox7.setPrefHeight(30.0);
        hBox7.setPrefWidth(199.0);

        pane4.setPrefHeight(30.0);
        pane4.setPrefWidth(14.0);

        btnNotfication.setMnemonicParsing(false);
        btnNotfication.setPrefHeight(30.0);
        btnNotfication.setPrefWidth(176.0);
        btnNotfication.setStyle("-fx-background-color: #79a9e7; -fx-background-radius: 15;");
        btnNotfication.setText("Notfication");
        btnNotfication.setTextFill(javafx.scene.paint.Color.WHITE);
        btnNotfication.setCursor(Cursor.HAND);

        hBox8.setPrefHeight(33.0);
        hBox8.setPrefWidth(204.0);

        hBox9.setPrefHeight(30.0);
        hBox9.setPrefWidth(199.0);

        pane5.setPrefHeight(30.0);
        pane5.setPrefWidth(64.0);

        btnLogout.setMnemonicParsing(false);
        btnLogout.setPrefHeight(42.0);
        btnLogout.setPrefWidth(79.0);
        btnLogout.setStyle("-fx-background-color: #79a9e7; -fx-background-radius: 15;");
        btnLogout.setText("Logout");
        btnLogout.setTextFill(javafx.scene.paint.Color.WHITE);
        btnLogout.setCursor(Cursor.HAND);
        setLeft(vBox);

        BorderPane.setAlignment(anchorPane0, javafx.geometry.Pos.CENTER);
        anchorPane0.setPrefHeight(200.0);
        anchorPane0.setPrefWidth(200.0);

        wishlistController = new WishlistController(userDto);
        setCenter(wishlistController);

        anchorPane.getChildren().add(imageView);
        anchorPane.getChildren().add(labelName);
        anchorPane.getChildren().add(labelBalance);
        anchorPane.getChildren().add(imageView0);
        vBox.getChildren().add(anchorPane);
        vBox.getChildren().add(pane);
        hBox.getChildren().add(pane0);
        hBox.getChildren().add(btnWishlist);
        vBox.getChildren().add(hBox);
        vBox.getChildren().add(hBox0);
        hBox1.getChildren().add(pane1);
        hBox1.getChildren().add(btnFriendlist);
        vBox.getChildren().add(hBox1);
        vBox.getChildren().add(hBox2);
        hBox3.getChildren().add(pane2);
        hBox3.getChildren().add(btnAddFriend);
        vBox.getChildren().add(hBox3);
        vBox.getChildren().add(hBox4);
        hBox5.getChildren().add(pane3);
        hBox5.getChildren().add(btnAddBalance);
        vBox.getChildren().add(hBox5);
        vBox.getChildren().add(hBox6);
        hBox7.getChildren().add(pane4);
        hBox7.getChildren().add(btnNotfication);
        vBox.getChildren().add(hBox7);
        vBox.getChildren().add(hBox8);
        hBox9.getChildren().add(pane5);
        hBox9.getChildren().add(btnLogout);
        vBox.getChildren().add(hBox9);

        try {
            socket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }

        btnWishlist.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    JSONObject userData = new JSONObject();
                    userData.put("HEADER", "fnUserWishlist");
                    userData.put("USERID", userDto.getId());
                    ps.println(userData.toString());

                    String response = dis.readLine();
                    JSONObject userWishlist = new JSONObject(response);
                    userDto.setWishlist(parseItemsJson(userWishlist.getJSONArray("wishlist")));
                    wishlistController = new WishlistController(userDto);
                    setCenter(wishlistController);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnFriendlist.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JSONObject userData = new JSONObject();
                userData.put("HEADER", "fnFriendList");
                userData.put("USERID", userDto.getId());
                ps.println(userData.toString());

                try {
                    String response = dis.readLine();

                    setCenter(new FriendListController(parseFriendsData(response), userDto.getId(), userDto.getFullName()));
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnAddFriend.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    JSONObject friendRequestData = new JSONObject();
                    friendRequestData.put("HEADER", "fnFriendRequestList");
                    friendRequestData.put("USERID", userDto.getId());
                    ps.println(friendRequestData.toString());
                    String response = dis.readLine();
                    JSONArray friendRequestArray = new JSONArray(response);
                    ArrayList<SearchedRequstedDTO> friendRequestList = new ArrayList<SearchedRequstedDTO>();
                    for (int i = 0; i < friendRequestArray.length(); i++) {
                        JSONObject friendRequestIndex = friendRequestArray.getJSONObject(i);
                        int id = friendRequestIndex.getInt("Id");
                        String username = friendRequestIndex.getString("UserName");
                        String email = friendRequestIndex.getString("Email");
                        SearchedRequstedDTO requestedDTO = new SearchedRequstedDTO(id, username, email);
                        friendRequestList.add(requestedDTO);
                    }
                    addFriendRequestController = new AddFriendRequestController(userDto.getId(), userDto.getEmail(), friendRequestList);
                    setCenter(addFriendRequestController);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnNotfication.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                JSONObject userData = new JSONObject();
                userData.put("HEADER", "fnNotifications");
                userData.put("id", userDto.getId());

                ps.println(userData.toString());
                String response;
                try {
                    response = dis.readLine();
                    JSONArray notificationsArray = new JSONArray(response);
                    List<NotificationsDTO> notficationList = new ArrayList<>();
                    for (int i = 0; i < notificationsArray.length(); i++) {
                        JSONObject notficationJson = notificationsArray.getJSONObject(i);
                        NotificationsDTO notfication = new NotificationsDTO(
                                notficationJson.getString("message")
                        );
                        notficationList.add(notfication);
                    }
                    setCenter(notificationsController = new NotificationsController(notficationList));

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        );

        btnLogout.addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {

                int choice = JOptionPane.showConfirmDialog(null, "Are you sure you want to sign out?", "Sign Out Confirmation", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    JSONObject logoutRequest = new JSONObject();
                    logoutRequest.put("HEADER", "fnLogout");
                    ps.println(logoutRequest.toString());
                    Parent root = new LoginController(); // Replace this with your LoginController setup
                    Scene scene = new Scene(root);
                    // Get the stage from the button
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                    stage.setScene(scene);
                    stage.setMaxHeight(600);
                    stage.setMaxWidth(850);
                    stage.show();
                }
            }
        }
        );

        btnAddBalance.addEventHandler(ActionEvent.ACTION,
                new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event
            ) {
                // Instantiate the AddItemHandler class and call the handle method
                AddBalanceHandler addBalanceHandler = new AddBalanceHandler(userDto, dis, ps, labelBalance);
                addBalanceHandler.handle(event);
            }
        }
        );
    }

    public static void changeBalanceLabel(int balance) {
        labelBalance.setText(String.valueOf(balance));
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

    public ArrayList<FriendsDTO> parseFriendsData(String jsonData) {
        ArrayList<FriendsDTO> friendsList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray friendsArray = jsonObject.getJSONArray("Friends");

        for (int i = 0; i < friendsArray.length(); i++) {
            JSONObject friendObject = friendsArray.getJSONObject(i);

            int friendId = friendObject.getInt("friend_id");
            String friendUsername = friendObject.getString("friend_username");

            FriendsDTO friend = new FriendsDTO(friendUsername, friendId);

            JSONArray itemsArray = friendObject.getJSONArray("items");
            for (int j = 0; j < itemsArray.length(); j++) {
                JSONObject itemObject = itemsArray.getJSONObject(j);

                String itemId = itemObject.getString("item_id");
                String imageString = itemObject.getString("image");
                byte[] image = java.util.Base64.getDecoder().decode(imageString);
                String itemName = itemObject.getString("item_name");
                String itemPrice = itemObject.getString("price");
                String itemRemaining = itemObject.getString("paid");

                ItemDTO item = new ItemDTO(Integer.parseInt(itemId), Integer.parseInt(itemPrice), itemName, Integer.parseInt(itemRemaining), image);
                friend.addItem(item);
            }

            friendsList.add(friend);
        }

        return friendsList;
    }

    /*public ArrayList<ItemDTO> parseItemsJson(JSONArray itemsArray) {
        // Implement the logic to parse the JSON array and create an ArrayList<ItemDTO>
        ArrayList<ItemDTO> items = new ArrayList<>();

        for (int i = 0; i < itemsArray.length(); i++) {
            JSONObject itemJson = itemsArray.getJSONObject(i);
            String itemId = itemJson.getString("item_id");
            String imageString = itemJson.getString("image");
            byte[] image = java.util.Base64.getDecoder().decode(imageString);
            String itemName = itemJson.getString("item_name");
            String itemPrice = itemJson.getString("Remaining price");
            String price = itemJson.getString("price");
// Adjust the key accordingly
            // Create an ItemDTO object and add it to the list
            ItemDTO item = new ItemDTO(Integer.parseInt(itemId), itemName, Integer.parseInt(itemPrice), image);
            items.add(item);
        }

        return items;

    }*/
}
