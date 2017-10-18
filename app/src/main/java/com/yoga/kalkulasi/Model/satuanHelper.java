package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class satuanHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_satuan";
    public static final  String TABLE_NAME = "tb_satuan";

    public static final  String COL_1 = "KODE_SATUAN";
    public static final  String COL_2 = "NAMA_SATUAN";

    public satuanHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (KODE_SATUAN INTEGER PRIMARY KEY AUTOINCREMENT,NAMA_SATUAN TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String nama)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();

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

    public boolean cekBarang()
    {
        boolean flag = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        if (res != null && res.getCount() > 0)
        {
            flag = true;
        }

        return flag;
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
    
    public String getNamaSatuan(String kode_satuan){
        Cursor get = getSatuanById(kode_satuan);
        String nama = "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                nama = get.getString(1);
            }
        }
        return nama;
    }

    public Cursor getSatuanById(String kode_satuan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where KODE_SATUAN = ?", new String[]{kode_satuan});
        return  res;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "KODE_BARANG=?", new String[]{id});
        return i;
    }
    public void deleteTabel()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean updateData(String kode_barang, String nama_barang)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama_barang);

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
