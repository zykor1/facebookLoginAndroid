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
	
	/**
	 * Inicia el webService haciendo uso de websockets, lo mas recomendable es que solo se instancie una solo vez
	 * esta clase en toda la aplicacion. Si el usuario cierra la aplicacion esta clase se borrara de memoria, asi que asegurate
	 * de que la clase se vuelva a instanciar cada vez que el usuario abra la aplicacion
	 */
	public WebService(){
		startClient("ws://198.199.120.36:9000");
	}
	
	/**
	 * Obtiene la conexion al servidor lista para solo hacer la llamada por ejemplo:
	 *   private void InscribeRpc(EventInscribe evt) {
  		 getMConnection().mConnection.call("http://filper.com/rpc/notifications#changeUserStateConexion",
  			EventInscribe.class,
  	                    new Autobahn.CallHandler() {
  	 
  	                        @Override
  	                        public void onResult(Object result) {
  	                        	EventInscribe res = (EventInscribe) result;
  	                        	Log.d(TAG, "websocket: respuesta = " + res.message);  	                       
  	                        }
  	 
  	                        @Override
  	                        public void onError(String errorUri, String errorDesc) {
  	                        	Log.d(TAG, "websocket:  Error-1 = " + errorUri);
  	                        	Log.d(TAG, "websocket: Error-2 = " + errorDesc);
  	                        }
  	                    },
  	                    evt 
  	   );
  	}	
	 * @return
	 */
	public AutobahnConnection getMConnection(){
		return mConnection;
	}
	
	
	 /**
	  * Metodo principal del cliente webSocket inicializa la comunicacion
	  * @param wuri : Ruta del websocket ejem : "ws://localhost:9000"
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
