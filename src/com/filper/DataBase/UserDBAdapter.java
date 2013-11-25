package com.filper.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDBAdapter{
	public static final String C_TABLA = "UserFilperDB";
	
	public static final String C_COLUMNA_ID = "_id";
	public static final String C_COLUMNA_IDFACEBOOK = "id_facebook";
	public static final String C_COLUMNA_ACCESSTOKEN = "accessToken";
	public static final String C_COLUMNA_FIRSTNAME = "first_name";
	public static final String C_COLUMNA_WSURI = "wsuri";
	
	private Context contexto;
	private UserSQLiteHelper dbHelper;
	private SQLiteDatabase db;
	
	private String[] colums = new String[]{ C_COLUMNA_ID, C_COLUMNA_IDFACEBOOK, C_COLUMNA_ACCESSTOKEN, C_COLUMNA_FIRSTNAME, C_COLUMNA_WSURI};
	
	public UserDBAdapter(Context context){
		this.contexto = context;
	}
	
	public UserDBAdapter open() throws SQLException
	{
		dbHelper = new  UserSQLiteHelper(contexto);
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Cursor getCursor() throws SQLException
	{
		Cursor c = db.query(true, C_TABLA, colums, null, null, null, null, null, null);
		return c;		
	}


	/**
	 * Obtine el id de usuario del facebook
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Cursor getIdFaceebook() throws SQLException
	   {
	      Cursor c = db.query( true, C_TABLA, colums, C_COLUMNA_IDFACEBOOK, null, null, null, null, null);
	 
	      //Nos movemos al primer registro de la consulta
	      if (c != null) {
	         c.moveToFirst();
	      }
	      
	      return c;
	   }
	
	/**
	 * Obtine el primer nombre que tenga el usuario de facebook
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Cursor getIdFirstName() throws SQLException
	   {
	      Cursor c = db.query( true, C_TABLA, colums, C_COLUMNA_FIRSTNAME, null, null, null, null, null);
	 
	      //Nos movemos al primer registro de la consulta
	      if (c != null) {
	         c.moveToFirst();
	      }
	      
	      return c;
	   }	
	
	/**
	 * Obtine el accessToken del usuario
	 * 
	 * @return
	 * @throws SQLException
	 */
	public Cursor getIdAccessToken() throws SQLException
	   {
	      Cursor c = db.query( true, C_TABLA, colums, C_COLUMNA_ACCESSTOKEN, null, null, null, null, null);
	 
	      //Nos movemos al primer registro de la consulta
	      if (c != null) {
	         c.moveToFirst();
	      }
	      
	      return c;
	   }	
	
	
	
/**
 * Retorna todos los datos del usuario conectado
 * @return
 * @throws SQLException
 */
	public Cursor getUser() throws SQLException
	   {
	      Cursor c = db.query( true, C_TABLA, colums,  null, null, null, null, null, null);
	 
	      //Nos movemos al primer registro de la consulta
	      if (c != null) {
	         c.moveToFirst();
	      }
	      
	      return c;
	   }

	
	
	
	
	public long insert(ContentValues reg)
	{
	   if (db == null)
	      open();
	    
	   return db.insert(C_TABLA, null, reg);
	}

	

	public long delete(String idFacebook)
	   {
	      if (db == null)
	         open();
	       
	      return db.delete(C_TABLA, C_COLUMNA_IDFACEBOOK + "=\"" + idFacebook + "\"", null);
	   }
	
	public long delete()
	   {
	      if (db == null)
	         open();
	       
	      return db.delete(C_TABLA, null, null);
	   }
	
	
	
	public int update(ContentValues reg)
	{
	   int result = 0;
	    
	   if (db == null)
	      open();
	    
	   if (reg.containsKey(C_COLUMNA_ID))
	   {
	      //
	      // Obtenemos el id y lo borramos de los valores
	      //
	      long id = reg.getAsLong(C_COLUMNA_ID);
	       
	      reg.remove(C_COLUMNA_ID);
	       
	      //
	      // Actualizamos el registro con el identificador que hemos extraido 
	      //
	      result = db.update(C_TABLA, reg, "_id=" + id, null); 
	   }
	   return result;
	}
	
}