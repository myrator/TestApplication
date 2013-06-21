package com.andruha.android.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;



public class DB {
	
	private static final String DB_NAME = "mydb";
	private static final int DB_VERSION = 1;
	private static final String DB_TABLE = "mytbl";
	
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_IMG = "img";
	public static final String COLUMN_TXT = "txt";
	
	private static final String DB_CREATE = "create table " + DB_TABLE + "(" +
	COLUMN_ID + " integer primary key autoincrement, " + COLUMN_IMG + " integer, " +
	COLUMN_TXT + " text" + ");";
	
	private final Context mCtx;
	
	private DBHelper mDBHlpr;
	private SQLiteDatabase myDB;
	
	public DB(Context ctx){
		mCtx = ctx;
	}
	
	public void open() {
		mDBHlpr = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
		myDB = mDBHlpr.getWritableDatabase();
	}
	
	public void close() {
		if (mDBHlpr != null) mDBHlpr.close();
	}
	
	// получить все данные
	public Cursor getAllData() {
		return myDB.query(DB_TABLE, null, null, null, null, null, null);
	}
	
	// добавить запись в таблицу 
	public void addRec(String txt, int img) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TXT, txt);
		cv.put(COLUMN_IMG, img);
		myDB.insert(DB_TABLE, null, cv);
	}
	
	//обновляем значения по id
	public void updateRec(int id, String txt, int img) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TXT, txt);
		cv.put(COLUMN_IMG, img);
		myDB.update(DB_TABLE, cv, "_id = ?", new String[] { String.valueOf(id)});
	}
	
	//обновляем значения
	public void updateRec(String txt, int img) {
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_TXT, txt);
		cv.put(COLUMN_IMG, img);
		myDB.update(DB_TABLE, cv, "txt = ?", new String[] {txt});
	}
	
	//удаляем запись по id
	public void deleteRec(int id){
		myDB.delete(DB_TABLE, "_id = ?", new String[] {String.valueOf(id)});
	}
	
	private class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DB_CREATE);
			
			ContentValues cv = new ContentValues();
			for (int i = 1; i < 5; i++){
				cv.put(COLUMN_TXT, "item" + i);
				cv.put(COLUMN_IMG, i % 2);
				db.insert(DB_TABLE, null, cv);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		}		
	}
}
