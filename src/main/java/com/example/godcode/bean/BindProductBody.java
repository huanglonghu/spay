package com.example.godcode.bean;

/**
 * Created by Administrator on 2018/6/25.
 */

public class BindProductBody {

    private int fK_UserID;
    private String productNumber;
    private String machineAddress;
    private boolean isBind;
    private String productName;
    private int fK_ProductCategoryID;
    private String description;
    private String price;

    public int getFK_UserID() {
        return fK_UserID;
    }

    public void setFK_UserID(int fK_UserID) {
        this.fK_UserID = fK_UserID;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public String getMachineAddress() {
        return machineAddress;
    }

    public void setMachineAddress(String machineAddress) {
        this.machineAddress = machineAddress;
    }

    public boolean isIsBind() {
        return isBind;
    }

    public void setIsBind(boolean isBind) {
        this.isBind = isBind;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getFK_ProductCategoryID() {
        return fK_ProductCategoryID;
    }

    public void setFK_ProductCategoryID(int fK_ProductCategoryID) {
        this.fK_ProductCategoryID = fK_ProductCategoryID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
