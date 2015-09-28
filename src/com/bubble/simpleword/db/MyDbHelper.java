package com.bubble.simpleword.db;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bubble.simpleword.R;

/**
 * <p>Title: WordsDbHelper</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-6
 */
public class MyDbHelper extends SQLiteOpenHelper {
	
	private final static int BUFFER_SIZE = 400000;
    private static SQLiteDatabase mDatabase;
	private static File dbFile;
	private static File dbDir;
	private static InputStream is;
	private static FileOutputStream fos;
	private static byte[] buffers;

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public MyDbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
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
	public MyDbHelper(Context context, String name, CursorFactory factory,
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
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6 
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 
	}
 
    /**
     * <p>Title: openDatabase</p>
     * <p>Description: 获取SD卡数据库，若不存在则导入R.raw下的数据库</p>
     * @param dbFilePath
     * @return
     * @author bubble
     * @date 2015-8-7 下午7:51:34
     */
    public static void loadDatabase(Context context, String dbFilePath) {
        try {
        	dbFile = new File(dbFilePath);
        	dbDir = dbFile.getParentFile();
        	if ( ! dbDir.isDirectory() ){
        			Log.i("databases", dbFile.getParent() + "不存在");
        			dbDir .mkdir();
    		} 
        	//判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
            if ( dbDir.exists() && ! (new File(dbFilePath).exists()) ) {	
                is = context.getResources().openRawResource(R.raw.simpleword);
                fos = new FileOutputStream(dbFilePath);
                buffers = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffers)) > 0) {
                    fos.write(buffers, 0, count);
                }
                fos.close();
                is.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
    }
}
