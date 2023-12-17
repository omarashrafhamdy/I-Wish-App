package i.wish.client;

import DTO.SearchedRequstedDTO;
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
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFriendRequestController extends AnchorPane {

    protected final TableView<SearchedRequstedDTO> tableView;
    protected final TableColumn<SearchedRequstedDTO, String> searchUserName;
    protected final TableColumn<SearchedRequstedDTO, String> searchEmail;
    protected final TableColumn<SearchedRequstedDTO, String> searchId;
    protected final TextField txtUserName;
    protected final Button btnSearch;
    protected final TableView<SearchedRequstedDTO> tableView0;
    protected final TableColumn<SearchedRequstedDTO, String> requestUserName;
    protected final TableColumn<SearchedRequstedDTO, String> requestEmail;
    protected final TableColumn<SearchedRequstedDTO, String> requestId;
    protected final Label label;
    protected final Button btnAcceptFriend;
    protected final Button btnDeclineFriend;
    protected final Label label0;
    protected final Button btnAddFriend;
    private int userId;
    private String email;
    Socket socket;
    DataInputStream dis;
    PrintStream ps;
    ArrayList<SearchedRequstedDTO> searchedArrayList;
    ArrayList<SearchedRequstedDTO> requestedArrayList;

    public AddFriendRequestController(int userId ,String email, ArrayList<SearchedRequstedDTO> requestedArrayList) {
        this.requestedArrayList = requestedArrayList;
        ObservableList<SearchedRequstedDTO> requestTableData = FXCollections.observableArrayList(requestedArrayList);
        
        this.userId = userId;
        this.email=email;
        
        tableView = new TableView();
        searchUserName = new TableColumn();
        searchEmail = new TableColumn();
        searchId = new TableColumn();
        txtUserName = new TextField();
        btnSearch = new Button();
        tableView0 = new TableView();
        requestUserName = new TableColumn();
        requestEmail = new TableColumn();
        requestId = new TableColumn();
        label = new Label();
        btnAcceptFriend = new Button();
        btnDeclineFriend = new Button();
        label0 = new Label();
        btnAddFriend = new Button();

        setId("AnchorPane");
        setPrefHeight(500.0);
        setPrefWidth(596.0);

        tableView.setLayoutX(20.0);
        tableView.setLayoutY(107.0);
        tableView.setPrefHeight(330.0);
        tableView.setPrefWidth(266.0);
        tableView.getStylesheets().add(getClass().getResource("header.css").toExternalForm());

        searchUserName.setEditable(false);
        searchUserName.setPrefWidth(136.0);
        searchUserName.setSortable(false);
        searchUserName.setText("Username");

        searchEmail.setEditable(false);
        searchEmail.setPrefWidth(129.0);
        searchEmail.setSortable(false);
        searchEmail.setText("Email");

        searchId.setEditable(false);
        searchId.setPrefWidth(129.0);
        searchId.setSortable(false);
        searchId.setText("Id");

        searchUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        searchEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        searchId.setCellValueFactory(new PropertyValueFactory<>("id"));

        txtUserName.setLayoutX(20.0);
        txtUserName.setLayoutY(68.0);
        txtUserName.setPrefHeight(25.0);
        txtUserName.setPrefWidth(197.0);
        txtUserName.setPromptText("Enter username to search");
        txtUserName.setStyle("-fx-border-color: #7badec; -fx-border-radius: 15; -fx-background-radius: 15;");

        btnSearch.setLayoutX(230.0);
        btnSearch.setLayoutY(68.0);
        btnSearch.setMnemonicParsing(false);
        btnSearch.setPrefHeight(25.0);
        btnSearch.setPrefWidth(56.0);
        btnSearch.setStyle("-fx-background-radius: 15; -fx-border-radius: 15; -fx-background-color: transparent; -fx-border-color: #79a9e7;");
        btnSearch.setText("Search");
        btnSearch.setTextFill(javafx.scene.paint.Color.valueOf("#587ea9"));
        btnSearch.setCursor(Cursor.HAND);

        tableView0.setLayoutX(316.0);
        tableView0.setLayoutY(68.0);
        tableView0.setPrefHeight(370.0);
        tableView0.setPrefWidth(266.0);
        tableView0.getStylesheets().add(getClass().getResource("header.css").toExternalForm());

        requestUserName.setEditable(false);
        requestUserName.setPrefWidth(132.0);
        requestUserName.setSortable(false);
        requestUserName.setText("Username");

        requestEmail.setEditable(false);
        requestEmail.setPrefWidth(132.0);
        requestEmail.setSortable(false);
        requestEmail.setText("Email");

        requestId.setEditable(false);
        requestId.setPrefWidth(132.0);
        requestId.setSortable(false);
        requestId.setText("Id");
        
        requestUserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
        requestEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        requestId.setCellValueFactory(new PropertyValueFactory<>("id"));
        
        tableView0.setItems(requestTableData);

        label.setLayoutX(401.0);
        label.setLayoutY(30.0);
        label.setText("Pending List");
        label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label.setFont(new Font("System Bold", 18.0));

        btnAcceptFriend.setLayoutX(316.0);
        btnAcceptFriend.setLayoutY(452.0);
        btnAcceptFriend.setMnemonicParsing(false);
        btnAcceptFriend.setPrefHeight(25.0);
        btnAcceptFriend.setPrefWidth(129.0);
        btnAcceptFriend.setStyle("-fx-border-radius: 15; -fx-background-radius: 15; -fx-background-color: transparent; -fx-border-color: #79a9e7;");
        btnAcceptFriend.setText("Accept");
        btnAcceptFriend.setTextFill(javafx.scene.paint.Color.valueOf("#587ea9"));
        btnAcceptFriend.setCursor(Cursor.HAND);

        btnDeclineFriend.setLayoutX(453.0);
        btnDeclineFriend.setLayoutY(452.0);
        btnDeclineFriend.setMnemonicParsing(false);
        btnDeclineFriend.setPrefHeight(25.0);
        btnDeclineFriend.setPrefWidth(129.0);
        btnDeclineFriend.setStyle("-fx-border-radius: 15; -fx-background-radius: 15; -fx-background-color: transparent; -fx-border-color: #79a9e7;");
        btnDeclineFriend.setText("Decline");
        btnDeclineFriend.setTextFill(javafx.scene.paint.Color.valueOf("#587ea9"));
        btnDeclineFriend.setCursor(Cursor.HAND);

        label0.setLayoutX(78.0);
        label0.setLayoutY(30.0);
        label0.setText("Make A New Friend");
        label0.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        label0.setFont(new Font("System Bold", 18.0));

        btnAddFriend.setLayoutX(89.0);
        btnAddFriend.setLayoutY(452.0);
        btnAddFriend.setMnemonicParsing(false);
        btnAddFriend.setPrefHeight(25.0);
        btnAddFriend.setPrefWidth(129.0);
        btnAddFriend.setStyle("-fx-border-radius: 15; -fx-background-radius: 15; -fx-background-color: transparent; -fx-border-color: #79a9e7;");
        btnAddFriend.setText("Add Friend");
        btnAddFriend.setTextFill(javafx.scene.paint.Color.valueOf("#587ea9"));
        btnAddFriend.setCursor(Cursor.HAND);

        tableView.getColumns().add(searchUserName);
        tableView.getColumns().add(searchEmail);
        tableView.getColumns().add(searchId);
        getChildren().add(tableView);
        getChildren().add(txtUserName);
        getChildren().add(btnSearch);
        tableView0.getColumns().add(requestUserName);
        tableView0.getColumns().add(requestEmail);
        tableView0.getColumns().add(requestId);
        getChildren().add(tableView0);
        getChildren().add(label);
        getChildren().add(btnAcceptFriend);
        getChildren().add(btnDeclineFriend);
        getChildren().add(label0);
        getChildren().add(btnAddFriend);

        try {
            socket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(WishlistController.class.getName()).log(Level.SEVERE, null, ex);
        }

        btnSearch.addEventHandler(ActionEvent.ACTION, event -> {
            if (!txtUserName.getText().isEmpty()) {
                try {
                    JSONObject searchData = new JSONObject();
                    searchData.put("HEADER", "fnSearchFriend");
                    searchData.put("USERID", userId);
                    searchData.put("SEARCH", txtUserName.getText());
                    ps.println(searchData.toString());

                    String response = dis.readLine();

                    if (response != null && !response.isEmpty()) {
                        JSONArray searchedData = new JSONArray(response);
                        searchedArrayList = new ArrayList<>();

                        for (int i = 0; i < searchedData.length(); i++) {
                            JSONObject searchDataIndex = searchedData.getJSONObject(i);
                            int id = searchDataIndex.getInt("Id");
                            String username = searchDataIndex.getString("UserName");
                            String email2 = searchDataIndex.getString("Email");
                            SearchedRequstedDTO searchedDTO = new SearchedRequstedDTO(id, username, email2);

                            searchedArrayList.add(searchedDTO);
                        }

                        ObservableList<SearchedRequstedDTO> searchTableData = FXCollections.observableArrayList(searchedArrayList);
                        tableView.setItems(searchTableData);
                    } else {
                        // Handle empty response here
                        System.out.println("No results found.");
                        tableView.getItems().clear();
                    }
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnAddFriend.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchedRequstedDTO searchDTO = tableView.getSelectionModel().getSelectedItem();
                if(searchDTO != null){
                    try {
                        int requestFriendId = searchDTO.getId();
                        String receiverEmail=searchDTO.getEmail();
                        JSONObject addFriendData = new JSONObject();
                        addFriendData.put("HEADER", "fnAddFriend");
                        // addFriend.put("senderEmail", userDto.getEmail());
                       // addFriend.put("receiverEmail", friendEmail.getText());
                    //ps.println(addFriend.toString());
                        addFriendData.put("USERID", userId);
                        addFriendData.put("REQUESTID", requestFriendId);
                        addFriendData.put("senderEmail", email);
                        //addFriend.put("receiverEmail", receiverEmail);
                        
                        ps.println(addFriendData);
                        
                        String response = dis.readLine();
                        
                        if(response.equals("Request Sent")){
                            int selectedId = tableView.getSelectionModel().getSelectedIndex();
                            tableView.getItems().remove(selectedId);
                            tableView.getSelectionModel().clearSelection();
                            JOptionPane.showMessageDialog(null, "Request Sent Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Error could't send request", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                }
                else{
                    JOptionPane.showMessageDialog(null, "Choose user first", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnAcceptFriend.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchedRequstedDTO requestDTO = tableView0.getSelectionModel().getSelectedItem();
                if(requestDTO!=null){
                    try {
                        int requestId = requestDTO.getId();
                        JSONObject acceptData = new JSONObject();
                        acceptData.put("HEADER", "fnAcceptFriend");
                        acceptData.put("USERID",userId);
                        acceptData.put("REQUESTID", requestId);
                        acceptData.put("senderEmail", email);
                        ps.println(acceptData.toString());
                        
                        String response = dis.readLine();
                        if(response.equals("Friend Accepted")){
                            int selectedId = tableView0.getSelectionModel().getSelectedIndex();
                            tableView0.getItems().remove(selectedId);
                            tableView0.getSelectionModel().clearSelection();
                            JOptionPane.showMessageDialog(null, "Friend Accepted Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Error could't accept friend", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Choose user first", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        btnDeclineFriend.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                SearchedRequstedDTO requestDTO = tableView0.getSelectionModel().getSelectedItem();
                if(requestDTO!=null){
                    try {
                        int requestId = requestDTO.getId();
                        JSONObject acceptData = new JSONObject();
                        acceptData.put("HEADER", "fnDeclineFriend");
                        acceptData.put("USERID",userId);
                        acceptData.put("REQUESTID", requestId);
                        ps.println(acceptData.toString());
                        
                        String response = dis.readLine();
                        if(response.equals("Friend Declined")){
                            int selectedId = tableView0.getSelectionModel().getSelectedIndex();
                            tableView0.getItems().remove(selectedId);
                            tableView0.getSelectionModel().clearSelection();
                            JOptionPane.showMessageDialog(null, "Friend Declined Successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                        }
                        else{
                            JOptionPane.showMessageDialog(null, "Error could't Decline friend", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
                else{
                    JOptionPane.showMessageDialog(null, "Choose user first", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }
}
