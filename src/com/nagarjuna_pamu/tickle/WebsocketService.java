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
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

public class WebsocketService extends Service {

	final WebSocketConnection mConnection = new WebSocketConnection();
	
	public static final boolean NEW_API = android.os.Build.VERSION.SDK_INT > 
	  android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
	
	private static final String TAG = "WebsocketService";
	
	public static final String tickle = "ws://10.8.2.44:9000/websocket";
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
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
		
		
		String message = intent.getStringExtra("message");
		String name = intent.getStringExtra("name");
		
		send(name, message);
		
		Toast.makeText(getApplicationContext(), message +" will be sent", Toast.LENGTH_LONG).show();
		
		return super.START_NOT_STICKY;
	}
	
	@SuppressLint("NewApi") 
	public void lauchNotification(String message, String title, String info) {
		/**
		 * Get reference to notification Manager
		 */
		NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		/**
		 * Creating a PendingIntent
		 */
		Intent nIntent = new Intent(getApplicationContext(), MessageDisplayActivity.class);
		
		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("message", message);
		
		nIntent.putExtras(args);
		
		
		PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, nIntent, PendingIntent.FLAG_ONE_SHOT);
		
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
									.setSound(Uri.parse("android.resource://com.nagarjuna_pamu.tickle/"+R.raw.notification))
									.build();
			nm.notify(1, n);
		}else {
			Notification n = new Notification();
			n.icon = R.drawable.ic_launcher;
			n.setLatestEventInfo(getApplicationContext(), title, message, pi);
			n.sound = Uri.parse("android.resource://com.nagarjuna_pamu.tickle/"+R.raw.notification);
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
	
	public void send(String name, final String msg) {
		if(mConnection.isConnected()) {
			mConnection.sendTextMessage(msg);
		}else{
			String androidId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
			connect(tickle + "/" + androidId + "/" + name);
			//mConnection.sendTextMessage(msg);
		}
	}
	
	public void failed(int code, String reason) {
		
		Toast.makeText(getApplicationContext(), code+" returned .... Websocket connection failed due to "+reason, Toast.LENGTH_SHORT).show();
		
		//connect(tickle+androidId);
	}

}
