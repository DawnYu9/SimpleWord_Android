package com.bubble.simpleword.menu;

import android.app.Fragment;
import android.content.Context;
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
		WordsClass wordsClass = WordsDB.getWord();
		Log.i("正序", wordsClass.toString());
		sb.append(wordsClass.toString() + "\n");
		tv.setText(sb.toString());
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
