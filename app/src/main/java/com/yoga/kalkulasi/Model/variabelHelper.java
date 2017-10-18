package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class variabelHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_variabel";
    public static final  String TABLE_NAME = "tb_variabel";

    public static final  String COL_1 = "ID_VARIABEL";
    public static final  String COL_2 = "NAMA_VARIABEL";
    public static final  String COL_3 = "KODE_SATUAN";

    public variabelHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + 
                   "(ID_VARIABEL INTEGER PRIMARY KEY AUTOINCREMENT," +
                   "NAMA_VARIABEL TEXT,KODE_SATUAN INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String nama, String kode_satuan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, kode_satuan);
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

    public Cursor getVariableById(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_VARIABEL = ?", new String[]{id});
        return  res;
    }
    public String getLastVariable()
    {
        String id_variabel = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " ORDER BY ID_VARIABEL DESC LIMIT 1", null);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id_variabel = res.getString(0);
            }

        }
        return id_variabel;
    }

    public String getNamaVariabel(String id_variabel){
        
        Cursor get = getVariableById(id_variabel);
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
    
    public String getSatuan(String id_variabel){
        Cursor get = getVariableById(id_variabel);
        String id_satuan = "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                id_satuan = get.getString(2);
            }

        }
        return id_satuan;
    }


    public boolean updateData(String id, String nama, String kode_satuan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, nama);

        contentValues.put(COL_3, kode_satuan);

        int result =db.update(TABLE_NAME, contentValues, " ID_VARIABEL = " + id, null);
        if (result > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public Integer deleteData(String id_variabel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_VARIABEL=?", new String[]{id_variabel});
        return i;
    }

}
