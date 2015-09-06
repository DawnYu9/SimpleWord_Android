package com.bubble.simpleword.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.wordbook.WordCardAdapter;
import com.bubble.simpleword.wordbook.WordCls;

/**
 * <p>Title: WordBiikFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-3
 */
public class WordBookFragment extends Fragment {
	WordCls word ;
	RecyclerView mRecyclerView;  
	  
    WordCardAdapter wordCardAdapter;  
    View view;
    List<WordCls> wordsList = new ArrayList<WordCls>();  
    Spinner mActionbarSpinner;    
    
    ActionBar actionBar;  
    Activity activity;
   /* @Override  
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch(item.getItemId()) {  
            // 当点击actionbar上的添加按钮时，向adapter中添加一个新数据并通知刷新  
            case R.id.action_add:  
                if (myAdapter.getItemCount() != names.length) {  
                    actors.add(new Actor(names[myAdapter.getItemCount()], pics[myAdapter.getItemCount()]));  
                    mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);  
                    myAdapter.notifyDataSetChanged();  
                }  
                return true;  
            // 当点击actionbar上的删除按钮时，向adapter中移除最后一个数据并通知刷新  
            case R.id.action_remove:  
                if (myAdapter.getItemCount() != 0) {  
                    actors.remove(myAdapter.getItemCount()-1);  
                    mRecyclerView.scrollToPosition(myAdapter.getItemCount() - 1);  
                    myAdapter.notifyDataSetChanged();  
                }  
                return true;  
        }  
        return super.onOptionsItemSelected(item);  
    }  */
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public WordBookFragment() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.wordbook_recycler_view,container, false); 
		activity = getActivity();
		word = WordsManager.wordCls;
		if ( word == null )
			word = WordsManager.updateWord();
		wordsList.add(word);  
//		actionBar.setTitle(R.string.wordbook);	//set all fragments's actionbar title
		getActivity().setTitle(R.string.wordbook);	//set one fragment's actionbar title

		actionBar = activity.getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);	//make custom actionbar view work

        initSpinner();  
        
        
        mRecyclerView = (RecyclerView) view.findViewById(R.id.wordbook_recycler_list);  
        mRecyclerView.setLayoutManager(new LinearLayoutManager(activity));  
        
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());  
        mRecyclerView.setHasFixedSize(true);
        wordCardAdapter = new WordCardAdapter(activity, wordsList);  
        mRecyclerView.setAdapter(wordCardAdapter); 
		return view; 
	}
	 
    private void initSpinner()  
    {  
//        View actionbarLayout = LayoutInflater.from(activity).inflate(R.layout.wordbook_actionbar, null);  
        View actionbarLayout = view.inflate(activity,R.layout.wordbook_actionbar, null);  
        mActionbarSpinner = (Spinner) actionbarLayout.findViewById(R.id.wordbook_actionbar_spinner);  
          
        //方法一  
//        initSpinnerMethod1();  
          
        //方法二  
        initSpinnerMethod2();  
          
        //事件监听  
        mActionbarSpinner.setOnItemSelectedListener(new SpinnerItemSelectedListener());  
          
        //在Bar上显示定制view  
        actionBar.setCustomView(actionbarLayout);  
    }  
      
    /** 
     * 建立数据源 方法一  
     */  
/*    private void initSpinnerMethod1()  
    {  
         String[] mItems = getResources().getStringArray(R.array.spinner_page);  
         ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, mItems);  
         spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         mActionbarSpinner.setAdapter(spinnerAdapter);  
           
    }  */
      
    /** 
     * 建立数据源 方法二  
     */  
    private void initSpinnerMethod2()  
    {  
        mActionbarSpinner.setAdapter(  
                new ArrayAdapter<String>(activity,   
                        android.R.layout.simple_expandable_list_item_1,getData()));    
    }  
      
    /** 
     * 下拉列表数据源 
     * @return 
     */  
    private List<String> getData(){          
        List<String> data = new ArrayList<String>();  
        data.add("考研单词");  
        data.add("B");  
        data.add("C");  
        data.add("D");           
        return data;  
    }  
      
      
    /** 
    * 监听action_bar的spinner item选择事件 
    */  
    private class SpinnerItemSelectedListener implements OnItemSelectedListener {  
          
        @Override  
        public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {  
             String str= parent.getItemAtPosition(position).toString();  
             Toast.makeText(activity, "你点击的是:"+str, 2000).show();   
        }  
          
        @Override  
        public void onNothingSelected(AdapterView<?> arg0) {}  
    }  
}
