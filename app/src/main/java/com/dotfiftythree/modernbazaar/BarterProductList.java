package com.dotfiftythree.modernbazaar;

public class BarterProductList {
    int items = 6;
    String productName, productID, barterProductID, sellerID, buyerProductImg, sellerProductImg;

    public BarterProductList(String productName, String productID, String barterProductID, String sellerID, String buyerProductImg, String sellerProductImg) {
        this.productName = productName;
        this.productID = productID;
        this.barterProductID = barterProductID;
        this.sellerID = sellerID;
        this.buyerProductImg = buyerProductImg;
        this.sellerProductImg = sellerProductImg;

    }

    public String getBuyerProductImg() {
        return buyerProductImg;
    }

    public void setBuyerProductImg(String buyerProductImg) {
        this.buyerProductImg = buyerProductImg;
    }

    public String getSellerProductImg() {
        return sellerProductImg;
    }

    public void setSellerProductImg(String sellerProductImg) {
        this.sellerProductImg = sellerProductImg;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(String sellerID) {
        this.sellerID = sellerID;
    }

    public String getBarterProductID() {
        return barterProductID;
    }

    public void setBarterProductID(String barterProductID) {
        this.barterProductID = barterProductID;
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
