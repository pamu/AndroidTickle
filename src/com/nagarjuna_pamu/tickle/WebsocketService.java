package com.nagarjuna_pamu.tickle;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class WebsocketService extends Service {

	final WebSocketConnection mConnection = new WebSocketConnection();
	
	public static final boolean NEW_API = android.os.Build.VERSION.SDK_INT > 
	  android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;

	private String androidId;
	
	private static final String TAG = "WebsocketService";
	
	public static final String tickle = "ws://10.8.4.220:9000/websocket/";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
		
		connect(tickle+androidId);
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mConnection.disconnect();
		/**
		 * Finalization goes here
		 */
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		Toast.makeText(getApplicationContext(), "android_ID: "+androidId, Toast.LENGTH_LONG).show();
		
		String message = intent.getStringExtra("message");
		send(message);
		Toast.makeText(getApplicationContext(), message +"will be sent", Toast.LENGTH_LONG).show();
		
		return super.START_NOT_STICKY;
	}
	
	@SuppressLint("NewApi") 
	public void lauchNotification(String message, String title, String info) {
		/**
		 * Get reference to notification Manager
		 */
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancelAll();
		/**
		 * Creating a PendingIntent
		 */
		Intent nIntent = new Intent();
		PendingIntent pi = PendingIntent.getService(getApplicationContext(), 0, nIntent, 0);
		
		if(NEW_API) {
			Toast.makeText(getApplicationContext(), "new api", Toast.LENGTH_SHORT).show();
			Notification n = new Notification.Builder(getApplicationContext())
									.setAutoCancel(true)
									.setContentIntent(pi)
									.setSmallIcon(R.drawable.ic_launcher)
									.setContentTitle(title)
									.setContentInfo(info)
									.setContentText(message)
									.setWhen(System.currentTimeMillis())
									.build();
			nm.notify(1, n);
		}else {
			Notification n = new Notification();
			n.icon = R.drawable.ic_launcher;
			n.setLatestEventInfo(getApplicationContext(), title, message, pi);
			n.when = System.currentTimeMillis();
			nm.notify(1, n);
		}
	}
	
	public void connect(final String wsURI) {
		
			
			try {
				mConnection.connect(wsURI, new WebSocketHandler(){
					@Override
			         public void onOpen() {
			            Log.d(TAG, "Status: Connected to " + wsURI);
			            mConnection.sendTextMessage("Hello, world!");
			         }
	
			         @Override
			         public void onTextMessage(String payload) {
			            Log.d(TAG, "Got echo: " + payload);
			            lauchNotification(payload, "Message", "Broadcasted Message");
			         }
	
			         @Override
			         public void onClose(int code, String reason) {
			            Log.d(TAG, "Connection lost.");
			            failed(code, reason);
			         }
				});
			} catch (WebSocketException e) {
				// TODO Auto-generated catch block
				failed(-1, e.getMessage());
				e.printStackTrace();
			}
			
	}
	
	public void send(String msg) {
		if(mConnection.isConnected()) {
			mConnection.sendTextMessage(msg);
		}
	}
	
	public void failed(int code, String reason) {
		
		Toast.makeText(getApplicationContext(), "Websocket connection failed due to "+reason, Toast.LENGTH_SHORT).show();
		connect(tickle+androidId);
	}

}
