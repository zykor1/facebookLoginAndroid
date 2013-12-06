package com.filper.webService;


import android.util.Log;
import de.tavendo.autobahn.Autobahn;
import de.tavendo.autobahn.AutobahnConnection;

public class WebService {
	protected static final String TAG = "com.filper.webService";
	private final AutobahnConnection mConnection = new AutobahnConnection();
	
	public static class EventInscribe {
	  	   public String idFacebook;
	  	   public String status;
	  	   public String message;
	}
	
	
	public WebService(){
		startClient("ws://198.199.120.36:9000");
	}
	
	public AutobahnConnection getMConnection(){
		return mConnection;
	}
	
	
	 /**
	  * Metodo principal del cliente webSocket inicializa todo el servicio
	  * @param wuri : Ruta del websocket ejem : "ws://localhost:9000"
	  * @param idFacebook : Id de facebook del usuario logeado
	  */
	  private void startClient(String wuri) {
	      final String wsuri = wuri;	      
	      mConnection.connect(wsuri, new Autobahn.SessionHandler() {
	    	  
	            @Override
	            public void onOpen() {
	               Log.d(TAG, "websocket: Status: Connected to " + wsuri);	             
	            }
	            
	            @Override
	            public void onClose(int code, String reason) {
	               Log.d(TAG, "websocket: Connection lost.");
	            }
	         });
	   }	
	

	
}
