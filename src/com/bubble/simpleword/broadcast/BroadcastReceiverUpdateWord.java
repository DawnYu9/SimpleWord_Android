package com.bubble.simpleword.broadcast;

import com.bubble.simpleword.service.ServicePopNotiWord;
import com.bubble.simpleword.service.ServiceUpdateWord;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <p>Title: BroadcastUpdateWord</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-28 上午11:32:49
 */
public class BroadcastReceiverUpdateWord extends BroadcastReceiver {

	private Intent i;

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public BroadcastReceiverUpdateWord() {
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-28 上午11:32:49
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		i = new Intent(context, ServiceUpdateWord.class);
		context.startService(i);
	}

}
