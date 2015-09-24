package com.bubble.simpleword.activity;

import java.io.File;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.PersistableBundle;
import android.text.InputType;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bubble.simpleword.R;
import com.bubble.simpleword.db.MyDbHelper;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.fragment.HomeFragment;
import com.bubble.simpleword.fragment.SettingsFragment;
import com.bubble.simpleword.fragment.SlidingMenuFragment;
import com.bubble.simpleword.fragment.WordBookFragment;
import com.bubble.simpleword.service.ServicePopNotiWord;
import com.bubble.simpleword.service.ServiceUpdateWord;
import com.bubble.simpleword.util.SearchViewFormatter;
import com.bubble.simpleword.util.Util;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-2
 */
public class MainActivity extends SlidingFragmentActivity {
	public static RequestQueue mQueue; 
	
	private FragmentTransaction transaction;
	int spinnerWordSortSelection;
//	static final String KEY_FILE_NAME_SETTINGS = SettingsFragment.KEY_FILE_NAME_SETTINGS;
	private Fragment contentFragment;
	public static SlidingMenu sm;
	private Point size;
	private WindowManager wm;
	private Display display;
	private ActionBar mActionBar;
	
	private SQLiteDatabase db;
	
	public static final String PACKAGE_NAME = "com.bubble.simpleword";
	public static final String BASE_DIRECTORY = File.separator + "data"
			+ Environment.getDataDirectory().getAbsolutePath() + File.separator
			+ PACKAGE_NAME;
	
	public static final String DB_NAME = "simpleword.db"; //the database file's name
	public static final String DB_FOLDER_NAME = "databases";
	public static final String DB_DIRECTORY = BASE_DIRECTORY + File.separator + DB_FOLDER_NAME ;  //the path to save database
    
	public static final String DOWNLOAD_FOLDER_NAME = "download";
    public static final String DOWNLOAD_DIRECTORY = BASE_DIRECTORY + File.separator + DOWNLOAD_FOLDER_NAME ;  //the path to save database
    
    public static WordCls word ;
    
    private SharedPreferences pref;
    private Editor editor;
//    public static final String PREFS_FILE_NAME = "SimpleWord_Prefs_File";  
    public static final String IS_FIRST_START = "is_first_start";  
    public static Boolean isFirstStart;
    
    private Cursor cursor;
    public static int cursorIndex = 0;
    public static final String CURSOR_INDEX = "cursor_index";
    
    private Handler handler = new Handler();

    private MenuInflater inflater;
    
    private MenuItem menuitemSwiOrientation;
    
    private MenuItem menuitemSearch;
    private SearchView searchView;
    private SearchManager searchManager;
    
    private MenuItem menuitemBtnAddWord;
    
    public static String URL_SHANBAY = "https://api.shanbay.com/bdc/search/?word=";
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i("onCreate", "开始");
		
		mQueue = Volley.newRequestQueue(this);
		
		sm = getSlidingMenu();
		initSlidingMenu(savedInstanceState);
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);//show back button
		mActionBar.setDisplayShowHomeEnabled(false);//don't show app's img
		
		loadDatabase();	//load SDcard's database
		
		pref = Util.getSharedPreferences(this);
		editor = pref.edit();

		isFirstStart = pref.getBoolean(IS_FIRST_START, true);
		
