package com.examkabila.currentaffairs.app;


import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Html;
import android.util.Log;


import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMIntentService extends IntentService {

	public static final int NOTIFICATION_ID = 1;
	NotificationCompat.Builder builder;

	public GCMIntentService() {
		super("GCMIntentService");
	}

	public static final String TAG = "GCMIntentService";
	public static String pushMessageHeader;
	public static String pushMessageText;
	public static int pushMessageOffersId;
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		Log.i("SUSHIL","Notification Comes ");
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) {
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
				Log.i("GCM", "MESSAGE_TYPE_SEND_ERROR");
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
				Log.i("GCM", "MESSAGE_TYPE_DELETED");
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
					.equals(messageType)) {
				Log.i("GCM", "MESSAGE_TYPE_MESSAGE");
				/*
				 * for (int i = 0; i < 3; i++) { Log.i(TAG, "Working... " + (i +
				 * 1) + "/5 @ " + SystemClock.elapsedRealtime()); try {
				 * Thread.sleep(5000); } catch (InterruptedException e) { }
				 * 
				 * } Log.i(TAG, "Completed work @ " +
				 * SystemClock.elapsedRealtime());
				 */
				Object_AppConfig objConfig = new Object_AppConfig(this);
				Log.i("SUSHIL","Notification enabled "+objConfig.isNotificationEnabled());
				if(objConfig.isNotificationEnabled())
					sendNotification(extras);
				else
					return;
			}
		}else{
			Log.i("GCM", "Extras Empty");
		}
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	
	@SuppressLint("NewApi")
	private void sendNotification(Bundle extras) {
		if (extras != null) {
			pushMessageHeader = extras.getString("heading", "");
			pushMessageText = extras.getString("message", "");
			//pushMessageOffersId = extras.getInt("offersId", 0);
			
			Log.i("SUSHIL", "Heading : "+pushMessageHeader + " Content  :"+pushMessageText);
			
			//NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			
			if(pushMessageHeader.isEmpty()){
				return;
			}
			
			Object_AppConfig obj = new Object_AppConfig(this);
			if(!obj.isNotificationEnabled()){
				return;
			}
			try{
				//Activity_Home.comingFromPushMessage = true;
				Intent intent = new Intent(this, Activity_Home.class);
				intent.putExtra("notification","notification");
				Activity_Home.isPushNoti = true;
				if (android.os.Build.VERSION.SDK_INT < 11) {
					NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
							this).setSmallIcon(R.mipmap.ic_launcher)
							.setContentTitle(this.getResources().getString(R.string.app_name)).setContentText(Html.fromHtml(pushMessageHeader));
					
					TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
					stackBuilder.addParentStack(Activity_Home.class);
					stackBuilder.addNextIntent(intent);
					PendingIntent resultPendingIntent = stackBuilder
							.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
					mBuilder.setContentIntent(resultPendingIntent);
					NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
					// mId allows you to update the notification later on.
					mNotificationManager.notify(999, mBuilder.build());

					return;
				}

				// Starts the activity on notification click
				PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);

				// Create the notification with a notification builder
				Notification notification;

				if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
					notification = new Notification.Builder(this)
					.setSmallIcon(R.mipmap.ic_launcher)
							.setWhen(System.currentTimeMillis())
							.setContentTitle(this.getResources().getString(R.string.app_name)).setContentText(Html.fromHtml(pushMessageHeader))
							.setContentIntent(pIntent)
									// .setContentInfo(
									// String.valueOf(++MainActivity.numOfNotifications) )
							.setLights(0xFFFF0000, 500, 500).getNotification();
				} else {
					notification = new Notification.Builder(this)
					.setSmallIcon(R.mipmap.ic_launcher)
							.setWhen(System.currentTimeMillis())
							.setContentTitle(this.getResources().getString(R.string.app_name))
							.setContentText(Html.fromHtml(pushMessageHeader)).setContentIntent(pIntent)
									// .setContentInfo(
									// String.valueOf(++MainActivity.numOfNotifications) )
							.setLights(0xFFFF0000, 500, 500).build();
				}

				// Remove the notification on click
				notification.flags |= Notification.FLAG_AUTO_CANCEL;

				NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				// manager.notify(R.string.app_name, notification);

				{
					// Wake Android Device when notification received
					PowerManager pm = (PowerManager) this
							.getSystemService(Context.POWER_SERVICE);
					final PowerManager.WakeLock mWakelock = pm.newWakeLock(
							PowerManager.FULL_WAKE_LOCK
							| PowerManager.ACQUIRE_CAUSES_WAKEUP,
							"GCM_PUSH");
					mWakelock.acquire();

					// Play default notification sound
					// notification.defaults |= Notification.DEFAULT_SOUND;

					Uri notificationuri = RingtoneManager
							.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
					Ringtone ringer = RingtoneManager.getRingtone(
							getApplicationContext(), notificationuri);
					ringer.play();

					// Vibrate if vibrate is enabled
					notification.defaults |= Notification.DEFAULT_VIBRATE;
					manager.notify(0, notification);

					// Timer before putting Android Device to sleep mode.
					Timer timer = new Timer();
					TimerTask task = new TimerTask() {
						public void run() {
							mWakelock.release();
						}
					};
					timer.schedule(task, 5000);
				}
			}catch(Exception ex)
			{
				
			}
			

			
		}
	}

}


/*@SuppressLint("NewApi")
	private void sendNotify(Bundle extras){
		
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(extras.getString("heading", "news"))
				.setStyle(
						new NotificationCompat.BigTextStyle().bigText(extras.getString("content", "news")))
				.setDefaults(Notification.DEFAULT_ALL);
		
		Intent resultIntent = new Intent(this, Activity_Home.class);
		resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent resultpintent = PendingIntent.getActivity(this, 0,
				resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(resultpintent);
		notificationManager.notify(NOTIFICATION_ID, builder.build());
	}
	*/
