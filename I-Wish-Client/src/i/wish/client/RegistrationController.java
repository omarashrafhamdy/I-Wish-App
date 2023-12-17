package i.wish.client;

import java.io.*;
import java.net.*;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.event.*;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import org.json.JSONObject;

public class RegistrationController extends AnchorPane {

    protected final Text text;
    protected final TextField txtFName;
    protected final TextField txtLName;
    protected final TextField txtUserName;
    protected final TextField txtEmail;
    protected final PasswordField txtPassword;
    protected final TextField txtBalance;
    protected final DatePicker dateDOB;
    protected final Button btnSignUp;
    protected final Label label;
    protected final Button btnSignIn;
    
    

    public RegistrationController() {

        text = new Text();
        txtFName = new TextField();
        txtLName = new TextField();
        txtUserName = new TextField();
        txtEmail = new TextField();
        txtPassword = new PasswordField();
        txtBalance = new TextField();
        dateDOB = new DatePicker();
        btnSignUp = new Button();
        label = new Label();
        btnSignIn = new Button();
        
        String imagePath = "RegistrationBG.png"; // Change this to the actual path of your image
        Image backgroundImage = new Image(getClass().getResource(imagePath).toExternalForm());
        
        // Set the BackgroundSize to 100% width and height without preserving the aspect ratio
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(background));
        