//		initSwitch();
		
		Log.i("isFirstStart", Boolean.toString(isFirstStart));
		Log.i("cursorIndex", Integer.toString(cursorIndex));
		Log.i("onCreate", "结束");
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午1:02:15
	 */
	@Override
	protected void onStart() {
		super.onStart();
		Log.i("onStart", "开始");
		Log.i("onStart", "结束");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午10:34:12
	 */
	@Override
	protected void onResume() {
		super.onResume();
		Log.i("onResume", "开始");
//		initSettings();
		

		WordsManager.initDbHelper(this);
		
		if ( isFirstStart ) {
			WordsManager.createInfoTable();
		}
		
		if ( WordsManager.getTableList().size() > 0) {
			initWordsManager(isFirstStart);
			initWordClass();
		}
		

		Log.i("onResume", "结束");
	}
	
	/**
	 * <p>Title: initWordClass</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-31 下午10:36:30
	 */
	private void initWordClass() {
		if ( WordsManager.wordCls == null ) {
			WordsManager.updateWordCls();
		}
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午10:35:07
	 */
	@Override
	protected void onPause() {
		super.onPause();
		Log.i("onPause", "开始");
		Log.i("onPause", "结束");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午10:35:16
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("onRestart", "开始");
		Log.i("onRestart", "结束");
	}
	
	/**
	 * <p>Title: initSettings</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-22 上午10:19:07
	 */
//	private void initSettings() {
//		initWordsDB(isFirstStart);
//	}

	/**
	 * <p>Title: initWords</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6
	 */
	private void initWordsManager(boolean isFirstStart){
		
		if ( isFirstStart ) {
			WordsManager.initWordsManager(this);
			WordsManager.setWordInOrder();	//if it is the first time to start the app, the default mode to get word is "in order"
		} else {
			WordsManager.isInOrder = pref.getBoolean(WordsManager.IS_INORDER, true);
			WordsManager.isReverseOrder = pref.getBoolean(WordsManager.IS_REVERSEORDER, false);
			WordsManager.isRandom = pref.getBoolean(WordsManager.IS_RANDOM, false);
			cursorIndex = pref.getInt(CURSOR_INDEX, 0);
//			initWords(cursorIndex);
			WordsManager.initWordsManager(this, cursorIndex);
		}
	}
	
	/**
	 * <p>Title: initSwitch</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-22 上午10:07:04
	 */
	Intent intentService;
	PendingIntent pendingIntentService;
	int hour;
	int minute;
	int second;
	private void initSwitch() {
		if ( isSwitchOn(SettingsFragment.KEY_SWITCH_POP_NOTI_WORD) ) {
			intentService = new Intent(this, ServicePopNotiWord.class);
			pendingIntentService = PendingIntent.getService(this, 0, intentService, 0);
			hour = Util.getPrefStr2Int(pref, SettingsFragment.KEY_POP_NOTI_WORD_INTERVAL_HOUR, SettingsFragment.INTERVAL_00);
			minute = Util.getPrefStr2Int(pref, SettingsFragment.KEY_POP_NOTI_WORD_INTERVAL_MINUTE, SettingsFragment.INTERVAL_00);
			second = Util.getPrefStr2Int(pref, SettingsFragment.KEY_POP_NOTI_WORD_INTERVAL_SECOND, SettingsFragment.INTERVAL_30);
			startPendingIntent(pendingIntentService, hour, minute, second, 1);
		}
		if ( isSwitchOn(SettingsFragment.KEY_SWITCH_UPDATE_WORD) ) {
			intentService = new Intent(this, ServiceUpdateWord.class);
			pendingIntentService = PendingIntent.getService(this, 0, intentService, 0);
			hour = Util.getPrefStr2Int(pref, SettingsFragment.KEY_UPDATE_WORD_INTERVAL_HOUR, SettingsFragment.INTERVAL_00);
			minute = Util.getPrefStr2Int(pref, SettingsFragment.KEY_UPDATE_WORD_INTERVAL_MINUTE, SettingsFragment.INTERVAL_00);
			second = Util.getPrefStr2Int(pref, SettingsFragment.KEY_UPDATE_WORD_INTERVAL_SECOND, SettingsFragment.INTERVAL_30);
			startPendingIntent(pendingIntentService, hour, minute, second, 0);
		}
	}
	
	
	/**
	 * <p>Title: isSwitchOn</p>
	 * <p>Description: </p>
	 * @param keySwitch
	 * @author bubble
	 * @date 2015-8-22 上午10:27:48
	 */

	private boolean isSwitchOn(String keySwitch) {
		return pref.getBoolean(keySwitch, false);
	}
    public void startPendingIntent(PendingIntent pendingIntent, int hour,int minute, int second,long delayTime) {
		long alarmInterval = DateUtils.HOUR_IN_MILLIS * hour 
    		+ DateUtils.MINUTE_IN_MILLIS * minute
    		+ DateUtils.SECOND_IN_MILLIS * second;
    	
    	long alarmFirstWake = System.currentTimeMillis() + delayTime;
    	AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
    	am.setRepeating(AlarmManager.RTC, alarmFirstWake, alarmInterval, pendingIntent);
    }
	
	/**
	 * <p>Title: initWords</p>
	 * <p>Description: </p>
	 * @param position
	 * @author bubble
	 * @date 2015-8-20
	 */
//	private void initWords(int position){
//		WordsDB.initWordsDB(this, position);
//		}
//	}
	/**
	 * <p>Title: initSlidingMenu</p>
	 * <p>Description: </p>
	 * @param savedInstanceState
	 * @author bubble
	 * @date 2015-8-2
	 */
	private void initSlidingMenu(Bundle savedInstanceState){	
		if (savedInstanceState != null)
			contentFragment = getFragmentManager().getFragment(savedInstanceState, "contentFragment");
		if (contentFragment == null)
			contentFragment = new HomeFragment(this);	
		
		setContentView(R.layout.main_frame_content);
		getFragmentManager().beginTransaction().replace(R.id.frame_content, contentFragment).commit();

		setBehindContentView(R.layout.main_frame_menu);
		getFragmentManager().beginTransaction().replace(R.id.frame_menu, new SlidingMenuFragment()).commit();

		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);	
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setFadeDegree(0);
		sm.setBehindScrollScale(0);
		
		size = new Point();
    	wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
    	display = wm.getDefaultDisplay();
    	display.getSize(size);
    	int slidingWidth = size.x * 2 / 5;
		sm.setBehindWidth(slidingWidth);
	}
	
	/**
	 * <p>Title: setSlidingAboveMode</p>
	 * <p>Description: </p>
	 * @param mode
	 * @author bubble
	 * @date 2015-9-10 下午3:29:50
	 */
	public static void setSlidingAboveMode(int mode) {
		sm.setTouchModeAbove(mode);
	}
	
	/**
	 * <p>Title: loadDatabase</p>
	 * <p>Description: load SDcard's database</p>
	 * @author bubble
	 * @date 2015-8-6 下午11:52:05
	 */
	public void loadDatabase(){
		MyDbHelper.loadDatabase(this, DB_DIRECTORY + "/" + DB_NAME);
	}
	
	/**
	 * <p>Title: switchContent</p>
	 * <p>Description: switch content's fragment</p>
	 * @param fragment
	 * @author bubble
	 * @date 2015-8-5
	 */
//	public void switchContent(Fragment fragment) {
//		contentFragment = fragment;
//		getFragmentManager().beginTransaction().replace(R.id.content_frame, fragment).commit();
////		sm.showContent();
//        Handler h = new Handler();
//        h.postDelayed(new Runnable() {
//            public void run() {
//            	sm.showContent();
//            }
//        }, 50);
//	}
	
	/**
	 * <p>Title: switchContent</p>
	 * <p>Description: </p>
	 * @param contentFragment
	 * @param newFragment
	 * @author bubble
	 * @date 2015-9-15 下午2:08:22
	 */
	public void switchContent(Fragment newFragment) {
		if ( contentFragment == null || newFragment == null)
			return;
		
   	    transaction = getFragmentManager().beginTransaction();
   	    
   	    if (contentFragment != newFragment) {
	   	    if (!newFragment.isAdded()) {
	   	        // 隐藏当前的fragment，add下一个到Activity中
	   	        transaction.hide(contentFragment).add(R.id.frame_content,newFragment).commit();
	   	    } else {
	   	       // 隐藏当前的fragment，显示下一个
	    		transaction.hide(contentFragment).show(newFragment).commit();
	   	    }
	   	    contentFragment = newFragment;
	   	    
	   	    if ( contentFragment == SlidingMenuFragment.wordBookFragment) {
	   	    	menuitemSwiOrientation.setVisible(true);
	   	    	menuitemBtnAddWord.setVisible(true);
	   	    }
	   	    else {
	   	    	menuitemSwiOrientation.setVisible(false);
	   	    	menuitemBtnAddWord.setVisible(false);
	   	    	setSlidingAboveMode(SlidingMenu.TOUCHMODE_FULLSCREEN);
	   	    }
	   	    	
   	    }
   	    
   	    handler.post(new Runnable() {
			
			@Override
			public void run() {
				sm.showContent();
			}
		});
   	    
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();
			return true;	
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-2
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getFragmentManager().putFragment(outState, "contentFragment", contentFragment);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-6
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch (keyCode) {
		//press smartphone's "Menu" button to open and close the slidingmenu
		case KeyEvent.KEYCODE_MENU:
			sm.toggle();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-19 下午9:19:42
	 */
	@Override
	protected void onStop() {
		super.onStop();
		Log.i("onStop", "开始");
		if ( isFirstStart ) {
			editor.putBoolean(IS_FIRST_START, false);
			editor.commit();
		}
		cursorIndex = WordsManager.getCursorPosition();
		editor.putInt(CURSOR_INDEX, cursorIndex);
		editor.putBoolean(WordsManager.IS_INORDER, WordsManager.isInOrder);
		editor.putBoolean(WordsManager.IS_REVERSEORDER, WordsManager.isReverseOrder);
		editor.putBoolean(WordsManager.IS_RANDOM, WordsManager.isRandom);
		editor.commit();
		Log.i("cursorIndex", Integer.toString(cursorIndex));
		Log.i("onStop", "结束");
	}

	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 上午00:52:05
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("onDestroy", "开始");
		Log.i("onDestroy", "结束");
	}
	
	/**
	 * <p>Description: save temporary data</p>
	 * @author bubble
	 * @date 2015-8-20 下午12:58:39
	 */
	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		super.onSaveInstanceState(outState, outPersistentState);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-17 下午9:46:43
	 */
	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		inflater = getMenuInflater();
	    inflater.inflate(R.menu.actionbar_menu, menu);
	    
	    menuitemSearch = menu.findItem(R.id.actionbar_menu_search);
        searchView = (SearchView) menuitemSearch.getActionView();
	    
	    // Associate searchable configuration with the SearchView
	    searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
//	    searchView = (SearchView) menu.findItem(R.id.actionbar_item_search).getActionView();
	    searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

	    //设置该SearchView默认是否自动缩小为图标
        searchView.setIconifiedByDefault(true);
        
        //设置该SearchView显示搜索按钮
        searchView.setSubmitButtonEnabled(false);
        
        new SearchViewFormatter()
        .setSearchBackGroundResource(R.drawable.corners_bg)
        .setSearchIconResource(R.drawable.search, true, true) //true to icon inside edittext, false to outside
        .setSearchCloseIconResource(R.drawable.clear)
        .setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS)
        .setSearchTextColorResource(R.color.white)
        .setSearchHintColorResource(R.color.gray_light_text)
        .setSearchHintText("请输入单词")
        .setCursorResource(R.drawable.cursor)
        .format(searchView);
        
        
        //为该SearchView组件设置事件监听器
        searchView.setOnQueryTextListener(new OnQueryTextListener() {
			
			@Override
			public boolean onQueryTextSubmit(String query) {
//				// 除了输入查询的值，还可额外绑定一些数据
//				Bundle appSearchData = new Bundle();
//				appSearchData.putString("KEY", "text");
//				
//				startSearch(null, false, appSearchData, false);
				Intent intent = new Intent();  
		        intent.setClass(MainActivity.this, SearchResultsActivity.class);  
		        Bundle mBundle = new Bundle();  
		        mBundle.putString("query", query);//压入数据  
		        intent.putExtras(mBundle);  
		        startActivity(intent);  
		        return true;
			}
			
			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});
        
        menuitemBtnAddWord = menu.findItem(R.id.actionbar_menu_wordbook_add_word);
        menuitemBtnAddWord.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				WordBookFragment.showEditWordDialog(0, word, WordBookFragment.TYPE_ADD_WORD);
				return false;
			}
		});
        menuitemBtnAddWord.setVisible(false);
        
        menuitemSwiOrientation = menu.findItem(R.id.actionbar_menu_wordbook_switch);
	    menuitemSwiOrientation.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (WordBookFragment.getItemViewType()) {
				case WordBookFragment.VIEW_TYPE_HORIZON:
					WordBookFragment.setRecyclerViewWordType(WordBookFragment.VIEW_TYPE_VERTICAL);
					break;
				case WordBookFragment.VIEW_TYPE_VERTICAL:
					WordBookFragment.setRecyclerViewWordType(WordBookFragment.VIEW_TYPE_HORIZON);
				}
				return true;
			}
		});
	    
	    menuitemSwiOrientation.setVisible(false);
	    
	    return true;
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble 
	 * @date 2015-9-21 下午3:33:23
	 */
	@Override
	public void onBackPressed() {
		if ( searchView != null ) {
            if ( ! closeSearchView() ) {
                super.onBackPressed();
            }
        }
	}

	/**
	 * <p>Title: closeSearchView</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-9-21 下午3:52:12
	 */
	private boolean closeSearchView() {
		if ( searchView != null ) {
		    if ( ! searchView.isIconified() ) {
		        searchView.setIconified(true);
		        return true;
		    }
		}
		return false;
	}
	
//	public void setupUI(View view) {
//
//	    if(!(view instanceof SearchView)) {
//
//	        view.setOnTouchListener(new OnTouchListener() {
//
//	            public boolean onTouch(View v, MotionEvent event) {
//	                searchMenuItem.collapseActionView();
//	                return false;
//	            }
//
//	        });
//	    }
//
//	    //If a layout container, iterate over children and seed recursion.
//	    if (view instanceof ViewGroup) {
//
//	        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//
//	            View innerView = ((ViewGroup) view).getChildAt(i);
//
//	            setupUI(innerView);
//	        }
//	    }
//	}
	
	/**
	 * <p>Title: onOptionsItemSelected</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-17 下午11:24:27
	 */
	private void onOptionsItemSelected() {
	}
}
