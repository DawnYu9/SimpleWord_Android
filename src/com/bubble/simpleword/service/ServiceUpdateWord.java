package com.bubble.simpleword.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.IBinder;
import android.util.Log;

import com.bubble.simpleword.activity.MainActivity;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.util.Util;

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
	private int cursorIndex;
	private Editor editor;

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
		Log.d("更新单词", WordsManager.wordCls.getWholeString());
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
		WordsManager.updateWordCls();
		cursorIndex = WordsManager.getCursorPosition();
		editor = Util.getSharedPreferencesEditor(getApplicationContext());  
		editor.putInt(MainActivity.CURSOR_INDEX, cursorIndex);
		editor.commit();
	}
}
