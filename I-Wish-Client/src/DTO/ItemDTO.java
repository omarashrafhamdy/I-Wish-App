package DTO;

import java.awt.Button;
import java.util.ArrayList;

public class ItemDTO {

    byte[] image;
    int id;
    String item_name;
    int price;
    int remaining;
    String contributers;
    Button delete = new Button();
    ArrayList<ContributorInfoDTO> contributors = new ArrayList<ContributorInfoDTO>();

    public ItemDTO(int id, int price, String item_name, int remaining, byte[] image, String contributers) {
        this.id = id;
        this.price = price;
        this.item_name = item_name;
        this.remaining = remaining;
        this.contributers = contributers;
        this.image = image;
        // this.contributers=contributers;
    }

    /* public ItemDTO(int id,  String item_name, int price,byte[] image) {
        this.id = id;
        this.price = price;
        this.item_name = item_name;
       this.image=image;
        // this.contributers=contributers;
    }*/
    public ItemDTO(int id, int price, String item_name, byte[] image) {
        this.id = id;
        this.price = price;
        this.item_name = item_name;

        this.image = image;

        // this.contributers=contributers;
    }

    public ItemDTO(int id, int price, String item_name, int remaining, byte[] image) {
        this.id = id;
        this.price = price;
        this.item_name = item_name;
        this.remaining = remaining;
        this.image = image;

        // this.contributers=contributers;
    }

    public ItemDTO(int id, int price, String item_name, int remaining) {
        this.id = id;
        this.price = price;
        this.item_name = item_name;
        this.remaining = remaining;

        // this.contributers=contributers;
    }

    public int getId() {
        return id;
    }

    public ItemDTO(int id, String item_name, int price) {
        this.id = id;
        this.item_name = item_name;
        this.price = price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    public void setContributers(String contributers) {
        this.contributers = contributers;
    }

    public byte[] getImage() {
        return image;
    }

    public String getItemName() {
        return item_name;
    }

    public int getPrice() {
        return price;
    }

    public int getRemaining() {
        return remaining;
    }

    public String getContributers() {
        return contributers;
    }

    public ArrayList<ContributorInfoDTO> getContributors() {
        return contributors;
    }

    public void setContributors(ArrayList<ContributorInfoDTO> contributors) {
        this.contributors = contributors;
    }

}
