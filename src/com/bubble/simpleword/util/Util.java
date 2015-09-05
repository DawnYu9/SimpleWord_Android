package com.bubble.simpleword.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bubble.simpleword.menu.SettingsFragment;

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
	private static InputMethodManager inputMethodManager;
	
	private static int statusBarHeight;
	
	private static DisplayMetrics mDisplayMetrics;
    private static int screenWidth;
    private static int screenHeight;
	
	private static WindowManager mWindowManager;
	
	private static SharedPreferences prefSettings;
	private static SharedPreferences.Editor prefEditorSettings;
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
     * <p>Title: getPrefSettings</p>
     * <p>Description: </p>
     * @param context
     * @return
     * @author bubble
     * @date 2015-9-4
     */
    public static SharedPreferences getPrefSettings(Context context) {
    	prefSettings = context.getSharedPreferences(SettingsFragment.KEY_FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
    	return prefSettings;
    }
}
