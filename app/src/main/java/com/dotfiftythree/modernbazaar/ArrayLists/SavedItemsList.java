package com.dotfiftythree.modernbazaar.ArrayLists;

public class SavedItemsList {
    int items = 3;
    public String productName, productImage, productID;

    public SavedItemsList(String productName, String productImage, String productID) {
        this.productName = productName;
        this.productImage = productImage;
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

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }
}
