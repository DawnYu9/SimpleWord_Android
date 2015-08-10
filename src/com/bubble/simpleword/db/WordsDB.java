package com.bubble.simpleword.db;

import java.util.ArrayList;

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
public class WordsDB {
	private static Context mContext;
	private static SQLiteDatabase db;
    private static WordsDbHelper wordsDbHelper;
    
    public static int counts;
    private static Cursor cursor;
    private static Cursor cursorRandom;
    private static Cursor cursorInOrder;
    private static Cursor cursorReverseOrder;
    
    public static WordsClass wordClass;
    
    public final static String TABLE_NAME = "Words";
    public final static String COLUMN_WORD = "word";
    public final static String COLUMN_PHONETIC = "phonetic_symbol";
    public final static String COLUMN_DEFINITION = "definition";
    public static String word;  
    public static String phonetic_symbol;  
    public static String definition;  
    
    private final static String ORDERBY_RANDOM = "RANDOM()"; //整体随机重新排序，未改变原数据库数据顺序
    private final static String ORDERBY_IN_ORDER = "word"; 
    private final static String ORDERBY_REVERSE_ORDER = "word desc"; 
    
    /**
     * <p>Title: initWordsDB</p>
     * <p>Description: 对表执行操作前先初始化</p>
     * @param context
     * @author bubble
     * @date 2015-8-7
     */
    public static void initWordsDB(Context context){
    	mContext = context;
		wordsDbHelper = new WordsDbHelper(mContext, MainActivity.DB_NAME, null, 1);
		db = wordsDbHelper.getWritableDatabase();
		cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		counts = cursor.getCount();
		Log.i("Words表的行数counts", Integer.toString(counts));
    }

	public static void addWord(){
		
	}
	
	public static void deleteWord(){
		
	}
	
	public static void editWord(){
		
	}
	
	/**
	 * <p>Title: getAllWords</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-7
	 */
	public static ArrayList<WordsClass> getAllWords() {
		cursor.moveToFirst();
		ArrayList<WordsClass> allWords = new ArrayList<WordsClass>();
		if (cursor != null && cursor.moveToFirst()) {  
		    do {  
		        setWordClass(cursor);
		        allWords.add(wordClass);  
		    } while (cursor.moveToNext());  
		    db.close();
		    return allWords;
		} else {
			db.close();
			return null;
		}
    }
	
	/**
	 * <p>Title: setWordRandom</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordRandom(){
		cursorRandom = setWordOrderBy(cursorRandom, ORDERBY_RANDOM);
		cursorRandom.moveToFirst();
	}
	/**
	 * <p>Title: getWordRandom</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static WordsClass getWordRandom(){
		getWordOrderBy(cursorRandom);
		cursorRandom.moveToNext();
        return wordClass;
	}
	
	
	/**
	 * <p>Title: setWordInOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordInOrder(){
		cursorInOrder = setWordOrderBy(cursorInOrder, ORDERBY_IN_ORDER);
		cursorInOrder.moveToFirst();
	}
	/**
	 * <p>Title: getWordInOrder</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static WordsClass getWordInOrder(){
		getWordOrderBy(cursorInOrder);
        cursorInOrder.moveToNext();
        return wordClass;
	}
	
	/**
	 * <p>Title: setWordReverseOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordReverseOrder(){
		cursorReverseOrder = setWordOrderBy(cursorReverseOrder,ORDERBY_REVERSE_ORDER);
		cursorReverseOrder.moveToFirst();
	}
	/**
	 * <p>Title: getWordReverseOrder</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static WordsClass getWordReverseOrder(){
		getWordOrderBy(cursorReverseOrder);
        cursorReverseOrder.moveToNext();
        return wordClass;
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
	private static WordsClass getWordOrderBy(Cursor cur){
		if ( ! isCursorValid(cur) )
			return null;
		
		setWordClass(cur);
		return wordClass;
	}
	
	/**
	 * <p>Title: setWordClass</p>
	 * <p>Description: </p>
	 * @param cur
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordClass(Cursor cur){
		wordClass = new WordsClass();  
		word = cur.getString(cur.getColumnIndex(COLUMN_WORD));  
		phonetic_symbol = cur.getString(cur.getColumnIndex(COLUMN_PHONETIC));  
		definition = cur.getString(cur.getColumnIndex(COLUMN_DEFINITION));  
		wordClass.setWord(word);  
		wordClass.setPhonetic_symbol(phonetic_symbol);  
		wordClass.setDefinition(definition);
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
		if ( cursor == null || cursor.isAfterLast() || cursor.isBeforeFirst())
			return false;
		else
			return true;
	}
}
