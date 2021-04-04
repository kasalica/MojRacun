package com.example.mojracun.receipt;

import java.io.Serializable;

public class Seller implements Serializable {

    private Integer id;
    private Integer idNum;
    private String name;
    private String town;
    private String address;
    private Float receiptsSum;

    public Seller(){}

    public Seller(int sellerId, String name, String town, String address) {
        this.name = name;
        this.town = town;
        this.address = address;
    }

    public int getId() {
        return id;
    }


    public Integer getIdNum() { return idNum; }
    public void setIdNum(Integer idNum) { this.idNum = idNum; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getTown() { return town;}
    public void setTown(String town) { this.town = town; }

    public String getAddress() { return address;}
    public void setAddress(String address) { this.address = address; }

    public Float getReceiptsSum() { return receiptsSum; }
    public void setReceiptsSum(Float receiptsSum) { this.receiptsSum = receiptsSum; }

}