/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author OMAR
 */
public class DeleteItemDTO {
    int userId;
    int itemId;
    int remainingPrice;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getRemainingPrice() {
        return remainingPrice;
    }

    public void setRemainingPrice(int remainingPrice) {
        this.remainingPrice = remainingPrice;
    }

    public DeleteItemDTO(int userId, int itemId, int remainingPrice) {
        this.userId = userId;
        this.itemId = itemId;
        this.remainingPrice = remainingPrice;
    }
}
