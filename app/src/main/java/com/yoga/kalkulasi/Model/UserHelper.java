package com.yoga.kalkulasi.Model;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

public class UserHelper extends SQLiteOpenHelper
{
    public static final  String DATABASE_NAME = "db_rumus";
    public static final  String TABLE_NAME = "tb_user";

    public static final  String COL_1 = "ID";
    public static final  String COL_2 = "NAMA";
    public static final  String COL_3 = "USERNAME";
    public static final  String COL_4 = "PASSWORD";

    public UserHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT,NAMA TEXT,USERNAME TEXT,PASSWORD TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

	public boolean insertData(String nama,String username,String password){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(COL_2,nama);
		contentValues.put(COL_3,username);
		contentValues.put(COL_4,password);
		long result = db.insert(TABLE_NAME,null,contentValues);
		db.close();

		//To Check Whether Data is Inserted in DataBase
		if(result==-1){
			return false;
		}else{
			return true;
		}
	}
	public Cursor getAllData(){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("Select * from " + TABLE_NAME,null);
		return  res;
	}
	
	public boolean VerifyLogin(String username, String password){
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where username = '"+username+"' AND password = '"+password+"' ",null);
		if(res!=null && res.getCount()>0){
            return true;
        }else{
            return false;
        }
	} 
	
	public String getUserLogin(String username, String password){
		String id = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("Select * from " + TABLE_NAME + " where username = '"+username+"' AND password = '"+password+"' ",null);
		if(res!=null && res.getCount()>0){
          
			while (res.moveToNext()){
				id = res.getString(0);
			}
			
			return id;

        }else{
            return null;
        }
	}
	
	public String getNamaUser(String id){
		String nama = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = db.rawQuery("Select NAMA from " + TABLE_NAME + " where ID =  '"+id+"' ",null);
		if(res!=null && res.getCount()>0){

			while (res.moveToNext()){
				nama = res.getString(0);
			}

			return nama;

        }else{
            return null;
        }
	}
}
