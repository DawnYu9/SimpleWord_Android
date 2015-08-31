package com.bubble.simpleword.util;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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
	private static int statusBarHeight;
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
	    InputMethodManager inputMethodManager = (InputMethodManager)mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
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
}
