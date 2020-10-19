package com.dotfiftythree.modernbazaar;

public class FetchProductArrayList {
    int items = 5;
    String productImage, productName, periodOfUsage, productDes, productID;

    public FetchProductArrayList(String productImage, String productName, String periodOfUsage, String productDes, String productID) {
        this.productImage = productImage;
        this.productName = productName;
        this.periodOfUsage = periodOfUsage;
        this.productDes = productDes;
        this.productID = productID;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPeriodOfUsage() {
        return periodOfUsage;
    }

    public void setPeriodOfUsage(String periodOfUsage) {
        this.periodOfUsage = periodOfUsage;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
