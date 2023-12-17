package DTO;

public class WishListDTO {
    
    int user_id;
    int item_id;
    int paid;
    int item_price;

    public WishListDTO(int user_id, int item_id, int paid, int item_price) {
        this.user_id = user_id;
        this.item_id = item_id;
        this.paid = paid;
        this.item_price = item_price;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getItem_price() {
        return item_price;
    }

    public void setItem_price(int item_price) {
        this.item_price = item_price;
    }
    
}