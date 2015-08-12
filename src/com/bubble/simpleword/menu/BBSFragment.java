package com.bubble.simpleword.menu;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bubble.simpleword.R;

/**
 * <p>Title: BBSFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-5 下午3:42:34
 */
public class BBSFragment extends Fragment {

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public BBSFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.menu_item_fg_bbs,container, false);  
		getActivity().setTitle(R.string.BBS);
		getActivity().getActionBar().setDisplayShowCustomEnabled(false);
		return view; 
	}
}
