package com.bubble.simpleword.fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bubble.simpleword.MainActivity;
import com.bubble.simpleword.R;
import com.bubble.simpleword.db.MyDbHelper;
import com.bubble.simpleword.db.WordsManager;

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
	private MyDbHelper dbHelper;
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
		mView=inflater.inflate(R.layout.fg_layout_main,container, false);  
		getActivity().setTitle(R.string.app_name);
		
		dbHelper = new MyDbHelper(mContext, MainActivity.DB_NAME, null, 1);
		
		Button btn1 = (Button)mView.findViewById(R.id.btn1);
		btn1.setOnClickListener(this);
		Button btn2 = (Button)mView.findViewById(R.id.btn2);
		btn2.setOnClickListener(this);
		Button btn3 = (Button)mView.findViewById(R.id.btn3);
		btn3.setOnClickListener(this);
		Button btn4 = (Button)mView.findViewById(R.id.btn4);
		btn4.setOnClickListener(this);
		Button btn5 = (Button)mView.findViewById(R.id.btn5);
		btn5.setOnClickListener(this);
		Button btn6 = (Button)mView.findViewById(R.id.btn6);
		btn6.setOnClickListener(this);
		Button btn7 = (Button)mView.findViewById(R.id.btn7);
		btn7.setOnClickListener(this);
		
		tv = (TextView)mView.findViewById(R.id.text);
		getActivity().getActionBar().setDisplayShowCustomEnabled(false);
		return mView; 
	}
	@Override
	public void onClick(View v) {
		db = dbHelper.getWritableDatabase();
		String tableName = "表1";
		switch (v.getId()) {
		case R.id.btn1:
			WordsManager.createTable(tableName);
			break;
		case R.id.btn2:
			WordsManager.addWord(tableName, "add", "[æd]", "add");
			WordsManager.addWord(tableName, "delete", "[diˈlit]", "delete");
			break;
		case R.id.btn3:
			WordsManager.deleteWord(tableName, "delete");
			break;
		case R.id.btn4:
			WordsManager.queryWord(tableName, "word", "add");
			break;
		case R.id.btn5:
			WordsManager.editWord(tableName, "add", "phonetic", "音标");
			break;
		case R.id.btn6:
			WordsManager.deleteTable(tableName);
			break;
		case R.id.btn7:
			WordsManager.alterTableName(tableName, "表2");
			break;
		default:
			break;
		}
//		Cursor cursor = WordsManager.query(tableName);  
//		StringBuilder sb = new StringBuilder();
//		if( cursor != null ){
//			if( cursor.moveToFirst() ){
//				do{
//					String word = cursor.getString(cursor.getColumnIndex("word"));  
//		            String phonetic = cursor.getString(cursor.getColumnIndex("phonetic"));  
//		            String definition = cursor.getString(cursor.getColumnIndex("definition")); 
//		            sb.append(word + phonetic + definition + "\n");
//				}while( cursor.moveToNext());
//			}
//		}
//		tv.setText(sb.toString());
		
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
