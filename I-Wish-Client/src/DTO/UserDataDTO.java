package DTO;

import java.util.ArrayList;
import static javax.swing.UIManager.getInt;

public class UserDataDTO {

    int id;
    String username;
    String email;
    String fullName;
    static int balance;
    ArrayList<ItemDTO> wishlist = new ArrayList<ItemDTO>();

    public UserDataDTO() {
    }

    ;
   public UserDataDTO(int id, String username, String email, String fullName, int balance, ArrayList<ItemDTO> wishlist) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.fullName = fullName;
        this.balance = balance;
        this.wishlist.addAll(wishlist);
    }

    public UserDataDTO(int balance) {
        this.balance = balance;
        
    }
   

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return fullName;
    }

    public static int getBalance() {
        return balance;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setWishlist(ArrayList<ItemDTO> wishlist) {
        this.wishlist = wishlist;
    }

    public static void setBalance(int balance) {
        UserDataDTO.balance = balance;
    }

    public ArrayList<ItemDTO> getWishlist() {
        return wishlist;
    }

}
