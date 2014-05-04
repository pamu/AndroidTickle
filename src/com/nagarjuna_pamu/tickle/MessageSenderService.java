package com.nagarjuna_pamu.tickle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.widget.Toast;

public class MessageSenderService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		/**
		 * Intialization the MessageSender Service
		 */
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		/**
		 * Finalization goes here
		 */
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		String androidId = Secure.getString(this.getContentResolver(),
	            Secure.ANDROID_ID);
		
		Toast.makeText(getApplicationContext(), "android_ID: "+androidId, Toast.LENGTH_LONG).show();
		
		return super.START_NOT_STICKY;
	}
	
	

}
