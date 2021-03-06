package com.filper.location;

import java.util.Locale;

import com.filper.facebook.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ManageLocation implements LocationListener{
	protected LocationManager locationManager;
	protected LocationListener locationListener;
	protected  Context context;
	String lat;
	String provider;
	private String latitude,longitude;
	public String _latitude, _longitude;
	protected boolean gps_enabled,network_enabled;

	/**
	 * Inicializa la navegacion de la aplicacion. Si el gps esta apagado mandamos un alert dialog, recibe el contexto o la actividad
	 * @param contexto 
	 */
	public ManageLocation(final Context contexto){
		this.context = contexto;		
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setMessage(R.string.messageAlertDialogGPS);
			alertDialog.setCancelable(false);
			alertDialog.setPositiveButton("Activar GPS", 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
                            Intent callGPSSettingIntent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            contexto.startActivity(callGPSSettingIntent);														
						}
					});
			alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();					
				}
			});
			AlertDialog alert = alertDialog.create();
			alert.show();
		}else{
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
		}
	}
	
	/**
	 * Abre google maps marcando un punto en el dispositivo pasando las coordenadas y el nombre del local
	 * @param latitude 
	 * 			Recibe la latitud en float
	 * @param longitude
	 * 			Recibe la longitud en float
	 * @param labelName
	 * 			Recibe el nombre de la etiqueta a mostrar en el mapa String
	 */
	public void openGoogleMaps(double latitude, double longitude, String labelName){
		String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=17&q=%f,%f(%s)",
				latitude, longitude,
				latitude, longitude,
				labelName);
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
		context.startActivity(intent);	
	}
	
	
	/**
	 * Obtiene un arreglo de Strings que contiene 
	 * String[0] - latitude, String[1] - Longitude
	 * @return
	 */
	public String[] getCoordenates(){	
		if ((_latitude != null) && (_longitude != null)){
			String[] aux = new String[2];
			Log.d("com.filper.location", "_latitude: " + _latitude);
			Log.d("com.filper.location", "_longitude: " + _longitude);
			aux[0] = _latitude;
			aux[1] = _longitude;
			return aux;
		}		
		return null;
	}
	
	
	@Override
	public void onLocationChanged(Location location) {	
		//Log.d("com.filper.location", "location: " + location);		
		_latitude = String.valueOf(location.getLatitude());
		_longitude = String.valueOf(location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String arg0) {
		Log.d("Latitude","disable: " + arg0);
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		Log.d("Latitude","Enabled: " + arg0);
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		Log.d("Latitude","Status: " + arg1);		
	}

}
