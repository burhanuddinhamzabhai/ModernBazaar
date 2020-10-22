package com.dotfiftythree.modernbazaar;

public class OngoingBarterRequestList {
    int item = 10;
    String buyer, seller, sellerProduct, buyerProduct, date, phone, mail, barterID, buyerProductImage, sellerProductImage;

    public OngoingBarterRequestList(String buyer, String seller, String sellerProduct, String buyerProduct, String date, String phone, String mail, String barterID, String buyerProductImage, String sellerProductImage) {
        this.buyer = buyer;
        this.seller = seller;
        this.sellerProduct = sellerProduct;
        this.buyerProduct = buyerProduct;
        this.date = date;
        this.phone = phone;
        this.mail = mail;
        this.barterID = barterID;
        this.buyerProductImage = buyerProductImage;
        this.sellerProductImage = sellerProductImage;

    }

    public String getBuyerProductImage() {
        return buyerProductImage;
    }

    public void setBuyerProductImage(String buyerProductImage) {
        this.buyerProductImage = buyerProductImage;
    }

    public String getSellerProductImage() {
        return sellerProductImage;
    }

    public void setSellerProductImage(String sellerProductImage) {
        this.sellerProductImage = sellerProductImage;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerProduct() {
        return sellerProduct;
    }

    public void setSellerProduct(String sellerProduct) {
        this.sellerProduct = sellerProduct;
    }

    public String getBuyerProduct() {
        return buyerProduct;
    }

    public void setBuyerProduct(String buyerProduct) {
        this.buyerProduct = buyerProduct;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getBarterID() {
        return barterID;
    }

    public void setBarterID(String barterID) {
        this.barterID = barterID;
    }
}
