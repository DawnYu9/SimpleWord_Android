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
    public static void createSmallWindow(Context context) {  
        WindowManager windowManager = getWindowManager(context);  
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager(context).getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        if (smallFloatWindow == null) {  
            smallFloatWindow = new ViewSmallFloatWindow(context);  
            if (smallFloatWindowParams == null) {  
                smallFloatWindowParams = new LayoutParams();  
                //LayoutParams.TYPE_PHONE: display in full screen(not contain statusbar)
                //LayoutParams.TYPE_SYSTEM_ALERT: display in full screen(contain statusbar,below statusbar)
                //LayoutParams.TYPE_SYSTEM_ERROR: display in full screen(contain statusbar,above statusbar)
                smallFloatWindowParams.type = LayoutParams.TYPE_SYSTEM_ERROR;  
                //the format of picture: transparent background
                smallFloatWindowParams.format = PixelFormat.RGBA_8888;  
                //FLAG_NOT_TOUCH_MODAL or FLAG_NOT_FOCUSABLE or both,and FLAG_LAYOUT_IN_SCREEN
                //if only set FLAG_LAYOUT_IN_SCREEN,the float window's focus will be full screen,the gesture operation outside the float window will be invalid
                smallFloatWindowParams.flags =  LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN;  
                //smallWindowParams.flags =  LayoutParams.FLAG_NOT_TOUCH_MODAL | LayoutParams.FLAG_NOT_FOCUSABLE | LayoutParams.FLAG_LAYOUT_IN_SCREEN
                // | LayoutParams.FLAG_LAYOUT_INSET_DECOR | LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH; 
                smallFloatWindowParams.gravity = Gravity.LEFT | Gravity.TOP;  
                smallFloatWindowParams.width = ViewSmallFloatWindow.viewWidth;  
                smallFloatWindowParams.height = ViewSmallFloatWindow.viewHeight;  
                smallFloatWindowParams.x = 0;  
                smallFloatWindowParams.y = Util.getStatusBarHeight(context);  
            }  
            smallFloatWindow.setParams(smallFloatWindowParams);  
            windowManager.addView(smallFloatWindow, smallFloatWindowParams);  
        }  
    }  
  
    /**
     * <p>Title: removeSmallWindow</p>
     * <p>Description: </p>
     * @param context the app's context
     * @author bubble
     * @date 2015-8-31 下午5:23:08
     */
    public static void removeSmallFloatWindow(Context context) {  
        if (smallFloatWindow != null) {  
            WindowManager windowManager = getWindowManager(context);  
            windowManager.removeView(smallFloatWindow);  
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
    public static void createBigFloatWindow(Context context) {  
        WindowManager windowManager = getWindowManager(context);  
        int screenWidth = windowManager.getDefaultDisplay().getWidth();  
        int screenHeight = windowManager.getDefaultDisplay().getHeight();  
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
            windowManager.addView(bigFloatWindow, bigFloatWindowParams);  
        }  
    }  
  
    /**
     * <p>Title: removeBigWindow</p>
     * <p>Description: </p>
     * @param context the app's context
     * @author bubble
     * @date 2015-8-31 下午5:23:33
     */
    public static void removeBigWindow(Context context) {  
        if (bigFloatWindow != null) {  
            WindowManager windowManager = getWindowManager(context);  
            windowManager.removeView(bigFloatWindow);  
            bigFloatWindow = null;  
        }  
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
     * @param context the app's context
     * @return WindowManager to manage the float window views(add or remove)
     * @author bubble
     * @date 2015-8-31 下午5:26:21
     */
    private static WindowManager getWindowManager(Context context) {  
        if (mWindowManager == null) {  
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        }  
        return mWindowManager;  
    }  
  
    /**
     * <p>Title: updateUsedPercent</p>
     * <p>Description: </p>
     * @param context
     * @author bubble
     * @date 2015-8-31 下午9:32:03
     */
    public static void updateWordClass(Context context) {  
        if (smallFloatWindow != null) {  
            TextView tvWordClass = (TextView) smallFloatWindow.findViewById(R.id.word_float_window_small_textview);  
            tvWordClass.setText(WordsManager.wordClass.toString());  
        }  
    }  
}  