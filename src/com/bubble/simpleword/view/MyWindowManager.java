package com.bubble.simpleword.view;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.util.Util;

/**
 * <p>Title: MyWindowManager</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-30 下午1:03:24
 */
public class MyWindowManager {
	private static int screenWidth;  
	private static int screenHeight;
	private static TextView tvWordCls;
	
    private static ViewSmallFloatWindow smallFloatWindow;  
  
    private static ViewBigFloatWindow bigFloatWindow;  
  
    private static LayoutParams smallFloatWindowParams;  
  
    private static LayoutParams bigFloatWindowParams;  
  
    /** 
     * 用于控制在屏幕上添加或移除悬浮窗 
     * the manager to add or remove the float window view
     */  
    private static WindowManager mWindowManager;  
  
    /**
     * <p>Title: createSmallWindow</p>
     * <p>Description: </p>
     * @param context the app's context
     * @author bubble
     * @date 2015-8-31 下午5:22:35
     */
    public static void createSmallFloatWord(Context context,int width) {  
        getWindowManager(context);  
        if (smallFloatWindow == null) {  
            smallFloatWindow = new ViewSmallFloatWindow(context);  
            if (smallFloatWindowParams == null) {  
                smallFloatWindowParams = new LayoutParams();  
                //LayoutParams.TYPE_PHONE: display in full screen(not contain statusbar)
                //LayoutParams.TYPE_SYSTEM_ALERT: display in full screen(contain statusbar,below statusbar)
                //LayoutParams.TYPE_SYSTEM_ERROR: display in full screen(contain statusbar,above statusbar)
                smallFloatWindowParams.type = LayoutParams.TYPE_SYSTEM_ERROR;  
                //the format of background
                smallFloatWindowParams.format = PixelFormat.TRANSLUCENT;  
                //FLAG_NOT_TOUCH_MODAL or FLAG_NOT_FOCUSABLE or both,and FLAG_LAYOUT_IN_SCREEN
                //if only set FLAG_LAYOUT_IN_SCREEN,the float window's focus will be full screen,the gesture operation outside the float window will be invalid
                smallFloatWindowParams.flags =  LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE 
                		| LayoutParams.FLAG_LAYOUT_IN_SCREEN ;  
                //smallWindowParams.flags =  LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN
                // | LayoutParams.FLAG_LAYOUT_INSET_DECOR | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH; 
                smallFloatWindowParams.gravity = Gravity.LEFT | Gravity.TOP;  
                smallFloatWindowParams.height = Util.getStatusBarHeight(context);  
                smallFloatWindowParams.x = 0;  
                smallFloatWindowParams.y = Util.getStatusBarHeight(context);  
            }  
            smallFloatWindowParams.width = width;  
            smallFloatWindow.setParams(smallFloatWindowParams);  
            mWindowManager.addView(smallFloatWindow, smallFloatWindowParams);  
        }  
    }  
  
    /**
	 * <p>Title: changeSmallFloatWordWidth</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-4 下午4:59:57
	 */
	public static void changeSmallFloatWordWidth(Context context,int width) {
		smallFloatWindowParams.width = width; 
		mWindowManager.updateViewLayout(smallFloatWindow, smallFloatWindowParams);  
	}
    /**
     * <p>Title: removeSmallWindow</p>
     * <p>Description: </p>
     * @param context the app's context
     * @author bubble
     * @date 2015-8-31 下午5:23:08
     */
    public static void removeSmallFloatWord(Context context) {  
        if (smallFloatWindow != null) {  
            getWindowManager(context);  
            mWindowManager.removeView(smallFloatWindow);  
            smallFloatWindow = null;  
        }  
    }  
  
    /**
     * <p>Title: createBigWindow</p>
     * <p>Description: </p>
     * @param context the app's context
     * @author bubble
     * @date 2015-8-31 下午5:23:25
     */
    public static void createBigFloatWord(Context context) {  
        getWindowManager(context);  
        screenWidth = Util.getScreenWidth();  
        screenHeight = Util.getScreenHeight();  
        if (bigFloatWindow == null) {  
            bigFloatWindow = new ViewBigFloatWindow(context);  
            if (bigFloatWindowParams == null) {  
                bigFloatWindowParams = new LayoutParams();  
                bigFloatWindowParams.x = screenWidth / 2 - ViewBigFloatWindow.viewWidth / 2;  
                bigFloatWindowParams.y = screenHeight / 2 - ViewBigFloatWindow.viewHeight / 2;  
                bigFloatWindowParams.type = LayoutParams.TYPE_PHONE;  
                bigFloatWindowParams.format = PixelFormat.RGBA_8888;  
                bigFloatWindowParams.gravity = Gravity.LEFT | Gravity.TOP;  
                bigFloatWindowParams.width = ViewBigFloatWindow.viewWidth;  
                bigFloatWindowParams.height = ViewBigFloatWindow.viewHeight;  
            }  
            mWindowManager.addView(bigFloatWindow, bigFloatWindowParams);  
        }  
    }  
  
    /**
     * <p>Title: removeBigWindow</p>
     * <p>Description: </p>
     * @param context the app's context
     * @author bubble
     * @date 2015-8-31 下午5:23:33
     */
    public static void removeBigFloatWord(Context context) {  
        if (bigFloatWindow != null) {  
            getWindowManager(context);  
            mWindowManager.removeView(bigFloatWindow);  
            bigFloatWindow = null;  
        }  
    }  

    /**
	 * <p>Title: removeAllFloatWindow</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-4 下午11:12:13
	 */
	public static void removeAllFloatWord(Context context) {
		removeSmallFloatWord(context);
		removeBigFloatWord(context);
	}
    
    /**
     * <p>Title: isFloatWindowShowing</p>
     * <p>Description: if there are float windows (smallFloatWindow or bigFloatWindow) showing on the screen</p>
     * @return 
     * @author bubble
     * @date 2015-8-31 下午5:24:20
     */
    public static boolean isFloatWindowShowing() {  
        return ( (smallFloatWindow != null) || (bigFloatWindow != null) );  
    }  
  
    /**
     * <p>Title: getWindowManager</p>
     * <p>Description: </p>
     * @param context
     * @author bubble
     * @date 2015-9-4 
     */
    private static WindowManager getWindowManager(Context context) { 
		mWindowManager = Util.getMyWindowManager(context);
		return mWindowManager;
    }  
  
    /**
     * <p>Title: updateUsedPercent</p>
     * <p>Description: </p>
     * @param context
     * @author bubble
     * @date 2015-8-31 下午9:32:03
     */
    public static void updateWordCls(Context context) {  
        if (smallFloatWindow != null) {  
            tvWordCls = (TextView) smallFloatWindow.findViewById(R.id.word_small_float_window_tv);  
            tvWordCls.setText(WordsManager.wordCls.getSpannedHtml());  
        }  
    }  
}  