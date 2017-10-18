package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class detailKalkulasiHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_detail_kalkulasi";
    public static final  String TABLE_NAME = "tb_detail_kalkulasi";

    public static final  String COL_1 = "ID_DETAIL_KALKULASI";
    public static final  String COL_2 = "ID_KALKULASI";
    public static final  String COL_3 = "ID_PROJECT";
    public static final  String COL_4 = "FORMULA";

    public detailKalkulasiHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID_DETAIL_KALKULASI INTEGER PRIMARY KEY AUTOINCREMENT,ID_KALKULASI INTEGER,ID_PROJECT INTEGER,FORMULA TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String id_kalkulasi, String id_project,String formula)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, id_kalkulasi);
        contentValues.put(COL_3, id_project);
        contentValues.put(COL_4, formula);
        
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

    public boolean insertCopyData(String id_detail_kalkulasi, String id_project)
    {
        String id_kalkulasi = "";
        String formula = "";
        SQLiteDatabase db = this.getWritableDatabase();

        db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_DETAIL_KALKULASI = ?", new String[]{id_detail_kalkulasi});
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id_kalkulasi = res.getString(1);
                formula = res.getString(3);
            }
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, id_kalkulasi);
        contentValues.put(COL_3, id_project);
        contentValues.put(COL_4, formula);

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
    public Cursor getDetailById(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_DETAIL_KALKULASI = ?", new String[]{id});
        return  res;
    }
    public Cursor getDetailByProject(String id_project)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_PROJECT = ?", new String[]{id_project});
        return  res;
    }
    public String getIdKalkulasi(String id_detail_kalkulasi){
        String id_kalkulasi = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " WHERE ID_DETAIL_KALKULASI = ?",new String[]{id_detail_kalkulasi});
        if(res!=null && res.getCount()>0){
            while (res.moveToNext()){ 
                id_kalkulasi = res.getString(1);
            }

        }
        return id_kalkulasi;
    }
    public boolean isTemplate(String id_kalkulasi){
        Cursor res = getDetailByKalkulasi(id_kalkulasi);
        boolean flag = false;
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                if(res.getString(2).equals("-")){
                    flag = true;
                }
            }
        }
        return flag;
        
    }
    
    public String getLastIdKalkulasi(){
        String id_detail_kalkulasi = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " ORDER BY ID_DETAIL_KALKULASI DESC LIMIT 1",null);
        if(res!=null && res.getCount()>0){
            while (res.moveToNext()){ 
                id_detail_kalkulasi = res.getString(0);
            }

        }
        return id_detail_kalkulasi;
    }
    public String getFormula(String id_detail_kalkulasi){
        Cursor res = getDetailById(id_detail_kalkulasi);
        String formula = "";
        if(res!=null && res.getCount()>0){
            while (res.moveToNext()){ 
                formula = res.getString(3);
            }

        }
        
        return formula;
    }
    
    public String getIdDetailKalkulasi(String id_kalkulasi,String id_project)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = getDetailKalkulasi(id_kalkulasi,id_project);
        
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

    public Cursor getDetailByKalkulasi(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_KALKULASI = ?", new String[]{id});
        return  res;
    }

    public Cursor getDetailKalkulasi(String id, String id_project)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_KALKULASI = ? and ID_PROJECT=?", new String[]{id,id_project});
        return  res;
    }
    
    
    public String getIdDetailByProject(String id_kalkulasi, String id_project){
        Cursor det = getDetailKalkulasi(id_kalkulasi,id_project);
        String id_detail_kalkulasi = "";
        if(det != null && det.getCount() >0){
            while(det.moveToNext()){
               id_detail_kalkulasi = det.getString(0); 
            }
        }
        
        return id_detail_kalkulasi;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_DETAIL_KALKULASI=?", new String[]{id});
        return i;
    }
    
    public Integer deleteDataByProject(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_PROJECT=?", new String[]{id});
        return i;
    }

    public boolean updateData(String id_detail, String id_kalkulasi, String id_project)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, id_kalkulasi);
        contentValues.put(COL_3, id_project);
 

        int result =db.update(TABLE_NAME, contentValues, " ID_DETAIL_PROJECT = " + id_detail, null);
        if (result > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public boolean updaeFormula(String id_detail, String formula)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_4, formula);


        int result =db.update(TABLE_NAME, contentValues, " ID_DETAIL_KALKULASI = " + id_detail, null);
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
