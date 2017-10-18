package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class projectHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_project";
    public static final  String TABLE_NAME = "tb_project";

    public static final  String COL_1 = "ID_PROJECT";
    public static final  String COL_2 = "ID_USER";
    public static final  String COL_3 = "NAMA_PROJECT";
    public static final  String COL_4 = "DESKRIPSI";
    public static final  String COL_5 = "TANGGAL";
    public static final  String COL_6 = "LAST_MOD";

    public projectHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID_PROJECT INTEGER PRIMARY KEY AUTOINCREMENT,ID_USER INTEGER,NAMA_PROJECT TEXT,DESKRIPSI TEXT,TANGGAL DATE,LAST_MOD DATE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }

    public boolean insertData(String id_user, String nama_project, String deskripsi, String tanggal, String last_mod)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, id_user);
        contentValues.put(COL_3, nama_project);
        contentValues.put(COL_4, deskripsi);
        contentValues.put(COL_5, tanggal);
        contentValues.put(COL_6, last_mod);

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
    
    public Cursor getProjectById(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_PROJECT = ?", new String[]{id});
        return  res;
    }
    
    public Cursor getProjectByUser(String id_user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where ID_USER = ?", new String[]{id_user});
        return  res;
    }
    
    public Integer deleteData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        int i =db.delete(TABLE_NAME, "ID_PROJECT=?", new String[]{id});
        return i;
    }

    public String getLastProject()
    {
        String id_project = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " ORDER BY ID_PROJECT DESC LIMIT 1", null);
        if (res != null && res.getCount() > 0)
        {
            while (res.moveToNext())
            { 
                id_project = res.getString(0);
            }
        }
        return id_project;
    }

    public boolean updateData(String id_project, String id_user, String nama_project, String deskripsi, String last_mod)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, id_user);
        contentValues.put(COL_3, nama_project);
        contentValues.put(COL_4, deskripsi);
        contentValues.put(COL_6, last_mod);
        int result =db.update(TABLE_NAME, contentValues, " ID_PROJECT = " + id_project, null);
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
