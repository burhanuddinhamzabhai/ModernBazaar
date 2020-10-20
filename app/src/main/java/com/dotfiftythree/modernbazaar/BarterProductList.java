package com.dotfiftythree.modernbazaar;

public class BarterProductList {
    int items = 2;
    String productName, productID;

    public BarterProductList(String productName, String productID) {
        this.productName = productName;
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
