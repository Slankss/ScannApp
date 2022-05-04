package com.example.scanapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "Shop.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "product";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_QUALITY = "quality";

    private static final String TABLE_NAME2 = "card";
    private static final String COLUMN_TOTAL_PRICE = "totalPrice";



    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {



        String query = "CREATE TABLE " + TABLE_NAME + " ( " +
                COLUMN_ID + " TEXT , "+
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " FLOAT, " +
                COLUMN_QUALITY + " INTEGER )";

        String query2 = "CREATE TABLE " + TABLE_NAME2 + " ( " +
                COLUMN_ID + " TEXT , "+
                COLUMN_NAME + " TEXT, " +
                COLUMN_PRICE + " FLOAT, " +
                COLUMN_TOTAL_PRICE + " FLOAT, " +
                COLUMN_QUALITY + " INTEGER )";

        db.execSQL(query2);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME );
        onCreate(db);

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME2 );
        onCreate(db);
    }

    public void AddProduct(String id,String name, Double price, int quality){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID,id);
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_PRICE,price);
        cv.put(COLUMN_QUALITY,quality);
        long result = db.insert(TABLE_NAME,null,cv);
        if(result == -1){
            Toast.makeText(context, "Ürünü veri tabanına ekleyemedik", Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(context, "Ürünü veri tabanına başarıyla ekledik", Toast.LENGTH_SHORT).show();
        }

    }
    
    public Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }


    public Cursor readSepetData(){
        String query = "SELECT * FROM " + TABLE_NAME2;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null){
            cursor = db.rawQuery(query,null);
        }
        return cursor;

    }


    public void SepeteEkle(String id, String name, double price,double totalPrice, int quality){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ID,id);
        cv.put(COLUMN_NAME,name);
        cv.put(COLUMN_PRICE,price);
        cv.put(COLUMN_TOTAL_PRICE,totalPrice);
        cv.put(COLUMN_QUALITY,quality);
        long result = db.insert(TABLE_NAME2,null,cv);
        if(result == -1){
            Toast.makeText(context, "Ürünü Sepete ekleyemedik", Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(context, "Ürünü sepete ekledik", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteProduct(){

        // Delete All Product

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        long result = db.delete(TABLE_NAME,null,null);
        if(result == -1){
            Toast.makeText(context, "Ürünler Silinemedi", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Ürünler Silindi", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteAllSepet(){

        // Delete All Sepet product

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();


        long result = db.delete(TABLE_NAME2,null,null);
        if(result == -1){
            Toast.makeText(context, "Sepet temizlenirken bir hata oluştu!", Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(context, "Sepet Temizlendi", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateSepetInAdd(String id,double totalPrice,int quality){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TOTAL_PRICE,totalPrice);
        cv.put(COLUMN_QUALITY,quality);

        long result = db.update(TABLE_NAME2,cv,"id=?",new String[]{id});

        if(result == -1){
            Toast.makeText(context, "Ürün sepete eklenemedi.", Toast.LENGTH_SHORT).show();
        }
        else{
           // Toast.makeText(context, "Ürün sepete eklendi.", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateProductInAdd(String id,int quality){

        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_QUALITY,quality);

        long result = db.update(TABLE_NAME,cv,"id=?",new String[]{id});

        if(result == -1){
            Toast.makeText(context, "Stok miktarı güncellenmedi.", Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(context, "Stok miktarı güncellendi.", Toast.LENGTH_SHORT).show();
        }

    }

    public void updateProductInClearCard(String id,int quality){

        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_QUALITY,quality);

        long result = db.update(TABLE_NAME,cv,"id=?",new String[]{id});

        if(result == -1){
            Toast.makeText(context, "Stok miktarı güncellenmedi.", Toast.LENGTH_SHORT).show();
        }
        else{
            //Toast.makeText(context, "Stok miktarı güncellendi.", Toast.LENGTH_SHORT).show();
        }

    }

    public void deleteSelectedItem(String id){
        System.out.println("gelen id = "+id);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME2,"id=?",new String[]{id});

        if(result == -1){
            // başarısız
            Toast.makeText(context, "Ürün silme başarısız", Toast.LENGTH_SHORT).show();
        }
        else{
            // başarılı
            Toast.makeText(context, "Seçilen ürün silindi", Toast.LENGTH_SHORT).show();
        }
    }
}
