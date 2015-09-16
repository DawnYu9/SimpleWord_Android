package com.bubble.simpleword.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bubble.simpleword.R;

/**
 * <p>Title: CenterFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-5 下午3:35:42
 */
public class CenterFragment extends Fragment {

	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public CenterFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fg_layout_center,container, false);  
		return view; 
	}
}
