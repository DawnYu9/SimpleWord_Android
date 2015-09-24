package com.bubble.simpleword.util;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.fragment.SettingsFragment;

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
	private static String audioUrlUS;
	
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
                Class<?> c = Class.forName("com.android.internal.R$dimen");  
                Object o = c.newInstance();  
                Field field = c.getField("status_bar_height");  
                int x = (Integer) field.get(o);  
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
//			mDisplayMetrics = new DisplayMetrics();
//			getMyWindowManager(context).getDefaultDisplay().getMetrics(mDisplayMetrics);
			DisplayMetrics mDisplayMetrics = Resources.getSystem().getDisplayMetrics();
			
			screenWidth = mDisplayMetrics.widthPixels;
			screenHeight = mDisplayMetrics.heightPixels;
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
	 * @date 2015-9-24 上午11:41:30
	 */
	public static void pronounce(final WordCls wordCls, final Context context) {
		if ( wordCls.isLoaded() ) {
			String path=wordCls.getAudioUrlUS();   
            Uri uri = Uri.parse(path);
            MediaPlayer player = new MediaPlayer().create(context, uri);
            player.start();
		} else {
			url = MainActivity.URL_SHANBAY + wordCls.getWord(); 
			  
			jsonRequest = new JsonObjectRequest
			        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			            @Override
			            public void onResponse(JSONObject response) {
			                // the response is already constructed as a JSONObject!
			                try {
			                    
			                	Util.parseJsonPartData(wordCls, response);
			                	pronounce(wordCls, context);
//			                	WordsManager.addWordLoadInfo(tableName, wordCls);
//			                	updateItem(position, wordCls);
//			                	
//			                	horizonViewHolder.progressBar.setVisibility(View.INVISIBLE);
//			                	horizonViewHolder.tvHint.setVisibility(View.INVISIBLE);
			                } catch (JSONException e) {
			                    e.printStackTrace();
			                }
			            }
			        }, new Response.ErrorListener() {
			  
			            @Override
			            public void onErrorResponse(VolleyError error) {
			            	Toast.makeText(context, "数据获取失败，请重试", Toast.LENGTH_SHORT).show();
			                error.printStackTrace();
			            }
			        });
			
			  
			MainActivity.mQueue.add(jsonRequest);  
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
	public static WordCls parseJsonPartData(final WordCls wordCls, JSONObject response)
			throws JSONException {
		data = response.getJSONObject("data");
		 
		defEN = data.getJSONObject("en_definition");
		definitionEN = defEN.getString("pos") + "." + defEN.getString("defn"); 
		 
		defCN = data.getJSONObject("cn_definition");
		definitionCN = defCN.getString("pos") + defCN.getString("defn"); 
		 
		audioUrlUS = data.getString("us_audio");
		
		wordCls.setDefinitionEN(definitionEN);
		wordCls.setDefinitionCN(definitionCN);
		wordCls.setAudioUrlUS(audioUrlUS);
		wordCls.setLoaded(true);
		
		return wordCls;
	}
}
