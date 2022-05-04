package com.example.scanapp.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.scanapp.Adapter.RecylerAdapter;
import com.example.scanapp.Model.DatabaseHelper;
import com.example.scanapp.Model.Functions;
import com.example.scanapp.Model.Product;
import com.example.scanapp.Model.Sepet;
import com.example.scanapp.databinding.ActivityMainBinding;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    private ActivityMainBinding binding;
    DatabaseHelper db;
    ArrayList<Sepet> cardList;
    ArrayList<Product> productList;
    Functions functions = new Functions();


    RecylerAdapter recylerAdapter;

    double total_price=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        db = new DatabaseHelper(MainActivity.this);

        productList = new ArrayList<Product>();
        storeProductDataArray();

        if(productList.size() == 0 ){
            createFirsTime();
        }

        binding.btnGoAddPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddProductActivity.class);
                startActivity(intent);
            }
        });

        binding.btnGoSepeteEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SepeteEkleActivity.class);
                startActivity(intent);
            }
        });

        binding.btnClearCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCard();
            }
        });

        binding.btnGoConfirmCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardList.size() > 0) {
                    Intent intent = new Intent(MainActivity.this,ConfirmCardActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this, "Sepetinizde hiç ürün yok", Toast.LENGTH_SHORT).show();
                }

            }
        });


        cardList = new ArrayList<Sepet>();


        recylerAdapter = new RecylerAdapter(MainActivity.this,cardList);
        binding.recyclerView.setAdapter(recylerAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

    }

    public void createFirsTime(){
        System.out.println("Ürünler İlk Kez Eklendi");
        ArrayList<Product> firsList = new ArrayList<Product>();

        Product p1=new Product("pbYOmcYz","Ülker Çikolatalı Gofret",2.35,300);
        Product p2=new Product("fx5AtDKJ","Ülker Çikolatalı Beyaz Gofret",2.47,300);
        Product p3=new Product("nqu3mIQJ","Karam Çikolata",3.7,200);
        Product p4=new Product("vS4baSnO","Su",2,1000);
        Product p5=new Product("7t9SznTU","Soda",2.10,500);
        Product p6=new Product("nqIehLBX","Telefon Tutucu",5,100);
        Product p7=new Product("CKSNYsxz","Şampuan",35.39,150);
        firsList.add(p1);
        firsList.add(p2);
        firsList.add(p3);
        firsList.add(p4);
        firsList.add(p5);
        firsList.add(p6);
        firsList.add(p7);

        DatabaseHelper myDB = new DatabaseHelper(MainActivity.this);



        int i=0;
        while(i < firsList.size()){
            myDB.AddProduct(
                    firsList.get(i).getId(),
                    firsList.get(i).getName(),
                    firsList.get(i).getPrice(),
                    firsList.get(i).getQuality()
            );
            i++;
        }


    }

    public void clearCard(){

        DatabaseHelper myDb = new DatabaseHelper(MainActivity.this);


        Cursor cursor = db.readSepetData();
        if(cursor.getCount() == 0){
            //Toast.makeText(this, "Veri yok", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                Product product = findProduct(cursor.getString(0));
                if(product != null){
                    int old_quality_product = product.getQuality();
                    int quality_card = cursor.getInt(4);
                    int new_quality_product = old_quality_product + quality_card;

                    db.updateProductInClearCard(cursor.getString(0),new_quality_product);
                }
            }
            Toast.makeText(this, "Sepet Temizlendi", Toast.LENGTH_SHORT).show();
        }
        myDb.deleteAllSepet();
        storeCardDataArray();
        recylerAdapter.notifyDataSetChanged();

    }



    @Override
    protected void onStart() {
        super.onStart();

        storeCardDataArray();
        recylerAdapter.notifyDataSetChanged();
    }

    Product findProduct(String id){

        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            Toast.makeText(this, "Veri bulunamadı", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()) {

                if (cursor.getString(0).equals(id)) {
                    Product product = new Product(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3));
                    return product;
                }
            }
        }
        return null;
    }

    void storeProductDataArray(){
        productList.clear();
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            //Toast.makeText(this, "Veri yok", Toast.LENGTH_SHORT).show();
        }
        else{

            while(cursor.moveToNext()){

                Product product = new Product(cursor.getString(0),cursor.getString(1),cursor.getDouble(2),cursor.getInt(3));
                productList.add(product);

            }

        }
    }

    void storeCardDataArray(){

        cardList.clear();
        Cursor cursor = db.readSepetData();
        if(cursor.getCount() == 0){
            //Toast.makeText(this, "Veri yok", Toast.LENGTH_SHORT).show();
        }
        else{

            while(cursor.moveToNext()){

                Sepet sepet = new Sepet(cursor.getString(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getInt(4));
                total_price+=sepet.getTotal_price();
                total_price = functions.setFraction(total_price);
                cardList.add(sepet);
            }
        }



    }

}