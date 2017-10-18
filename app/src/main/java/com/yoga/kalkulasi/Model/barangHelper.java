package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;

public class barangHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_barang";
    public static final  String TABLE_NAME = "tb_barang";

    public static final  String COL_1 = "KODE_BARANG";
    public static final  String COL_2 = "NAMA_BARANG";
    public static final  String COL_3 = "ID_KATEGORI";
    public static final  String COL_4 = "BESARAN";
    
    Context con;


    public barangHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        con = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (KODE_BARANG CHAR(12) PRIMARY KEY NOT NULL,NAMA_BARANG TEXT,ID_KATEGORI INTEGER,BESARAN NUMBER)");
        db.execSQL("CREATE UNIQUE INDEX index_nama ON "+TABLE_NAME+" (NAMA_BARANG)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String kode_barang, String nama, String id_kategori, String besaran)
    {
        long result = 0;
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(COL_1, kode_barang);
            contentValues.put(COL_2, nama);
            contentValues.put(COL_3, id_kategori);
            contentValues.put(COL_4, besaran);
            result = db.insert(TABLE_NAME, null, contentValues);
            db.close();
        }catch(SQLiteFullException e){
            Toast.makeText(con,e.toString(),Toast.LENGTH_SHORT).show();
        }
        

        //To Check Whether Data is Inserted in DataBase
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        return  res;
    }
    public String getBarangTerakhir()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select KODE_BARANG from " + TABLE_NAME + " ORDER BY KODE_BARANG ASC", null);
        String id= "";
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id = res.getString(0);
            }
        }
        return id;
    }

    public Cursor getBarangById(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where KODE_BARANG = ?", new String[]{id});
        return  res;
    }
    
    public Cursor getBarangByKategori(String id_kategori)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_KATEGORI = ?", new String[]{id_kategori});
        return  res;
    }
    
    public String getNamaBarang(String kodeBarang){
        Cursor get = getBarangById(kodeBarang);
        String id = "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                id = get.getString(1);
            }
        }
        return id;
    }
    public String getBesaran(String kode_barang){
        Cursor get = getBarangById(kode_barang);
        String id = "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                id = get.getString(3);
            }
        }
        return id;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "KODE_BARANG=?", new String[]{id});
        return i;
    }
    
    public boolean updateKategori(String id_kategori){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        
        contentValues.put(COL_3, "1");
        int result =db.update(TABLE_NAME, contentValues, " ID_KATEGORI = ?", new String[]{id_kategori});
        if (result > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean updateData(String kode_barang, String nama_barang, String id_kategori, String besaran)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama_barang);
        contentValues.put(COL_3, id_kategori);
        contentValues.put(COL_4, besaran);
        int result =db.update(TABLE_NAME, contentValues, " KODE_BARANG = ?", new String[]{kode_barang});
        if (result > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }


}
