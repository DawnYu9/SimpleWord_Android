package com.bubble.simpleword.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bubble.simpleword.MainActivity;


/**
 * <p>Title: WordsManager</p>
 * <p>Description: 管理单词库</p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-6 下午11:35:11
 */
public class WordsManager {
	public final static String DB_NAME = "simpleword.db";
	public final static String TABLE_NAME = "GraduateWords";
	public final static String COLUMN_WORD = "word";
	public final static String COLUMN_PHONETIC = "phonetic";
	public final static String COLUMN_DEFINITION = "definition";
	public static String wordWord;  
	public static String wordPhonetic;  
	public static String wordDefinition;  
	
	private static Context mContext;
	private static SQLiteDatabase db;
    private static MyDbHelper wordsDbHelper;
    private static ContentValues cValue;
    private static final String WHERE_CLAUSE_BY_WORD = COLUMN_WORD + " = ?";  
    
    public static int counts;
    public static Cursor cursor;
    
    public static WordCls wordCls; //the current word
    
    
    //"db.query"'s parameter: "orderby"
    private final static String ORDERBY_RANDOM = "RANDOM()"; //整体随机重新排序，未改变原数据库数据顺序
    private final static String ORDERBY_IN_ORDER = "word"; 
    private final static String ORDERBY_REVERSE_ORDER = "word desc"; 
    
    public static int MODE_GET_WORD ;
    private final static int MODE_GET_WORD_RANDOM = 0 ;
    private final static int MODE_GET_WORD_IN_ORDER = 1;
    private final static int MODE_GET_WORD_REVERSE_ORDER = 2;
    
    public static boolean isInOrder = true;
    public static boolean isReverseOrder = false;
    public static boolean isRandom = false;
    public static final String IS_INORDER = "isInOrder";
    public static final String IS_REVERSEORDER = "isReverseOrder";
    public static final String IS_RANDOM = "isRandom";
    
    /**
     * <p>Title: initWordsDB</p>
     * <p>Description: init WordsManager before operate the table</p>
     * @param context
     * @author bubble
     * @date 2015-8-7
     */
    public static void initWordsManager(Context context){
    	mContext = context;
		wordsDbHelper = new MyDbHelper(mContext, MainActivity.DB_NAME, null, 1);
		db = wordsDbHelper.getReadableDatabase();
		cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		cursor.moveToFirst();
		counts = cursor.getCount();
//		Log.i("Words表的行数counts", Integer.toString(counts));
    }
    /**
     * <p>Title: initWordsDB</p>
     * <p>Description: init WordsManager before operate the table</p>
     * @param context
     * @param position
     * @author bubble
     * @date 2015-8-20 上午12:32:56
     */
    public static void initWordsManager(Context context, int position){
    	mContext = context;
    	wordsDbHelper = new MyDbHelper(mContext, MainActivity.DB_NAME, null, 1);
    	db = wordsDbHelper.getReadableDatabase();
    	cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
    	if ( isInOrder ) {
    		isInOrder = false;
    		setWordInOrder();
    	} else if ( isReverseOrder ) {
    		isReverseOrder = false;
    		setWordReverseOrder();
    	} else if ( isRandom ) {
    		isRandom = false;
    		setWordRandom();
    	}
    	cursor.moveToPosition(position);
    }

    /**
     * <p>Title: getTableList</p>
     * <p>Description: </p>
     * @return
     * @author bubble
     * @date 2015-9-8 下午9:09:02
     */
    public static List<String> getTableList() {
    	db = wordsDbHelper.getReadableDatabase();
    	List<String> tableList = new ArrayList<String>();
    	Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' order by name", null);
    	while(cursor.moveToNext()){
    		tableList.add(cursor.getString(0));
    	}
    	db.close();
    	return tableList;
    }
	
	/**
	 * <p>Title: createTable</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-7 下午5:23:48
	 */
	public static void createTable(String tableName) {
		db = wordsDbHelper.getReadableDatabase();
		String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
				"word TEXT NOT NULL," +
				"phonetic TEXT," +
				"definition TEXT," +
				"time TEXT DEFAULT (datetime('now','localtime'))," +
				"isRemembered INTEGER DEFAULT 0," +
				"PRIMARY KEY (\"word\" ASC)" +
						");";    
        db.execSQL(sql);
        db.close();
	}
	
