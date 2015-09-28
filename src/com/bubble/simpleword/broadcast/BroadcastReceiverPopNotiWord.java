package com.bubble.simpleword.broadcast;

import com.bubble.simpleword.service.ServicePopNotiWord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <p>Title: BroadcastPopNotiWord</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-27 上午11:06:23
 */
public class BroadcastReceiverPopNotiWord extends BroadcastReceiver {

	private Intent i;

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public BroadcastReceiverPopNotiWord() {
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-27 上午11:06:23
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		i = new Intent(context, ServicePopNotiWord.class);
		context.startService(i);
	}

}
