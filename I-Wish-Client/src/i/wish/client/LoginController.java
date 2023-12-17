package i.wish.client;

import DTO.ContributorInfoDTO;
import DTO.ItemDTO;
import DTO.UserDataDTO;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginController extends AnchorPane {

    protected final TextField txtEmail;
    protected final PasswordField txtPassword;
    protected final Button btnSignIn;
    protected final Label label;
    protected final Button btnRegister;
    protected final ImageView imageView;
    Socket socket;
    DataInputStream dis;
    public PrintStream ps;
    public UserDataDTO userDataDTO = new UserDataDTO();

    public LoginController() {

        txtEmail = new TextField();
        txtPassword = new PasswordField();
        btnSignIn = new Button();
        label = new Label();
        btnRegister = new Button();
        imageView = new ImageView();

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(500.0);
        setPrefWidth(800.0);
        
        String imagePath = "LoginBG.png"; // Change this to the actual path of your image
        Image backgroundImage = new Image(getClass().getResource(imagePath).toExternalForm());
        
        // Set the BackgroundSize to 100% width and height without preserving the aspect ratio
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(background));

        txtEmail.setLayoutX(281.0);
        txtEmail.setLayoutY(240.0);
        txtEmail.setPrefHeight(25.0);
        txtEmail.setPrefWidth(265.0);
        txtEmail.setPromptText("Username");
        txtEmail.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        txtPassword.setLayoutX(281.0);
        txtPassword.setLayoutY(284.0);
        txtPassword.setPrefHeight(25.0);
        txtPassword.setPrefWidth(265.0);
        txtPassword.setPromptText("Password");
        txtPassword.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        btnSignIn.setLayoutX(311.0);
        btnSignIn.setLayoutY(341.0);
        btnSignIn.setMnemonicParsing(false);
        btnSignIn.setPrefHeight(25.0);
        btnSignIn.setPrefWidth(204.0);
        btnSignIn.setStyle("-fx-background-radius: 10; -fx-background-color: #7badec;");;
        btnSignIn.setText("Sign In");
        btnSignIn.setTextFill(javafx.scene.paint.Color.WHITE);

        label.setLayoutX(339.0);
        label.setLayoutY(400.0);
        label.setText("New User?");

        btnRegister.setLayoutX(396.0);
        btnRegister.setLayoutY(396.0);
        btnRegister.setMnemonicParsing(false);
        btnRegister.setStyle("-fx-background-color: transparent;");
        btnRegister.setText("Create Account");
        btnRegister.setTextFill(javafx.scene.paint.Color.valueOf("#5881b4"));

        imageView.setFitHeight(179.0);
        imageView.setFitWidth(161.0);
        imageView.setLayoutX(333.0);
        imageView.setLayoutY(29.0);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);
        imageView.setImage(new Image(getClass().getResource("logo2.png").toExternalForm()));

        getChildren().add(txtEmail);
        getChildren().add(txtPassword);
        getChildren().add(btnSignIn);
        getChildren().add(label);
        getChildren().add(btnRegister);
        getChildren().add(imageView);

        btnSignIn.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    socket = new Socket("127.0.0.1", 5005);
                    dis = new DataInputStream(socket.getInputStream());
                    ps = new PrintStream(socket.getOutputStream());

                    // Creating a JSON object for login
                    JSONObject userData = new JSONObject();
                    userData.put("HEADER", "fnLogin");
                    userData.put("USERNAME", txtEmail.getText());
                    userData.put("PASSWORD", txtPassword.getText());
                    ps.println(userData.toString());

                    // Handling server response in a separate thread
                    new Thread() {
                        public void run() {

                            try {
                                String response = dis.readLine();

                                Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (response.equals("Login Successfully")) {
                                            try {
                                                JOptionPane.showMessageDialog(null, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                                                String response2 = dis.readLine();
                                                JSONObject r = new JSONObject(response2);
                                                userDataDTO = new UserDataDTO(
                                                        r.getInt("id"),
                                                        r.getString("username"),
                                                        r.getString("email"),
                                                        r.getString("Name"),
                                                        Integer.parseInt(r.getString("Balance")),
                                                        parseItemsJson(r.getJSONArray("items")));

                                                System.out.println(userDataDTO.getWishlist());

                                                //JSONObject getUserData = new JSONObject();
                                                //getUserData.put("HEADER", "fnWishlist");
                                                //getUserData.put("USERNAME",txtEmail.getText());
                                                Parent root = new MainPageController(userDataDTO); 
                                                Scene scene = new Scene(root);
                                                // Get the stage from the button
                                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                                stage.setScene(scene);
                                                stage.show();
                                            } catch (IOException ex) {
                                                JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
                                            }
                                        } else {
                                            JOptionPane.showMessageDialog(null, "Login Failed. Please check your credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                                        }
                                    }
                                });
                            } catch (IOException ex) {
                                // Handle disconnection from the server
                                JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);

                                // Close the client application
                                Platform.runLater(new Runnable() {

                                    @Override
                                    public void run() {

                                        /*Stage stage = (Stage) getScene().getWindow();
                                        stage.close();*/
                                    }
                                });

                            }
                        }

                    }.start();
                } catch (IOException ex) {
                    // Handle connection failure
                    JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);

                    // Close the client application
                    /*Stage stage = (Stage) getScene().getWindow();
                    stage.close();*/
                }
            }
        });

        btnRegister.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = new RegistrationController();

                Scene scene = new Scene(root);

                
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.setMaxHeight(600);
                stage.setMaxWidth(850);
                stage.show();
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

}
