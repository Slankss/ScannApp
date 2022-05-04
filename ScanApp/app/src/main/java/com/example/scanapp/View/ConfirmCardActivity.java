package com.example.scanapp.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.scanapp.Model.DatabaseHelper;
import com.example.scanapp.Model.Functions;
import com.example.scanapp.Model.Product;
import com.example.scanapp.Model.Sepet;
import com.example.scanapp.R;
import com.example.scanapp.databinding.ActivityConfirmCardBinding;

import java.util.ArrayList;

public class ConfirmCardActivity extends AppCompatActivity {

    ActivityConfirmCardBinding binding;
    Functions function = new Functions();
    DatabaseHelper db;
    ArrayList<Sepet> cardList;
    ArrayList<Product> productList;
    String errorMessage="";
    double total_price=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityConfirmCardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = new DatabaseHelper(ConfirmCardActivity.this);

        binding.btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int result = checkInformation();
                System.out.println(result);
                if(result == 1){

                    AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmCardActivity.this);
                    builder.setTitle("Ödeme İşlemi");
                    builder.setMessage("Ödemeyi Onaylıyor musunuz?");
                    builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            clearCard();
                            Intent intent = new Intent(ConfirmCardActivity.this,MainActivity.class);
                            startActivity(intent);

                        }
                    });
                    builder.show();


                }
                else
                {
                    Toast.makeText(ConfirmCardActivity.this, ""+errorMessage, Toast.LENGTH_SHORT).show();
                }

            }
        });

        cardList = new ArrayList<Sepet>();
        storeCardDataArray();
        total_price = function.setFraction(total_price);
        binding.lblTotalPrice.setText("Toplam tutar = "+total_price+" TL");


    }

    public int checkInformation(){

        String nameSurname="",cardNumber="";
        int month=0,year=0,cvv=0;


        if( binding.editTextNameSurname.getText().length() >0 && binding.editTextCardNumber.getText().length() > 0 &&
            binding.editTextMonth.getText().length() > 0 && binding.editTextYear.getText().length() > 0 &&
                binding.editTextCvv.getText().length() >0)
        {
            nameSurname = binding.editTextNameSurname.getText().toString().trim();
            cardNumber = binding.editTextCardNumber.getText().toString().trim();
            month = Integer.valueOf(binding.editTextMonth.getText().toString());
            year = Integer.valueOf(binding.editTextYear.getText().toString());
            cvv = Integer.valueOf(binding.editTextCvv.getText().toString());


            if(cardNumber.length() == 16 && month > 0 && month <=12 && year >= 22 && cvv >= 100){
                return 1;
            }
            else{
                errorMessage = "Girdiğiniz kart bilgilerini kontrol ediniz.";
            }
        }
        else{
            errorMessage = "Lütfen Gerekli Alanları Doldurunuz.";
        }


        return 0;
    }

    public void clearCard(){

        DatabaseHelper myDb = new DatabaseHelper(ConfirmCardActivity.this);


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
        }
        myDb.deleteAllSepet();
        Toast.makeText(this, "Ödemeniz Alındı.", Toast.LENGTH_SHORT).show();

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
                cardList.add(sepet);
                //System.out.println(sepet.getTotal_price());
            }
        }



    }

}