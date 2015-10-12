package com.bubble.simpleword.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bubble.simpleword.activity.MainActivity;
import com.bubble.simpleword.db.WordCls;

/**
 * <p>Title: UtilOnView</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-22 下午6:33:12
 */
public class Util {
	public static final String PREFS_FILE_NAME = "SimpleWord_Prefs_File";
	
	private static InputMethodManager inputMethodManager;
	
	private static int statusBarHeight;
	
	private static DisplayMetrics mDisplayMetrics;
    private static int screenWidth;
    private static int screenHeight;
	
	private static WindowManager mWindowManager;
	
	private static SharedPreferences.Editor prefEditorSettings;
	
	private static String url;
    private static JsonObjectRequest jsonRequest;
    
	private static JSONObject data;
	private static JSONObject defEN;
	private static String definitionEN; 
	private static JSONObject defCN;
	private static String definitionCN; 
	private static String audioUrlUS = "";

	private static Uri uri;

	private static MediaPlayer player = null;

	private static Class<?> cls;

	private static Object obj;

	private static Field field;

	private static int x;

	private static DisplayMetrics mDisplayMetrics2;

	public static final String ERROR = "数据获取失败，请重试";

	private static File dir;
	private static File file;
	
	private static String cachePath;
	
	
	private static String currentDate = "";
	private static final String CURRENT_DATE = "current_date";

	private static SimpleDateFormat dateFormat;

	private static Date date;

	private static File delFolder;

	private static FileWriter fileWriter;

	private static SharedPreferences cachePref;

	private static Editor cacheEditor;

	private static String audioPath = "";
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public Util() {
	}

