package com.nagarjuna_pamu.tickle;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class NotificationService extends Service {

	/**
	 * helps to choose NEW APIs on newer devices and older apis on older devices
	 */
	public static final boolean NEW_API = false;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
