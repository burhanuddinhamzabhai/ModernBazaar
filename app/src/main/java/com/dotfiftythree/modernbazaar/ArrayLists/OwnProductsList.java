package com.dotfiftythree.modernbazaar.ArrayLists;

public class OwnProductsList {
    String productID, productName, productImage;

    public OwnProductsList(String productID, String productName, String productImage) {
        this.productID = productID;
        this.productName = productName;
        this.productImage = productImage;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
