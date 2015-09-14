package com.bubble.simpleword.fragment;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.bubble.simpleword.MainActivity;
import com.bubble.simpleword.R;
import com.bubble.simpleword.adapter.WordRecyclerViewAdapter;
import com.bubble.simpleword.adapter.WordRecyclerViewAdapter.OnRecyclerViewItemLongClickListener;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.util.Util;
import com.bubble.simpleword.view.SnappingRecyclerView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

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
	
	ActionBar actionBar;  
	Activity activity;
	  
    
    Spinner spnWordbook;  
    List<String> wordbookList;
    HashMap<String, List> wordsDatasetHM = new HashMap<String, List>();
    public static final String KEY_SPINNER_SELECTED_TABLENAME = "KEY_SPINNER_SELECTED_NAME";
    int spnSelectedPosition;
    String tableName;
    
    SnappingRecyclerView recyclerView;
    WordRecyclerViewAdapter wordCardAdapter;  
    View view;
    List<WordCls> wordsDataset = new ArrayList<WordCls>();  
    WordCls word ;
    
    
    private Switch swi;
    public static final int TYPE_VIEW_VERTICAL = WordRecyclerViewAdapter.TYPE_VIEW_VERTICAL;
    public static final int TYPE_VIEW_HORIZON = WordRecyclerViewAdapter.TYPE_VIEW_HORIZON;
    
    private static final String KEY_WORDBOOK_ORIENTATION = "KEY_WORDBOOK_ORIENTATION";
    private int viewHolderType;
    
    private HashMap <String, Parcelable> hmRecyclerViewState = new HashMap<String, Parcelable>();    
    private Parcelable recyclerViewState;
    
    private boolean isTableFirstShow = true;
    private int scrollX;
    private int scrollY;
    private static final String KEY_RECYCLERVIEW_SCROLL_POSITION = "KEY_RECYCLERVIEW_SCROLL_POSITION";
    private static final String KEY_RECYCLERVIEW_SCROLL_DY_TOP = "KEY_RECYCLERVIEW_SCROLL_DY_TOP";
    private static final String KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM = "KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM";
    
    Handler handler = new Handler();
    
    int firstVisibleItemPosition;
    int firstCompletelyVisibleItemPosition;
    int lastCompletelyVisibleItemPosition;
    int lastVisibleItemPosition;
    
    
    View savedFirstVisibleChild;
    View currentFirstVisibleChild;
	int dyTop;
	int dyBottom;
	int savedFirstVisiblePosition;
	int savedFirstVisibleBottom;
	int savedFirstVisibleHeight;
	
	int currentFirstVisiblePosition;
	int currentFirstVisibleTop;
	
	boolean isRecyclerInit = false;
    /**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public WordBookFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fg_layout_wordbook,container, false); 
		
		
		activity = getActivity();
		
		prefSettings = Util.getSharedPreferences(getActivity());
    	prefEditorSettings = prefSettings.edit();
    	
    	
    	
//		actionBar.setTitle(R.string.wordbook);	//set all fragments's actionbar title
		getActivity().setTitle(R.string.wordbook);	//set one fragment's actionbar title

		actionBar = activity.getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);	//make custom actionbar view work
        
        initSpinner();  
        
//        initRecyclerView();
        
        
        
        swi = (Switch)view.findViewById(R.id.switch_layout);
        swi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if ( !isChecked ) {
					setRecyclerViewWordType(TYPE_VIEW_VERTICAL);
				} else {
					setRecyclerViewWordType(TYPE_VIEW_HORIZON);
				}
			}
		});
        
		return view; 
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-12 上午12:05:56
	 */
	@Override
	public void onResume() {
		super.onResume();
	}

	/**
     * <p>Title: initSpinner</p>
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8
     */
    private void initSpinner() {  
//        View actionbarLayout = LayoutInflater.from(activity).inflate(R.layout.wordbook_actionbar, null);  
        View actionbarLayout = view.inflate(activity,R.layout.wordbook_actionbar, null);  
        spnWordbook = (Spinner) actionbarLayout.findViewById(R.id.wordbook_actionbar_spinner);  
          
//        initSpinnerDataMethod1();  
          
        initSpinnerDataMethod2();  
        
        tableName = prefSettings.getString(KEY_SPINNER_SELECTED_TABLENAME, getWordbookList().get(0));
        spnSelectedPosition = getWordbookList().indexOf(tableName);
        spnWordbook.setSelection(spnSelectedPosition);
          
        spnWordbook.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				tableName= parent.getItemAtPosition(position).toString(); 
	        	prefEditorSettings.putString(KEY_SPINNER_SELECTED_TABLENAME, tableName);
	        	prefEditorSettings.commit();
	        	
//	        	if ( wordsDatasetHM.get(tableName) == null ) {	//如果有更新怎么办？？？
//	        		wordsDataset = WordsManager.getWordsDataset(tableName);
//	        		wordsDatasetHM.put(tableName, wordsDataset);
//	        	} else {
//	        		wordsDataset = wordsDatasetHM.get(tableName);
//	        	}
//	        	
	        	if ( ! isRecyclerInit ) {
	        		initRecyclerView();
	        		isRecyclerInit = true;
	        	}
	        	
	        	wordsDataset = WordsManager.getWordsDataset(tableName);
	        	wordCardAdapter.updateList(wordsDataset);
	        	Toast.makeText(activity, "你点击的是:"+tableName, Toast.LENGTH_SHORT).show();   
	        	
	        	
	        	restoreRecyclerViewPosition(tableName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
          
        //在Bar上显示定制view  
        actionBar.setCustomView(actionbarLayout);  
    }

    /** 
     * 建立数据源 方法一  
     */  
/*    private void initSpinnerDataMethod1() {  
         String[] mItems = getResources().getStringArray(R.array.spinner_page);  
         ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(activity,android.R.layout.simple_spinner_item, mItems);  
         spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);  
         mActionbarSpinner.setAdapter(spinnerAdapter);  
           
    }  */
      
    /** 
     * 建立数据源 方法二  
     */  
    private void initSpinnerDataMethod2() {  
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
	 * <p>Title: initRecyclerView</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-10 下午10:15:11
	 */
	private void initRecyclerView() {
		isRecyclerInit = true;
		recyclerView = (SnappingRecyclerView) view.findViewById(R.id.wordbook_recycler_list); 
	
		wordCardAdapter = new WordRecyclerViewAdapter(activity, tableName, wordsDataset);  
		recyclerView.setAdapter(wordCardAdapter); 
		
		
		recyclerView.addOnScrollListener(new OnScrollListener() {
			
			@Override
		    public void onScrolled(RecyclerView recyclerView, int dx, final int dy) {
		        super.onScrolled(recyclerView, dx, dy);
		    }
			
			/**
			 * <p>Description: </p>
			 * @author bubble
			 * @date 2015-9-11 下午9:56:06
			 */
			@Override
			public void onScrollStateChanged(final RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				
				switch (newState) {
	            case RecyclerView.SCROLL_STATE_IDLE:
	        
	            	Log.i("SCROLL_STATE_IDLE", "SCROLL_STATE_IDLE");
					saveRecyclerViewPosition(tableName);
					
	                break;
	            case RecyclerView.SCROLL_STATE_DRAGGING:
	                break;
	            case RecyclerView.SCROLL_STATE_SETTLING:
	                break;
	         }
			}
	
		});
		
		viewHolderType = prefSettings.getInt(KEY_WORDBOOK_ORIENTATION, TYPE_VIEW_VERTICAL);
		setRecyclerViewWordType(viewHolderType);
		
	    recyclerView.setItemAnimator(new DefaultItemAnimator());  
	    recyclerView.setHasFixedSize(true);
	
	    wordCardAdapter.setOnItemClickListener(new WordRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
	
			@Override
			public void onItemClick(View view, int position, WordCls wordCls) {
				switch (wordCardAdapter.getItemViewType(position)) {
				case TYPE_VIEW_HORIZON:
					//水平为全屏卡片模式，单击发音
					Toast.makeText(getActivity(), "发音" + wordCls.getWord(), Toast.LENGTH_SHORT).show();
					break;
				case TYPE_VIEW_VERTICAL:
					//垂直为小卡片list模式，单击进入大卡片的该单词position，联网获取完整释义
				default:
					setRecyclerViewWordType(TYPE_VIEW_HORIZON);
					recyclerView.scrollToPosition(position);
					break;
				}
			}
	    });
	    wordCardAdapter.setOnItemLongClickListener(new OnRecyclerViewItemLongClickListener() {
	
			@Override
			public void onItemLongClick(View view, int position, WordCls wordCls) {
				showCardMenu(view, position, wordCls);
			}
		});
	}

	/**
	 * <p>Title: saveInstanceState</p>
	 * <p>Description: </p>
	 * @param recyclerView
	 * @author bubble
	 * @date 2015-9-11 
	 */
	public void saveRecyclerViewPosition(String tableName) {
		firstVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
		
		savedFirstVisibleChild = recyclerView.getChildAt(0);
		//dy正，列表下滑
		//dy负，列表上滑
		dyTop = savedFirstVisibleChild.getHeight() - savedFirstVisibleChild.getBottom(); 
		dyBottom = recyclerView.getHeight() - savedFirstVisibleChild.getBottom();
		
		prefEditorSettings.putInt(KEY_RECYCLERVIEW_SCROLL_POSITION + tableName, firstVisibleItemPosition);
		prefEditorSettings.putInt(KEY_RECYCLERVIEW_SCROLL_DY_TOP + tableName, dyTop);
		prefEditorSettings.putInt(KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM + tableName, dyBottom);
		prefEditorSettings.commit();
		
	}
	
	/**
	 * <p>Title: restoreInstanceState</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-11 
	 */
	public void restoreRecyclerViewPosition(final String tableName) {
		if ( recyclerView != null) {
			savedFirstVisiblePosition = prefSettings.getInt(KEY_RECYCLERVIEW_SCROLL_POSITION + tableName, 0);
			dyTop = prefSettings.getInt(KEY_RECYCLERVIEW_SCROLL_DY_TOP + tableName, 0);
			dyBottom = prefSettings.getInt(KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM + tableName, 0);
			
			currentFirstVisiblePosition =  ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
			
			recyclerView.scrollToPosition(savedFirstVisiblePosition);
			if(currentFirstVisiblePosition > -1) {
				if (currentFirstVisiblePosition >= savedFirstVisiblePosition) {	//savedFirstVisiblePosition在顶部，手指往下滑-列表往上滑，列表应该下滑
					recyclerView.scrollBy(0, dyTop);
				} else if (currentFirstVisiblePosition < savedFirstVisiblePosition){	//savedFirstVisiblePosition在底部，手指往上滑-列表往下滑，列表应该继续上滑
					recyclerView.scrollBy(0, dyBottom);	//第一页的item会不准，暂时会找到合适的办法，用runnable会有跳转动作显示。			
				} 
			}
		}
	}
	
	
	/**
	 * <p>Title: setWordRecyclerViewOrientation</p>
	 * <p>Description: </p>
	 * @param b
	 * @author bubble
	 * @date 2015-9-10 下午9:45:11
	 */
	private void setRecyclerViewWordType(int type) {
		switch (type) {
		case TYPE_VIEW_HORIZON:
			prefEditorSettings.putInt(KEY_WORDBOOK_ORIENTATION, type);
			wordCardAdapter.setItemViewType(type);
			recyclerView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
			recyclerView.setSnapEnabled(true);
			MainActivity.setSlidingAboveMode(SlidingMenu.TOUCHMODE_MARGIN);
			break;
		case TYPE_VIEW_VERTICAL:
		default:
			prefEditorSettings.putInt(KEY_WORDBOOK_ORIENTATION, type);
			wordCardAdapter.setItemViewType(type);
			recyclerView.setLayoutManager(new LinearLayoutManager(activity)); 
			recyclerView.setSnapEnabled(false);
			MainActivity.setSlidingAboveMode(SlidingMenu.TOUCHMODE_FULLSCREEN);
			break;
		}
		prefEditorSettings.commit();
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
                    	editWord(position, wordCls, edtWord, edtPhonetic, edtDefinition);
                    }  
                })  
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
  
                    @Override  
                    public void onClick(DialogInterface dialog, int which) {  
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
            	deleteWord(wordCls);
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
	/**
	 * <p>Title: deleteWord</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-11 下午5:57:06
	 */
	private void deleteWord(final WordCls wordCls) {
		wordCardAdapter.deleteItem(wordCls);
    	WordsManager.deleteWord(tableName, wordCls.getWord());
    	
    	saveRecyclerViewPosition(tableName);
	}
	
      
    /**
	 * <p>Title: editWord</p>
	 * <p>Description: </p>
	 * @param position
	 * @param wordCls
	 * @param edtWord
	 * @param edtPhonetic
	 * @param edtDefinition
	 * @author bubble
	 * @date 2015-9-11 下午6:00:27
	 */
	private void editWord(final int position, final WordCls wordCls,
			final EditText edtWord, final EditText edtPhonetic,
			final EditText edtDefinition) {
		wordCls.setWord(edtWord.getText().toString());
		wordCls.setPhonetic(edtPhonetic.getText().toString());
		wordCls.setDefinition(edtDefinition.getText().toString());
		
		WordsManager.editWord(tableName, wordCls);
		
		wordCardAdapter.updateItem(position, wordCls);
		
		saveRecyclerViewPosition(tableName);
	}
    
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-11 下午11:06:24
	 */
	@Override
	public void onPause() {
		super.onPause();
		saveRecyclerViewPosition(tableName);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-12 下午1:16:23
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
}
