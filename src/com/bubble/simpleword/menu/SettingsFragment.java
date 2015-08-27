package com.bubble.simpleword.menu;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsDB;
import com.bubble.simpleword.service.ServicePopNotiWord;
import com.bubble.simpleword.service.ServiceUpdateWord;
import com.bubble.simpleword.util.Util;

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
	/**
	 * 临时存储EditText的内容
	 * temporarily store EditText's content
	 */
	String edtString;
	/**
	 * temporarily store widget's tag ,and also the key when use SharedPreferences
	 */
	String tag;
	/**
	 * temporarily store a boolean value
	 */
	boolean b;
	
	InputMethodManager inputMethodManager;
	
	Context mContext;
	Activity mActivity;
	View mView;
	
	public static SharedPreferences prefSettings;
	SharedPreferences.Editor prefEditorSettings;
	public static final String KEY_FILE_NAME_SETTINGS = "SimpleWord_Settings_File";
	
	Intent intent;
	PendingIntent pendingIntent;
	boolean isSwitchOn = false;
	
	/** widgets **/
	
	//Spinner : "select the word's sort"
	int spinnerWordSortSelection;
	Spinner spinnerWordsort;
	public static final String KEY_SPINNER_SELECTION_WODE_MODE = "KEY_SPINNER_SELECTION_WODE_MODE";

	//Switch : "display word in status bar's notification"
	public static Switch switchPopNotiWord;
	boolean isSwitchOnNotiWord;
	public static final String KEY_SWITCH_POP_NOTI_WORD = "KEY_SWITCH_POP_NOTI_WORD";
	Intent intentServicePopNotiWord;
//	PendingIntent pendingIntentNotiService;
	
	//EditTexts : "the interval to pop the notification word"
	EditText edtPopNotiWordIntervalHour;
	EditText edtPopNotiWordIntervalMinute;
	EditText edtPopNotiWordIntervalSecond;
	int intPopNotiWordIntervalHour = 0;
	int intPopNotiWordIntervalMinute = 0;
	int intPopNotiWordIntervalSecond = 30;
	public static final String KEY_POP_NOTI_WORD_INTERVAL_HOUR = "KEY_POP_NOTI_WORD_INTERVAL_HOUR";
	public static final String KEY_POP_NOTI_WORD_INTERVAL_MINUTE = "KEY_POP_NOTI_WORD_INTERVAL_MINUTE";
	public static final String KEY_POP_NOTI_WORD_INTERVAL_SECOND = "KEY_POP_NOTI_WORD_INTERVAL_SECOND";
	
	//Switch : "auto update word"
	public static Switch switchUpdateWord;
	public static final String KEY_SWITCH_UPDATE_WORD = "KEY_SWITCH_UPDATE_WORD";
	Intent intentServiceUpdateWord;
