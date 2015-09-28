package com.bubble.simpleword.broadcast;

import com.bubble.simpleword.service.ServiceFloatWord;
import com.bubble.simpleword.service.ServicePopNotiWord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <p>Title: BroadcastReceiverFloatWord</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-30 上午11:34:37
 */
public class BroadcastReceiverFloatWord extends BroadcastReceiver {

	private Intent i;

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public BroadcastReceiverFloatWord() {
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-30 上午11:34:37
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		i = new Intent(context, ServiceFloatWord.class);
		context.startService(i);
	}

}
