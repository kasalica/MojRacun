package com.example.mojracun.receipt;

public class Receipt {

    private Integer id;
    private String iic;
    private String totalPrice;
    private String dateTimeCreated;

    private Seller seller;
    private Item[] items;


    public Receipt(){
    }

    public Receipt(int userId, String totalPrice, String dateTimeCreated, Seller seller, Item[] items) {
        this.totalPrice = totalPrice;
        this.dateTimeCreated = dateTimeCreated;
        this.seller = seller;
        this.items = items;
    }

    public int getId() {
        return id;
    }

    public String getIic(){ return iic; }
    public void setIic(String iic){ this.iic = iic; }

    public String getTotalPrice() { return totalPrice; }
    public void setTotalPrice(String totalPrice) { this.totalPrice = totalPrice; }

    public Seller getSeller() { return seller; }
    public void setSeller(Seller seller) { this.seller = seller; }

    public Item[] getItems() { return items; }
    public void setItems(Item[] items) { this.items = items; }

    public String getDateTimeCreated() {  return dateTimeCreated; }
    public void setDateTimeCreated(String dateTimeCreated) { this.dateTimeCreated = dateTimeCreated; }

}