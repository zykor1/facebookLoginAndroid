package com.filper.notifications;


import com.filper.DataBase.UserDBManager;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;




public class ManageService {
	
	private UserDBManager userManager;
	
	/**
	 * Constructor inicia el servicio
	 * @param userName nombre del usuario
	 * @param wsuri ruta del websocket server
	 * @param context el contexto de la actividad que invoca al servicio
	 */
	public ManageService(String idFacebook, String accessToken, String firstName, String wsuri, Context context){
		userManager = new UserDBManager(context);
		userManager.setUser(idFacebook, accessToken, firstName, wsuri);
		userManager.close();
		startService(context);
	}
/**
 *  Constructor vacio
 */
	public ManageService(){};
	
	
/**
 * Detiene al servicio websocket
 * @param context el contexto de la actividad que detiene al servicio
 * @return Retorna false si no se pudo detener el servicio y true si es que lo detuvo
 */
	public boolean stopService(Context context){
		userManager = new UserDBManager(context);
		if (userManager.deleteUser() == 1){
			context.stopService(new Intent(context, WebSocketService.class));
			userManager.close();
			return true;
		}		
		return false;
	}
	
	/**
	 * Arranca el servicio
	 * @param context el contexto de la actividad que invoca al servicio
	 */
    public void startService(Context context){
        if (!isMyServiceRunning(context)){
            Intent serviceIntent = new Intent(context, WebSocketService.class);
            context.startService(serviceIntent);
        }
    }
/**
 * Inicia el servicio si es que no se ha iniciado ya mediante el constructor
 * @param userName el nombre de usuario 
 * @param wsuri La direccion de websocket server
 * @param context el contexto de la actividad que invoca al servicio
 */
    public void startService(String idFacebook, String accessToken, String firstName, String wsuri, Context context){
        if (!isMyServiceRunning(context)){
    		userManager = new UserDBManager(context);
    		userManager.setUser(idFacebook, accessToken, firstName, wsuri);
    		userManager.close();

            Intent serviceIntent = new Intent(context, WebSocketService.class);
            context.startService(serviceIntent);
        }
    }

/**
 * Verifica si el servicio esta corriendo    
 * @param context el contexto de la actividad que invoco al servicio
 * @return true Si el servicio esta corriendo returna true, si no retorna false
 */
    private boolean isMyServiceRunning(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (WebSocketService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }	
	
	
	
	
	

}
