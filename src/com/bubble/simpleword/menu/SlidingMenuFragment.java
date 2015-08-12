package com.bubble.simpleword.menu;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bubble.simpleword.MainActivity;
import com.bubble.simpleword.R;

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

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public SlidingMenuFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.menu_listview,container, false);  
        return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] menuItem = getResources().getStringArray(R.array.menu);
		
		ArrayAdapter<String> menuAdapter = new ArrayAdapter<String>(getActivity(), 
				android.R.layout.simple_list_item_1, android.R.id.text1, menuItem);
		
		setListAdapter(menuAdapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContentFragment = null;
		Object o = lv.getItemAtPosition(position);
		String str=(String)o;
		if (str.matches(getResources().getString(R.string.center))){
			newContentFragment = new CenterFragment();
		} else if (str.matches(getResources().getString(R.string.home))){
			newContentFragment = new MainFragment(getActivity());
		} else if (str.matches(getResources().getString(R.string.wordbook))){
			newContentFragment = new WordBookFragment();
		} else if (str.matches(getResources().getString(R.string.BBS))){
			newContentFragment = new BBSFragment();
		} else if (str.matches(getResources().getString(R.string.settings))){
			newContentFragment = new SettingsFragment();
		}
		
		if (newContentFragment != null){
			switchFragment(newContentFragment);
		}
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;
		
		if (getActivity() instanceof MainActivity) {
			MainActivity fca = (MainActivity) getActivity();
			fca.switchContent(fragment);
		} 
	}
}
