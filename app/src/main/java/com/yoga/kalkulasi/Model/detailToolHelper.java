package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class detailToolHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_detail_tool";
    public static final  String TABLE_NAME = "tb_detail_tool";

    public static final  String COL_1 = "ID_TOOL";
    public static final  String COL_2 = "ID_VARIABEL";
    public static final  String COL_3 = "FORMULA";

    public detailToolHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + 
                   "(ID_TOOL INTEGER," +
                   "ID_VARIABEL INTEGER,FORMULA TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String id_tool, String id_variabel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, id_tool);
        contentValues.put(COL_2, id_variabel);
        contentValues.put(COL_3, "-");
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

    public Cursor getVariableById(String id_variabel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_VARIABEL = ?", new String[]{id_variabel});
        return  res;
    }
    public Cursor getVariableByTool(String id_tool)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_TOOL = ?", new String[]{id_tool});
        return  res;
    }
    public String getFormula(String id_tool,String id_variabel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_TOOL = ? AND ID_VARIABEL=?", new String[]{id_tool,id_variabel});
         String formula = "";
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                formula =  res.getString(2);
            }
        }
        return formula;
    }
    
    public boolean updateFormula(String id_tool,String id_variabel,String formula){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        
        contentValues.put(COL_3, formula);
        
        int result =db.update(TABLE_NAME, contentValues, " ID_TOOL = ? AND ID_VARIABEL = ?" , new String[]{id_tool,id_variabel});
        
        if (result > 0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
 

    public Integer deleteData(String id_tool,String id_variabel)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, " ID_VARIABEL=? AND ID_TOOL=?", new String[]{id_variabel,id_tool});
        return i;
    }
    
    public Integer deleteDataByTool(String id_tool)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, " ID_TOOL=?", new String[]{id_tool});
        return i;
    }

}
