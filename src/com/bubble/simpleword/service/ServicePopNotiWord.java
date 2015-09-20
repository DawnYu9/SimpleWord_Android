package com.bubble.simpleword.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bubble.simpleword.R;
import com.bubble.simpleword.activity.MainActivity;
import com.bubble.simpleword.db.WordsManager;

/**
 * <p>Title: NotificationService</p>
 * <p>Description: the resident notification in Status Bar</p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-20 下午5:40:23
 */
public class ServicePopNotiWord extends Service {
	NotificationCompat.Builder mBuilder;
	NotificationManager mNotificationManager;
	Notification notification;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;
	PendingIntent resultPendingIntent;
	int notifyID = 1;
	private static AlarmManager am;
    private static PendingIntent pendingIntent;
    
    Intent intentServiseUpdateWord;
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public ServicePopNotiWord() {
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 下午5:40:23
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 下午5:48:28
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		
		initNotification();
	}
	
	/**
	 * <p>Description: update the displaying word's data</p>
	 * @author bubble
	 * @date 2015-8-20 下午7:22:49
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mBuilder
			.setContentText(WordsManager.wordCls.getSpannedHtml())
			.setWhen(System.currentTimeMillis())
			.setTicker(WordsManager.wordCls.getSpannedHtml());	//pop up in Status Bar
		mNotificationManager.notify(notifyID, notification);	//update data
		startForeground(notifyID, mBuilder.build());	//display in "ongoing"
		Log.d("通知栏单词", WordsManager.wordCls.toString());
		return super.onStartCommand(intent, flags, startId);
	}
	
	/**
	 * <p>Title: initNoti</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 下午8:11:22
	 */
	private void initNotification() {
		mBuilder = new NotificationCompat.Builder(this)
		        .setSmallIcon(R.mipmap.ic_launcher)
		        .setContentTitle("当前word");
		notification = mBuilder.build();
		
		resultIntent = new Intent(this, MainActivity.class);
		stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
}
