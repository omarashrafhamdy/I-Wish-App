package i.wish.server;

import DTO.DeleteFriendDTO;
import DTO.DeleteItemDTO;
import DTO.LoginDTO;
import DTO.RegistrationDTO;
import Database.DataAccessLayer;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class IWishServer {

    ServerSocket serverSocket;

    public static void main(String[] args) {
        new IWishServer();
    }

    public IWishServer() {
        try {
            serverSocket = new ServerSocket(5005);
            while (true) {
                Socket s = serverSocket.accept();
                new ClientHandler(s);
            }
        } catch (IOException ex) {
            Logger.getLogger(IWishServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

class ClientHandler extends Thread {

    DataInputStream dis;
    PrintStream ps;
    Socket s;
    //int currClient;
    static Vector<ClientHandler> clientsVector = new Vector<ClientHandler>();

    public ClientHandler(Socket s) {
        try {
            this.s = s;
            dis = new DataInputStream(s.getInputStream());
            ps = new PrintStream(s.getOutputStream());
            clientsVector.add(this);
            //currClient = clientsVector.indexOf(this);
            start();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        DataAccessLayer dal = new DataAccessLayer();
        while (true) {
            try {
                String userData = dis.readLine();
                if (userData == null) {
                    // Client disconnected
                    clientsVector.remove(this);
                    break;
                }

                JSONObject jsonObject = new JSONObject(userData);
                String method = jsonObject.getString("HEADER");

                switch (method) {
                    case "fnLogin":
                        LoginDTO loginDTO = new LoginDTO(jsonObject.getString("HEADER"),
                                jsonObject.getString("USERNAME"), jsonObject.getString("PASSWORD"));

                        if (dal.checkLogin(loginDTO.getUserName(), loginDTO.getPassword())) {
                            ps.println("Login Successfully");
                            ps.println(dal.getUserData(loginDTO.getUserName()).toString());
                        } else {
                            ps.println("Error");
                        }
                        break;
                    case "fnRegistration":
                        RegistrationDTO registrationDTO = new RegistrationDTO(jsonObject.getString("HEADER"),
                                jsonObject.getString("FNAME"), jsonObject.getString("LNAME"),
                                jsonObject.getString("USERNAME"), jsonObject.getString("EMAIL"),
                                jsonObject.getString("PASSWORD"), jsonObject.getString("DOB"),
                                Integer.parseInt(jsonObject.getString("BALANCE")));
                        if (dal.registerAccont(registrationDTO.getUserName(), registrationDTO.getEmail(),
                                registrationDTO.getfName(), registrationDTO.getlName(), registrationDTO.getDob(),
                                registrationDTO.getPassword(), registrationDTO.getBalance())) {
                            ps.println("Account Created");
                        } else {
                            ps.println("Error");
                        }
                        break;
                    case "fnUserWishlist":
                        ps.println(dal.getUserWishlist(jsonObject.getInt("USERID")));
                        break;
                    case "fnDeletItem":
                        DeleteItemDTO deleteItemDTO = new DeleteItemDTO(jsonObject.getInt("USERID"), jsonObject.getInt("ITEMID"), jsonObject.getInt("REMAININGPRICE"));
                        System.out.println(deleteItemDTO.getItemId() + " " + deleteItemDTO.getUserId() + "" + deleteItemDTO.getRemainingPrice());
                        if (dal.deleteItem(deleteItemDTO.getUserId(), deleteItemDTO.getItemId(), deleteItemDTO.getRemainingPrice())) {
                           ps.println("Item Deleted");
                        } else {
                            ps.println("Error");
                        }
                        break;

                    case "fnFriendList":
                        ps.println(dal.getFriendData(jsonObject.getInt("USERID")));
                        break;

                    case "fnDeleteFriend":
                        DeleteFriendDTO deleteFriendDTO = new DeleteFriendDTO(jsonObject.getInt("USERID"), jsonObject.getInt("FRIENDID"));
                        if (dal.deleteFriend(deleteFriendDTO.getUserId(), deleteFriendDTO.getFriendId())) {
                            ps.println("Friend Deleted");
                        } else {
                            ps.println("Error");
                        }
                        break;
                    case "fnUpdateBalance":
                        // Update the balance in the database
                        int userId = jsonObject.getInt("USER_ID");
                        int newBalance = jsonObject.getInt("NEW_BALANCE");

                        if (dal.updateBalance(userId, newBalance)) {
                            ps.println("SUCCESS");
                        } else {
                            ps.println("ERROR");
                        }
                        break;
                    case "fnGetItems":
                        // Fetch the list of items from the database
                        // Adjust the method to match your database retrieval logic
                        JSONArray itemsArray = dal.getItems();
                        // Send the list of items to the client
                        ps.println(itemsArray.toString());
                        break;
                    case "fnAddItemToWishlist":
                        int user_Id = jsonObject.getInt("USER_ID");
                        int item_Id = jsonObject.getInt("ITEM_ID");
                        int paid = jsonObject.getInt("PAID");
                        int item_price = jsonObject.getInt("ITEM_PRICE");

                        if (dal.addItemToWishlist(user_Id, item_Id, paid, item_price)) {
                            ps.println("Item added successfully");
                        } else {
                            ps.println("Error");
                        }
                        break;
                    case "fnContribute":
                        int contributeUserId = jsonObject.getInt("USER_ID");
                        int contributeFriendId = jsonObject.getInt("FRIEND_ID");
                        int contributeItemId = jsonObject.getInt("ITEM_ID");
                        String contributeItemName = jsonObject.getString("itemName");
                        int contributionAmount = jsonObject.getInt("CONTRIBUTION_AMOUNT");
                        int itemPrice = (int) dal.getItemPrice(contributeItemId);
                        String name=jsonObject.getString("FNAME");
                        String friend_name=jsonObject.getString("friend_name");
                        if (contributionAmount <= itemPrice) {
                            boolean contributionSuccess = dal.updateRemainingPrice(contributeFriendId, contributeItemId, contributionAmount,contributeItemName,friend_name);
                            System.out.println(contributionSuccess);
                            if (contributionSuccess) {
                                // Update the user's balance
                                int userBalance = (int) dal.getUserBalance(contributeUserId);
                                int newContributeBalance = userBalance - contributionAmount;
                                dal.updateBalance(contributeUserId, newContributeBalance);
                                dal.addContributor(contributeUserId, contributeItemId, contributeFriendId, contributionAmount,name,contributeItemName);

                                ps.println("Contribution successful");
                            } else {
                                ps.println("Contribution failed");
                            }
                        } else {
                            ps.println("Contribution amount exceeds item price");
                        }
                        break;
                    case "fnLogout":
                        clientsVector.remove(this);
                        break;
                    case "fnSearchFriend":
                        int user_id = jsonObject.getInt("USERID");
                        String searchText = jsonObject.getString("SEARCH");
                        JSONArray searchedFriendsArray = dal.searchFriend(user_id, searchText);
                        ps.println(searchedFriendsArray);
                        break;
                    case "fnFriendRequestList":
                        int userid = jsonObject.getInt("USERID");
                        JSONArray FriendRequestsArray = dal.ReuqestFriend(userid);
                        ps.println(FriendRequestsArray);
                        break;
                    case "fnAddFriend":
                        if (dal.addFriend(jsonObject.getInt("USERID"),jsonObject.getInt("REQUESTID"),jsonObject.getString("senderEmail"))) {
                            ps.println("Request Sent");
                        } else {
                            ps.println("Error");
                        }
                        break;
                        
                    case "fnAcceptFriend":
                        if (dal.acceptFriend(jsonObject.getInt("USERID"),jsonObject.getInt("REQUESTID"),jsonObject.getString("senderEmail"))) {
                            ps.println("Friend Accepted");
                        } else {
                            ps.println("Error");
                        }
                        break;
                    case "fnDeclineFriend":
                        if (dal.declineFriend(jsonObject.getInt("USERID"),jsonObject.getInt("REQUESTID"))) {
                            ps.println("Friend Declined");
                        } else{
                            ps.println("Error");
                        }
                        break;
                         case "fnNotifications":
                        int id =jsonObject.getInt("id");
                        JSONArray notificationsArray = dal.getNotificationsData(id);
                        //System.out.println("Hi" + notificationsArray.toString());
                        ps.println(notificationsArray.toString());
                        System.out.println(notificationsArray);
                        break;
                }
            } catch (IOException ex) {
                // Client disconnected
                clientsVector.remove(this);
                break;
            } catch (SQLException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