	  /**
	 * <p>Title: hideKeyboard</p>
	 * <p>Description: </p>
	 * @param view
	 * @param mActivity
	 * @author bubble
	 * @date 2015-8-22 下午6:35:16
	 */
	public static void hideKeyboard(View view,Activity mActivity) {
	    inputMethodManager = (InputMethodManager)mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
	    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
	
	/**
	 * <p>Title: getPrefStr2Int</p>
	 * <p>Description: </p>
	 * @param v
	 * @param defValue
	 * @return
	 * @author bubble
	 * @date 2015-8-22 下午10:01:40
	 */
	public static int getPrefStr2Int(SharedPreferences pref,View v,String defValue) {
		return Integer.parseInt(pref.getString(v.getTag().toString(), defValue));
	}
	/**
	 * <p>Title: getPrefStr2Int</p>
	 * <p>Description: </p>
	 * @param pref
	 * @param key
	 * @param defValue
	 * @return
	 * @author bubble
	 * @date 2015-8-23 上午00:49:43
	 */
	public static int getPrefStr2Int(SharedPreferences pref,String key,String defValue) {
		return Integer.parseInt(pref.getString(key, defValue));
	}
	
    /**
     * <p>Title: getStatusBarHeight</p>
     * <p>Description: </p>
     * @param context
     * @return
     * @author bubble
     * @date 2015-8-31 下午6:41:30
     */
    public static int getStatusBarHeight(Context context) {  
        if (statusBarHeight == 0) {  
            try {  
                cls = Class.forName("com.android.internal.R$dimen");  
                obj = cls.newInstance();  
                field = cls.getField("status_bar_height");  
                x = (Integer) field.get(obj);  
                statusBarHeight = context.getResources().getDimensionPixelSize(x);  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
        return statusBarHeight;  
    }
    
	/**
	 * <p>Title: getScreenSize</p>
	 * <p>Description: </p>
	 * @param context
	 * @author bubble
	 * @date 2015-9-4 下午5:21:22
	 */
	public static void getScreenSize() {
		if ( screenWidth == 0 || screenHeight == 0 ) {
			mDisplayMetrics2 = Resources.getSystem().getDisplayMetrics();
			
			screenWidth = mDisplayMetrics2.widthPixels;
			screenHeight = mDisplayMetrics2.heightPixels;
		}
	}
    
	/**
	 * <p>Title: getScreenWidth</p>
	 * <p>Description: </p>
	 * @param context
	 * @return
	 * @author bubble
	 * @date 2015-9-4 下午5:23:12
	 */
	public static int getScreenWidth() {
		getScreenSize();
		return screenWidth;
	}
	
	/**
	 * <p>Title: getScreenHeight</p>
	 * <p>Description: </p>
	 * @param context
	 * @return
	 * @author bubble
	 * @date 2015-9-4 下午5:23:34
	 */
	public static int getScreenHeight() {
		getScreenSize();
		return screenHeight;
	}
	
    /**
     * <p>Title: getWindowManager</p>
     * <p>Description: </p>
     * @param context the app's context
     * @return WindowManager to manage the float window views(add or remove)
     * @author bubble
     * @date 2015-8-31 下午5:26:21
     */
    public static WindowManager getMyWindowManager(Context context) {  
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        return mWindowManager;  
    } 
    
    /**
     * <p>Title: getSharedPreferences</p>
     * <p>Description: </p>
     * @param context
     * @return
     * @author bubble
     * @date 2015-9
     */
    public static SharedPreferences getSharedPreferences(Context context) {
    	return context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * <p>Title: getSharedPreferences</p>
     * <p>Description: </p>
     * @param context
     * @param name
     * @return
     * @author bubble
     * @date 2015-10-11 下午11:39:44
     */
    public static SharedPreferences getSharedPreferences(Context context, String name) {
    	return context.getSharedPreferences(name, Context.MODE_PRIVATE);
    }
    
    /**
     * <p>Title: getCacheSharedPreferences</p>
     * <p>Description: </p>
     * @param context
     * @return
     * @author bubble
     * @date 2015-10-11 下午11:43:19
     */
    public static SharedPreferences getCacheSharedPreferences(Context context) {
    	return context.getSharedPreferences(MainActivity.CACHE_PREFS_DATA_FILE_NAME, Context.MODE_PRIVATE);
    }
    
    /**
     * <p>Title: getSharedPreferencesEditor</p>
     * <p>Description: </p>
     * @param context
     * @return
     * @author bubble
     * @date 2015-9
     */
    public static SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
    	return getSharedPreferences(context).edit();
    }
    
	/**
	 * <p>Title: pronounce</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @throws IOException 
	 * @throws IllegalStateException 
	 * @throws SecurityException 
	 * @throws IllegalArgumentException 
	 * @date 2015-9-24 上午11:41:30
	 */
	public static void pronounceWord(final WordCls wordCls, final Context context) {
		cachePath = MainActivity.CACHE_WORD_DIRECTORY + File.separator + wordCls.getWord() + ".mp3";
		file = new File(cachePath);
		if ( file.exists() ) {
			try {
				playAudio(cachePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if ( wordCls.isLoaded() ) {
			if ( ! isStringEmpty(wordCls.getAudioUrlUS()) ) {
				if ( audioUrlUS != wordCls.getAudioUrlUS() ) {   
					audioUrlUS = wordCls.getAudioUrlUS();
					uri = Uri.parse(audioUrlUS);
					player = new MediaPlayer().create(context, uri);
					player.start();
				} else {
					if ( ! isStringEmpty(audioUrlUS) ) {
					   if ( player != null ) {
					        if ( player.isPlaying() ) {
					            player.seekTo(0);
					        } else {
					            player.start();
					        }
					    } else {
					    	uri = Uri.parse(audioUrlUS);
					    	player = new MediaPlayer().create(context, uri);
					    	player.start();
					    }
					}
				}
				
			} else {
				Toast.makeText(context, ERROR, Toast.LENGTH_SHORT);
			}
		} else {
			url = MainActivity.URL_SHANBAY + wordCls.getWord(); 
			  
			jsonRequest = new JsonObjectRequest
			        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			            @Override
			            public void onResponse(JSONObject response) {
			                // the response is already constructed as a JSONObject!
			                try {
			                    
			                	Util.getJsonPartData(wordCls, response);
			                	pronounceWord(wordCls, context);
//			                	WordsManager.addWordLoadInfo(tableName, wordCls);
//			                	updateItem(position, wordCls);
//			                	
//			                	horizonViewHolder.progressBar.setVisibility(View.INVISIBLE);
//			                	horizonViewHolder.tvHint.setVisibility(View.INVISIBLE);
			                } catch (JSONException e) {
			                    e.printStackTrace();
			                } catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (IllegalStateException e) {
								e.printStackTrace();
							}
			            }
			        }, new Response.ErrorListener() {
			  
			            @Override
			            public void onErrorResponse(VolleyError error) {
			            	Toast.makeText(context, ERROR, Toast.LENGTH_SHORT).show();
			                error.printStackTrace();
			            }
			        });
			
			  
			MainActivity.mQueue.add(jsonRequest);  
		}
	}

	/**
	 * <p>Title: playAudio</p>
	 * <p>Description: </p>
	 * @throws IOException
	 * @author bubble
	 * @date 2015-10-11 下午8:31:42
	 */
	private static void playAudio(String path) throws IOException {
		try {
			if ( audioPath.equals(path) ) {
				if ( ! isStringEmpty(audioPath) ) {
					   if ( player != null ) {
					        if ( player.isPlaying() ) {
					            player.seekTo(0);
					        } else {
					            player.start();
					        }
					    } else {
					    	player = new MediaPlayer();
							player.setDataSource(path);
							player.prepare();
							player.start();
					    }
					}
			} else {
				audioPath = path;
				if ( ! isStringEmpty(audioPath) ) {
					player = new MediaPlayer();
					player.setDataSource(path);
					player.prepare();
					player.start();
				}
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * <p>Title: pronounceSentence</p>
	 * <p>Description: </p>
	 * @param url
	 * @author bubble
	 * @throws IOException 
	 * @date 2015-9-26 下午10:47:46
	 */
	public static void pronounceSentence(Context context, String url) throws IOException {
		cachePath = MainActivity.CACHE_SENTENCE_DIRECTORY + File.separator + getCurrentDate() + "-day.mp3";
		file = new File(cachePath);
		if ( file.exists() ) {
			playAudio(cachePath);
		} else {
	        uri = Uri.parse(url);
	        player = new MediaPlayer().create(context, uri);
	        player.start();
		}
	}
	
	/**
	 * <p>Title: parseJsonData</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @param response
	 * @throws JSONException
	 * @author bubble
	 * @date 2015-9-24 下午8:57:32
	 */
	public static WordCls getJsonPartData(final WordCls wordCls, JSONObject response)
			throws JSONException {
		data = response.getJSONObject("data");
		 
		defEN = data.getJSONObject("en_definition");
		definitionEN = defEN.getString("pos") + "." + defEN.getString("defn"); 
		 
		defCN = data.getJSONObject("cn_definition");
		definitionCN = defCN.getString("pos") + defCN.getString("defn"); 
		 
//		audioUrlUS = data.getString("us_audio");
		
		wordCls.setDefinitionEN(definitionEN);
		wordCls.setDefinitionCN(definitionCN);
		wordCls.setAudioUrlUS(data.getString("us_audio"));
		wordCls.setLoaded(true);
		
		download(data.getString("us_audio"), MainActivity.CACHE_WORD_DIRECTORY);
		
		return wordCls;
	}
	
	/**
	 * <p>Title: isStringEmpty</p>
	 * <p>Description: </p>
	 * @param s
	 * @return
	 * @author bubble
	 * @date 2015-10-9 下午8:05:39
	 */
	public static boolean isStringEmpty(String s) {
		if ( s == "" || s == null )
			return true;
		
		return false;
	}
	
	/**
	 * <p>Title: downloadAudio</p>
	 * <p>Description: </p>
	 * @param url
	 * @param path
	 * @author bubble
	 * @date 2015-10-11 下午7:41:28
	 */
	public static void download(String url, String path) {
		dir = createDir(path);
		if ( url.isEmpty() )
			return;
		
		OkHttpClientManager.getDownloadDelegate().downloadAsyn(
				url,
				path,
				new OkHttpClientManager.ResultCallback<String>() {
			
					@Override
					public void onResponse(String response) {
					}
	
					@Override
					public void onError(com.squareup.okhttp.Request request,
							Exception e) {
					}
				});
	}
	
	/**
	 * <p>Title: clearCache</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-11 下午7:59:59
	 */
	public static void clearCache(Context context) {
		clearFolder(MainActivity.CACHE_IMG_DIRECTORY);
		clearFolder(MainActivity.CACHE_SENTENCE_DIRECTORY);
		clearFolder(MainActivity.CACHE_WORD_DIRECTORY);
		
		cachePref = getCacheSharedPreferences(context);
		cacheEditor = cachePref.edit();
		cacheEditor.clear();
		cacheEditor.commit();
	}
	
	/**
	 * <p>Title: clearFolder</p>
	 * <p>Description: </p>
	 * @param dir
	 * @author bubble
	 * @date 2015-10-11 下午8:12:41
	 */
	public static void clearFolder(String dir) {
		  delFolder = new File(dir); 
		  File delFolderFiles[] = delFolder.listFiles();
		  try  { 
		     for (int i = 0; i < delFolderFiles.length; i++) {
		        if(delFolderFiles[i].isDirectory()) {
		           clearFolder(dir+delFolderFiles[i].getName()+File.separator); //递归清空子文件夹
		        }
		        delFolderFiles[i].delete();
		     }
		  } catch (Exception e) { 
		    System.out.println("清空文件夹操作出错!"); 
		    e.printStackTrace(); 
		  }
		}
		 
	/**
	 * <p>Title: releaseMediaPlayer</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-11 下午8:22:53
	 */
	public static void releaseMediaPlayer() {
		 if ( player != null ) {
			 player.release();
			 player = null;
			 audioPath = "";
		 }
	}
	
	/**
	 * <p>Title: getLoacalBitmap</p>
	 * <p>Description: </p>
	 * @param url
	 * @return
	 * @author bubble
	 * @date 2015-10-11 下午9:12:53
	 */
	public static Bitmap getLoacalBitmap(String url) {
        try {
             FileInputStream fis = new FileInputStream(url);
             return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片        

          } catch (FileNotFoundException e) {
             e.printStackTrace();
             return null;
        }
	}
	
	/**
	 * <p>Title: createDir</p>
	 * <p>Description: </p>
	 * @param path
	 * @author bubble
	 * @date 2015-10-11 下午10:21:58
	 */
	public static File createDir(String path) {
		dir = new File(path);
		if ( ! dir.isDirectory() ) {
			dir.mkdirs();
		}
		
		return dir;
	}
	
	/**
	 * <p>Title: createFile</p>
	 * <p>Description: </p>
	 * @param path
	 * @return
	 * @author bubble
	 * @date 2015-10-11 下午10:25:57
	 */
	public static File createFile(String path) {
		file = new File(path);
		if ( ! file.exists() ) {
			createDir(file.getParent());
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	/**
	 * <p>Title: getCurrentDate</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-10-11 下午10:50:37
	 */
	public static String getCurrentDate() {
		if ( currentDate.isEmpty() ) {
		    dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		    date = new Date();
		    currentDate = dateFormat.format(date);
		}
	    return currentDate;
	}
	
	/**
	 * <p>Title: stopPlayerSentence</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-13 上午1:03:41
	 */
	public static void stopMediaPlayer() {
		if ( player != null ) {
			if ( player.isPlaying() )
				player.stop();
				audioPath = "";
		}
	}
}