//	PendingIntent pendingIntentUpdateWordService;
	
	
	//EditTexts : "the interval of auto updating word" 
	EditText edtUpdateWordIntervalHour;
	EditText edtUpdateWordIntervalMinute;
	EditText edtUpdateWordIntervalSecond;
	int intUpdateWordIntervalHour = 0;
	int intUpdateWordIntervalMinute = 0;
	int intUpdateWordIntervalSecond = 30;
	public static final String KEY_UPDATE_WORD_INTERVAL_HOUR = "KEY_UPDATE_WORD_INTERVAL_HOUR";
	public static final String KEY_UPDATE_WORD_INTERVAL_MINUTE = "KEY_UPDATE_WORD_INTERVAL_MINUTE";
	public static final String KEY_UPDATE_WORD_INTERVAL_SECOND = "KEY_UPDATE_WORD_INTERVAL_SECOND";
	
	//default auto pop/update interval's value
	public static final String INTERVAL_00 = "00";
	public static final String INTERVAL_30 = "30";
	
	private static AlarmManager am;
	/**
	 * the interval of updating data regularly
	 */
	long alarmInterval;
	long alarmFirstWake;
	
	//Switch : "display word in float window"
	Intent intentFloatWindowService;
	PendingIntent pendingIntentFloatWindowService;
	

    
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public SettingsFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mView=inflater.inflate(R.layout.menu_item_fg_settings,container, false);  
		
		mContext = getActivity();
		mActivity = getActivity();
		mActivity.setTitle(R.string.settings);
		mActivity.getActionBar().setDisplayShowCustomEnabled(false);
		
		prefSettings = mActivity.getSharedPreferences(KEY_FILE_NAME_SETTINGS, Context.MODE_PRIVATE);
    	prefEditorSettings = prefSettings.edit();
    	
		return mView; 
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-22 上午10:09:52
	 */
	@Override
	public void onResume() {
		super.onResume();
		
		initSpinnerWordSort(mView);
		
		initSwitchPopNotiWord(mView);
		
		initSwitchUpdateWord(mView);
		
	}
	
	/**
	 * <p>Title: initSpinnerWordSort</p>
	 * <p>Description: </p>
	 * @param view
	 * @author bubble
	 * @date 2015-8-20 下午10:57:48
	 */
	private void initSpinnerWordSort(View view) {
		spinnerWordsort = (Spinner)view.findViewById(R.id.setting_spinner_word_sort_mode);
		String[] mItems = getResources().getStringArray(R.array.word_get_mode);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(mActivity,android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//绑定 Adapter到控件
		spinnerWordsort .setAdapter(adapter);
		
		spinnerWordSortSelection = prefSettings.getInt(KEY_SPINNER_SELECTION_WODE_MODE, 0);
		if ( spinnerWordSortSelection != 0 )
			spinnerWordsort.setSelection(spinnerWordSortSelection);
		
		spinnerWordsort.setOnItemSelectedListener(new OnItemSelectedListener() {
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
		    	
		    	prefEditorSettings.putInt(KEY_SPINNER_SELECTION_WODE_MODE, position);
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
	private void initSwitchPopNotiWord(View view) {
		switchPopNotiWord = (Switch)view.findViewById(R.id.setting_switch_pop_noti_word); 
		switchPopNotiWord.setTag(KEY_SWITCH_POP_NOTI_WORD);

		initIntervalPopNotiWord(mView);
		
		switchPopNotiWord = initSwitch(switchPopNotiWord, KEY_SWITCH_POP_NOTI_WORD, ServicePopNotiWord.class);
	}

	
	/**
	 * <p>Title: initSwitchUpdateWord</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-21 下午3:43:54
	 */
	private void initSwitchUpdateWord(View view) {
		switchUpdateWord = (Switch)view.findViewById(R.id.setting_switch_update_word);
		switchUpdateWord.setTag(KEY_SWITCH_UPDATE_WORD);
		
		initIntervalUpdateWord(mView);
		
		switchUpdateWord = initSwitch(switchUpdateWord, KEY_SWITCH_UPDATE_WORD, ServiceUpdateWord.class);
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
	private Switch initSwitch(final Switch s, final String prefString, final Class<?> cls) {
		isSwitchOn = prefSettings.getBoolean(prefString, false);
		s.setChecked(isSwitchOn);
		
		s.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
            	intent = new Intent(mContext, cls);
            	pendingIntent = PendingIntent.getService(mContext, 0, intent, 0);
            	tag = s.getTag().toString();
                if (isChecked) {
                	prefEditorSettings.putBoolean(prefString, true);
                	prefEditorSettings.commit();
                	if ( s.getTag() != null ) {
	                	switch (tag) {
	                	case KEY_SWITCH_POP_NOTI_WORD:
	                		intentServicePopNotiWord = intent;
	                		setEdtIntervalEditable(tag);
	                		startPendingIntent(tag,pendingIntent, 1);
	                		break;
	                	case KEY_SWITCH_UPDATE_WORD:
	                		intentServiceUpdateWord = intent;
	                		setEdtIntervalEditable(tag);
	                		startPendingIntent(tag,pendingIntent,0);//update should before others
	                		break;
	                	default:
	                		break;
	                	}
                	}
                } else {
                	if ( s.getTag() != null ) {
	                	switch (tag) {
	                	case KEY_SWITCH_POP_NOTI_WORD:
//	                		prefEditorSettings.putBoolean(prefString, false);
//	                		prefEditorSettings.commit();
//	                		setEdtIntervalEnabled(tag);
//	                		stopPendingIntent(pendingIntentService);
//	                		break;
	                	case KEY_SWITCH_UPDATE_WORD:
	                		prefEditorSettings.putBoolean(prefString, false);
	                		prefEditorSettings.commit();
	                		setEdtIntervalEditable(tag);
	                		stopPendingIntent(pendingIntent,prefString);
	                		break;
	                	default:
	                		break;
	                	}
                	}
                }
                
                
            }
        });
		return s;
	}
	
	/**
     * <p>Title: startPendingIntent</p>
     * <p>Description: use AlarmManager to start periodic service</p>
     * @param pendingIntent
     * @author bubble
     * @date 2015-8-21 
     */
    private void startPendingIntent(String keySwitch,PendingIntent pendingIntent, long delayTime) {
    	switch (keySwitch) {
    	case KEY_SWITCH_POP_NOTI_WORD:
    		alarmInterval = DateUtils.HOUR_IN_MILLIS * intPopNotiWordIntervalHour 
				+ DateUtils.MINUTE_IN_MILLIS * intPopNotiWordIntervalMinute
				+ DateUtils.SECOND_IN_MILLIS * intPopNotiWordIntervalSecond;
    		break;
    	case KEY_SWITCH_UPDATE_WORD:
    		alarmInterval = DateUtils.HOUR_IN_MILLIS * intUpdateWordIntervalHour 
				+ DateUtils.MINUTE_IN_MILLIS * intUpdateWordIntervalMinute
				+ DateUtils.SECOND_IN_MILLIS * intUpdateWordIntervalSecond;
    		break;
    	}
    	
    	Log.d("alarmInterval", Integer.toString((int)alarmInterval));
    	alarmFirstWake = System.currentTimeMillis() + delayTime;
    	if ( am == null ) {
			am = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
		}
    	am.setRepeating(AlarmManager.RTC, alarmFirstWake, alarmInterval, pendingIntent);
    }


    /**
     * <p>Title: stopPendingIntent</p>
     * <p>Description: to stop periodic service</p>
     * @param pendingIntent
     * @author bubble
     * @date 2015-8-21
     */
    public void stopPendingIntent(PendingIntent pendingIntent, String keySwitch) {
        if (pendingIntent != null) {
            pendingIntent.cancel();
        }
        switch (keySwitch) {
        case KEY_SWITCH_POP_NOTI_WORD:
        	if ( intentServicePopNotiWord != null)
        		mContext.stopService(intentServicePopNotiWord);
        	break;
        case KEY_SWITCH_UPDATE_WORD:
        	if ( intentServiceUpdateWord != null)
        		mContext.stopService(intentServiceUpdateWord);
        	break;
        }
    }

	/**
	 * <p>Title: initEdtTime</p>
	 * <p>Description: </p>
	 * @param
	 * @author bubble
	 * @date 2015-8-22 上午11:34:20
	 */
	private void initIntervalUpdateWord(View view) {
		edtUpdateWordIntervalHour = (EditText)view.findViewById(R.id.setting_edittext_update_word_hour);
		edtUpdateWordIntervalMinute = (EditText)view.findViewById(R.id.setting_edittext_update_word_minute);
		edtUpdateWordIntervalSecond = (EditText)view.findViewById(R.id.setting_edittext_update_word_second);
		
		edtUpdateWordIntervalHour.setTag(KEY_UPDATE_WORD_INTERVAL_HOUR);
		edtUpdateWordIntervalMinute.setTag(KEY_UPDATE_WORD_INTERVAL_MINUTE);
		edtUpdateWordIntervalSecond.setTag(KEY_UPDATE_WORD_INTERVAL_SECOND);
		
		intUpdateWordIntervalHour = Util.getPrefStr2Int(prefSettings, KEY_UPDATE_WORD_INTERVAL_HOUR, INTERVAL_00);
		intUpdateWordIntervalMinute = Util.getPrefStr2Int(prefSettings, KEY_UPDATE_WORD_INTERVAL_MINUTE, INTERVAL_00);
		intUpdateWordIntervalSecond = Util.getPrefStr2Int(prefSettings, KEY_UPDATE_WORD_INTERVAL_SECOND, INTERVAL_30);

		
		edtUpdateWordIntervalHour.setText(prefSettings.getString(KEY_UPDATE_WORD_INTERVAL_HOUR, INTERVAL_00));
		edtUpdateWordIntervalMinute.setText(prefSettings.getString(KEY_UPDATE_WORD_INTERVAL_MINUTE, INTERVAL_00));
		edtUpdateWordIntervalSecond.setText(prefSettings.getString(KEY_UPDATE_WORD_INTERVAL_SECOND, INTERVAL_30));
		
		setEdtIntervalEditable(KEY_SWITCH_UPDATE_WORD);
		
		setEdtIntervalListener(edtUpdateWordIntervalHour);
		setEdtIntervalListener(edtUpdateWordIntervalMinute);
		setEdtIntervalListener(edtUpdateWordIntervalSecond);
	}
	
	private void initIntervalPopNotiWord(View view) {
		edtPopNotiWordIntervalHour = (EditText)view.findViewById(R.id.setting_edittext_pop_noti_word_hour);
		edtPopNotiWordIntervalMinute = (EditText)view.findViewById(R.id.setting_edittext_pop_noti_word_minute);
		edtPopNotiWordIntervalSecond = (EditText)view.findViewById(R.id.setting_edittext_pop_noti_word_second);
		
		edtPopNotiWordIntervalHour.setTag(KEY_POP_NOTI_WORD_INTERVAL_HOUR);
		edtPopNotiWordIntervalMinute.setTag(KEY_POP_NOTI_WORD_INTERVAL_MINUTE);
		edtPopNotiWordIntervalSecond.setTag(KEY_POP_NOTI_WORD_INTERVAL_SECOND);
		
		intPopNotiWordIntervalHour = Util.getPrefStr2Int(prefSettings, KEY_POP_NOTI_WORD_INTERVAL_HOUR, INTERVAL_00);
		intPopNotiWordIntervalMinute = Util.getPrefStr2Int(prefSettings, KEY_POP_NOTI_WORD_INTERVAL_MINUTE, INTERVAL_00);
		intPopNotiWordIntervalSecond = Util.getPrefStr2Int(prefSettings, KEY_POP_NOTI_WORD_INTERVAL_SECOND, INTERVAL_30);

		edtPopNotiWordIntervalHour.setText(prefSettings.getString(KEY_POP_NOTI_WORD_INTERVAL_HOUR, INTERVAL_00));
		edtPopNotiWordIntervalMinute.setText(prefSettings.getString(KEY_POP_NOTI_WORD_INTERVAL_MINUTE, INTERVAL_00));
		edtPopNotiWordIntervalSecond.setText(prefSettings.getString(KEY_POP_NOTI_WORD_INTERVAL_SECOND, INTERVAL_30));
		
		setEdtIntervalEditable(KEY_SWITCH_POP_NOTI_WORD);
		
		setEdtIntervalListener(edtPopNotiWordIntervalHour);
		setEdtIntervalListener(edtPopNotiWordIntervalMinute);
		setEdtIntervalListener(edtPopNotiWordIntervalSecond);
	}
	/**
	 * <p>Title: isEdtIntervalEnabled</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-22 下午12:03:20
	 */
	private void setEdtIntervalEditable(String keySwitch) {
		switch (keySwitch) {
		case KEY_SWITCH_POP_NOTI_WORD:
			if ( ! prefSettings.getBoolean(keySwitch, false) ) {
				edtPopNotiWordIntervalHour.setEnabled(false);
				edtPopNotiWordIntervalMinute.setEnabled(false);
				edtPopNotiWordIntervalSecond.setEnabled(false);
			} else {
				edtPopNotiWordIntervalHour.setEnabled(true);
				edtPopNotiWordIntervalMinute.setEnabled(true);
				edtPopNotiWordIntervalSecond.setEnabled(true);
			}
			break;
		case KEY_SWITCH_UPDATE_WORD:
			if ( ! prefSettings.getBoolean(keySwitch, false) ) {
				edtUpdateWordIntervalHour.setEnabled(false);
				edtUpdateWordIntervalMinute.setEnabled(false);
				edtUpdateWordIntervalSecond.setEnabled(false);
			} else {
				edtUpdateWordIntervalHour.setEnabled(true);
				edtUpdateWordIntervalMinute.setEnabled(true);
				edtUpdateWordIntervalSecond.setEnabled(true);
			}
			break;
		}
	}
	
	
	
	/**
	 * <p>Title: setEdtIntervalListener</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-22 下午12:13:43
	 */
	private void setEdtIntervalListener(final EditText edt) {
		edt.setOnFocusChangeListener(new OnFocusChangeListener(){
		    @Override
		    public void onFocusChange(View v, boolean hasFocus) {
		    // 这里通过传入的V可以获得ID来判断是否是需要的View，hasFocus为true表示获得焦点，反之失去焦点。
		    	if ( ! hasFocus ) {
		    		hideKeyboard(v);
		    		edtString = edt.getText().toString();
		    		if ( edtString.matches("0?0?") ) {
		    			switch ( v.getId() ) {
		    			case R.id.setting_edittext_pop_noti_word_hour:
		    			case R.id.setting_edittext_pop_noti_word_minute:
				    	case R.id.setting_edittext_update_word_hour:
				    	case R.id.setting_edittext_update_word_minute:
				    		edt.setText(INTERVAL_00);
				    		break;
				    	case R.id.setting_edittext_pop_noti_word_second:
				    		b = edtPopNotiWordIntervalHour.getText().toString().equals(INTERVAL_00) 
	    						&& edtPopNotiWordIntervalMinute.getText().toString().equals(INTERVAL_00);
				    		if ( b ) {
				    			edt.setText(INTERVAL_30);
				    		} else {
				    			edt.setText(INTERVAL_00);
				    		}
				    		break;
				    	case R.id.setting_edittext_update_word_second:
				    		b = edtUpdateWordIntervalHour.getText().toString().equals(INTERVAL_00) 
		    					&& edtUpdateWordIntervalMinute.getText().toString().equals(INTERVAL_00);
				    		if ( b ) {
				    			edt.setText(INTERVAL_30);
				    		} else {
				    			edt.setText(INTERVAL_00);
				    		}
				    		break;
				    	}
		    		} 
		    		switch ( v.getId() ) {
		    		case R.id.setting_edittext_pop_noti_word_hour:
		    			prefEdit(edt, v);
		    			intPopNotiWordIntervalHour = Util.getPrefStr2Int(prefSettings,v,INTERVAL_00);
		    			break;
		    		case R.id.setting_edittext_pop_noti_word_minute:
		    			prefEdit(edt, v);
		    			intPopNotiWordIntervalMinute = Util.getPrefStr2Int(prefSettings,v,INTERVAL_00);
		    			break;
		    		case R.id.setting_edittext_pop_noti_word_second:
		    			prefEdit(edt, v);
		    			intPopNotiWordIntervalSecond = Util.getPrefStr2Int(prefSettings,v,INTERVAL_30);
		    			break;
		    		case R.id.setting_edittext_update_word_hour:
		    			prefEdit(edt, v);
		    			intUpdateWordIntervalHour = Util.getPrefStr2Int(prefSettings,v,INTERVAL_00);
		    			break;
		    		case R.id.setting_edittext_update_word_minute:
		    			prefEdit(edt, v);
		    			intUpdateWordIntervalMinute = Util.getPrefStr2Int(prefSettings,v,INTERVAL_00);
		    			break;
		    		case R.id.setting_edittext_update_word_second:
		    			prefEdit(edt, v);
		    			intUpdateWordIntervalSecond = Util.getPrefStr2Int(prefSettings,v,INTERVAL_30);
		    			break;
	    			default:
	    				break;
		    		}
		    		Log.d("putString", edtString);
		    		Log.d("settings", "edittext失焦");
		    		renewSwitch(switchPopNotiWord);
		    		renewSwitch(switchUpdateWord);
		    	}
		    }

			/**
			 * <p>Title: prefEdit</p>
			 * <p>Description: </p>
			 * @param edt
			 * @param v
			 * @author bubble
			 * @date 2015-8-22 下午10:02:50
			 */
			private void prefEdit(final EditText edt, View v) {
				prefEditorSettings.putString(v.getTag().toString(), edt.getText().toString());
				prefEditorSettings.commit();
			}

			
		});
		edt.addTextChangedListener(new TextWatcher() {  
	          
	        /**
	         * text 输入框中改变后的字符串信息  
	         * start 输入框中改变后的字符串的起始位置  
	         * before 输入框中改变前的字符串的位置 默认为0  
	         * count 输入框中改变后的一共输入字符串的数量  
	         */
	        @Override  
	        public void onTextChanged(CharSequence text, int start, int before, int count) {  

	        }  
	          
	        /**
	         * <p>Description: </p>
	         * @author bubble
	         * @date 2015-8-22 下午1:27:06
	         */
	        @Override  
	        public void beforeTextChanged(CharSequence text, int start, int count,int after) {  
	        //text  输入框中改变前的字符串信息  
	        //start 输入框中改变前的字符串的起始位置  
	        //count 输入框中改变前后的字符串改变数量一般为0  
	        //after 输入框中改变后的字符串与起始位置的偏移量  
	        
	        }  
	          
	        @Override  
	        public void afterTextChanged(Editable edit) {  
	        //edit  输入结束呈现在输入框中的信息  
	       
	        }  
	    });
	}
	/**
	 * <p>Title: hideKeyboard</p>
	 * <p>Description: </p>
	 * @param view
	 * @author bubble
	 * @date 2015-8-22 下午6:36:55
	 */
	public void hideKeyboard(View view) {
		if ( inputMethodManager == null ) {
			inputMethodManager =(InputMethodManager)mActivity.getSystemService(Activity.INPUT_METHOD_SERVICE);
		}
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
	/**
	 * <p>Title: resetSwitch</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-22 下午10:53:42
	 */
	private static void renewSwitch(Switch swi) {
		if ( swi != null ) {
			swi.setChecked(false);
			swi.setChecked(true);
		}
	}
	/**
	 * <p>Title: renewSwitch</p>
	 * <p>Description: </p>
	 * @param keySwitch
	 * @author bubble
	 * @date 2015-8-22 下午11:05:52
	 */
	public static void renewSwitch(String keySwitch) {
		switch (keySwitch) {
		case KEY_SWITCH_POP_NOTI_WORD:
			renewSwitch(switchPopNotiWord);
		case KEY_SWITCH_UPDATE_WORD:
			renewSwitch(switchUpdateWord);
		}
	}

	
	
}
