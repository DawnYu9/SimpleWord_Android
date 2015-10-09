package com.bubble.simpleword.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bubble.simpleword.activity.MainActivity;
import com.bubble.simpleword.fragment.SettingsFragment;
import com.bubble.simpleword.util.Util;


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
	
	private static String selectedTable;
	private static List<String> tableList;
	
	
	/**
	 * the table to store all words's information
	 */
	public final static String NAME_MAIN_TABLE = "全部单词";
	
	/**
	 * the table to store the "wordbook tables" 's information, such as : name and create time 
	 */
	public final static String NAME_INFO_TABLE = "table_infos";
	
	public final static String COLUMN_TABLE_NAME = "name";
	public final static String COLUMN_WORD = "word";
	public final static String COLUMN_PHONETIC = "phonetic";
	public final static String COLUMN_DEFINITION = "definition";
	public final static String COLUMN_DEFINITION_EN = "definition_en";
	public final static String COLUMN_DEFINITION_CN = "definition_cn";
	public final static String COLUMN_AUDIO_URL_US = "audio_url_us";
	public final static String COLUMN_TIME = "time";
	public final static String COLUMN_IS_REMEMBERED = "is_remembered";
	public final static String COLUMN_IS_LOADED = "is_loaded";
	public static String wordWord;  
	public static String wordPhonetic;  
	public static String wordDefinition;  
	public static String wordDefinitionEN;  
	public static String wordDefinitionCN;  
	public static String wordAudioURLUS;  
	public static int isRemembered;  
	public static int isLoaded;  
	
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
    private final static int MODE_WORD_SORT_IN_ORDER = 0;
    private final static int MODE_WORD_SORT_REVERSE_ORDER = 1;
    private final static int MODE_WORD_SORT_RANDOM = 2;
    
    public static boolean isInOrder = true;
    public static boolean isReverseOrder = false;
    public static boolean isRandom = false;
    public static final String IS_INORDER = "isInOrder";
    public static final String IS_REVERSEORDER = "isReverseOrder";
    public static final String IS_RANDOM = "isRandom";
    
    private static boolean isSelectedTableChanged = false;

	private static String sql;

	private static Cursor cursorMainTable;

	/**
	 * <p>Title: initDbHelper</p>
	 * <p>Description: </p>
	 * @param context
	 * @author bubble
	 * @date 2015-9-20 下午4:07:58
	 */
	public static void initDbHelper(Context context) {
		mContext = context;
		if ( wordsDbHelper == null)
			wordsDbHelper = new MyDbHelper(mContext, MainActivity.DB_NAME, null, 1);
	}
	
	/**
	 * <p>Title: setSelectedTable</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-20 下午6:17:54
	 */
	public static void setSelectedTable(String tableName, int positionWordSort) {
		isSelectedTableChanged = true;
		selectedTable = tableName;
		updateCursor(positionWordSort);
		updateWordCls();
	}
	
	private static void updateCursor(int positionWordSort) {
		tableList = getTableList();
		if ( tableList.contains(selectedTable) ) {
			setWordSort(positionWordSort);
		}
	}
	
    /**
     * <p>Title: initWordsDB</p>
     * <p>Description: init WordsManager before operate the table</p>
     * @param context
     * @author bubble
     * @date 2015-8-7
     */
    public static void initWordsManager(Context context){
    	tableList = getTableList();
    	
		if ( tableList.size() > 0) {
			selectedTable = Util.getSharedPreferences(context).getString(SettingsFragment.KEY_SPINNER_SELECTED_WODEBOOK, tableList.get(0));
			db = wordsDbHelper.getReadableDatabase();
			if ( tableList.contains(selectedTable) ) {
				cursor = db.query(selectedTable, null, null, null, null, null, null);
				cursor.moveToFirst();
				counts = cursor.getCount();
			}
		}
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
    	tableList = getTableList();
    	if ( tableList.size() > 0) {
    		selectedTable = Util.getSharedPreferences(context).getString(SettingsFragment.KEY_SPINNER_SELECTED_WODEBOOK, tableList.get(0));
    		if ( getTableList().contains(selectedTable) ) {
    			db = wordsDbHelper.getReadableDatabase();
    			cursor = db.query(selectedTable, null, null, null, null, null, null);
    			cursor.moveToFirst();
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
    	}
    }

    /**
     * <p>Title: getTableList</p>
     * <p>Description: </p>
     * @return
     * @author bubble
     * @date 2015-9-8 下午9:09:02
     */
    public static List<String> getTableList() {
    	tableList = new ArrayList<String>();
    	Cursor cursor = null;
    	try {
			db = wordsDbHelper.getReadableDatabase();
			cursor = db.rawQuery("SELECT " + COLUMN_TABLE_NAME + " FROM " + 
					NAME_INFO_TABLE + " order by " + COLUMN_TIME, null);
			while(cursor.moveToNext()){
				tableList.add(cursor.getString(0));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (cursor != null) {
		    	cursor.close();
		    }
		    if (db != null) {
		    	db.close();
		    }
		}
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
		try {
			db = wordsDbHelper.getReadableDatabase();
			sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
					COLUMN_WORD + " TEXT NOT NULL," +
//					COLUMN_PHONETIC + " TEXT," +
//					COLUMN_DEFINITION + " TEXT," +
//					COLUMN_DEFINITION_EN + " TEXT," +
//					COLUMN_DEFINITION_CN + " TEXT," +
//					COLUMN_AUDIO_URL_US + " TEXT," +
//					COLUMN_TIME + " TEXT DEFAULT (datetime('now','localtime'))," +
//					COLUMN_IS_REMEMBERED + " INTEGER DEFAULT 0," +
//					COLUMN_IS_LOADED + " INTEGER DEFAULT 0," +
					"PRIMARY KEY (\"" + COLUMN_WORD + "\" ASC)" +
							");";    
			
			db.execSQL(sql);
			
			addTable2Info(tableName);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
        
	}
	
	/**
	 * <p>Title: createInfoTable</p>
	 * <p>Description: create a table names TABLE_INFO_NAME to store every table's info ,like  create_time</p>
	 * @author bubble
	 * @date 2015-9-19 下午3:27:40
	 */
	public static void createInfoTable() {
		try {
			createMainTable();
			
			db = wordsDbHelper.getReadableDatabase();
			sql = "CREATE TABLE IF NOT EXISTS " + NAME_INFO_TABLE + " (" +
					COLUMN_TABLE_NAME + " TEXT NOT NULL," +
					COLUMN_TIME + " TEXT DEFAULT (datetime('now','localtime'))" +
					");";    
			db.execSQL(sql);
			
			Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name!='android_metadata' order by name", null);
			cValue = new ContentValues();  
			while(cursor.moveToNext()) { 
				if ( ! cursor.getString(0).matches(NAME_INFO_TABLE) ) {
					cValue.put(COLUMN_TABLE_NAME, cursor.getString(0));  
			    	db.insert(NAME_INFO_TABLE, null, cValue); 
			    	insert2MainTable(cursor.getString(0));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if (cursor != null) {
				cursor.close();
			}
		    if (db != null) {
		    	db.close();
		    }
		}
	}
	
	/**
	 * <p>Title: createMainTable</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-6 下午11:51:48
	 */
	public static void createMainTable() {
		try {
			db = wordsDbHelper.getReadableDatabase();
			sql = "CREATE TABLE IF NOT EXISTS " + NAME_MAIN_TABLE + " (" +
					COLUMN_WORD + " TEXT NOT NULL," +
					COLUMN_PHONETIC + " TEXT," +
					COLUMN_DEFINITION + " TEXT," +
					COLUMN_DEFINITION_EN + " TEXT," +
					COLUMN_DEFINITION_CN + " TEXT," +
					COLUMN_AUDIO_URL_US + " TEXT," +
					COLUMN_TIME + " TEXT DEFAULT (datetime('now','localtime'))," +
					COLUMN_IS_REMEMBERED + " INTEGER DEFAULT 0," +
					COLUMN_IS_LOADED + " INTEGER DEFAULT 0," +
					"PRIMARY KEY (\"" + COLUMN_WORD + "\" ASC)" +
							");";    
			db.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			if (db != null) {
				db.close();
			}
		}
	}

	/**
	 * <p>Title: insert2MainTable</p>
	 * <p>Description: </p>
	 * @param i
	 * @author bubble
	 * @date 2015-10-9 下午7:40:51
	 */
	private static void insert2MainTable(String tableName) {
		sql = "insert into " + NAME_MAIN_TABLE +" select * from " + tableName;
		db.execSQL(sql);
		
		sql = "CREATE TABLE IF NOT EXISTS " + tableName + tableName + 
				" as select " + COLUMN_WORD +
				" from " + tableName;
		db.execSQL(sql);
		
		sql = "drop table if exists " + tableName;
		db.execSQL(sql);
		
		sql = "alter table " + tableName + tableName + " rename to " + tableName;
		db.execSQL(sql);
	}
	
	/**
	 * <p>Title: addTable2Info</p>
	 * <p>Description: store every table's create_time when it created</p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-19 下午3:30:52
	 */
	public static void addTable2Info(String tableName) {
	    try {
	    	cValue = new ContentValues();  
	    	cValue.put(COLUMN_TABLE_NAME, tableName);  
	    	
			String[] columns = { COLUMN_TABLE_NAME };
			String selection = COLUMN_TABLE_NAME + " = ?"; 
			String[] selectionArgs = new String[]{ tableName };
			
			db = wordsDbHelper.getWritableDatabase();
			Cursor cursor = db.query(NAME_INFO_TABLE, columns, selection, selectionArgs, null, null, null);  
		    if ( ! cursor.moveToNext() )
		    	db.insert(NAME_INFO_TABLE, null, cValue); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if ( cursor != null ) {
				cursor.close();
			}
		    if ( db != null ) {
		    	db.close();
		    }
		}
	}
	
	/**
	 * <p>Title: editTableInfo</p>
	 * <p>Description: edit table name in TABLE_INFO_NAME</p>
	 * @param oldName
	 * @param newName
	 * @author bubble
	 * @date 2015-9-19 下午9:24:15
	 */
	public static void editTableInfo(String oldName, String newName) {
		cValue = new ContentValues();  
		cValue.put(COLUMN_TABLE_NAME, newName);
		
		String[] whereArgs={ oldName };  
		
		try {
			db = wordsDbHelper.getWritableDatabase();
			db.update(NAME_INFO_TABLE, cValue, COLUMN_TABLE_NAME + " = ?", whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
	}
	
	/**
	 * <p>Title: deleteTable</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-8 下午9:37:58
	 */
	public static void deleteTable(String tableName) {
		try {
			db = wordsDbHelper.getWritableDatabase();
			db.execSQL("DROP TABLE IF EXISTS " + tableName );
			
			String[] whereArgs = { tableName };  
			db.delete(NAME_INFO_TABLE, COLUMN_TABLE_NAME + " = ?", whereArgs);   
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
	}
	
	/**
	 * <p>Title: alterTableName</p>
	 * <p>Description: </p>
	 * @param oldName
	 * @param newName
	 * @author bubble
	 * @date 2015-9-8 下午9:41:06
	 */
	public static boolean alterTableName(String oldName, String newName) {
		if ( ! getTableList().contains(newName) ) {
			try {
				db = wordsDbHelper.getReadableDatabase();
				db.execSQL("ALTER TABLE " + oldName + " RENAME TO " + newName + ";");
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
			    if (db != null) {
			    	db.close();
			    }
			}
			return true;
		}
		else
			return false;
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
		try {
			db = wordsDbHelper.getWritableDatabase();
			
		    cValue = new ContentValues();  
		    
		    cValue.put(COLUMN_WORD, word);  
		    db.insert(tableName, null, cValue); 
		    
		    cValue.put(COLUMN_PHONETIC, phonetic);  
		    cValue.put(COLUMN_DEFINITION, definition);  
	   
			db.insert(NAME_MAIN_TABLE, null, cValue); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
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
		try {
			db = wordsDbHelper.getWritableDatabase();
			
			cValue = new ContentValues();  
			
		    cValue.put(COLUMN_WORD, wordCls.getWord());  
		    db.insert(tableName, null, cValue); 
		    
		    cValue.put(COLUMN_PHONETIC, wordCls.getPhonetic());  
		    cValue.put(COLUMN_DEFINITION, wordCls.getDefinition());  
	   
			db.insert(NAME_MAIN_TABLE, null, cValue); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
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
		
		try {
			db = wordsDbHelper.getWritableDatabase();
			db.delete(tableName, WHERE_CLAUSE_BY_WORD, whereArgs);   
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
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
	    case COLUMN_DEFINITION_EN:
	    	cValue.put(COLUMN_DEFINITION_EN, editValue);
	    	break;
	    case COLUMN_DEFINITION_CN:
	    	cValue.put(COLUMN_DEFINITION_CN, editValue);
	    	break;
	    case COLUMN_AUDIO_URL_US:
	    	cValue.put(COLUMN_AUDIO_URL_US, editValue);
	    	break;
	    case COLUMN_IS_REMEMBERED:
	    	cValue.put(COLUMN_IS_REMEMBERED, editValue);
	    	break;
	    case COLUMN_IS_LOADED:
	    	cValue.put(COLUMN_IS_LOADED, editValue);
	    	break;
	    }
	    
	    String[] whereArgs={ word };  
	    
	    try {
			db = wordsDbHelper.getWritableDatabase();
			db.update(NAME_MAIN_TABLE, cValue, WHERE_CLAUSE_BY_WORD, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
	}
	
	/**
	 * <p>Title: editWord</p>
	 * <p>Description: edit: word, phonetic, definition</p>
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
		
		try {
			db = wordsDbHelper.getWritableDatabase();
			db.update(NAME_MAIN_TABLE, cValue, WHERE_CLAUSE_BY_WORD, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
	}
	/**
	 * <p>Title: editWholeWord</p>
	 * <p>Description: add the loaded information of the word
	 * editing these columns: COLUMN_DEFINITION_EN, COLUMN_DEFINITION_CN, COLUMN_AUDIO_URL_US, COLUMN_IS_LOADED</p>
	 * @param tableName
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-16 下午11:48:05
	 */
	public static void addWordLoadInfo(String tableName, WordCls wordCls) {
		cValue = new ContentValues();  
		cValue.put(COLUMN_DEFINITION_EN, wordCls.getDefinitionEN());
		cValue.put(COLUMN_DEFINITION_CN, wordCls.getDefinitionCN());
		cValue.put(COLUMN_AUDIO_URL_US, wordCls.getAudioUrlUS());
		cValue.put(COLUMN_IS_LOADED, wordCls.isLoaded());
		
		String[] whereArgs={ wordCls.getWord() };  
		
		try {
			db = wordsDbHelper.getWritableDatabase();
			db.update(NAME_MAIN_TABLE, cValue, WHERE_CLAUSE_BY_WORD, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
		    if (db != null) {
		    	db.close();
		    }
		}
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
	public static List<WordCls> queryWordCh(String value) {
		String[] columns = { COLUMN_WORD, COLUMN_DEFINITION };
		String selection = COLUMN_DEFINITION + " like ? "; 
		String[] selectionArgs = new String[]{ "%" + value + "%" };
		
		List<WordCls> wordClsList = null;
		try {
			db = wordsDbHelper.getReadableDatabase();
			Cursor cursor = db.query(NAME_MAIN_TABLE, columns, selection, selectionArgs, null, null, COLUMN_WORD);  
			
			wordClsList = new ArrayList<>();
			while(cursor.moveToNext()){  
				WordCls wordCls = new WordCls();
			    wordCls.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));  
			    wordCls.setDefinition(cursor.getString(cursor.getColumnIndex(COLUMN_DEFINITION)));  
			    wordClsList.add(wordCls);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null )
				cursor.close();
			if ( db != null )
				db.close();
		}
		
        return wordClsList;
	}
	
	public static WordCls queryWordEn(String value) {
		String[] columns = { COLUMN_WORD, COLUMN_PHONETIC, COLUMN_DEFINITION };
		String selection = WHERE_CLAUSE_BY_WORD; 
		String[] selectionArgs = { value };
		
		db = wordsDbHelper.getReadableDatabase();
		Cursor cursor = db.query(NAME_MAIN_TABLE, columns, selection, selectionArgs, null, null, COLUMN_WORD);  
		WordCls wordCls = null;
		if ( cursor.moveToNext() ) {
			wordCls = new WordCls();
			wordCls.setWord(cursor.getString(cursor.getColumnIndex(COLUMN_WORD)));  
			wordCls.setPhonetic(cursor.getString(cursor.getColumnIndex(COLUMN_PHONETIC)));  
			wordCls.setDefinition(cursor.getString(cursor.getColumnIndex(COLUMN_DEFINITION)));  
		} 
			
		db.close(); 
		return wordCls;
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
		WordCls wordCls;
		if (cur != null && cur.moveToFirst()) {  
		    do {  
//		        setWordCls(cur, 1);
		    	wordCls = new WordCls();
		    	wordCls.setWord(cur.getString(cur.getColumnIndex(COLUMN_WORD)));
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
			setWordCls(cursor, 0);
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
			setWordCls(cursor, 0);
			cursor.moveToNext();
			return wordCls;
		}
	}
	
	public static void setWordSort(int mode) {
		switch (mode) {
		case MODE_WORD_SORT_IN_ORDER:
			setWordInOrder();
			break;
		case MODE_WORD_SORT_RANDOM:
			setWordRandom();
			break;
		case MODE_WORD_SORT_REVERSE_ORDER:
			setWordReverseOrder();
			break;
		default:
			break;
		}
	}
	
	/**
	 * <p>Title: setWordRandom</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordRandom(){
		if ( (! isRandom) || isSelectedTableChanged) {
			if ( ! isRandom ) {
				MODE_GET_WORD = MODE_WORD_SORT_RANDOM;
				setWordModeFlag(MODE_GET_WORD);
			}
			if ( isSelectedTableChanged ) 
				isSelectedTableChanged = false;
			
			cursor = setWordOrderBy(cursor, ORDERBY_RANDOM);
			cursor.moveToFirst();
		}
		
//		if ( ! isRandom ) {
//			MODE_GET_WORD = MODE_WORD_SORT_RANDOM;
//			cursor = setWordOrderBy(cursor, ORDERBY_RANDOM);
//			cursor.moveToFirst();
//			setWordModeFlag(MODE_GET_WORD);
//		} else if (isSelectedTableChanged) {
//			isSelectedTableChanged = false;
//			cursor = setWordOrderBy(cursor, ORDERBY_RANDOM);
//			cursor.moveToFirst();
//		}
	}
	
	/**
	 * <p>Title: setWordInOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordInOrder(){
		if ( (! isInOrder) || isSelectedTableChanged) {
			if (! isInOrder) {
				MODE_GET_WORD = MODE_WORD_SORT_IN_ORDER;
				setWordModeFlag(MODE_GET_WORD);
			}
			if ( isSelectedTableChanged ) 
				isSelectedTableChanged = false;
			
			cursor = setWordOrderBy(cursor, ORDERBY_IN_ORDER);
			cursor.moveToFirst();
		}
	}
	
	/**
	 * <p>Title: setWordReverseOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordReverseOrder(){
		if ( (! isReverseOrder) ||  isSelectedTableChanged) {
			if (! isReverseOrder) {
				MODE_GET_WORD = MODE_WORD_SORT_REVERSE_ORDER;
				setWordModeFlag(MODE_GET_WORD);
			}
			if (isSelectedTableChanged)
				isSelectedTableChanged = false;
			
			cursor = setWordOrderBy(cursor,ORDERBY_REVERSE_ORDER);
			cursor.moveToFirst();
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
		case MODE_WORD_SORT_RANDOM:
			isRandom = true;
			isInOrder = false;
			isReverseOrder = false;
			break;
		case MODE_WORD_SORT_IN_ORDER:
			isInOrder = true;
			isRandom = false;
			isReverseOrder = false;
			break;
		case MODE_WORD_SORT_REVERSE_ORDER:
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
		db = wordsDbHelper.getReadableDatabase();
		cur = db.query(selectedTable, null, null, null, null, null, orderby);
		
		return cur;
	}
	
	/**
	 * <p>Title: setWordClass</p>
	 * <p>Description: </p>
	 * @param cur
	 * @author bubble
	 * @date 2015-8-8
	 */
//	public static void setWordCls(Cursor cur){
//		try {
//			wordWord = cur.getString(cur.getColumnIndex(COLUMN_WORD));  
//			
//			sql = "select * from " + NAME_MAIN_TABLE + " where " + COLUMN_WORD + "=?";
//			db = wordsDbHelper.getReadableDatabase();
//			cursorMainTable = db.rawQuery(sql, new String[]{ wordWord });
//			if ( cursorMainTable.moveToNext() ) {
//				wordPhonetic = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_PHONETIC));  
//				wordDefinition = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION));  
//				wordCls = new WordCls(wordWord, wordPhonetic, wordDefinition);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally{
//		    if (cursorMainTable != null) {
//		    	cursorMainTable.close();
//		    }
//		    if (db != null) {
//		    	db.close();
//		    }
//		}
//	}
	
	public static WordCls getWordCls() {
		if ( wordCls == null )
			wordCls = updateWordCls();
		
		return wordCls;
	}
	/**
	 * <p>Title: setWordCls</p>
	 * <p>Description: </p>
	 * @param cur
	 * @param type 0: 简洁释义; 1: 完整释义
	 * @author bubble
	 * @date 2015-9-16 下午9:29:56
	 */
	public static void setWordCls(Cursor cur,int type){
		try {
			wordWord = cur.getString(cur.getColumnIndex(COLUMN_WORD));  
			
			sql = "select * from " + NAME_MAIN_TABLE + " where " + COLUMN_WORD + "=?";
			db = wordsDbHelper.getReadableDatabase();
			cursorMainTable = db.rawQuery(sql, new String[]{ wordWord });
			
			if ( cursorMainTable.moveToNext() ) {
				switch (type) {
				case 0:
					wordPhonetic = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_PHONETIC));  
					wordDefinition = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION));  
					wordCls = new WordCls(wordWord, wordPhonetic, wordDefinition);
					break;
				case 1:
					wordPhonetic = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_PHONETIC));  
					wordDefinition = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION));  
					wordDefinitionEN = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION_EN));  
					wordDefinitionCN = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION_CN));  
					wordAudioURLUS = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_AUDIO_URL_US));  
					isRemembered = cursorMainTable.getInt(cursorMainTable.getColumnIndex(COLUMN_IS_REMEMBERED));  
					isLoaded = cursorMainTable.getInt(cursorMainTable.getColumnIndex(COLUMN_IS_LOADED));  
					wordCls = new WordCls(wordWord, wordPhonetic, wordDefinition, wordDefinitionEN,
							wordDefinitionCN, wordAudioURLUS,isRemembered, isLoaded);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( cursorMainTable != null )
				cursorMainTable.close();
			if ( db != null )
				db.close();
		}
	}
	
	/**
	 * <p>Title: setWordCls</p>
	 * <p>Description: </p>
	 * @param type 0: 简洁释义; 1: 完整释义
	 * @author bubble
	 * @date 2015-10-7 上午2:35:02
	 */
	public static WordCls getWordCls(String wordWord, int type){
		WordCls wordCls = null;
		try {
			sql = "select * from " + NAME_MAIN_TABLE + " where " + COLUMN_WORD + "=?";
			db = wordsDbHelper.getReadableDatabase();
			cursorMainTable = db.rawQuery(sql, new String[]{ wordWord });
			
			if ( cursorMainTable.moveToNext() ) {
				switch (type) {
				case 0:
					wordPhonetic = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_PHONETIC));  
					wordDefinition = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION));  
					wordCls = new WordCls(wordWord, wordPhonetic, wordDefinition);
					break;
				case 1:
					wordPhonetic = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_PHONETIC));  
					wordDefinition = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION));  
					wordDefinitionEN = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION_EN));  
					wordDefinitionCN = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_DEFINITION_CN));  
					wordAudioURLUS = cursorMainTable.getString(cursorMainTable.getColumnIndex(COLUMN_AUDIO_URL_US));  
					isRemembered = cursorMainTable.getInt(cursorMainTable.getColumnIndex(COLUMN_IS_REMEMBERED));  
					isLoaded = cursorMainTable.getInt(cursorMainTable.getColumnIndex(COLUMN_IS_LOADED));  
					wordCls = new WordCls(wordWord, wordPhonetic, wordDefinition, wordDefinitionEN,
							wordDefinitionCN, wordAudioURLUS,isRemembered, isLoaded);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ( cursorMainTable != null )
				cursorMainTable.close();
			if ( db != null )
				db.close();
		}
		return wordCls;
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
