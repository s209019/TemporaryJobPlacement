package it.polito.mobile.temporaryjobplacement.commons.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;

import it.polito.mobile.temporaryjobplacement.R;
import it.polito.mobile.temporaryjobplacement.commons.viewmanaging.DialogManager;


public class Connectivity {


public static boolean hasNetworkConnection(Context ctx) {
	    boolean haveConnectedWifi = false;
	    boolean haveConnectedMobile = false;

	    ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo[] netInfo = cm.getAllNetworkInfo();
	    for (NetworkInfo ni : netInfo) {
	        if (ni.getTypeName().equalsIgnoreCase("WIFI"))
	            if (ni.isConnected())
	                haveConnectedWifi = true;
	        
	        if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
	            if (ni.isConnected())
	                haveConnectedMobile = true;
	      }
	    
	    return haveConnectedWifi || haveConnectedMobile;
	}






	public synchronized static void connectionError(final Activity activity){
		if(activity!=null)
		DialogManager.setDialog("CONNECTION ERROR", activity.getResources().getString(R.string.no_connectivity_messagge), (ActionBarActivity) activity, "RETRY",
				new Runnable() {
					@Override
					public void run() {
						activity.recreate();
					}
				},false);

	}

	
	
	
	
	
	
	
	
	
	
	
}
