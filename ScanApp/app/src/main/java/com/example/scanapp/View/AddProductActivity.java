package com.example.scanapp.View;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.scanapp.Model.DatabaseHelper;
import com.example.scanapp.R;
import com.example.scanapp.databinding.ActivityAddProductBinding;

import java.util.Random;

public class AddProductActivity extends AppCompatActivity {

    private ActivityAddProductBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityAddProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnGoProductList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddProductActivity.this,ProductListActivity.class);
                startActivity(intent);
            }
        });

        binding.btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = createId();

                Double price = Double.valueOf(binding.txtPrice.getText().toString());

                Toast.makeText(AddProductActivity.this, "price = "+price, Toast.LENGTH_SHORT).show();

                DatabaseHelper myDB = new DatabaseHelper(AddProductActivity.this);
                myDB.AddProduct(
                        id,
                        binding.txtName.getText().toString().trim(),
                        price,
                        Integer.valueOf(binding.txtQuality.getText().toString()));

            }
        });

    }


    private String createId(){

        String[] liste = {"A","B","C","D","E","G","H","I","J","K","L","M","N","O","P","Q","R","S","T",
                        "U","V","W","X","Y","Z","a","b","c","d","e","f", "g","h","i","j","k",
                        "l","m","n","o","p","q","r","s","t","u","v","w", "x","y","z","0","1",
                "2","3","4","5","6","7","8","9"
        };
        // length is 62

        Random rand = new Random();
        String id="";

        int i=0;
        while(i<8){

            int n = rand.nextInt(61) ;

            id += liste[n];


            i++;
       }


        return id;
    }
}