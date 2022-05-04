package com.example.scanapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanapp.Model.DatabaseHelper;
import com.example.scanapp.Model.Functions;
import com.example.scanapp.Model.Product;
import com.example.scanapp.Model.Sepet;
import com.example.scanapp.R;

import java.util.ArrayList;

public class RecylerAdapter extends RecyclerView.Adapter<RecylerAdapter.MyViewHolder>  {

    Context context;
    ArrayList<Sepet> arraySepet;
    Functions functions = new Functions();
    DatabaseHelper db;


    public RecylerAdapter(Context context, ArrayList<Sepet> arraySepet){
        this.context=context;
        this.arraySepet=arraySepet;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txtP_Name,txtP_Quality,txtP_Price;
        ImageView btn_SepetDecreaseQuality,btn_SepetIncreaseQuality,btn_Delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);



            txtP_Name = itemView.findViewById(R.id.txtP_Name);
            txtP_Quality = itemView.findViewById(R.id.txtP_Quality);
            txtP_Price = itemView.findViewById(R.id.txtP_Price);
            btn_SepetDecreaseQuality = itemView.findViewById(R.id.btn_SepetDecreaseQuality);
            btn_SepetIncreaseQuality = itemView.findViewById(R.id.btn_SepetIncreaseQuality);
            btn_Delete = itemView.findViewById(R.id.btnDelete);


        }
    }

    @NonNull
    @Override
    public RecylerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_row,parent,false);
        db = new DatabaseHelper(parent.getContext());


        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecylerAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.txtP_Name.setText(String.valueOf(arraySepet.get(position).getName()));
        holder.txtP_Quality.setText(String.valueOf(arraySepet.get(position).getQuality()));

        holder.txtP_Price.setText(String.valueOf(functions.setFraction(arraySepet.get(position).getTotal_price())));



        holder.btn_SepetDecreaseQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(arraySepet.size() > 0 ) {

                    DatabaseHelper db = new DatabaseHelper(context);

                    Product product = findProduct(arraySepet.get(position).getId());

                    String id= arraySepet.get(position).getId();
                    int old_quality_card = arraySepet.get(position).getQuality();
                    if(old_quality_card > 1){

                        int new_quality_product = product.getQuality() + 1;
                        int new_quality_card = old_quality_card -1;
                        double old_total_price_card = arraySepet.get(position).getTotal_price();
                        double new_total_price_card = old_total_price_card - arraySepet.get(position).getPrice();

                        new_total_price_card = functions.setFraction(new_total_price_card);


                        db.updateSepetInAdd(id,new_total_price_card,new_quality_card);
                        db.updateProductInClearCard(id,new_quality_product);

                        arraySepet.get(position).setQuality(new_quality_card);
                        arraySepet.get(position).setTotal_price(new_total_price_card);

                        holder.txtP_Quality.setText(String.valueOf(arraySepet.get(position).getQuality()));
                        holder.txtP_Price.setText(String.valueOf(arraySepet.get(position).getTotal_price()));


                    }
                }
            }
        });


        holder.btn_SepetIncreaseQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(arraySepet.size() > 0) {
                    DatabaseHelper db = new DatabaseHelper(context);

                    Product product = findProduct(arraySepet.get(position).getId());

                    String id= arraySepet.get(position).getId();
                    int old_quality_card = arraySepet.get(position).getQuality();
                    int old_product_card = product.getQuality();
                    if(old_quality_card <= old_product_card){

                        int new_quality_product = product.getQuality() - 1;
                        int new_quality_card = old_quality_card + 1;
                        double old_total_price_card = arraySepet.get(position).getTotal_price();
                        double new_total_price_card = old_total_price_card + arraySepet.get(position).getPrice();

                        db.updateSepetInAdd(id,new_total_price_card,new_quality_card);
                        db.updateProductInClearCard(id,new_quality_product);

                        new_total_price_card = functions.setFraction(new_total_price_card);

                        arraySepet.get(position).setQuality(new_quality_card);
                        arraySepet.get(position).setTotal_price(new_total_price_card);

                        holder.txtP_Quality.setText(String.valueOf(arraySepet.get(position).getQuality()));
                        holder.txtP_Price.setText(String.valueOf(arraySepet.get(position).getTotal_price()));


                    }
                } // OnClick

            }
        });

        holder.btn_Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                DatabaseHelper db = new DatabaseHelper(context);

                Product product = findProduct(arraySepet.get(position).getId());

                String id= arraySepet.get(position).getId();
                int old_quality_product = product.getQuality();
                int quality_card = arraySepet.get(position).getQuality();
                int new_quality_product = old_quality_product + quality_card;

                db.deleteSelectedItem(arraySepet.get(position).getId());
                db.updateProductInClearCard(id,new_quality_product);

                arraySepet.remove(position);
                notifyItemChanged(position);
                notifyItemRangeChanged(position,arraySepet.size());
            }



        });


    }

    @Override
    public int getItemCount() {
        return arraySepet.size();
    }

    Product findProduct(String id){

        Cursor cursor = db.readAllData();
        if(cursor.getCount() == 0){
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


}
