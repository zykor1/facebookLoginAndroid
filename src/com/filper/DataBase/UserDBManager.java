package com.filper.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class UserDBManager{
	private UserDBAdapter userAdapter;
	private Cursor cursor;
	
	
	
	
	public UserDBManager(Context context){
		userAdapter = new UserDBAdapter(context);
		userAdapter.open();		
	}
	
	public void close(){
		userAdapter.close();
	}
	
	
	/**
	 *  Retorna Si el usuario se encuentra conectado
	 *  
	 * @param userName : Get the user name as String
	 * @return  0 - If the user is not connected,
	 * 			1 - If the user is connected
	 */
	public int getStatusUser(String userName){
		cursor = userAdapter.getUser();
		if (cursor == null)
			return 0;
		else
			return 1;
	}

	
	/**
	 * Regresa un arreglo de los datos del usuario conectado en el siguiente orden
	 * [0] - Id, [1] - Id Facebook, [2] - AccessToken, [3] - Primer nombre, [4] - direccion del websocket 
	 * @return null - If the user no exist, 
	 * 		   String with userName - If the user exist	
	 */			
	public String[] getInfoUser(){
		cursor = userAdapter.getUser();
		if (cursor != null){
			String[] infoUser = new String[5];
			infoUser[0] = cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_ID));
			infoUser[1] = cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_IDFACEBOOK));
			infoUser[2] = cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_ACCESSTOKEN));
			infoUser[3] = cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_FIRSTNAME));
			infoUser[4] = cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_WSURI));
			return infoUser;
		}
		return null;
	}	
	
	/**
	 * Obtiene el accessToken de facebook del usuario
	 * @return
	 * 	Retorna AccessToken como String
	 */
	public String getAccessToken(){
		cursor = userAdapter.getIdAccessToken();
		if (cursor != null){
			return  cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_ACCESSTOKEN));
		}
		return null;
	}
	
	/**
	 * Obtiene el Id de facebook del usuario
	 * @return
	 * 	Retorna el id facebook del usuario como String
	 */ 
	public String getIdFacebook(){
		cursor = userAdapter.getIdFaceebook();
		if (cursor != null){
			return  cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_IDFACEBOOK));
		}
		return null;
	}
	
	/**
	 * Obtiene el primer nombre en facebook del usuario
	 * @return
	 * 	Retorna primer nombre como String
	 */	
	public String getFirstName(){
		cursor = userAdapter.getIdFirstName();
		if (cursor != null){
			return  cursor.getString(cursor.getColumnIndex(userAdapter.C_COLUMNA_FIRSTNAME));
		}
		return null;
	}
	
	
	
/**
 * Inserta el nuevo usuario a la tabla
 * 
 * @param idFacebook Recibe el id del usuario en facebook
 * @param accessToken Recibe el access token del usuario en Facebook
 * @param firstName Recibe el primer nombre del usuario en Facebook
 * @param wsuri Recibe la direccion del websocket a donde se conectara el usuario
 * @return Retorna 0 si no se ha podido guardar el usuario
 * 	o retorna 1 si es que el valor ha sido guardado.
 * Si la base de datos ya cuenta con un usuario no podra gudardarse un 
 * nuevo usuario
 */
	public int setUser(String idFacebook, String accessToken, String firstName, String wsuri){
		cursor = userAdapter.getCursor();
		int count = cursor.getCount();
		if (count > 0)
			return 0;
		else{
			ContentValues reg = new ContentValues();
			reg.put(userAdapter.C_COLUMNA_IDFACEBOOK, idFacebook);
			reg.put(userAdapter.C_COLUMNA_ACCESSTOKEN, accessToken);
			reg.put(userAdapter.C_COLUMNA_FIRSTNAME, firstName);
			reg.put(userAdapter.C_COLUMNA_WSURI, wsuri);
			userAdapter.insert(reg);
		}
		return 0;
	}

/**
 * Elimina al usuario conectado
 * @return
 * Regresa el resultado en int de la eliminacion
 */
	public int deleteUser(){
		int auxLong = (int) userAdapter.delete();
		return auxLong;
	}
	
	/**
	 * Actualiza el accessToken y el primer nombre del usuario
	 * @param accessToken  de facebook String
	 * @param firstName de facebook String
	 * @return
	 */
	public int updateUser(String accessToken, String firstName){
		String[] infoUser = this.getInfoUser();
		cursor = userAdapter.getCursor();
		int count = cursor.getCount();
		if (count <= 0)
			return 0;
		else{
			ContentValues reg = new ContentValues();
			reg.put(userAdapter.C_COLUMNA_ID, infoUser[0]);
			reg.put(userAdapter.C_COLUMNA_IDFACEBOOK, infoUser[1]);
			reg.put(userAdapter.C_COLUMNA_ACCESSTOKEN, accessToken);
			reg.put(userAdapter.C_COLUMNA_FIRSTNAME, firstName);
			reg.put(userAdapter.C_COLUMNA_WSURI, infoUser[4]);
			return userAdapter.update(reg);
		}		
		
	}
	
}