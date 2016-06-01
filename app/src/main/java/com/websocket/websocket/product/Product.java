package com.websocket.websocket.product;

/**
 * Created by Jang on 2016-06-01.
 */
public class Product {

    private int id ;
    private String prodName;
    private int price;
    private String content;
    private String barcode;

    public Product(){

    }

    public Product(String prodName , String content, int price){
        this.prodName = prodName;
        this.content = content;
        this.price = price;
    }


    public Product(int id, String prodName, int price, String content, String barcode) {
        super();
        this.id = id;
        this.prodName = prodName;
        this.price = price;
        this.content = content;
        this.barcode = barcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBarcode(){
        return this.barcode;
    }

    public void setBarcode(String barcode){
        this.barcode = barcode;
    }

    public String toString(){
        return "id : " + id + ", prodName : " + prodName + "\n" + "price : " + price ;
    }


}
