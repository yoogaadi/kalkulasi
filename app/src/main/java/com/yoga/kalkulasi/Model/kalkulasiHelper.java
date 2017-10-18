package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class kalkulasiHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_subKalkulasi";
    public static final  String TABLE_NAME = "tb_sub_kalkulasi";

    public static final  String COL_1 = "ID_KALKULASI";
    public static final  String COL_2 = "NAMA";
    public static final  String COL_3 = "ID_SUBKALKULASI_UP";
    public static final  String COL_4 = "DESKRIPSI";
    public static final  String COL_5 = "GLOBAL";

    public kalkulasiHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID_KALKULASI INTEGER PRIMARY KEY AUTOINCREMENT,NAMA TEXT,ID_SUBKALKULASI_UP INTEGER NULL,DESKRIPSI TEXT,GLOBAL TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String nama, String id_kalkulasi_up, String deskripsi,String global)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, id_kalkulasi_up);
        contentValues.put(COL_4, deskripsi);
        contentValues.put(COL_5, global);

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
    public Cursor getKalkulasiNoGlobal(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE GLOBAL LIKE '0' ", null);
        return  res;
    }
    public Cursor getKalkulasiById(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_KALKULASI = ?", new String[]{id});
        return  res;
    }
    public Cursor getKalkulasiParrent()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_SUBKALKULASI_UP IS NULL OR ID_SUBKALKULASI_UP = '' ", null);
        return  res;
    }
    public Cursor getKalkulasiByParrent(String id_parrent)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_SUBKALKULASI_UP =?", new String[]{id_parrent});
        return  res;
    }
    public Cursor getKalkulasiParentNoSubById(String id_kalkulasi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_KALKULASI = ? AND ID_SUBKALKULASI_UP IS NULL ", new String[]{id_kalkulasi});
        return  res;
    }
    public String getNamaKalkulasi(String id_kalkulasi){
        String nama = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor coba = db.rawQuery("Select * from " + TABLE_NAME + " WHERE ID_KALKULASI = ?", new String[]{id_kalkulasi});
        if (coba != null && coba.getCount() > 0)
        {
            while (coba.moveToNext())
            { 
                nama = coba.getString(1);
            }
        }
        return nama;
    }
    
    public int cekJumlahSub(String id_kalkulasi){
        Cursor get = getKalkulasiById(id_kalkulasi);
        return get.getCount();
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_KALKULASI=?", new String[]{id});
        return i;
    }
    public String getLastKalkulasi()
    {
        String id_kalkulasi = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " ORDER BY ID_KALKULASI DESC LIMIT 1", null);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id_kalkulasi = res.getString(0);
            }

        }
        return id_kalkulasi;
    }

    public boolean updateData(String id_kalkulasi, String nama, String id_subkalkulasi_up, String deskripsi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, id_subkalkulasi_up);
        contentValues.put(COL_4, deskripsi);

        int result =db.update(TABLE_NAME, contentValues, " ID_KALKULASI = " + id_kalkulasi, null);
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
