package com.bubble.simpleword.menu;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.util.Util;
import com.bubble.simpleword.wordbook.WordCardAdapter;
import com.bubble.simpleword.wordbook.WordCardAdapter.OnRecyclerViewItemLongClickListener;
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
	private static SharedPreferences prefSettings;
	private SharedPreferences.Editor prefEditorSettings;
	
	WordCls word ;
	RecyclerView recyclerView;  
	  
    WordCardAdapter wordCardAdapter;  
    View view;
    List<WordCls> wordsList = new ArrayList<WordCls>();  
    
    Spinner spnWordbook;  
    List<String> wordbookList;
    List<String> spnList;
    public static final String KEY_SPINNER_SELECTED_ITEM = "KEY_SPINNER_SELECTED_ITEM";
    int spnSelectedPosition;
    String tableName;
    
    ActionBar actionBar;  
    Activity activity;
    
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
		
		prefSettings = Util.getSharedPreferences(getActivity());
    	prefEditorSettings = prefSettings.edit();
    	
		
//		actionBar.setTitle(R.string.wordbook);	//set all fragments's actionbar title
		getActivity().setTitle(R.string.wordbook);	//set one fragment's actionbar title

		actionBar = activity.getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);	//make custom actionbar view work
        
        initSpinner();  
        
        recyclerView = (RecyclerView) view.findViewById(R.id.wordbook_recycler_list);  
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));  
        recyclerView.setItemAnimator(new DefaultItemAnimator());  
        recyclerView.setHasFixedSize(true);

        wordsList = WordsManager.getWordsList(tableName); 
        wordCardAdapter = new WordCardAdapter(activity, tableName, wordsList);  
        recyclerView.setAdapter(wordCardAdapter); 
        
        wordCardAdapter.setOnItemClickListener(new WordCardAdapter.OnRecyclerViewItemClickListener() {

			@Override
			public void onItemClick(View view, int position, WordCls wordCls) {
				Toast.makeText(getActivity(), "点击了" + wordCls.getWord(), Toast.LENGTH_SHORT).show();
			}
        });
        wordCardAdapter.setOnItemLongClickListener(new OnRecyclerViewItemLongClickListener() {

			@Override
			public void onItemLongClick(View view, int position, WordCls wordCls) {
				showCardMenu(view, position, wordCls);
			}
		});
        
		return view; 
	}
	 
	/**
	 * <p>Title: showCardMenu</p>
	 * <p>Description: </p>
	 * @param v
	 * @author bubble
	 * @date 2015-9-8 下午6:15:18
	 */
	public void showCardMenu(final View v, final int position, final WordCls wordCls) {
		PopupMenu popMenu = new PopupMenu(v.getContext(), v);
		popMenu.inflate(R.menu.wordbook_card_menu);
		popMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
				case R.id.wordbook_card_menu_edit:
					showEditWordDialog(v, position,wordCls);
					break;
				case R.id.wordbook_card_menu_delete:
					showDeleteWordDialog(wordCls);
					break;
				case R.id.wordbook_card_menu_addtobook:
					showChooseBookDialog(wordCls);
					break;
				default:
					break;
				}
			    return true;
			}
		});
		popMenu.show();
	} 
	
	/**
	 * <p>Title: showEditWordDialog</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午11:41:49
	 */
	private void showEditWordDialog(final View v, final int position, final WordCls wordCls) {
		// 取得自定义View  
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());  
        View viewDlgEditWord = layoutInflater.inflate(R.layout.wordbook_dlg_edit_word, null);  
        final EditText edtWord = (EditText)viewDlgEditWord.findViewById(R.id.wordbook_dlg_edttxt_word);
        final EditText edtPhonetic = (EditText)viewDlgEditWord.findViewById(R.id.wordbook_dlg_edttxt_phonetic);
        final EditText edtDefinition = (EditText)viewDlgEditWord.findViewById(R.id.wordbook_dlg_edttxt_definition);
        edtWord.setText(wordCls.getWord());
        edtPhonetic.setText(wordCls.getPhonetic());
        edtDefinition.setText(wordCls.getDefinition());
        
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()); 
        builder.setTitle("编辑单词")
                .setIcon(R.mipmap.ic_launcher)
                .setView(viewDlgEditWord)  
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                    	wordCls.setWord(edtWord.getText().toString());
                    	wordCls.setPhonetic(edtPhonetic.getText().toString());
                    	wordCls.setDefinition(edtDefinition.getText().toString());
                    	WordsManager.editWord(tableName, wordCls);
                    	
                    	wordCardAdapter.updateItem(position, wordCls);
                    }  
                })  
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                        // TODO Auto-generated method stub  
                    }  
                }).  
                create();  
        builder.show(); 
	}
	
	/**
	 * <p>Title: showChooseBookDialog</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午11:35:12
	 */
	private void showChooseBookDialog(final WordCls wordCls) {
		List<String> list = WordsManager.getTableList();
		list.remove(tableName);
		final String[] tableList = list.toArray(new String[list.size()]);
		final boolean[] selectedBookArray = new boolean[tableList.length];
		
		AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
		builder.setTitle("请选择单词本")  
				.setIcon(R.mipmap.ic_launcher)  
                .setMultiChoiceItems(tableList, selectedBookArray, new DialogInterface.OnMultiChoiceClickListener() {  
                      
                    @Override  
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {  
                        selectedBookArray[which] = isChecked;  
                    }  
                })  
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                    	for (int i = 0; i < selectedBookArray.length; i++) {
                    		if ( selectedBookArray[i] ) {
                    			WordsManager.addWord(tableList[i], wordCls);
                    		}
                    	}
                        Toast.makeText(view.getContext(), "添加成功", Toast.LENGTH_SHORT).show();  
                    }  
                })  
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
                        // TODO Auto-generated method stub  
                    }  
                });
		builder.show(); 
	}
	
	/**
	 * <p>Title: showDeleteDialog</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-8 下午5:25:58
	 */
	private void showDeleteWordDialog(final WordCls wordCls) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("弹出警告框");
        builder.setMessage("确定删除吗？");
        
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            	wordCardAdapter.removeItem(wordCls);
            	WordsManager.deleteWord(tableName, wordCls.getWord());
            	Toast.makeText(view.getContext(), wordCls.getWord() + "删除成功", Toast.LENGTH_SHORT).show();
            	
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
	}
	
    private void initSpinner() {  
//        View actionbarLayout = LayoutInflater.from(activity).inflate(R.layout.wordbook_actionbar, null);  
        View actionbarLayout = view.inflate(activity,R.layout.wordbook_actionbar, null);  
        spnWordbook = (Spinner) actionbarLayout.findViewById(R.id.wordbook_actionbar_spinner);  
          
//        initSpinnerMethod1();  
          
        initSpinnerMethod2();  
        
        spnSelectedPosition = prefSettings.getInt(KEY_SPINNER_SELECTED_ITEM, 0);
        spnWordbook.setSelection(spnSelectedPosition);
        tableName = getWordbookList().get(spnSelectedPosition);
          
        //事件监听  
        spnWordbook.setOnItemSelectedListener(new SpinnerItemSelectedListener());  
          
        //在Bar上显示定制view  
        actionBar.setCustomView(actionbarLayout);  
    }  
      
    /** 
     * 建立数据源 方法一  
     */  
