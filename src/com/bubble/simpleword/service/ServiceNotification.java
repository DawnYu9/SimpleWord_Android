package com.bubble.simpleword.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.bubble.simpleword.MainActivity;
import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsDB;

/**
 * <p>Title: NotificationService</p>
 * <p>Description: the resident notification in Status Bar</p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-20 下午5:40:23
 */
public class ServiceNotification extends Service {
	NotificationCompat.Builder mBuilder;
	NotificationManager mNotificationManager;
	Intent resultIntent;
	TaskStackBuilder stackBuilder;
	PendingIntent resultPendingIntent;
	
	private static AlarmManager am;
    private static PendingIntent pendingIntent;
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public ServiceNotification() {
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
		mBuilder.setTicker(WordsDB.wordClass.toString())	//popup in Status Bar
			.setContentText(WordsDB.wordClass.toString())
			.setWhen(System.currentTimeMillis());
		startForeground(1, mBuilder.build());
		Log.d("通知栏单词", WordsDB.wordClass.toString());
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
		        .setSmallIcon(R.drawable.menu)
		        .setContentTitle("当前word");
		resultIntent = new Intent(this, MainActivity.class);
		stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(resultIntent);
		resultPendingIntent = stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		startForeground(1, mBuilder.build());
	}
	
}
