package com.bubble.simpleword.menu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Switch;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsDB;
import com.bubble.simpleword.service.ServiceNotification;

/**
 * <p>Title: SettingsFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-5
 */
public class SettingsFragment extends Fragment {
	Context mContext;
	Activity mActivity;

	SharedPreferences prefSettings;
	SharedPreferences.Editor prefEditorSettings;
	public static final String SETTINGS_FILE_NAME = "SimpleWord_Settings_File";
	public static final String SPINNER_SELECTION_WODE_MODE = "spinner_wode_mode_selected";
	
	Intent intentService;
	PendingIntent pendingIntentService;
	
	/**
	 * spinner selected position
	 */
	int spinnerSelection;
	
	//“开启通知栏单词”开关
	Switch switchNotiWord;
	boolean isSwitchOnNotiWord;
	public static final String SWITCH_NOTI_WORD = "switch_noti_word";
	Intent intentNotiService;
	PendingIntent pendingIntentNotiService;
	
	//“自动更新单词”开关
	Switch switchUpdateWord;
	boolean isSwitchOnUpdateWord;
	public static final String SWITCH_UPDATE_WORD = "switch_update_word";
	Intent intentUpdateWordService;
	PendingIntent pendingIntentUpdateWordService;
	
	//“悬浮窗单词”开关
	Intent intentFloatWindowService;
	PendingIntent pendingIntentFloatWindowService;
	
	private static AlarmManager am;
    
    
    /**
     * the interval of updating data regularly
     */
    int minute = 1;
    long interval;
    long firstWake;
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public SettingsFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.menu_item_fg_settings,container, false);  
		
		mContext = getActivity();
		mActivity = getActivity();
		mActivity.setTitle(R.string.settings);
		mActivity.getActionBar().setDisplayShowCustomEnabled(false);
		
		prefSettings = mActivity.getSharedPreferences(SETTINGS_FILE_NAME, Context.MODE_PRIVATE);
    	prefEditorSettings = prefSettings.edit();
    	
		initSpinnerWordSort(view);
		
		initSwitchNotiWord(view);
		
		initSwitchUpdateWord(view);
		
		return view; 
	}
	/**
	 * <p>Title: initSpinnerWordSort</p>
	 * <p>Description: </p>
	 * @param view
	 * @author bubble
	 * @date 2015-8-20 下午10:57:48
	 */
	private void initSpinnerWordSort(View view) {
		Spinner spinner = (Spinner)view.findViewById(R.id.setting_spinner_word_sort_mode);
		String[] mItems = getResources().getStringArray(R.array.word_get_mode);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(mActivity,android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//绑定 Adapter到控件
		spinner .setAdapter(adapter);
		
		spinnerSelection = prefSettings.getInt(SPINNER_SELECTION_WODE_MODE, 0);
		if ( spinnerSelection != 0 )
			spinner.setSelection(spinnerSelection);
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		    @Override
		    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		    	String str= parent.getItemAtPosition(position).toString();		        
		    	switch (str){
		    	case "正序":
	    			WordsDB.setWordInOrder();
		    		break;
		    	case "逆序":
	    			WordsDB.setWordReverseOrder();
		    		break;
		    	case "随机":
	    			WordsDB.setWordRandom();
		    		break;
		    	default:
		    		break;
		    	}
		    	
		    	prefEditorSettings.putInt(SPINNER_SELECTION_WODE_MODE, position);
		    	prefEditorSettings.commit();
		    	Log.i("MODE_GET_WORD", Integer.toString(WordsDB.MODE_GET_WORD));
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        // Another interface callback
		    }
		});
	}
	
	/**
	 * <p>Title: initSwitchNotiWord</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 下午11:02:54
	 */
	private void initSwitchNotiWord(View view) {
		switchNotiWord = (Switch)view.findViewById(R.id.setting_switch_noti_word); 
		
		isSwitchOnNotiWord = prefSettings.getBoolean(SWITCH_NOTI_WORD, false);
		switchNotiWord.setChecked(isSwitchOnNotiWord);
		
		switchNotiWord.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                	// switch on，开启通知栏单词
                	prefEditorSettings.putBoolean(SWITCH_NOTI_WORD, true);
                	intentNotiService = new Intent(mContext, ServiceNotification.class);
                	pendingIntentNotiService = PendingIntent.getService(mContext, 0, intentNotiService, 0);
                	startPendingIntent(pendingIntentNotiService);
                } else {
                	//switch off，关闭通知栏单词
                	prefEditorSettings.putBoolean(SWITCH_NOTI_WORD, false);
                	stopPendingIntent(pendingIntentNotiService);
                }
                prefEditorSettings.commit();
            }
        });
	}

	/**
	 * <p>Title: initSwitchUpdateWord</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-21 下午3:43:54
	 */
	private void initSwitchUpdateWord(View view) {
		switchUpdateWord = (Switch)view.findViewById(R.id.setting_switch_update_word);
		//测试：通知栏单词
		initSwitch(switchNotiWord, SWITCH_NOTI_WORD, isSwitchOnNotiWord, ServiceNotification.class);
	}

	/**
	 * <p>Title: initSwitch</p>
	 * <p>Description: </p>
	 * @param s Switch
	 * @param prefString the name of the key preference to retrieve.
	 * @param isSwitchOn the status of the switch:true is on,false is off
	 * @param cls the class to intent to,eg:MainActivity.class
	 * @author bubble
	 * @date 2015-8-21 下午4:06:46
	 */
	private void initSwitch(Switch s, final String prefString, boolean isSwitchOn, final Class<?> cls) {
		isSwitchOn = prefSettings.getBoolean(prefString, false);
		s.setChecked(isSwitchOn);
		
		s.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {
                	// switch on
                	prefEditorSettings.putBoolean(prefString, true);
                	intentService = new Intent(mContext, cls);
                	pendingIntentService = PendingIntent.getService(mContext, 0, intentService, 0);
                	startPendingIntent(pendingIntentService);
                } else {
                	//switch off
                	prefEditorSettings.putBoolean(prefString, false);
                	stopPendingIntent(pendingIntentService);
                }
                prefEditorSettings.commit();
            }
        });
	}
	
	/**
     * <p>Title: startPendingIntent</p>
     * <p>Description: use AlarmManager to start periodic service</p>
     * @param pendingIntent
     * @author bubble
     * @date 2015-8-21 
     */
    public void startPendingIntent(PendingIntent pendingIntent) {
    	
    	am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    	
    	interval = DateUtils.MINUTE_IN_MILLIS * minute;// minutes分钟一次
    	
    	firstWake = System.currentTimeMillis() + interval;
    	
    	am.setRepeating(AlarmManager.RTC, firstWake, interval, pendingIntent);
    	
    }

    /**
     * <p>Title: stopPendingIntent</p>
     * <p>Description: to stop periodic service</p>
     * @param pendingIntent
     * @author bubble
     * @date 2015-8-21
     */
    public static void stopPendingIntent(PendingIntent pendingIntent) {

        if (pendingIntent != null) {

            pendingIntent.cancel();

        }
    }
}
