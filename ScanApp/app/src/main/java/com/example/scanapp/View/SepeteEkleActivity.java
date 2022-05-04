package com.example.scanapp.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.example.scanapp.Model.Capture;
import com.example.scanapp.Model.DatabaseHelper;
import com.example.scanapp.Model.Functions;
import com.example.scanapp.Model.Product;
import com.example.scanapp.Model.Sepet;
import com.example.scanapp.databinding.ActivitySepeteEkleBinding;


public class SepeteEkleActivity extends AppCompatActivity {

    ActivitySepeteEkleBinding binding;
    DatabaseHelper db;
    Product product ;
    Sepet sepet;
    Functions functions = new Functions();

    private String id="";
    private String name;
    private double price;

    int quality = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(SepeteEkleActivity.this);

        binding = ActivitySepeteEkleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnIncreaseQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quality++;
                binding.txtIstenilenAdet.setText(""+quality);
            }
        });

        binding.btnDecreaseQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(quality > 1){
                    quality--;

                    binding.txtIstenilenAdet.setText(""+quality);
                }

            }
        });

        binding.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanCode();
            }
        });

        binding.btnSepeteEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SepeteEkle();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        product = null;
        quality = 1;

        binding.txtIstenilenAdet.setText(""+quality);
        binding.txtSId.setText("");
        binding.txtSName.setText("");
        binding.txtSPrice.setText("");

        getProduct("nqu3mIQJ");

    }

    void scanCode(){

        ScanOptions options = new ScanOptions();
        options.setPrompt("Yeterince parlak bir ortamda taratın");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(Capture.class);

        barLauncher.launch(options);


    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(),result->{

        if (result.getContents() != null){

            id = result.getContents();
            id = id.trim();
            getProduct(id);
        }

    });


     public void getProduct(String id){
        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
            //Toast.makeText(this, "Veri yok", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){

                if(cursor.getString(0).equals(id)){
                    product = new Product(cursor.getString(0),cursor.getString(1),cursor.getDouble(2),cursor.getInt(3));


                    binding.txtSId.setText(product.getId());
                    binding.txtSName.setText(product.getName());
                    binding.txtSPrice.setText(""+product.getPrice());

                }

            }
        }
    }


    Sepet checkSepetData(String id){

        Cursor cursor = db.readSepetData();
        if(cursor.getCount() == 0){
            //Toast.makeText(this, "Veri yok", Toast.LENGTH_SHORT).show();
        }
        else{
            while(cursor.moveToNext()){
                Sepet sepet = new Sepet(cursor.getString(0),cursor.getString(1),cursor.getDouble(2),cursor.getDouble(3),cursor.getInt(4));
                if(cursor.getString(0).equals(id)){
                    return sepet;
                }

            }
        }
        return null;
    }

    private void SepeteEkle(){

        DatabaseHelper myDB = new DatabaseHelper(SepeteEkleActivity.this);

        if(product != null){

            sepet = checkSepetData(product.getId());;
            if(sepet != null){


                if(product.getId().equals(sepet.getId())){
                    // Card processes


                    int old_qualityCard = sepet.getQuality();

                    int new_qualityCard =  old_qualityCard + quality;


                    double old_total_price = sepet.getTotal_price();
                    double new_total_price = old_total_price + product.getPrice() * quality;
                    db.updateSepetInAdd(sepet.getId(),new_total_price,new_qualityCard);

                    // Product processes
                    int old_qualityProduct = product.getQuality();
                    int new_qualityProduct = old_qualityProduct - quality;

                    db.updateProductInAdd(product.getId(),new_qualityProduct);

                    Toast.makeText(this, "Ürün sepete eklendi.", Toast.LENGTH_SHORT).show();
                }

                else{
                    // Ürün ilk defa eklenicek


                    double total_price = functions.setFraction(product.getPrice() * quality);


                    myDB.SepeteEkle(
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            total_price,
                            quality);

                    int old_qualityProduct = product.getQuality();
                    int new_qualityProduct = old_qualityProduct - quality;

                    db.updateProductInAdd(product.getId(),new_qualityProduct);

                    Toast.makeText(this, "Ürün sepete eklendi.", Toast.LENGTH_SHORT).show();

                }

            }

            else{
                // Ürün ilk defa eklenicek


                double total_price = functions.setFraction(product.getPrice() * quality);



                myDB.SepeteEkle(
                        product.getId(),
                        product.getName(),
                        product.getPrice(),
                        total_price,
                        quality);

                int old_qualityProduct = product.getQuality();
                int new_qualityProduct = old_qualityProduct - quality;

                db.updateProductInAdd(product.getId(),new_qualityProduct);

                Toast.makeText(this, "Ürün sepete eklendi.", Toast.LENGTH_SHORT).show();

            }


        }

        else{
            Toast.makeText(this, "Herhangi bir ürün taramadınız!", Toast.LENGTH_SHORT).show();
        }
    }




}