package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class hargaBarangHelper extends SQLiteOpenHelper
{

    public static final  String DATABASE_NAME = "db_harga_barang";
    public static final  String TABLE_NAME = "tb_harga_barang";

    public static final  String BRG_COL_1 = "KODE";
    public static final  String BRG_COL_2 = "KODE_BARANG";
    public static final  String BRG_COL_3 = "KODE_SATUAN";
    public static final  String BRG_COL_4 = "TOP";
    public static final  String BRG_COL_5 = "MOQ";
    public static final  String BRG_COL_6 = "LEAD_TIME";
    public static final  String BRG_COL_7 = "SAT_LEAD";
    public static final  String BRG_COL_8 = "VALIDITY";
    public static final  String BRG_COL_9 = "KET";
    public static final  String BRG_COL_10 = "BY_MASUK";
    public static final  String BRG_COL_11 = "BY_ADM";
    public static final  String BRG_COL_12 = "GRAFIK"; 


    public hargaBarangHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + 
                   " (KODE CHAR(8) not null,KODE_BARANG CHAR(12) not null,KODE_SATUAN CHAR(3)," +
                   "TOP NUMBER(3),MOQ NUMBER(12,2),LEAD_TIME NUMBER(3),SAT_LEAD NUMBER(3)," +
                   "VALIDITY DATE,KET VARCHAR2(200),BY_MASUK NUMBER(4,2),BY_ADM NUMBER(4,2),GRAFIK CHAR(1))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String kode, String kode_barang, String kode_satuan, String keterangan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BRG_COL_1, kode);
        contentValues.put(BRG_COL_2, kode_barang);
        contentValues.put(BRG_COL_3, kode_satuan);
        contentValues.put(BRG_COL_4, "");
        contentValues.put(BRG_COL_5, "");
        contentValues.put(BRG_COL_6, "");
        contentValues.put(BRG_COL_7, "");
        contentValues.put(BRG_COL_8, "");
        contentValues.put(BRG_COL_9, keterangan);
        contentValues.put(BRG_COL_10, "");
        contentValues.put(BRG_COL_11, "");
        contentValues.put(BRG_COL_12, "");


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
    public Cursor getDataByKodeBarang(String kode_barang)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE KODE_BARANG = ?", new String[]{kode_barang});
        return res;
    }
    public String getKodeSatuan(String kode_barang){
        Cursor get = getDataByKodeBarang(kode_barang);
        String kode_satuan = "";
        if(get!=null && get.getCount()>0){
            while(get.moveToNext()){
                kode_satuan = get.getString(2);
            }
        }
        return kode_satuan;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "KODE_BARANG=?", new String[]{id});
        return i;
    }


    public boolean updateData(String kode, String kode_barang, String kode_satuan, String keterangan)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BRG_COL_1, kode);
        contentValues.put(BRG_COL_3, kode_satuan);
        contentValues.put(BRG_COL_9, keterangan);

        int result =db.update(TABLE_NAME, contentValues, " KODE_BARANG LIKE ?",new String[]{kode_barang});
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
