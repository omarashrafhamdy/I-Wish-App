package i.wish.client;

import DTO.UserDataDTO;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalDate;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;

public class AddBalanceHandler {
    private final UserDataDTO userDto;
    private final DataInputStream dis;
    private final PrintStream ps;
    private final Label labelBalance;

    public AddBalanceHandler(UserDataDTO userDto, DataInputStream dis, PrintStream ps, Label labelBalance) {
        this.userDto = userDto;
        this.dis = dis;
        this.ps = ps;
        this.labelBalance = labelBalance;
    }
    
    public void handle(ActionEvent event) {
                // Create a GridPane to organize the input fields
                VBox vbox = new VBox();
                vbox.setAlignment(Pos.CENTER);
                vbox.setSpacing(10);
                vbox.setStyle("-fx-background-color: white;");

                HBox cardNumberBox = new HBox();
                cardNumberBox.setAlignment(Pos.CENTER_LEFT);
                TextField cardNumberField = new TextField();
                cardNumberField.setPromptText("Card Number* (16 digits)");
                cardNumberField.setMaxWidth(400);
                cardNumberBox.getChildren().addAll(cardNumberField);
                vbox.getChildren().add(cardNumberBox);

                // CVV
                HBox cvvBox = new HBox();
                cvvBox.setAlignment(Pos.CENTER_LEFT);
                TextField cvvField = new TextField();
                cvvField.setPromptText("CVV* (3 digits)");
                cvvField.setMaxWidth(400);
                cvvBox.getChildren().addAll(cvvField);
                vbox.getChildren().add(cvvBox);

                // Expiry Date
                HBox expiryDateBox = new HBox();
                expiryDateBox.setAlignment(Pos.CENTER_LEFT);
                DatePicker expiryDatePicker = new DatePicker();
                expiryDatePicker.setPromptText("Expiry Date* (MM/yyyy)");
                expiryDatePicker.setMaxWidth(150);
                expiryDateBox.getChildren().addAll(expiryDatePicker);
                vbox.getChildren().add(expiryDateBox);

                // Amount
                HBox amountBox = new HBox();
                amountBox.setAlignment(Pos.CENTER_LEFT);
                TextField amountField = new TextField();
                amountField.setPromptText("Amount*");
                amountField.setMaxWidth(400);
                amountBox.getChildren().addAll(amountField);
                vbox.getChildren().add(amountBox);

                // Image
                ImageView cardImage = new ImageView(new Image(getClass().getResource("card_icon1.jpg").toExternalForm()));
                cardImage.setFitHeight(200);
                cardImage.setFitWidth(200);

                // Set custom size for the dialog
                vbox.setPrefSize(250, 200);

                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Add Balance");
                dialog.setHeaderText("Enter payment details:");
                dialog.getDialogPane().setContent(vbox);

                // Add the image to seperate HBox
                HBox imageRow = new HBox();
                imageRow.getChildren().addAll(vbox, cardImage);
                dialog.getDialogPane().setContent(imageRow);

                // Add buttons to the dialog
                ButtonType confirmButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, ButtonType.CANCEL);

                // Disable the Confirm button initially
                Node confirmButton = dialog.getDialogPane().lookupButton(confirmButtonType);
                confirmButton.setDisable(true);

                // Listen for changes in the input fields to enable/disable the Confirm button
                amountField.textProperty().addListener((observable, oldValue, newValue) -> {
                    confirmButton.setDisable(newValue.isEmpty() || !newValue.matches("\\d*\\.?\\d*"));
                });

                cardNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
                    confirmButton.setDisable(newValue.length() != 16 || !newValue.matches("\\d+"));
                });

                cvvField.textProperty().addListener((observable, oldValue, newValue) -> {
                    confirmButton.setDisable(newValue.length() != 3 || !newValue.matches("\\d+"));
                });

                expiryDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                    LocalDate currentDate = LocalDate.now();
                    confirmButton.setDisable(newValue == null || newValue.isBefore(currentDate));
                });

                // Request focus on the Amount field initially
                Platform.runLater(() -> amountField.requestFocus());

                // Show the dialog and wait for user input
                Optional<?> result = dialog.showAndWait();

                // Process the result when the Confirm button is clicked
                if (result.isPresent() && result.get() == confirmButtonType) {
                    if (cardNumberField.getText().length() != 16 || !cardNumberField.getText().matches("\\d+")) {
                        showAlert("Invalid card number", "Please enter a valid 16-digit card number.");
                    } else if (cvvField.getText().length() != 3 || !cvvField.getText().matches("\\d+")) {
                        showAlert("Invalid CVV", "Please enter a valid 3-digit CVV.");
                    } else if (expiryDatePicker.getValue() == null || expiryDatePicker.getValue().isBefore(LocalDate.now())) {
                        showAlert("Invalid expiry date", "Please enter a valid future date.");
                    } else if (amountField.getText().isEmpty() || !amountField.getText().matches("\\d*\\.?\\d*")) {
                        showAlert("Invalid amount", "Please enter a numeric value.");
                    } else {
                        try {
                            int amountToAdd = Integer.parseInt(amountField.getText());
                            int currentBalance = userDto.getBalance();
                            int newBalance = currentBalance + amountToAdd;

                            userDto.setBalance(newBalance);
                            labelBalance.setText(String.valueOf(newBalance));

                            // Update balance in the database
                            if (updateBalanceInDatabase(userDto.getId(), newBalance)) {
                                System.out.println("Balance updated successfully");
                            } else {
                                System.out.println("Failed to update balance");
                            }
                        } catch (NumberFormatException e) {
                            // Handle invalid input (non-numeric)
                            System.out.println("Invalid input. Please enter a numeric value.");
                        }
                    }
                } else {
                    // Handle the case when the user closes the dialog without confirming
                    System.out.println("Payment canceled.");
                }
            }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean updateBalanceInDatabase(int userId, int newBalance) {
        try {
            Socket socket = new Socket("127.0.0.1", 5005);
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            PrintStream ps = new PrintStream(socket.getOutputStream());

            JSONObject balanceData = new JSONObject();
            balanceData.put("HEADER", "fnUpdateBalance");
            balanceData.put("USER_ID", userId);
            balanceData.put("NEW_BALANCE", newBalance);
            ps.println(balanceData.toString());

            // Read the response from the server
            String response = dis.readLine();

            // Handle the response as needed
            // For example, show a success message or log an error
            if (response.equals("SUCCESS")) {
                System.out.println("Balance updated successfully");
                return true;
            } else {
                System.out.println("Failed to update balance");
                return false;
            }

        } catch (IOException ex) {
            //System.out.println("Connection Lost From the Server");
            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }
}