package com.bubble.simpleword.fragment;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bubble.simpleword.R;
import com.bubble.simpleword.activity.MainActivity;

/**
 * <p>Title: MenuFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-2
 */
public class SlidingMenuFragment extends ListFragment {
	public static CenterFragment centerFragment;
	public static HomeFragment homeFragment;
	public static WordBookFragment wordBookFragment;
	public static BBSFragment bbsFragment;
	public static SettingsFragment settingsFragment;
	
	public static Fragment newContentFragment;
	
	private ActionBar actionBar;
	
	private Object object;
	private String str;
	private View view;
	private String[] menuItems;
	private ArrayAdapter<String> menuAdapter;
	private MainActivity mainActivity;
	
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public SlidingMenuFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.menu_listview,container, false);  
		actionBar = getActivity().getActionBar();
        return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		menuItems = new String[] {"首页", "单词本", "设置"};
		
		menuAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, menuItems);
		
		setListAdapter(menuAdapter);
	}

	@SuppressWarnings("static-access")
	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		
		if ( HomeFragment.isCurrent() ) {
   	    	HomeFragment.stopPlayerSentence();
   	    }
		
		newContentFragment = null;
		
		object = lv.getItemAtPosition(position);
		str = (String) object;
		
		getActivity().setTitle(str);
		actionBar.setDisplayShowCustomEnabled(false);
		
		HomeFragment.setIsCurrent(false);
		
		if (str.matches(getResources().getString(R.string.center))){
			if (centerFragment == null)
				centerFragment = new CenterFragment();
			newContentFragment = centerFragment;
		} else if (str.matches(getResources().getString(R.string.home))){
			if (homeFragment == null)
				homeFragment = new HomeFragment(getActivity());
			newContentFragment = homeFragment;
			HomeFragment.setIsCurrent(true);
		} else if (str.matches(getResources().getString(R.string.wordbook))){
			if (wordBookFragment == null)
				wordBookFragment = new WordBookFragment();
			newContentFragment = wordBookFragment;
			actionBar.setDisplayShowCustomEnabled(true);
		} else if (str.matches(getResources().getString(R.string.BBS))){
			if (bbsFragment == null)
				bbsFragment = new BBSFragment();
			newContentFragment = bbsFragment;
		} else if (str.matches(getResources().getString(R.string.settings))){
			if (settingsFragment == null)
				settingsFragment = new SettingsFragment();
			newContentFragment = settingsFragment;
		}

		if (newContentFragment != null){
			switchFragment(newContentFragment);
		}
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			mainActivity = (MainActivity) getActivity();
			mainActivity.switchContent(fragment);
		} 
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-15 下午4:11:34
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(getTag(), "slidingmenu——onCreate");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-15 下午4:12:03
	 */
	@Override
	public void onResume() {
		super.onResume();
		Log.i(getTag(), "slidingmenu——onResume");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-15 下午4:12:28
	 */
	@Override
	public void onPause() {
		super.onPause();
		Log.i(getTag(), "slidingmenu——onPause");
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-15 下午4:12:52
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(getTag(), "slidingmenu——onDestory");
	}
}
