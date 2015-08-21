package com.bubble.simpleword.menu;

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
	public static final String SETTINGS_FILE_NAME = "SimpleWord_Settings_File";
	SharedPreferences settingsPref;
	SharedPreferences.Editor settingsPrefEditor;
	public static final String SPINNER_SELECTION_WODE_MODE = "spinner_wode_mode_selected";
	
	private static AlarmManager am;
    private static PendingIntent pendingIntent;
    
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
		getActivity().setTitle(R.string.settings);
		getActivity().getActionBar().setDisplayShowCustomEnabled(false);
		
		settingsPref = getActivity().getSharedPreferences(SETTINGS_FILE_NAME, Context.MODE_PRIVATE);
    	settingsPrefEditor = settingsPref.edit();
    	
		initSpinnerWordSort(view);
		
		initSwitchNotificationWord(view);
		
		
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
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//绑定 Adapter到控件
		spinner .setAdapter(adapter);
		
		int spinnerSelection = settingsPref.getInt(SPINNER_SELECTION_WODE_MODE, 0);
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
		    	
		    	settingsPrefEditor.putInt(SPINNER_SELECTION_WODE_MODE, position);
		    	settingsPrefEditor.commit();
		    	Log.i("MODE_GET_WORD", Integer.toString(WordsDB.MODE_GET_WORD));
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        // Another interface callback
		    }
		});
	}
	
	/**
	 * <p>Title: initSwitchNotificationWord</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-20 下午11:02:54
	 */
	private void initSwitchNotificationWord(View view) {
		Switch mSwitch = (Switch)view.findViewById(R.id.setting_switch_notification_word); 
		mSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener(){
            
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if (isChecked) {	
                	// switch on，开启通知栏单词
                	startPendingIntent(getActivity());
                } else {
                	//switch off，关闭通知栏单词
                	stopPendingIntent();
                }
            }
        });
	}

    /**
     * 使用 AlarmManager 来 定时启动服务
     */
    public static void startPendingIntent(Context context) {

        am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ServiceNotification.class);

        pendingIntent = PendingIntent.getService(context, 0, intent, 0);

        long interval = DateUtils.MINUTE_IN_MILLIS * 1;// n分钟一次

        long firstWake = System.currentTimeMillis() + interval;

        am.setRepeating(AlarmManager.RTC, firstWake, interval, pendingIntent);

    }

    public static void stopPendingIntent() {

        if (pendingIntent != null) {

            pendingIntent.cancel();

        }
    };
}
