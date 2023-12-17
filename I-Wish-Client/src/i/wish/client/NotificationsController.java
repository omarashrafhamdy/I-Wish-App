package i.wish.client;

import DTO.NotificationsDTO;
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
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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

public class NotificationsController extends AnchorPane {

    Socket socket;
    DataInputStream dis;
    PrintStream ps;
    protected final Label label;
    protected final TableView<NotificationsDTO> tableView;
    protected final TableColumn<NotificationsDTO, String> message;
    //UserDataDTO userDto;
    //int userId;

    public NotificationsController(List<NotificationsDTO> notficationList){
        //this.userDto = userDto;
        //this.userId = userId;
        
        String imagePath = "WishlistBG.png"; 
        Image backgroundImage = new Image(getClass().getResource(imagePath).toExternalForm());
        
        
        BackgroundSize backgroundSize = new BackgroundSize(1.0, 1.0, true, true, false, false);
        
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        setBackground(new Background(background));
        
        tableView = new TableView();
        label = new Label();
        message = new TableColumn("Message");
        setId("AnchorPane");
        setPrefHeight(500.0);
        setPrefWidth(596.0);
        label.setLayoutX(230.0);
        label.setLayoutY(25.0);
        label.setText("Notification");
        label.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        label.setFont(new Font("System Bold", 25.0));
        tableView.setLayoutX(18.0);
        tableView.setLayoutY(92.0);
        tableView.setPrefHeight(370.0);
        tableView.setPrefWidth(563.0);
        tableView.getStylesheets().add(getClass().getResource("header.css").toExternalForm());
        message.setPrefWidth(562.0);
        message.setStyle("-fx-alignment: CENTER;");
        message.setCellValueFactory(new PropertyValueFactory<>("message"));
        getChildren().add(tableView);
        getChildren().add(label);
        try {
            socket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(socket.getInputStream());
            ps = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Connection Lost From the Server", "Connection Error", JOptionPane.ERROR_MESSAGE);
        }
            tableView.getColumns().add(message);
            tableView.setItems(FXCollections.observableArrayList(notficationList));
            //System.out.println("hello" + response);
        
    }
}
