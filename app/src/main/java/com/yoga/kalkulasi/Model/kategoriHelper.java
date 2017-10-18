package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class kategoriHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_kategori";
    public static final  String TABLE_NAME = "tb_kategori";

    public static final  String COL_1 = "ID_KATEGORI";
    public static final  String COL_2 = "NAMA_KATEGORI";
 
    public kategoriHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID_KATEGORI INTEGER PRIMARY KEY AUTOINCREMENT,NAMA_KATEGORI TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String nama_kategori)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama_kategori);
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
    public String getNamaKategori(String id_kategori)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE ID_KATEGORI = ?", new String[]{id_kategori});
        String nama_kategori= "";
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                nama_kategori = res.getString(1);
            }
        }
        return nama_kategori;
    }
    public Cursor getKategoriById(String id_kategori)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME +" WHERE ID_KATEGORI = ?", new String[]{id_kategori});
        return  res;
    }

    

    public Integer deleteData(String id_kategori)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_KATEGORI=?", new String[]{id_kategori});
        return i;
    }

    public boolean updateData(String id_kategori,String nama_kategori)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama_kategori);
        
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


}
