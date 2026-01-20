package com.example.wecollectfirebase;

public class User {

    String Item, Price, Size, Store, Barcode, Date;

    public User(){

    }

    public User(String item, String price, String size, String store, String barcode, String date) {
        Item = item;
        Price = price;
        Size = size;
        Store = store;
        Barcode = barcode;
        Date = date;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }

    public String getStore() {
        return Store;
    }

    public void setStore(String store) {
        Store = store;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {Barcode = barcode;}

    public String getDate() {return Date;}

    public void setDate(String date) {Date = date;}

}
