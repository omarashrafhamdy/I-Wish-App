package DTO;

import java.util.ArrayList;

public class FriendsDTO {
    String userName;
    int id;
    ArrayList<ItemDTO> wishlist = new ArrayList<ItemDTO>();

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<ItemDTO> getWishlist() {
        return wishlist;
    }

    public void setWishlist(ArrayList<ItemDTO> wishlist) {
        this.wishlist = wishlist;
    }

    public FriendsDTO(String userName, int id) {
        this.userName = userName;
        this.id = id;
    }
    
    public void addItem(ItemDTO item){
        this.wishlist.add(item);
    }
}