	/**
	 * <p>Title: deleteTable</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-8 下午9:37:58
	 */
	public static void deleteTable(String tableName) {
		db = wordsDbHelper.getReadableDatabase();
		db.execSQL("DROP TABLE IF EXISTS " + tableName );
		db.close();
	}
	
	/**
	 * <p>Title: alterTableName</p>
	 * <p>Description: </p>
	 * @param oldName
	 * @param newName
	 * @author bubble
	 * @date 2015-9-8 下午9:41:06
	 */
	public static void alterTableName(String oldName, String newName) {
		if ( ! getTableList().contains(newName) ) {
			db = wordsDbHelper.getReadableDatabase();
			db.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName + ";");
			db.close();
		}
	}
    
    
	/**
	 * <p>Title: addWord</p>
	 * <p>Description: </p>
	 * @param word
	 * @param phonetic
	 * @param definition
	 * @author bubble
	 * @date 2015-9-7 下午1:50:23
	 */
	public static void addWord(String tableName, String word, String phonetic, String definition) {
	    cValue = new ContentValues();  
	    cValue.put(COLUMN_WORD, word);  
	    cValue.put(COLUMN_PHONETIC, phonetic);  
	    cValue.put(COLUMN_DEFINITION, definition);  
	   
	    db = wordsDbHelper.getWritableDatabase();
	    db.insert(tableName, null, cValue); 
	    db.close();
	}
	
	/**
	 * <p>Title: addWord</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午8:10:02
	 */
	public static void addWord(String tableName, WordCls wordCls) {
		cValue = new ContentValues();  
	    cValue.put(COLUMN_WORD, wordCls.getWord());  
	    cValue.put(COLUMN_PHONETIC, wordCls.getPhonetic());  
	    cValue.put(COLUMN_DEFINITION, wordCls.getDefinition());  
	   
	    db = wordsDbHelper.getWritableDatabase();
	    db.insert(tableName, null, cValue); 
	    db.close();
	}
	
	/**
	 * <p>Title: deleteWord</p>
	 * <p>Description: </p>
	 * @param word
	 * @author bubble
	 * @date 2015-9-7 下午1:58:57
	 */
	public static void deleteWord(String tableName, String word) {
		String[] whereArgs = { word };  
		
		db = wordsDbHelper.getWritableDatabase();
		db.delete(tableName, WHERE_CLAUSE_BY_WORD, whereArgs);   
		db.close();
	}
	
	/**
	 * <p>Title: editWord</p>
	 * <p>Description: </p>
	 * @param word
	 * @param editKey
	 * @param editValue
	 * @author bubble
	 * @date 2015-9-7 下午2:15:14
	 */
	public static void editWord(String tableName, String word, String editKey, String editValue) {
	    cValue = new ContentValues();  
	    switch (editKey) {
	    case COLUMN_WORD:
	    	cValue.put(COLUMN_WORD, editValue);
	    	break;
	    case COLUMN_PHONETIC:
	    	cValue.put(COLUMN_PHONETIC, editValue);
	    	break;
	    case COLUMN_DEFINITION:
	    	cValue.put(COLUMN_DEFINITION, editValue);
	    	break;
	    }
	    
	    String[] whereArgs={ word };  
	    
	    db = wordsDbHelper.getWritableDatabase();
	    db.update(tableName, cValue, WHERE_CLAUSE_BY_WORD, whereArgs);
	    db.close();
	}
	
	/**
	 * <p>Title: editWord</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-10 上午12:09:51
	 */
	public static void editWord(String tableName, WordCls wordCls) {
		cValue = new ContentValues();  
		cValue.put(COLUMN_WORD, wordCls.getWord());
		cValue.put(COLUMN_PHONETIC, wordCls.getPhonetic());
		cValue.put(COLUMN_DEFINITION, wordCls.getDefinition());
		
		String[] whereArgs={ wordCls.getWord() };  
		
		db = wordsDbHelper.getWritableDatabase();
		db.update(tableName, cValue, WHERE_CLAUSE_BY_WORD, whereArgs);
		db.close();
	}
	
	/**
	 * <p>Title: queryWord</p>
	 * <p>Description: </p>
	 * @param column
	 * @param value
	 * @return
	 * @author bubble
	 * @date 2015-9-7 下午3:32:40
	 */
	public static ArrayList<WordCls> queryWord(String tableName, String column, String value) {
		db = wordsDbHelper.getReadableDatabase();
		String[] columns = { COLUMN_WORD, COLUMN_PHONETIC, COLUMN_DEFINITION };
		String selection = WHERE_CLAUSE_BY_WORD; 
		String[] selectionArgs = { value };
		switch (column) {
		case COLUMN_WORD:
			break;
		case COLUMN_DEFINITION:
			selection = COLUMN_DEFINITION + " = ?";
			break;
		default:
			break;
		}
		Cursor cursor = db.query(tableName, columns, selection, selectionArgs, null, null, COLUMN_WORD);  
		
		ArrayList<WordCls> wordClsList = new ArrayList<WordCls>();
        while(cursor.moveToNext()){  
        	WordCls wordCls = new WordCls();
            wordCls.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));  
            wordCls.setPhonetic(cursor.getString(cursor.getColumnIndex(COLUMN_PHONETIC)));  
            wordCls.setDefinition(cursor.getString(cursor.getColumnIndex(COLUMN_DEFINITION)));  
            wordClsList.add(wordCls);
        }  
        db.close(); 
        return wordClsList;
	}

	
	public static Cursor query(String tableName) {
		db = wordsDbHelper.getReadableDatabase();
		return db.query(tableName,null,null,null,null,null,null);
	}
	
	/**
	 * <p>Title: getAllWords</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-7
	 */
	public static ArrayList<WordCls> getWordsDataset(String tableName) {
		db = wordsDbHelper.getReadableDatabase();
		Cursor cur = db.query(tableName, null, null, null, null, null, null);
		cur.moveToFirst();
		ArrayList<WordCls> wordsDataset = new ArrayList<WordCls>();
		if (cur != null && cur.moveToFirst()) {  
		    do {  
		        setWordCls(cur);
		        wordsDataset.add(wordCls);  
		    } while (cur.moveToNext()); 
		    cur.close();
		    db.close();
		    return wordsDataset;
		} else {
			cur.close();
			db.close();
			return null;
		}
    }
	
	/**
	 * <p>Title: getWord</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-18 上午11:17:59
	 */
	public static WordCls updateWordCls(){
		if ( ! isCursorValid(cursor) ) {
			if ( wordCls != null ) {
				cursor.moveToNext();
				return wordCls;
			} else {
				cursor.moveToLast();
				return updateWordCls();
			}
		} else {
			setWordCls(cursor);
			cursor.moveToNext();
			return wordCls;
		}
	}
	/**
	 * <p>Title: getWord</p>
	 * <p>Description: </p>
	 * @param position
	 * @return
	 * @author bubble
	 * @date 2015-8-20 上午11:04:55
	 */
	public static WordCls getWord(int position){
		cursor.moveToPosition(position);
		if ( ! isCursorValid(cursor) ) {
			if ( wordCls != null ) {
				cursor.moveToNext();
				return wordCls;
			} else {
				cursor.moveToLast();
				return updateWordCls();
			}
		} else {
			setWordCls(cursor);
			cursor.moveToNext();
			return wordCls;
		}
	}
	
	/**
	 * <p>Title: setWordRandom</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordRandom(){
		if ( ! isRandom ) {
			MODE_GET_WORD = MODE_GET_WORD_RANDOM;
			cursor = setWordOrderBy(cursor, ORDERBY_RANDOM);
			cursor.moveToFirst();
			setWordModeFlag(MODE_GET_WORD);
		}
	}
	
	/**
	 * <p>Title: setWordInOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordInOrder(){
		if ( ! isInOrder ) {
			MODE_GET_WORD = MODE_GET_WORD_IN_ORDER;
			cursor = setWordOrderBy(cursor, ORDERBY_IN_ORDER);
			cursor.moveToFirst();
			setWordModeFlag(MODE_GET_WORD);
		}
	}
	
	/**
	 * <p>Title: setWordReverseOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordReverseOrder(){
		if ( ! isReverseOrder ) {
			MODE_GET_WORD = MODE_GET_WORD_REVERSE_ORDER;
			cursor = setWordOrderBy(cursor,ORDERBY_REVERSE_ORDER);
			cursor.moveToFirst();
			setWordModeFlag(MODE_GET_WORD);
		}
	}
	
	/**
	 * <p>Title: setWordModeFlag</p>
	 * <p>Description: change the state of get_word_mode's flags </p>
	 * @author bubble
	 * @date 2015-8-19 下午9:52:19
	 */
	public static void setWordModeFlag(int i) {
		switch (i) {
		case MODE_GET_WORD_RANDOM:
			isRandom = true;
			isInOrder = false;
			isReverseOrder = false;
			break;
		case MODE_GET_WORD_IN_ORDER:
			isInOrder = true;
			isRandom = false;
			isReverseOrder = false;
			break;
		case MODE_GET_WORD_REVERSE_ORDER:
			isReverseOrder = true;
			isInOrder = false;
			isRandom = false;
			break;
		default:
			break;
		}
	}
	
	/**
	 * <p>Title: setWordOrderBy</p>
	 * <p>Description: </p>
	 * @param cur
	 * @param orderby
	 * @return
	 * @author bubble
	 * @date 2015-8-9
	 */
	private static Cursor setWordOrderBy(Cursor cur,String orderby){
		cur = db.query(TABLE_NAME, null, null, null, null, null, orderby);
		return cur;
	}
	/**
	 * <p>Title: getWordOrderBy</p>
	 * <p>Description: </p>
	 * @param cur
	 * @return
	 * @author bubble
	 * @date 2015-8-9
	 */
