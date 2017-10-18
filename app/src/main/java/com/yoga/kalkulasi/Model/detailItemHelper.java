package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.widget.*;

public class detailItemHelper extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "db_detail_item";
    public static final String TABLE_NAME = "tb_detail_item";

    public static final String COL_1 = "ID_DETAIL_KALKULASI";
    public static final String COL_2 = "KODE_BARANG";
    public static final String COL_3 = "QTY";
    public static final String COL_4 = "KODE_SATUAN";
        public Context con;

    public detailItemHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        con = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID_DETAIL_KALKULASI INTEGER,KODE_BARANG CHAR(12),QTY TEXT, KODE_SATUAN INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String id_kalkulasi, String kode_barang, String qty, String kode_satuan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id_kalkulasi);
        contentValues.put(COL_2, kode_barang);
        contentValues.put(COL_3, qty);
        contentValues.put(COL_4, kode_satuan);

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

    public boolean insertCopyItem(String id_detail, String id_detail_baru)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = false;
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_DETAIL_KALKULASI = ?", new String[]{id_detail});
        String kode_barang = "";
        String qty = "";
        String kode_satuan = "";
        //Toast.makeText(con,res.getCount(),Toast.LENGTH_SHORT).show();
        if (res != null && res.getCount() > 0)
        {
            ContentValues contentValues = new ContentValues();
            long result = 0;
            while (res.moveToNext())
            { 
                kode_barang = res.getString(1);
                qty = res.getString(2);
                kode_satuan = res.getString(3);
                
                contentValues.put(COL_1, id_detail_baru);
                contentValues.put(COL_2, kode_barang);
                contentValues.put(COL_3, qty);
                contentValues.put(COL_4, kode_satuan);

                result = db.insert(TABLE_NAME, null, contentValues);
            }
            
            
            db.close();

            //To Check Whether Data is Inserted in DataBase
            if (result == -1)
            {
                flag = false;
            }
            else
            {
                flag = true;
            }
        }
        return flag;
    }


    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        return  res;
    }
    public Cursor getDetailByDetailKalkulasi(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_DETAIL_KALKULASI = ?", new String[]{id});
        return  res;
    }
    public Cursor getBarangByKode(String kode){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where KODE_BARANG = ?", new String[]{kode});
        return  res;
    }
  
    public String getQty(String kode_barang,String id_detail_kalkulasi){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor get = db.rawQuery("Select * from " + TABLE_NAME + " where KODE_BARANG = ? AND ID_DETAIL_KALKULASI = ? ", new String[]{kode_barang,id_detail_kalkulasi});
 
        String qty = "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                qty = get.getString(2);
            }

        }
        return qty;
    }
    
    public int getCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME ,null);
       
        
        return res.getCount();
    }
    
    public String getKodeBarang(String id_detail_kalkulasi){
        Cursor get = getDetailByDetailKalkulasi(id_detail_kalkulasi);
        String kode_barang = "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                kode_barang = get.getString(1);
            }

        }
        return kode_barang;
    }

    public Integer deleteData(String id_detail_kalkulasi, String kodeBarang)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, " ID_DETAIL_KALKULASI=? AND KODE_BARANG = ?", new String[]{id_detail_kalkulasi,kodeBarang});
        return i;
    }
    public Integer deleteDataByIdDetailKalkulasi(String id_detail_kalkulasi){
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, " ID_DETAIL_KALKULASI=?", new String[]{id_detail_kalkulasi});
        return i;
    }
    
    public Integer deleteDataByKodeBarang(String id_barang){
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, " KODE_BARANG=?", new String[]{id_barang});
        return i;
    }

    public boolean updateData(String id_detail, String kode_barang, String qty, String kode_satuan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id_detail);
        contentValues.put(COL_2, kode_barang);
        contentValues.put(COL_3, qty);
        contentValues.put(COL_4, kode_satuan);

        int result =db.update(TABLE_NAME, contentValues, " ID_DETAIL_KALKULASI = ? AND KODE_BARANG = ? ", new String[]{id_detail,kode_barang});
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
