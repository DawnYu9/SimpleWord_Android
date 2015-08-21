package com.bubble.simpleword.service;

import com.bubble.simpleword.db.WordsDB;
import com.bubble.simpleword.wordbook.WordsClass;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * <p>Title: ServiceUpdateWord</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-21 上午10:14:29
 */
public class ServiceUpdateWord extends Service {
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public ServiceUpdateWord() {
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-21 上午10:48:21
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		UpdateWord();
		return super.onStartCommand(intent, flags, startId);
	}
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-21 上午10:14:30
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * <p>Title: UpdateWord</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-21 上午10:15:52
	 */
	private void UpdateWord() {
		WordsDB.wordClass = WordsDB.getWord();
	}
}
