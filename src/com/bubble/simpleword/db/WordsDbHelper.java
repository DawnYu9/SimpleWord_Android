package com.bubble.simpleword.db;


import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <p>Title: WordsDbHelper</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-6
 */
public class WordsDbHelper extends SQLiteOpenHelper {
	public static final String CREATE_WORDS = "create table if not exists Words (" +
			"word text, " +
			"phonetic text, " +
			"definition text)";
	
	private Context mContext;

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public WordsDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mContext = context;
	}
	
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	public WordsDbHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6 
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_WORDS);
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 
	}

}
