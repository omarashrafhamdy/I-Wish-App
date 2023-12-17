package DTO;

public class AddItemDTO {

    int id;
    byte[] image;
    String name;
    int paid;
    int price;
    String category;

    public AddItemDTO(int id, String name, int price, String category,byte[] image) {
        this.id = id;
        this.image = image;
        this.name = name;
        
        this.price = price;
        this.category = category;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    

}
