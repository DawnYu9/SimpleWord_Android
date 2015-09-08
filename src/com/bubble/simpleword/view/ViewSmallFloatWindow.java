package com.bubble.simpleword.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsManager;

/**
 * <p>Title: FloatWindowSmallView</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-30 下午1:00:37
 */
public class ViewSmallFloatWindow extends LinearLayout implements OnTouchListener{
	private Context mContext; 
	 /** 
     * 记录小悬浮窗的宽度 
     */  
    public static int viewWidth;  
  
    /** 
     * 记录小悬浮窗的高度 
     */  
    public static int viewHeight;  
  
    /** 
     * 用于更新小悬浮窗的位置 
     */  
    private WindowManager windowManager;  
  
    /** 
     * 小悬浮窗的参数 
     */  
    private WindowManager.LayoutParams mParams;  
  
    /** 
     * 记录当前手指位置在屏幕上的横坐标值 
     */  
    private float xInScreen;  
  
    /** 
     * 记录当前手指位置在屏幕上的纵坐标值 
     */  
    private float yInScreen;  
  
    /** 
     * 记录手指按下时在屏幕上的横坐标的值 
     */  
    private float xDownInScreen;  
  
    /** 
     * 记录手指按下时在屏幕上的纵坐标的值 
     */  
    private float yDownInScreen;  
  
    /** 
     * 记录手指按下时在小悬浮窗的View上的横坐标的值 
     */  
    private float xInView;  
  
    /** 
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值 
     */  
    private float yInView;  
  
    public ViewSmallFloatWindow(Context context) {  
        super(context);  
        mContext = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);  
        LayoutInflater.from(context).inflate(R.layout.word_float_window_small, this);  
        View view = findViewById(R.id.word_small_float_window_layout);
        view.setOnTouchListener(this);
        
        viewWidth = view.getLayoutParams().width;  
        viewHeight = view.getLayoutParams().height;  
        
        
        ImageButton btnPlay = (ImageButton) findViewById(R.id.word_small_float_window_play_btn);
        btnPlay.setOnTouchListener(this);
        
        TextView tvWordCls = (TextView) findViewById(R.id.word_small_float_window_tv);  
        tvWordCls.setText(WordsManager.wordCls.getSpannedHtml());  
    }  
  
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-7 下午9:40:51
	 */
	@Override
	public boolean onTouch(View v, MotionEvent ev) {
		switch (ev.getAction()) {  
        case MotionEvent.ACTION_DOWN:  
            xInView = ev.getX();  
            yInView = ev.getY();  
            xDownInScreen = ev.getRawX();  
            yDownInScreen = ev.getRawY();  
            xInScreen = ev.getRawX();  
            yInScreen = ev.getRawY();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            xInScreen = ev.getRawX();  
            yInScreen = ev.getRawY();  
            updateViewPosition();  
            break;  
        case MotionEvent.ACTION_UP:  
            if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) { 
            	switch (v.getId()) {
				case R.id.word_small_float_window_layout:
					Log.d(VIEW_LOG_TAG, "点击了text");
					openBigFloatWindow();  
					break;
				case R.id.word_small_float_window_play_btn:
					Log.d(VIEW_LOG_TAG, "点击了btn");
					break;
				default:
					break;
				}
            }  
            break;  
        default:  
            break;  
        }
		return true;
	}  
	
    /** 
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。 
     *  
     * @param params 
     *            小悬浮窗的参数 
     */  
    public void setParams(WindowManager.LayoutParams params) {  
        mParams = params;  
    }  
  
    /** 
     * 更新小悬浮窗在屏幕中的位置。 
     */  
    private void updateViewPosition() {  
        mParams.x = (int) (xInScreen - xInView);  
        mParams.y = (int) (yInScreen - yInView);  
        windowManager.updateViewLayout(this, mParams);  
    }  
  
    /** 
     * 打开大悬浮窗，同时关闭小悬浮窗。 
     */  
    private void openBigFloatWindow() {  
        MyWindowManager.createBigFloatWord(getContext());  
        MyWindowManager.removeSmallFloatWord(getContext());  
    }

}
