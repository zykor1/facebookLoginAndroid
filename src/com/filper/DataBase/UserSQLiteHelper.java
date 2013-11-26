package com.filper.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserSQLiteHelper extends SQLiteOpenHelper{

	private static int version = 4;
	private static String dbName = "UserFilperDB";
	private static CursorFactory factory = null;
	private static final String TAG = "com.filper.websocketService";
	private static final String sqlCreate = "CREATE TABLE UserFilperDB(" +
	          " _id INTEGER PRIMARY KEY AUTOINCREMENT," +
	          " id_facebook TEXT NOT NULL," +
	          " accessToken TEXT NOT NULL," +
	          " first_name TEXT NOT NULL," +
	          " wsuri TEXT NOT NULL) ";
	
	public UserSQLiteHelper(Context context){
		super(context, dbName, factory, version);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db){
		
		db.execSQL(sqlCreate);
		db.execSQL( "CREATE UNIQUE INDEX id_facebook ON UserFilperDB (id_facebook ASC)" );
		 Log.d(TAG, "Table create");	
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL("DROP TABLE IF EXISTS UserFilperDB");
		
		//Se crea la nueva versión de la tabla
		db.execSQL(sqlCreate);
		db.execSQL( "CREATE UNIQUE INDEX id_facebook ON UserFilperDB (id_facebook ASC)" );
		Log.d(TAG, "Updated table");
	}
}