package com.bubble.simpleword.menu;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bubble.simpleword.MainActivity;
import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsDB;
import com.bubble.simpleword.db.WordsDbHelper;
import com.bubble.simpleword.service.ServiceNotiWord;
import com.bubble.simpleword.wordbook.WordsClass;

/**
 * <p>Title: MainFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-2
 */
public class MainFragment extends Fragment implements OnClickListener{
	public View mView;
	private WordsDbHelper dbHelper;
	SQLiteDatabase db;
	Context mContext;
	Button btn;
	TextView tv;
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public MainFragment(Context context) {
		mContext = context;
	}
	/**
	 * @author bubble
	 * @date 2015-8-2
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mView=inflater.inflate(R.layout.menu_item_fg_main,container, false);  
		getActivity().setTitle(R.string.app_name);
		
		dbHelper = new WordsDbHelper(mContext, MainActivity.DB_NAME, null, 1);
		
		btn = (Button)mView.findViewById(R.id.create_db);
		btn.setOnClickListener(this);
		
		tv = (TextView)mView.findViewById(R.id.text);
		getActivity().getActionBar().setDisplayShowCustomEnabled(false);
		return mView; 
	}
	@Override
	public void onClick(View v) {
		db = dbHelper.getWritableDatabase();
		
		StringBuilder sb = new StringBuilder();
		Log.i("cursor", Integer.toString(WordsDB.getCursorPosition()));
		Log.i("cursorIndex", Integer.toString(MainActivity.cursorIndex));
		WordsClass wordsClass = WordsDB.getWord();
		Log.i("正序", wordsClass.toString());
		sb.append(wordsClass.toString() + "\n");
		tv.setText(sb.toString());
		
		//状态栏通知
/*		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(getActivity())
		        .setSmallIcon(R.drawable.menu)
		        .setContentTitle("当前word")
		        .setContentText(sb.toString())
		        .setOngoing(true);*/
		// Creates an explicit intent for an Activity in your app
/*		Intent resultIntent = new Intent(getActivity(), MainActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);*/
/*		NotificationManager mNotificationManager =
		    (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(1, mBuilder.build());*/
		Intent intent = new Intent(getActivity() ,ServiceNotiWord.class);
		getActivity().startService(intent);
	}
	/**
	 * @author bubble
	 * @date 2015-8-6
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
