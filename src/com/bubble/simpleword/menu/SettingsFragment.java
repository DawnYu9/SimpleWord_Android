package com.bubble.simpleword.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bubble.simpleword.MainActivity;
import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsDB;

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
		
		Spinner spinner = (Spinner)view.findViewById(R.id.spinner_word_get_mode);
		String[] mItems = getResources().getStringArray(R.array.word_get_mode);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//绑定 Adapter到控件
		spinner .setAdapter(adapter);
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
		    	Log.i("MODE_GET_WORD", Integer.toString(WordsDB.MODE_GET_WORD));
		    }
		    @Override
		    public void onNothingSelected(AdapterView<?> parent) {
		        // Another interface callback
		    }
		});
		
		
		return view; 
	}
}
