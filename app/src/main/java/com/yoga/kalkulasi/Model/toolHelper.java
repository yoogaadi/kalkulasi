package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class toolHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_tool";
    public static final  String TABLE_NAME = "tb_tool";

    public static final  String COL_1 = "ID_TOOL";
    public static final  String COL_2 = "NAMA_TOOL";
    public static final  String COL_3 = "DESKRIPSI";
    public static final  String COL_4 = "ID_KATEGORI";

    public toolHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID_TOOL INTEGER PRIMARY KEY AUTOINCREMENT,NAMA_TOOL TEXT,DESKRIPSI TEXT,ID_KATEGORI INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String nama, String deskripsi, String id_kategori)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama);
        contentValues.put(COL_3, deskripsi);
        contentValues.put(COL_4, id_kategori);
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
    public String getIdToolTerakhir()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select ID from " + TABLE_NAME + " ORDER BY ID_TOOL ASC", null);
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
    public String getNamaTool(String id_tool){
        String nama = "";
        Cursor res = getToolById(id_tool);
        if(res!=null && res.getCount()>0){
            while(res.moveToNext()){
                nama = res.getString(1);
            }
            
        }
        return nama;
    }

    public Cursor getToolById(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_TOOL = ?", new String[]{id});
        return  res;
    }

    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_TOOL=?", new String[]{id});
        return i;
    }

    public String getLastTool()
    {
        String id_tool = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " ORDER BY ID_TOOL DESC LIMIT 1", null);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id_tool = res.getString(0);
            }

        }
        return id_tool;
    }

    public boolean updateData(String id_tool, String nama_tool, String deskripsi_tool)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, nama_tool);
        contentValues.put(COL_3, deskripsi_tool);
        int result =db.update(TABLE_NAME, contentValues, " ID_TOOL = ?", new String[]{id_tool});
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
