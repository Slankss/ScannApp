package com.example.scanapp.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;

import com.example.scanapp.Adapter.ProductListAdapter;
import com.example.scanapp.Model.DatabaseHelper;
import com.example.scanapp.Model.Product;
import com.example.scanapp.R;
import com.example.scanapp.databinding.ActivityMainBinding;
import com.example.scanapp.databinding.ActivityProductListBinding;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {

    private ActivityProductListBinding binding;
    DatabaseHelper db;

    ArrayList<Product> productList;

    ProductListAdapter recylerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        db = new DatabaseHelper(ProductListActivity.this);
        productList = new ArrayList<Product>();

        storeProductDataArray();

        recylerAdapter = new ProductListAdapter(ProductListActivity.this,productList);
        binding.recyclerView.setAdapter(recylerAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(ProductListActivity.this));

        binding.btnClearProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper myDb = new DatabaseHelper(ProductListActivity.this);
                myDb.deleteProduct();
                storeProductDataArray();
                recylerAdapter.notifyDataSetChanged();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();

        storeProductDataArray();
        recylerAdapter.notifyDataSetChanged();

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

}