
package com.filper.notifications;




// La siguiente linea debe ser cambiada para accesar a los recursos de tu proyecto android
import com.filper.DataBase.UserDBManager;
import com.filper.facebook.R;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import de.tavendo.autobahn.Autobahn;
import de.tavendo.autobahn.AutobahnConnection;

public class WebSocketService extends Service {
		UserDBManager manager;
		private String[] C_USER; 
		private Looper mServiceLooper;
		private ServiceHandler mServiceHandler;

		  // Handler that receives messages from the thread
		  private final class ServiceHandler extends Handler {
		      public ServiceHandler(Looper looper) {
		          super(looper);
		      }
		      UserDBManager manager;
		      @Override
		      public void handleMessage(Message msg) {
		          // Normally we would do some work here, like download a file.
		          // For our sample, we just sleep for 5 seconds.
			      // For each start request, send a message to start a job and deliver the
			      // start ID so we know which request we're stopping when we finish the job
				   manager = new UserDBManager(WebSocketService.this);
				   C_USER = null;
				   C_USER = manager.getInfoUser();
				  Log.d(TAG, " " + C_USER);
				  if ( C_USER[1] == null)
					  stopSelf(msg.arg1);
				  else
					  startClient(C_USER[4], C_USER[1]);
		          // Stop the service using the startId, so that we don't stop
		          // the service in the middle of handling another job
		          
		      }
		  }		
		
		
		
	  @Override
	  public void onCreate() {
	    // Start up the thread running the service.  Note that we create a
	    // separate thread because the service normally runs in the process's
	    // main thread, which we don't want to block.  We also make it
	    // background priority so CPU-intensive work will not disrupt our UI.
	    HandlerThread thread = new HandlerThread("ServiceStartArguments",
	            Process.THREAD_PRIORITY_BACKGROUND);
	    Log.d(TAG, "websocket 123");
	    thread.start();
	    

	  }
	
	  
	  @Override
	  public int onStartCommand(Intent intent, int flags, int startId) {

	      // For each start request, send a message to start a job and deliver the
	      // start ID so we know which request we're stopping when we finish the job
		   manager = new UserDBManager(this);
		   C_USER = null;
		   C_USER = manager.getInfoUser();
		  Log.d(TAG, "websocket 2: " + C_USER[1] );
		  if ( C_USER[1] == null)
			  stopSelf();
		  else
			  startClient(C_USER[4], C_USER[1]);
		  

	      Log.d(TAG, "websocket: lost. startId: " + startId + " flags: " + flags + " intent: " + intent  );


	      
	      // If we get killed, after returning from here, restart
	      return Service.START_STICKY;
	      //return START_NOT_STICKY;
	  }
	

	  @Override
	  public IBinder onBind(Intent intent) {
	      // We don't provide binding, so return null
	      return null;
	  }
	
	
	  @Override
	  public void onDestroy() {
          EventInscribe eve = new EventInscribe();
          eve.idFacebook = C_USER[1];
          eve.status = "False";
          InscribeRpc(eve);
		  Log.d(TAG, "websocket: lost.");
	  }	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
// Metodos para el webSocket ------------------------------------------------------	
	
	/**
	 *  Declaramos el Tag con el que se realizara el log del servicio (has extras) not found
	 */
	private static final String TAG = "com.filper.websocketService";
	/**
	 *  Instanciamos la coneccion con la api Autobahn
	 */
	private final AutobahnConnection mConnection = new AutobahnConnection();	
	/**
	 *  Creamos el evento escribe, nos sirve para poder crear json y leerlos
	 * @author zykor
	 *
	 */
	private static class EventInscribe {
	  	   public String idFacebook;
	  	   public String status;
	  	   public String message;
	}
	/**
	 *  Creamos el evento para leer y recibir notificaciones para el usuario conectado
	 * @author zykor
	 *
	 */
	private static class EventNotificacion {
		   public String to;
		   public String from;
		   public String title;
		   public String message;
		   public String activity;
	}
	
  /** 
   * A constructor is required, and must call the super IntentService(String)
   * constructor with a name for the worker thread.
   */

	
 /**
  * Metodo principal del cliente webSocket inicializa todo el servicio
  * @param wuri : Ruta del websocket ejem : "ws://localhost:9000"
  * @param idFacebook : Id de facebook del usuario logeado
  */
  private void startClient(String wuri, final String idFacebook) {
      final String wsuri = wuri;	      
      mConnection.connect(wsuri, new Autobahn.SessionHandler() {
    	  
            @Override
            public void onOpen() {
               Log.d(TAG, "websocket: Status: Connected to " + wsuri);	             
               //inscribeUsuario();
               EventInscribe eve = new EventInscribe();
               eve.idFacebook = idFacebook;
               eve.status = "True";
               InscribeRpc(eve);
               notificacionesUsuario(idFacebook);
            }
            
            @Override
            public void onClose(int code, String reason) {
                EventInscribe eve = new EventInscribe();
                eve.idFacebook = idFacebook;
                eve.status = "False";
                InscribeRpc(eve);
               Log.d(TAG, "websocket: Connection lost.");
            }
         });
   }	
  

  
  /**
   * Este metodo envia los datos del usuario para inscribirlo o mejor dicho indicar si ha iniciado
   * @param evt : El json que va a enviar al websocket para que se identifique que el usuario esta conectado
   * 
   */
  private void InscribeRpc(EventInscribe evt) {
	   	//mConnection.publish("notificacion:inscribe", evt);
  	mConnection.call("http://filper.com/rpc/notifications#changeUserStateConexion",
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
  
  

/**
 * Este metodo lanza un escucha del websocket con el nombre de usuario conectado actualmente
 * @param idFacebook : id Facebook del usuario conectado
 */
  private void notificacionesUsuario(String idFacebook) {
     mConnection.prefix("notificacion", "http://filper.com/pubSub/notifications#");
	   mConnection.subscribe("notificacion:" + idFacebook,
			  EventNotificacion.class,
	                         new Autobahn.EventHandler() {
	                            @Override
	                            public void onEvent(String topic, Object event) {	   
	                            	EventNotificacion evt = (EventNotificacion) event;
	                            	try {
	                            		Log.d(TAG, "websocket:  " + evt.message);	                            		
										notificacion(evt.title, evt.message, evt.activity);
									} catch (ClassNotFoundException e) {
										Log.d(TAG, "websocket: The class don't exist");
										e.printStackTrace();
									}	                            
	                            }
	                            
	                         }
	   );
	}
  

  
  
  
  
  /**
   * Metodo para enviar notificacion al usuario
   * @param title : Titulo de la notificacion
   * @param message : Mensaje de la notificacion
   * @param activity : Actividad que debe abrirse al dar clic en la notificacion
 * @throws ClassNotFoundException : Exception para identificar que la clase no existe
   */
  public void notificacion(String title, String message, String activity) throws ClassNotFoundException{
	  NotificationCompat.Builder mBuilder =
			  new NotificationCompat.Builder(this)
			  .setSmallIcon(R.drawable.ic_filper)
			  .setContentTitle(title)
			  .setContentText(message)
			  .setLights(297402367, 100, 100)
			  .setAutoCancel(true)
			  .setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
	  Intent resultIntent = new Intent(this, Class.forName("com.filper.facebook."+activity));


	  PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);

	  mBuilder.setContentIntent(resultPendingIntent);    	


	  NotificationManager mNotificationManager =
			  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	  mNotificationManager.notify(0, mBuilder.build());  	
  }
  
  
  
}