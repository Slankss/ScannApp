package com.example.scanapp.Model;

public class Sepet {

    private String  id;
    private String name;
    private double price;
    private int quality;
    private double total_price;

    public Sepet(String id,String name,double price,double total_price,int quality){
        this.id = id;
        this.name = name;
        this.price = price;
        this.total_price= total_price;
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

    public double getTotal_price(){
        return this.total_price;
    }

    public void setQuality(int quality){
        this.quality = quality;
    }

    public void setTotal_price(double total_price){
        this.total_price = total_price;
    }

}
