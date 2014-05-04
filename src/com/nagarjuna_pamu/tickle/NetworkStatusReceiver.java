package com.nagarjuna_pamu.tickle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkStatusReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-genertextated method stub
		Toast.makeText(context, "connection status changed", Toast.LENGTH_SHORT).show();
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();
		
		if(info != null) {
			
			if(info.isConnected()) {
				/**
				 * start the service
				 */
				
				Toast.makeText(context, "network connected", Toast.LENGTH_LONG).show();
			}else{
				/**
				 * stop the service
				 */
			}
			
		}
	}

}
