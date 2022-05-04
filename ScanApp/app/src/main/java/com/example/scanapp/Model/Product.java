package com.example.scanapp.Model;

public class Product {

    private String id;
    private String name;
    private double price;
    private int quality;

    public Product(String id,String name,double price,int quality){
        this.id = id;
        this.name = name;
        this.price = price;
        this.quality = quality;
    }

    public String getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getPrice(){
        return this.price;
    }

    public int getQuality(){
        return this.quality;
    }

    public void minusQuality(){
        quality--;
    }
}
