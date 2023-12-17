package Database;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataAccessLayer {

    Connection con = null;
    PreparedStatement prepare;
    ResultSet rs;
    boolean x;

    /*public DataAccessLayer(Connection con){
        this.con = con;
    }*/
    public boolean checkLogin(String username, String password) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            String sql = "SELECT * FROM users where username = ? and password = ? ;";
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username);
                prepare.setString(2, password);
                rs = prepare.executeQuery();
                if (rs.next()) {
                    // correct username and password
                    //System.out.println("Login Success");
                    return true;
                } else {
                    //incorrect username or password
                    //System.out.println("Login Failed");
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    public boolean registerAccont(String username, String email, String fName, String lName, String dob, String password, int balance) throws SQLException {
        try {
            int result = -1;
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            String sql = "INSERT INTO Users (Username, Email, Fname, Lname, DateOfBirth, Password, Balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, username);
                prepare.setString(2, email);
                prepare.setString(3, fName);
                prepare.setString(4, lName);
                prepare.setString(5, dob);
                prepare.setString(6, password);
                prepare.setInt(7, balance);
                result = prepare.executeUpdate();
                if (result != -1) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }

    public JSONObject getUserData(String userName) {
        JSONObject result = new JSONObject();

        try {/*
            String jdbcUrl = "jdbc:mysql://localhost:3306/iwishapp";
            String dbUser = "root";
            String dbPassword = "israa123";*/

            try (Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123")) {
                String userDataQuery = "SELECT * FROM users WHERE username = ?";
                try (PreparedStatement userDataStatement = connect.prepareStatement(userDataQuery)) {
                    userDataStatement.setString(1, userName);
                    try (ResultSet userDataResult = userDataStatement.executeQuery()) {
                        if (userDataResult.next()) {
                            int userId = userDataResult.getInt("id");

                            String wishlistQuery = "SELECT w.item_id, i.image, i.name, MIN(w.paid) AS paid, w.item_price, GROUP_CONCAT(c.contributer_name) AS contributors, GROUP_CONCAT(c.amount) AS contribution_amounts FROM items i INNER JOIN wishlist w ON i.id = w.item_id LEFT JOIN contributers c ON w.item_id = c.item_id AND w.user_id = c.owner_id WHERE w.user_id = ? GROUP BY w.item_id, i.image, i.name, w.item_price";

                            try (PreparedStatement wishlistStatement = connect.prepareStatement(wishlistQuery)) {
                                wishlistStatement.setInt(1, userId);
                                try (ResultSet wishlistResult = wishlistStatement.executeQuery()) {
                                    JSONArray itemsArray = new JSONArray();

                                    while (wishlistResult.next()) {
                                        Blob imgBlob = wishlistResult.getBlob("image");
                                        byte[] imgData = imgBlob.getBytes(1, (int) imgBlob.length());
                                        String imgBase64 = Base64.getEncoder().encodeToString(wishlistResult.getBytes("image"));
                                        JSONObject item = new JSONObject()
                                                .put("item_id", wishlistResult.getString("item_id"))
                                                .put("image", imgBase64)
                                                .put("price", wishlistResult.getString("item_price"))
                                                .put("item_name", wishlistResult.getString("name"))
                                                .put("paid", wishlistResult.getString("paid"));

                                        String contributorsString = wishlistResult.getString("contributors");
                                        String contributionAmountsString = wishlistResult.getString("contribution_amounts");
                                        JSONArray contributorsArray = new JSONArray();
                                        if (contributorsString != null && contributionAmountsString != null) {
                                            String[] contributorNames = contributorsString.split(",");
                                            String[] contributionAmounts = contributionAmountsString.split(",");

                                            for (int i = 0; i < contributorNames.length; i++) {
                                                String contributorName = contributorNames[i];
                                                int contributionAmount = Integer.parseInt(contributionAmounts[i]);

                                                // Populate contributorsArray with contributor details
                                                JSONObject contributor = new JSONObject()
                                                        .put("name", contributorName)
                                                        .put("contribution_amount", contributionAmount);
                                                contributorsArray.put(contributor);
                                            }

                                            // Add contributorsArray to the item
                                            item.put("contributors_details", contributorsArray);
                                        }

                                        itemsArray.put(item);
                                    }

                                    // Add the items array to the result
                                    result.put("id", userDataResult.getString("id"));
                                    result.put("username", userDataResult.getString("username"));
                                    result.put("email", userDataResult.getString("email"));
                                    result.put("Name", userDataResult.getString("fname") + " " + userDataResult.getString("lname"));
                                    result.put("Balance", userDataResult.getString("balance"));
                                    result.put("items", itemsArray);
                                }
                            }
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public JSONObject getUserWishlist(int userId) throws SQLException {
        JSONObject result = new JSONObject();
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
        String wishlistQuery = "SELECT w.item_id, i.image, i.name, MIN(w.paid) AS paid, w.item_price, GROUP_CONCAT(c.contributer_name) AS contributors, GROUP_CONCAT(c.amount) AS contribution_amounts FROM items i INNER JOIN wishlist w ON i.id = w.item_id LEFT JOIN contributers c ON w.item_id = c.item_id AND w.user_id = c.owner_id WHERE w.user_id = ? GROUP BY w.item_id, i.image, i.name, w.item_price";
        PreparedStatement wishlistStatement = connect.prepareStatement(wishlistQuery);
        wishlistStatement.setInt(1, userId);
        ResultSet wishlistResult = wishlistStatement.executeQuery();
        JSONArray itemsArray = new JSONArray();

        while (wishlistResult.next()) {
            Blob imgBlob = wishlistResult.getBlob("image");
            byte[] imgData = imgBlob.getBytes(1, (int) imgBlob.length());
            String imgBase64 = Base64.getEncoder().encodeToString(wishlistResult.getBytes("image"));
            JSONObject item = new JSONObject()
                    .put("item_id", wishlistResult.getString("item_id"))
                    .put("image", imgBase64)
                    .put("price", wishlistResult.getString("item_price"))
                    .put("item_name", wishlistResult.getString("name"))
                    .put("paid", wishlistResult.getString("paid"));

            String contributorsString = wishlistResult.getString("contributors");
            String contributionAmountsString = wishlistResult.getString("contribution_amounts");
            JSONArray contributorsArray = new JSONArray();
            if (contributorsString != null && contributionAmountsString != null) {
                String[] contributorNames = contributorsString.split(",");
                String[] contributionAmounts = contributionAmountsString.split(",");

                for (int i = 0; i < contributorNames.length; i++) {
                    String contributorName = contributorNames[i];
                    int contributionAmount = Integer.parseInt(contributionAmounts[i]);

                    // Populate contributorsArray with contributor details
                    JSONObject contributor = new JSONObject()
                            .put("name", contributorName)
                            .put("contribution_amount", contributionAmount);
                    contributorsArray.put(contributor);
                }

                // Add contributorsArray to the item
                item.put("contributors_details", contributorsArray);
            }

            itemsArray.put(item);
        }

        // Add the wishlist items array to the result
        result.put("wishlist", itemsArray);
        return result;

    }

    public boolean deleteItem(int userId, int itemId, int paid) throws SQLException {
        boolean success = false; // Initialize a flag to track deletion success

        try (Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123")) {
            String sql = "DELETE FROM wishlist WHERE user_id = ? AND item_id = ? AND paid = ?";

            try (PreparedStatement wishlistStatement = connect.prepareStatement(sql)) {
                wishlistStatement.setInt(1, userId);
                wishlistStatement.setInt(2, itemId);
                wishlistStatement.setInt(3, paid);

                int result = wishlistStatement.executeUpdate();
                success = (result > 0); // Check if any rows were affected

                // You might consider adding logging here to track success or failure of deletion
                if (success) {
                    System.out.println("Item successfully deleted for user: " + userId);
                } else {
                    System.out.println("Item deletion failed for user: " + userId);
                }
            }
        } catch (SQLException ex) {
            // Log any SQL exceptions
            System.err.println("Error deleting item: " + ex.getMessage());
            throw ex; // Propagate the exception upwards for handling
        }

        return success;
    }

    public JSONObject getFriendData(int userId) throws SQLException {
        JSONObject result = new JSONObject();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            // Query to get user data
            String friendDataQuery = "SELECT f.friendId , u.Username FROM friendlist f , users u WHERE f.friendId = u.Id and userid = ?";
            PreparedStatement friendDataStatement = connect.prepareStatement(friendDataQuery);
            friendDataStatement.setInt(1, userId);
            ResultSet friendDataResult = friendDataStatement.executeQuery();

            JSONArray friendsArray = new JSONArray();
            while (friendDataResult.next()) {
                int friendId = friendDataResult.getInt("FriendId");

                String friendWishlistQuery = "SELECT w.item_id,image, w.paid, i.name, i.price FROM items i INNER JOIN wishlist w ON i.id = w.item_id WHERE w.user_id = ?";
                PreparedStatement friendWishlestStatement = connect.prepareStatement(friendWishlistQuery);
                friendWishlestStatement.setInt(1, friendId);
                ResultSet friendWishlistResult = friendWishlestStatement.executeQuery();

                JSONArray friendItemsArray = new JSONArray();
                while (friendWishlistResult.next()) {
                    Blob imgBlob = friendWishlistResult.getBlob("image");

                    if (imgBlob != null) {
                        byte[] imgData = imgBlob.getBytes(1, (int) imgBlob.length());
                        String imgBase64 = Base64.getEncoder().encodeToString(imgData);
                        JSONObject friendItem = new JSONObject()
                                .put("image", imgBase64)
                                .put("item_id", friendWishlistResult.getString("item_id"))
                                .put("price", friendWishlistResult.getString("price"))
                                .put("paid", friendWishlistResult.getString("paid"))
                                .put("item_name", friendWishlistResult.getString("name"));
                        friendItemsArray.put(friendItem);
                    } else {
                        JSONObject item = new JSONObject()
                                .put("image", "")
                                .put("item_id", friendWishlistResult.getString("item_id"))
                                .put("price", friendWishlistResult.getString("price"))
                                .put("paid", friendWishlistResult.getString("paid"))
                                .put("item_name", friendWishlistResult.getString("name"));
                        friendItemsArray.put(item);

                    }
                }

                JSONObject friendData = new JSONObject();
                friendData.put("friend_id", friendId);
                friendData.put("friend_username", friendDataResult.getString(2));
                friendData.put("items", friendItemsArray);

                friendsArray.put(friendData);

            }
            result.put("Friends", friendsArray);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    public boolean deleteFriend(int userId, int friendId) throws SQLException {
        int result = -1;
        int result2 = -1;
        boolean x;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
        String sql = "delete from friendlist where UserId =? and FriendID=?";
        PreparedStatement deleteFriendStatement = connect.prepareStatement(sql);
        deleteFriendStatement.setInt(1, userId);
        deleteFriendStatement.setInt(2, friendId);
        result = deleteFriendStatement.executeUpdate();
        String sql2 = "delete from friendlist where UserId =? and FriendID=?";
        PreparedStatement deleteFriendStatement2 = connect.prepareStatement(sql2);
        deleteFriendStatement2.setInt(1, friendId);
        deleteFriendStatement2.setInt(2, userId);
        result2 = deleteFriendStatement2.executeUpdate();
        if (result != -1 && result2 != -1) {
            x = true;
        } else {
            x = false;
        }

        return x;
    }

    public boolean updateBalance(int userId, int newBalance) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            String sql = "UPDATE Users SET Balance = ? WHERE id = ?";
            try {
                prepare = connect.prepareStatement(sql);
                prepare.setInt(1, newBalance);
                prepare.setInt(2, userId);
                int result = prepare.executeUpdate();

                if (result != -1) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public JSONArray getItems() throws SQLException {
        JSONArray itemsArray = new JSONArray();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            // Query to get all items
            String itemsQuery = "SELECT * FROM items";
            PreparedStatement itemsStatement = connect.prepareStatement(itemsQuery);
            ResultSet itemsResult = itemsStatement.executeQuery();

            while (itemsResult.next()) {
                Blob imgBlob = itemsResult.getBlob("image");
                byte[] imgData = imgBlob.getBytes(1, (int) imgBlob.length());
                String imgBase64 = Base64.getEncoder().encodeToString(itemsResult.getBytes("image"));
                JSONObject item = new JSONObject()
                        .put("id", itemsResult.getString("id"))
                        .put("image", imgBase64)
                        .put("name", itemsResult.getString("name"))
                        .put("price", itemsResult.getString("price"))
                        .put("category", itemsResult.getString("category"));
                itemsArray.put(item);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itemsArray;
    }

    public boolean addItemToWishlist(int user_Id, int item_Id, int paid, int item_price) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            // Insert the item into the wishlist
            String addItemQuery = "INSERT INTO wishlist (user_id, item_id, paid, item_price) VALUES (?, ?, ?, ?)";
            PreparedStatement addItemStatement = connect.prepareStatement(addItemQuery);
            addItemStatement.setInt(1, user_Id);
            addItemStatement.setInt(2, item_Id);
            addItemStatement.setInt(3, paid);
            addItemStatement.setInt(4, item_price);

            int result = addItemStatement.executeUpdate();
            connect.close();
            return result != -1;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);

            return false;
        }
    }

    public boolean addContributor(int user_Id, int item_Id, int owner_id, int amount, String name, String contributeItemName) throws SQLException {
        String addNotification;
        int result2 = -1;
        int result=-1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            String addContributer = "INSERT INTO contributers VALUES (?, ?, ?, ?, ?)";
            String update = "update contributers set amount=amount + ? where user_id=? and owner_id=? and item_id=? ";
            String check = "select * from contributers where user_id=? and owner_id=? and item_id=?";
            PreparedStatement checkStatemnet= connect.prepareStatement(check);
            checkStatemnet.setInt(1, user_Id);
            checkStatemnet.setInt(2, owner_id);
            checkStatemnet.setInt(3,item_Id );
            ResultSet checkResult = checkStatemnet.executeQuery();
            if (checkResult.next()) {
                PreparedStatement updateStatement = connect.prepareStatement(update);
                updateStatement.setInt(1, amount);
                updateStatement.setInt(2, user_Id);
                updateStatement.setInt(3, owner_id);
                updateStatement.setInt(4, item_Id);

                result = updateStatement.executeUpdate();

            } else if (!checkResult.next()) {
                PreparedStatement addContributerStatement = connect.prepareStatement(addContributer);
                addContributerStatement.setInt(1, user_Id);
                addContributerStatement.setInt(2, item_Id);
                addContributerStatement.setInt(3, owner_id);
                addContributerStatement.setString(4, name);
                addContributerStatement.setInt(5, amount);
                result = addContributerStatement.executeUpdate();
            }

            if (result > 0) {
                addNotification = "INSERT INTO notifications (receiver, message) values (?, ?)";

                PreparedStatement prepare2 = connect.prepareStatement(addNotification);
                prepare2.setInt(1, owner_id);
                prepare2.setString(2, name + " contributed by " + amount + " for your " + contributeItemName);
                result2 = prepare2.executeUpdate();
                System.out.print("notification");
                x = true;
            } else {
                x = false;
            }
            connect.close();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);

        }
        return x;
    }

    public double getUserBalance(int userId) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            // Query to retrieve the user's balance
            String balanceQuery = "SELECT balance FROM users WHERE id = ?";
            try (PreparedStatement balanceStatement = connect.prepareStatement(balanceQuery)) {
                balanceStatement.setInt(1, userId);

                ResultSet balanceResult = balanceStatement.executeQuery();

                if (balanceResult.next()) {
                    return balanceResult.getInt("balance");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public boolean updateRemainingPrice(int userId, int itemId, int contribution, String item_name, String fname) {

        boolean success = false;
        int paid;
        int price;
        String sql2;
        String sql3;
        int result2;
        int result3;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            // Query to update the remaining_price for the item
            String updateRemainingPriceQuery = "UPDATE wishlist SET paid = paid + ? WHERE user_id = ? AND item_id = ?";
            String remainingPriceQuery = "SELECT paid,item_price FROM wishlist WHERE user_id = ? AND item_id = ?";
            String contributers = "select DISTINCT user_id from contributers where owner_id =? and item_id=?";
            try (PreparedStatement updateRemainingPriceStatement = connect.prepareStatement(updateRemainingPriceQuery)) {
                updateRemainingPriceStatement.setInt(1, contribution);
                updateRemainingPriceStatement.setInt(2, userId);
                updateRemainingPriceStatement.setInt(3, itemId);

                int result = updateRemainingPriceStatement.executeUpdate();
                success = (result > 0);

            }
            try (PreparedStatement remainingPriceStatement = connect.prepareStatement(remainingPriceQuery)) {
                remainingPriceStatement.setInt(1, userId);
                remainingPriceStatement.setInt(2, itemId);

                ResultSet remainingPriceResult = remainingPriceStatement.executeQuery();

                if (remainingPriceResult.next()) {
                    paid = remainingPriceResult.getInt("paid");
                    price = remainingPriceResult.getInt("item_price");
                    if (paid == price) {
                        sql2 = "INSERT INTO notifications (receiver, message) values (?, ?)";
                        PreparedStatement prepare2 = connect.prepareStatement(sql2);
                        prepare2.setInt(1, userId);
                        prepare2.setString(2, " Congrats !! The price of " + item_name + " you wanted is collected now.Your friends love you.");
                        result2 = prepare2.executeUpdate();

                        PreparedStatement prepare3 = connect.prepareStatement(contributers);
                        prepare3.setInt(1, userId);
                        prepare3.setInt(2, itemId);
                        ResultSet contributers2 = prepare3.executeQuery();
                        while (contributers2.next()) {
                            int Id = contributers2.getInt("user_id");
                            sql3 = "INSERT INTO notifications (receiver, message) values (?, ?)";
                            PreparedStatement prepare4 = connect.prepareStatement(sql3);
                            prepare4.setInt(1, Id);
                            prepare4.setString(2, "The " + item_name + " you contributed to purchhase for your friend " + fname + " is fully paid now and on its way to him");
                            result3 = prepare4.executeUpdate();

                        }

                    }
                }

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            //return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return success;
    }

    public int getRemainingPrice(int userId, int itemId) throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            // Query to retrieve the remaining price for the item
            String remainingPriceQuery = "SELECT paid FROM wishlist WHERE user_id = ? AND item_id = ?";
            try (PreparedStatement remainingPriceStatement = connect.prepareStatement(remainingPriceQuery)) {
                remainingPriceStatement.setInt(1, userId);
                remainingPriceStatement.setInt(2, itemId);

                ResultSet remainingPriceResult = remainingPriceStatement.executeQuery();

                if (remainingPriceResult.next()) {
                    return remainingPriceResult.getInt("paid");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return 0;
    }

    public double getItemPrice(int itemId) throws SQLException {
        double itemPrice = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            // Query to retrieve the item price
            String itemPriceQuery = "SELECT price FROM items WHERE id = ?";
            try (PreparedStatement itemPriceStatement = connect.prepareStatement(itemPriceQuery)) {
                itemPriceStatement.setInt(1, itemId);

                ResultSet itemPriceResult = itemPriceStatement.executeQuery();

                if (itemPriceResult.next()) {
                    itemPrice = itemPriceResult.getDouble("price");
                }
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return itemPrice;
    }

    public JSONArray searchFriend(int userId, String searchText) throws SQLException {
        JSONArray searchedFriendsArray = new JSONArray();
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            String searchQuery = "select Username , Email , id from users where Id not in (select friendID from friendlist where userId = ?) and Id <> ? and Username like ? and Id not in (Select RequestUserId from FriendRequest where userid = ?)";
            PreparedStatement searchStatement = connect.prepareStatement(searchQuery);
            searchStatement.setInt(1, userId);
            searchStatement.setInt(2, userId);
            searchStatement.setString(3, "" + searchText + "%");
            searchStatement.setInt(4, userId);
            ResultSet searchResult = searchStatement.executeQuery();
            while (searchResult.next()) {
                JSONObject item = new JSONObject()
                        .put("UserName", searchResult.getString("username"))
                        .put("Id", searchResult.getString("id"))
                        .put("Email", searchResult.getString("email"));
                searchedFriendsArray.put(item);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return searchedFriendsArray;
    }

    public JSONArray ReuqestFriend(int userId) throws SQLException {
        JSONArray friendRequestArray = new JSONArray();
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");

            String friendRequestQuery = "select f.UserId , u.username , u.email from friendRequest f , users u where f.UserId = u.Id and RequestUserId = ?";
            PreparedStatement friendRequestStatement = connect.prepareStatement(friendRequestQuery);
            friendRequestStatement.setInt(1, userId);
            ResultSet friendRequestResult = friendRequestStatement.executeQuery();
            while (friendRequestResult.next()) {
                JSONObject friendRequest = new JSONObject()
                        .put("UserName", friendRequestResult.getString("username"))
                        .put("Id", friendRequestResult.getString("UserId"))
                        .put("Email", friendRequestResult.getString("email"));
                friendRequestArray.put(friendRequest);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return friendRequestArray;
    }

    public boolean addFriend(int userId, int requestId, String senderEmail) throws SQLException {
        int result = -1;
        String sql2;
        int result2 = -1;

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            String addFriendQuery = "insert into friendrequest values (? , ?)";
            PreparedStatement addFriendStatement = connect.prepareStatement(addFriendQuery);
            addFriendStatement.setInt(1, userId);
            addFriendStatement.setInt(2, requestId);
            result = addFriendStatement.executeUpdate();

            if (result > 0) {
                sql2 = "INSERT INTO notifications (receiver, message) values (?, ?)";

                PreparedStatement prepare2 = connect.prepareStatement(sql2);
                prepare2.setInt(1, requestId);
                prepare2.setString(2, senderEmail + " sent you a friend request");
                result2 = prepare2.executeUpdate();
                System.out.print("notification");
                x = true;
            } else {
                x = false;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return x;
    }

    public boolean acceptFriend(int userId, int requestId, String senderEmail) throws SQLException {
        int deleteResult = -1;
        int insertResult = -1;
        String sql2;
        int result2 = -1;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            String deleteFriendRequestQuery = "delete from friendrequest where userid = ? and requestuserid = ?";
            PreparedStatement deleteFriendRequestStatement = connect.prepareStatement(deleteFriendRequestQuery);
            deleteFriendRequestStatement.setInt(1, requestId);
            deleteFriendRequestStatement.setInt(2, userId);
            deleteResult = deleteFriendRequestStatement.executeUpdate();
            if (deleteResult > 0) {
                String acceptFriendQuery = "insert into friendlist values (? , ?),(? , ?)";
                PreparedStatement acceptFriendStatement = connect.prepareStatement(acceptFriendQuery);
                acceptFriendStatement.setInt(1, requestId);
                acceptFriendStatement.setInt(2, userId);
                acceptFriendStatement.setInt(3, userId);
                acceptFriendStatement.setInt(4, requestId);
                insertResult = acceptFriendStatement.executeUpdate();
                //notification
                sql2 = "INSERT INTO notifications (receiver, message) values (?, ?)";

                PreparedStatement prepare2 = connect.prepareStatement(sql2);
                prepare2.setInt(1, requestId);
                prepare2.setString(2, senderEmail + " accepts your request");
                result2 = prepare2.executeUpdate();
                System.out.print("notification");

            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (insertResult > 0) {
            return true;
        } else {
            return false;
        }

    }

    public boolean declineFriend(int userId, int requestId) throws SQLException {
        int deleteResult = -1;
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            String deleteFriendRequestQuery = "delete from friendrequest where userid = ? and requestuserid = ?";
            PreparedStatement deleteFriendRequestStatement = connect.prepareStatement(deleteFriendRequestQuery);
            deleteFriendRequestStatement.setInt(1, requestId);
            deleteFriendRequestStatement.setInt(2, userId);
            deleteResult = deleteFriendRequestStatement.executeUpdate();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (deleteResult > 0) {
            return true;
        } else {
            return false;
        }

    }

    public JSONArray getNotificationsData(int id) throws SQLException {
        JSONArray items = new JSONArray();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/i-wish-db", "root", "oa123123");
            // Query to get user data
            String userDataQuery = "SELECT * FROM notifications WHERE receiver= ? order by id desc";
            PreparedStatement userDataStatement = connect.prepareStatement(userDataQuery);
            userDataStatement.setInt(1, id);
            ResultSet userDataResult = userDataStatement.executeQuery();

            while (userDataResult.next()) {
                JSONObject item = new JSONObject()
                        .put("message", userDataResult.getString("message"));

                // Add the notification to the ArrayList
                items.put(item);
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DataAccessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

}
