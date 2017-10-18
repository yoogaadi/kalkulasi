package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class detailVariabelHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_detail_variabel";
    public static final  String TABLE_NAME = "tb_detail_variabel";


    public static final  String COL_1 = "ID_DETAIL_KALKULASI";
    public static final  String COL_2 = "ID_VARIABEL";
    public static final  String COL_3 = "VALUE";
    public static final  String COL_4 = "ALIAS";
    
    public Context con;

    public detailVariabelHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        con = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + 
                   "(ID_DETAIL_KALKULASI INTEGER," +
                   "ID_VARIABEL INTEGER,VALUE NUMBER,ALIAS TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String id_detail, String id_variabel, String value, String alias)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id_detail);
        contentValues.put(COL_2, id_variabel);
        contentValues.put(COL_3, value);
        contentValues.put(COL_4, alias);
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

    public boolean insertCopyVariabel(String id_detail, String id_detail_baru)
    {

        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = false;
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_DETAIL_KALKULASI = ?", new String[]{id_detail});
        String id_variabel = "";
        String value = "";
        String alias = "";
        
        if (res != null && res.getCount() > 0)
        {
            ContentValues contentValues = new ContentValues();
            long result = 0;
            while (res.moveToNext())
            { 
                id_variabel = res.getString(1);
                value = res.getString(2);
                alias = res.getString(3);
                
                contentValues.put(COL_1, id_detail_baru);
                contentValues.put(COL_2, id_variabel);
                contentValues.put(COL_3, value);
                contentValues.put(COL_4, alias);
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


    public String insert(String id_detail_kalkulasi, String id_variabel, String value, String alias)
    {
        return id_detail_kalkulasi + " = " + id_variabel + " = " + value + " = " + alias;
    }

    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME, null);
        return  res;
    }
    public Cursor getDetailById(String id_detail_kalkulasi)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_DETAIL_KALKULASI = ?", new String[]{id_detail_kalkulasi});
        return  res;
    }
    public Cursor getDetailByIdAndKalkulasi(String id,String id_var)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_DETAIL_KALKULASI = ? AND ID_VARIABEL = ?", new String[]{id,id_var});
        return  res;
    }
    public String getValue(String id_detail_kal,String id_var){
        Cursor get = getDetailByIdAndKalkulasi(id_detail_kal,id_var);
        String nilai = "";
        if (get != null && get.getCount() > 0)
        {
            while (get.moveToNext())
            { 
                nilai = get.getString(2);
            }
        }
        
        return nilai;
    }
    
    public Cursor getDetailByVar(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_VARIABEL = ?", new String[]{id});
        return  res;
    }

    public Integer deleteData(String id_detail, String id_variabel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_DETAIL_KALKULASI=? AND ID_VARIABEL = ?", new String[]{id_detail,id_variabel});
        return i;
    }
    
    public Integer deleteDataByIdDetail(String id_detail){
        SQLiteDatabase db = this.getWritableDatabase();
        
        variabelHelper var = new variabelHelper(con);
        
           
        int i =db.delete(TABLE_NAME, "ID_DETAIL_KALKULASI=?", new String[]{id_detail});
        return i;
    }

    public boolean updateData(String id_detail_kalkulasi, String id_variabel, String value, String alias)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, id_variabel);
        contentValues.put(COL_3, value);
        contentValues.put(COL_4, alias);

        int result =db.update(TABLE_NAME, contentValues, " ID_DETAIL_KALKULASI = " + id_detail_kalkulasi, null);
        if (result > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean updateValue(String id_detail_kalkulasi, String id_variabel, String value)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_3, value);

        int result =db.update(TABLE_NAME, contentValues, " ID_DETAIL_KALKULASI = " + id_detail_kalkulasi + " AND ID_VARIABEL = " + id_variabel, null);
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

