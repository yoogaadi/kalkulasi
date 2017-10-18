package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class detailHargaBarangHelper extends SQLiteOpenHelper
{



    public static final  String DATABASE_NAME = "db_detail_harga_barang";
    public static final  String TABLE_NAME = "tb_detail_harga_barang";

    public static final  String DTL_COL[] = {"TANGGAL","KODE","KODE_BARANG","HARGA1","HARGA2","CUR1","CUR2","VALIDITY","KURS_TENGAH", "HARGA_BELI", "GRAFIK"};


    public static final  String BRG_COL_1 = "TANGGAL";
    public static final  String BRG_COL_2 = "KODE";
    public static final  String BRG_COL_3 = "KODE_BARANG";
    public static final  String BRG_COL_4 = "HARGA1";
    public static final  String BRG_COL_5 = "HARGA2";
    public static final  String BRG_COL_6 = "CUR1";
    public static final  String BRG_COL_7 = "CUR2";
    public static final  String BRG_COL_8 = "VALIDITY";
    public static final  String BRG_COL_9 = "KURS_TENGAH";
    public static final  String BRG_COL_10 = "HARGA_BELI";
    public static final  String BRG_COL_11 = "GRAFIK";

    Context cont;
    
    public detailHargaBarangHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        cont = context;
        
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {

        db.execSQL("CREATE TABLE " + TABLE_NAME + 
                   "(TANGGAL DATE not null,KODE CHAR(8) not null,KODE_BARANG CHAR(12) not null," +
                   "HARGA1 NUMBER(15,4) not null,HARGA2 NUMBER(15,4),CUR1 CHAR(2),CUR2 CHAR(2)," +
                   "VALIDITY DATE,KURS_TENGAH NUMBER(15,4),HARGA_BELI CHAR(1),GRAFIK CHAR(1) default '0')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String tanggal, String kode, String kode_barang, String harga)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BRG_COL_1, tanggal);
        contentValues.put(BRG_COL_2, kode);
        contentValues.put(BRG_COL_3, kode_barang);
        contentValues.put(BRG_COL_4, harga);
        contentValues.put(BRG_COL_5, "");
        contentValues.put(BRG_COL_6, "");
        contentValues.put(BRG_COL_7, "");
        contentValues.put(BRG_COL_8, "");
        contentValues.put(BRG_COL_9, "");
        contentValues.put(BRG_COL_10, "");
        contentValues.put(BRG_COL_11, "");


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


    public Cursor getHargaBarangTerakhir(String kode_barang)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE KODE_BARANG = ? ORDER BY TANGGAL ASC LIMIT 1", new String[]{kode_barang});
        return  res;

    }
    
    public String getHargaBarang(String kode_barang){
        Cursor get = getHargaBarangTerakhir(kode_barang);
        String harga = "0";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                harga = get.getString(3);
            }
        }
        return harga;
    }
    
    public String getPerhitunganBarang(String id_detail_kalkulasi,String kode_barang){
        detailItemHelper myItem = new detailItemHelper(cont);
        barangHelper myBarang = new barangHelper(cont);
        
        Double qty = Double.parseDouble(myItem.getQty(kode_barang,id_detail_kalkulasi));
        Double besaran = Double.parseDouble(myBarang.getBesaran(kode_barang));
        Double harga = Double.parseDouble(getHargaBarang(kode_barang));
        
        String total = String.valueOf((harga/besaran)*qty);
        
        return total;
    }
    

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, " KODE_BARANG=? ", new String[]{id});
        return i;
    }

    public boolean updateHarga(String kode_barang, String tanggal, String harga)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(BRG_COL_1, tanggal);
        contentValues.put(BRG_COL_4, harga);

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
