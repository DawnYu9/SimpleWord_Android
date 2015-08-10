package com.bubble.simpleword;

import java.io.File;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.WindowManager;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.DBManager;
import com.bubble.simpleword.menu.MainFragment;
import com.bubble.simpleword.menu.MenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-2
 */
public class MainActivity extends SlidingFragmentActivity {
	public DBManager dbManager;
	private Fragment contentFragment;
	SlidingMenu sm;
	
	SQLiteDatabase db;
	public static final String DB_NAME = "simpleword.db"; //保存的数据库文件名
    public static final String PACKAGE_NAME = "com.bubble.simpleword";
    public static final String FOLDER_NAME = "databases";
    public static final String DB_PATH = File.separator + "data"
            + Environment.getDataDirectory().getAbsolutePath() + File.separator
            + PACKAGE_NAME + File.separator
            + FOLDER_NAME ;  //在手机里存放数据库的位置
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sm = getSlidingMenu();
		initSlidingMenu(savedInstanceState);
		
		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);//显示返回按钮
		mActionBar.setDisplayShowHomeEnabled(false);//不显示应用图标
		
		loadDatabase();	//加载SD卡数据库
		
	}

	private void initSlidingMenu(Bundle savedInstanceState){	
		if (savedInstanceState != null)
			contentFragment = getFragmentManager().getFragment(savedInstanceState, "contentFragment");
		if (contentFragment == null)
			contentFragment = new MainFragment(this);	
		
		setContentView(R.layout.frame_content);
		getFragmentManager().beginTransaction().replace(R.id.content_frame, contentFragment).commit();

		setBehindContentView(R.layout.frame_menu);
		getFragmentManager().beginTransaction().replace(R.id.menu_frame, new MenuFragment()).commit();

		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setShadowWidthRes(R.dimen.shadow_width);	
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setFadeDegree(0);
		sm.setBehindScrollScale(0);
		
		Point size = new Point();
    	WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
    	Display display = wm.getDefaultDisplay();
    	display.getSize(size);
    	int slidingWidth = size.x * 2 / 5;
		sm.setBehindWidth(slidingWidth);
		
	}
	/**
	 * <p>Title: loadDatabase</p>
	 * <p>Description: 加载SD卡数据库</p>
	 * @author bubble
	 * @date 2015-8-6 下午11:52:05
	 */
	public void loadDatabase(){
		dbManager = new DBManager(this);
		db = dbManager.getDatabase(DB_PATH + "/" + DB_NAME);
        dbManager.closeDatabase(db);
	}
	
	/**
	 * <p>Title: switchContent</p>
	 * <p>Description: 切换fragment</p>
	 * @param fragment
	 * @author bubble
	 * @date 2015-8-5
	 */
	public void switchContent(Fragment fragment) {
		contentFragment = fragment;
		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
//		sm.showContent();
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            public void run() {
            	sm.showContent();
            }
        }, 50);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getFragmentManager().putFragment(outState, "contentFragment", contentFragment);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//按下手机Menu键弹出和关闭侧滑菜单
		if(keyCode==KeyEvent.KEYCODE_MENU){
			sm.toggle();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