/*	private static WordsClass getWordOrderBy(Cursor cur){
		if ( ! isCursorValid(cur) ) {
			if ( wordClass != null ) {
				return wordClass;
			} else {
				cursor.moveToLast();
				return getWordOrderBy(cursor);
			}
		} else {
			setWordClass(cur);
			return wordClass;
		}
	}*/
	
	/**
	 * <p>Title: setWordClass</p>
	 * <p>Description: </p>
	 * @param cur
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordCls(Cursor cur){
		wordWord = cur.getString(cur.getColumnIndex(COLUMN_WORD));  
		wordPhonetic = cur.getString(cur.getColumnIndex(COLUMN_PHONETIC));  
		wordDefinition = cur.getString(cur.getColumnIndex(COLUMN_DEFINITION));  
		wordCls = new WordCls(wordWord, wordPhonetic, wordDefinition);  
	}
	 
	/**
	 * <p>Title: isCursorValid</p>
	 * <p>Description: </p>
	 * @param cursor
	 * @return
	 * @author bubble
	 * @date 2015-8-9
	 */
	public static boolean isCursorValid(Cursor cursor){
		if ( cursor == null || cursor.isAfterLast() || cursor.isBeforeFirst()) {
			Log.i("isCursorValid", "false");
			return false;
		} else {
			Log.i("isCursorValid", "true");
			return true;
		}
	}
	/**
	 * <p>Title: isCursorValid</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-20 上午1:12:13
	 */
	public static boolean isCursorValid(){
		if ( cursor == null || cursor.isAfterLast() || cursor.isBeforeFirst()) 
			return false;
		else
			return true;
	}
	
	/**
	 * <p>Title: getCursorPosition</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-19 下午10:41:21
	 */
	public static int getCursorPosition() {
		return cursor.getPosition();
	}
	
	/**
	 * <p>Title: setCursorPosition</p>
	 * <p>Description: </p>
	 * @param position
	 * @author bubble
	 * @date 2015-8-19 下午10:42:32
	 */
	public static void setCursorPosition(int position) {
		cursor.moveToPosition(position);
	}
	
}