        final LocalDate maxDate = LocalDate.of(2018, 12, 31);
        dateDOB.setDayCellFactory(new javafx.util.Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(DatePicker picker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate date, boolean empty) {
                        super.updateItem(date, empty);
                        if (date.isAfter(maxDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;"); // Change the background color of disabled dates
                        }
                    }
                };
            }
        });
        
        dateDOB.setValue(maxDate);

        setMaxHeight(USE_PREF_SIZE);
        setMaxWidth(USE_PREF_SIZE);
        setMinHeight(USE_PREF_SIZE);
        setMinWidth(USE_PREF_SIZE);
        setPrefHeight(500.0);
        setPrefWidth(800.0);

        text.setFill(javafx.scene.paint.Color.valueOf("#7badec"));
        text.setLayoutX(162.0);
        text.setLayoutY(54.0);
        text.setStrokeType(javafx.scene.shape.StrokeType.OUTSIDE);
        text.setStrokeWidth(0.0);
        text.setText("Register on i-Wish");
        text.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);
        text.setWrappingWidth(485.00001430511475);
        text.setFont(new Font("System Bold", 26.0));

        txtFName.setLayoutX(162.0);
        txtFName.setLayoutY(84.0);
        txtFName.setPrefHeight(38.0);
        txtFName.setPrefWidth(233.0);
        txtFName.setPromptText("Enter Your First Name");
        txtFName.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        txtLName.setLayoutX(414.0);
        txtLName.setLayoutY(84.0);
        txtLName.setPrefHeight(38.0);
        txtLName.setPrefWidth(233.0);
        txtLName.setPromptText("Enter Your Last Name");
        txtLName.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        txtUserName.setLayoutX(162.0);
        txtUserName.setLayoutY(139.0);
        txtUserName.setPrefHeight(38.0);
        txtUserName.setPrefWidth(485.0);
        txtUserName.setPromptText("Enter Your User Name");
        txtUserName.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        txtEmail.setLayoutX(163.0);
        txtEmail.setLayoutY(192.0);
        txtEmail.setPrefHeight(38.0);
        txtEmail.setPrefWidth(485.0);
        txtEmail.setPromptText("Enter Your Email Address");
        txtEmail.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        txtPassword.setLayoutX(163.0);
        txtPassword.setLayoutY(296.0);
        txtPassword.setPrefHeight(38.0);
        txtPassword.setPrefWidth(485.0);
        txtPassword.setPromptText("Enter Your Password");
        txtPassword.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        txtBalance.setLayoutX(162.0);
        txtBalance.setLayoutY(346.0);
        txtBalance.setPrefHeight(38.0);
        txtBalance.setPrefWidth(485.0);
        txtBalance.setPromptText("Enter Your Start Balance");
        txtBalance.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        dateDOB.setLayoutX(163.0);
        dateDOB.setLayoutY(243.0);
        dateDOB.setPrefHeight(38.0);
        dateDOB.setPrefWidth(485.0);
        dateDOB.setPromptText("Enter Your Date Of Birth");
        dateDOB.setStyle("-fx-border-color: #7badec; -fx-border-radius: 5; -fx-background-radius: 5;");

        btnSignUp.setLayoutX(349.0);
        btnSignUp.setLayoutY(412.0);
        btnSignUp.setMnemonicParsing(false);
        btnSignUp.setPrefHeight(29.0);
        btnSignUp.setPrefWidth(113.0);
        btnSignUp.setStyle("-fx-background-radius: 10; -fx-background-color: #7badec;");
        btnSignUp.setText("Sign up");
        btnSignUp.setTextFill(javafx.scene.paint.Color.WHITE);

        label.setLayoutX(334.0);
        label.setLayoutY(458.0);
        label.setPrefHeight(17.0);
        label.setPrefWidth(106.0);
        label.setText("Have an account?");

        btnSignIn.setLayoutX(423.0);
        btnSignIn.setLayoutY(452.0);
        btnSignIn.setMnemonicParsing(false);
        btnSignIn.setStyle("-fx-background-color: transparent;");
        btnSignIn.setText("Sign in");
        btnSignIn.setTextFill(javafx.scene.paint.Color.valueOf("#5881b4"));
        btnSignIn.setFont(new Font(13.0));

        getChildren().add(text);
        getChildren().add(txtFName);
        getChildren().add(txtLName);
        getChildren().add(txtUserName);
        getChildren().add(txtEmail);
        getChildren().add(txtPassword);
        getChildren().add(txtBalance);
        getChildren().add(dateDOB);
        getChildren().add(btnSignUp);
        getChildren().add(label);
        getChildren().add(btnSignIn);
        
        
        
        btnSignIn.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Parent root = new LoginController(); // Replace this with your LoginController setup

                Scene scene = new Scene(root);

                // Get the stage from the button
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setScene(scene);
                stage.setMaxHeight(600);
                stage.setMaxWidth(850);
                stage.show();
            }
        });
        String passwordPattern = "^.{6,}$";
        String emailPattern = "^[\\w-\\.]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$";
        btnSignUp.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                boolean allFieldsFilled = !txtUserName.getText().isEmpty()
                        && !txtFName.getText().isEmpty()
                        && !txtLName.getText().isEmpty()
                        && !txtEmail.getText().isEmpty()
                        && dateDOB.getValue() != null
                        && // Check if datepicker has a value
                        !txtPassword.getText().isEmpty()
                        && !txtBalance.getText().isEmpty();
                if(!allFieldsFilled){
                    // Display message if any field is empty
                    JOptionPane.showMessageDialog(null, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(!txtPassword.getText().matches(passwordPattern)){
                    JOptionPane.showMessageDialog(null, "Password must be at least 6 characters long.", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(!txtEmail.getText().matches(emailPattern)){
                    JOptionPane.showMessageDialog(null, "Email should be entered correctly.", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(!txtBalance.getText().matches("^\\d+$")){
                    JOptionPane.showMessageDialog(null, "Balance Should be a number", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
                }
                else if(Integer.parseInt(txtBalance.getText())<0){
                    JOptionPane.showMessageDialog(null, "Balance must be greater than or equal zero.", "Input Validation Error", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    try {
                        // Connect to server and send data
                        Socket socket = new Socket("127.0.0.1", 5005);
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        PrintStream ps = new PrintStream(socket.getOutputStream());

                        // Create a JSON object with the data from the GUI text fields
                        JSONObject userData = new JSONObject();
                        userData.put("HEADER", "fnRegistration");
                        userData.put("USERNAME", txtUserName.getText());
                        userData.put("FNAME", txtFName.getText());
                        userData.put("LNAME", txtLName.getText());
                        userData.put("EMAIL", txtEmail.getText());
                        userData.put("DOB", dateDOB.getValue().toString());
                        userData.put("PASSWORD", txtPassword.getText());
                        userData.put("BALANCE", txtBalance.getText());
                        ps.println(userData.toString());

                        // Read response from server
                        String response = dis.readLine();

                        Platform.runLater(new Runnable() {

                            public void run() {
                                if (response.equals("Account Created")) {
                                    JOptionPane.showMessageDialog(null, "Account Created", "Success", JOptionPane.INFORMATION_MESSAGE);
                                    Parent root = new LoginController(); // Replace this with your LoginController setup
                                    Scene scene = new Scene(root);
                                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    stage.setScene(scene);
                                    stage.show();
                                } else {
                                    JOptionPane.showMessageDialog(null, "Registration Failed. Username Or Email Already Exist", "Error", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        });
                    } catch (IOException ex) {
                        // Handle connection failure
                        JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);

                        //Close the App
                        /*Stage stage = (Stage) getScene().getWindow();
                        stage.close();*/
                    }
                }
                    
                
            }
        });

    }
}
