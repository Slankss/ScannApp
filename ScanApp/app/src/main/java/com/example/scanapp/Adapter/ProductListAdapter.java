package com.example.scanapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scanapp.Model.Product;
import com.example.scanapp.R;

import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    Context context;
    ArrayList<Product> arrayProduct;

    public ProductListAdapter(Context context, ArrayList<Product> arrayProduct){
        this.context=context;
        this.arrayProduct=arrayProduct;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_id,txt_name,txt_quality,txt_price;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_id = itemView.findViewById(R.id.txt_id);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_quality = itemView.findViewById(R.id.txt_quality);
            txt_price = itemView.findViewById(R.id.txt_price);



        }
    }

    @NonNull
    @Override
    public ProductListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_list_recycler_row,parent,false);

        return new ProductListAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.MyViewHolder holder, int position) {

        holder.txt_id.setText(String.valueOf(arrayProduct.get(position).getId()+"|"));
        holder.txt_name.setText(String.valueOf(arrayProduct.get(position).getName()+"|"));
        holder.txt_quality.setText(String.valueOf(arrayProduct.get(position).getQuality())+" adet"+"|");
        holder.txt_price.setText(String.valueOf(arrayProduct.get(position).getPrice()));


    }

    @Override
    public int getItemCount() {
        return arrayProduct.size();
    }


}
