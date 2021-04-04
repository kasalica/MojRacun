package com.example.mojracun.receipt;

import java.io.Serializable;

public class Item implements Serializable {

    private Integer id;
    private String name;
    private String quantity;
    private String priceAfterVat;
    private String priceBeforeVat;
    private String vatRate;

    public Item(){ }

    public Item(int sellerId, String name, String town, String address) {
        this.name = name;
        this.quantity = quantity;
        this.priceAfterVat = priceAfterVat;
    }

    public int getId() {
        return id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity;}

    public String getPriceAfterVat() { return priceAfterVat; }
    public void setPriceAfterVat(String priceAfterVat) { this.priceAfterVat = priceAfterVat;}

    public String getPriceBeforeVat() { return priceBeforeVat; }
    public void setPriceBeforeVat(String priceBeforeVat) { this.priceBeforeVat = priceBeforeVat; }

    public String getVatRate() { return vatRate; }
    public void setVatRate(String vatRate) { this.vatRate = vatRate; }
}