/*    private void initSpinnerMethod1() {  
         String[] mItems = getResources().getStringArray(R.array.spinner_page);  
         ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, mItems);  
         spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         mActionbarSpinner.setAdapter(spinnerAdapter);  
           
    }  */
      
    /** 
     * 建立数据源 方法二  
     */  
    private void initSpinnerMethod2() {  
        spnWordbook.setAdapter(  
                new ArrayAdapter<String>(activity,   
                        android.R.layout.simple_expandable_list_item_1,getWordbookList()));    
    }  
      
    /** 
     * 下拉列表数据源 
     * @return 
     */  
    private List<String> getWordbookList(){          
        wordbookList = WordsManager.getTableList();  
        return wordbookList;  
    }  
      
    /**
     * <p>Title: addWordbook</p>
     * <p>Description: </p>
     * @param bookName
     * @author bubble
     * @date 2015-9-8 下午10:55:41
     */
    private void addWordbook(String bookName) {
    	WordsManager.createTable(bookName);
    }
    
    /**
     * <p>Title: deleteWordbook</p>
     * <p>Description: </p>
     * @param bookName
     * @author bubble
     * @date 2015-9-8 下午10:56:07
     */
    private void deleteWordbook(String bookName) {
    	WordsManager.deleteTable(bookName);
    }
    
    /**
     * <p>Title: editWordbookName</p>
     * <p>Description: </p>
     * @param oldName
     * @param newName
     * @author bubble
     * @date 2015-9-8 下午10:57:01
     */
    private void editWordbookName(String oldName, String newName) {
    	WordsManager.alterTableName(oldName, newName);
    }
    
    
    /** 
    * 监听action_bar的spinner item选择事件 
    */  
    private class SpinnerItemSelectedListener implements OnItemSelectedListener {  
          
        @Override  
        public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {  
        	tableName= parent.getItemAtPosition(position).toString(); 
        	prefEditorSettings.putInt(KEY_SPINNER_SELECTED_ITEM, position);
        	prefEditorSettings.commit();
        	wordsList = WordsManager.getWordsList(tableName);
        	wordCardAdapter.updateList(wordsList);
        	Toast.makeText(activity, "你点击的是:"+tableName, 2000).show();   
        }  
          
        @Override  
        public void onNothingSelected(AdapterView<?> arg0) {}  
    }  
}
