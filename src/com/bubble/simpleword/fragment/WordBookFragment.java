package com.bubble.simpleword.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bubble.simpleword.R;
import com.bubble.simpleword.activity.MainActivity;
import com.bubble.simpleword.adapter.BookMenuRecyclerViewAdapter;
import com.bubble.simpleword.adapter.EditBookRecyclerViewAdapter;
import com.bubble.simpleword.adapter.EditBookRecyclerViewAdapter.OnStartDragListener;
import com.bubble.simpleword.adapter.WordRecyclerViewAdapter;
import com.bubble.simpleword.adapter.WordRecyclerViewAdapter.OnRecyclerViewItemLongClickListener;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.drag.SimpleItemTouchHelperCallback;
import com.bubble.simpleword.util.MyLayoutManager;
import com.bubble.simpleword.util.Util;
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
public class WordBookFragment extends Fragment implements EditBookRecyclerViewAdapter.OnStartDragListener {
	private static SharedPreferences prefSettings;
	private static SharedPreferences.Editor prefEditorSettings;
	
	private ActionBar actionBar;  
	private static Activity activity;
	private View actionbarLayout;
    
	private Spinner spnWordbook;  
	private static List<String> wordbookList;
    public static final String KEY_WORDBOOK_SELECTED_TABLENAME = "KEY_SELECTED_TABLENAME";
    private int spnSelectedPosition;
    
    private static RecyclerView recyclerViewWord;
    private static WordRecyclerViewAdapter wordCardAdapter;  
    private static View view;
    private static List<WordCls> wordsDataset = new ArrayList<WordCls>();  
    private WordCls word ;
    
    private Switch swi;
    public static final int TYPE_VIEW_VERTICAL = WordRecyclerViewAdapter.TYPE_VIEW_VERTICAL;
    public static final int TYPE_VIEW_HORIZON = WordRecyclerViewAdapter.TYPE_VIEW_HORIZON;
    
    private static final String KEY_WORDBOOK_ORIENTATION = "KEY_WORDBOOK_ORIENTATION";
    private static int viewHolderType;
    
    private static final String KEY_RECYCLERVIEW_SCROLL_POSITION = "KEY_RECYCLERVIEW_SCROLL_POSITION";
    private static final String KEY_RECYCLERVIEW_SCROLL_DY_TOP = "KEY_RECYCLERVIEW_SCROLL_DY_TOP";
    private static final String KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM = "KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM";
    
    private Handler handler = new Handler();
    
    private static int recyclerViewWidth;
    private static int firstVisibleItemPosition;
    private static int lastVisibleItemPosition;
    private static View firstVisibleChild;
    private View lastVisibleChild;
    private static int firstChildVisibleWidth;
    private int lastChildVisibleWidth;
    
    private static int dyTop;
    private static int dyBottom;
    private static int savedFirstVisiblePosition;
    private static int currentFirstVisiblePosition;
	
    private static HashMap<String, List> hmDataset = new HashMap<String, List>();
    private static boolean isWordRecyclerInit = false;
	
    private static HashMap<String, Boolean> hmIsDatasetEdit = new HashMap<String, Boolean>();
    private boolean isDatasetEdit = false;
	
    private static HashMap<String, Parcelable> hmRecyclerViewState = new HashMap<String, Parcelable>();
    private static Parcelable recyclerViewState;
	
    private PopupWindow dropdownPopMenu;
    private View dropdownPopMenuView;
    private static TextView tvDropdownPopMenu;
    private RecyclerView recyclerviewBookList;
    private List<String> datasetBookList;
    private static BookMenuRecyclerViewAdapter bookMenuRecyclerViewAdapter;
    private Button btnPopMenuEditWordbook;
    private PopupWindow popWindowEditBook;
    private View viewEditBookPopWindow;
    private RecyclerView recyclerViewEditBook;
    private EditBookRecyclerViewAdapter editBookRecyclerViewAdapter;
    
    Button btnCreateBook;
    
    ItemTouchHelper.Callback callback;
	ItemTouchHelper touchHelper;
    private OnStartDragListener mDragStartListener;
    
    private static String tableName;
    
    /**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public WordBookFragment() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		
		Log.i(getTag(), "WordBookFragment——onCreateView");
		view=inflater.inflate(R.layout.fg_layout_wordbook,container, false); 
		
		
		activity = getActivity();
		
		prefSettings = Util.getSharedPreferences(getActivity());
    	prefEditorSettings = prefSettings.edit();
        
    	initActionBar(inflater);        
        
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

	public void initActionBar(LayoutInflater inflater) {
		actionBar = activity.getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);	//make custom actionbar view works
        
//      actionbarLayout = LayoutInflater.from(activity).inflate(R.layout.wordbook_actionbar, null);  
		actionbarLayout = view.inflate(activity,R.layout.wordbook_actionbar_2, null);  
      
		initPopMenu(inflater);
//		initSpinner();
		
        actionBar.setCustomView(actionbarLayout);  
	}
	
	/**
	 * <p>Title: initPopMenu</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-17 下午4:24:38
	 */
	private void initPopMenu(LayoutInflater inflater) {
		tableName = prefSettings.getString(KEY_WORDBOOK_SELECTED_TABLENAME, getWordbookList().get(0));
		
		tvDropdownPopMenu = (TextView) actionbarLayout.findViewById(R.id.wordbook_actionbar_tv_popmenu);
		tvDropdownPopMenu.setWidth(Util.getScreenWidth()/2);
		tvDropdownPopMenu.setText(tableName);
		tvDropdownPopMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dropdownPopMenu.showAsDropDown(v);
			}
		});

		dropdownPopMenuView = LayoutInflater.from(activity).inflate(R.layout.wordbook_actionbar_popmenu_ll, null);
		dropdownPopMenu = new PopupWindow(dropdownPopMenuView, Util.getScreenWidth()/2, LayoutParams.WRAP_CONTENT);
		dropdownPopMenu.setFocusable(true);
		dropdownPopMenu.setOutsideTouchable(true);
		dropdownPopMenu.setBackgroundDrawable(new BitmapDrawable()); 
		dropdownPopMenu.update();
		
		recyclerviewBookList = (RecyclerView) dropdownPopMenuView.findViewById(R.id.wordbook_actionbar_popmenu_recyclerview);
		bookMenuRecyclerViewAdapter = new BookMenuRecyclerViewAdapter(activity, getWordbookList());
		recyclerviewBookList.setLayoutManager(new MyLayoutManager(activity));
		recyclerviewBookList.setItemAnimator(new DefaultItemAnimator());  
		recyclerviewBookList.setHasFixedSize(true);
		recyclerviewBookList.setAdapter(bookMenuRecyclerViewAdapter);
		
		getWordDataset(tableName);
    	
    	if ( ! isWordRecyclerInit ) {
    		initWordRecyclerView();
    		isWordRecyclerInit = true;
    	}
    	
    	restoreWordRecyclerViewPosition(tableName);
		
    	bookMenuRecyclerViewAdapter.setOnItemClickListener(new BookMenuRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View view, int position, String tableName) {
				tableName = tableName;
				tvDropdownPopMenu.setText(tableName);
				view.setBackgroundColor(getResources().getColor(R.color.background_material_dark));
				dropdownPopMenu.dismiss();
				
	        	prefEditorSettings.putString(KEY_WORDBOOK_SELECTED_TABLENAME, tableName);
	        	prefEditorSettings.commit();
	        	
	        	getWordDataset(tableName);
	        	
	        	if ( ! isWordRecyclerInit ) {
	        		initWordRecyclerView();
	        		isWordRecyclerInit = true;
	        	}
	        	
	        	wordCardAdapter.updateDataset(wordsDataset);
	        	
	        	restoreWordRecyclerViewPosition(tableName);
	        	
			}
		});
		
		btnPopMenuEditWordbook = (Button) dropdownPopMenuView.findViewById(R.id.wordbook_actionbar_popmenu_btn_edit);
		btnPopMenuEditWordbook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dropdownPopMenu.dismiss();
				popWindowEditBook.showAsDropDown(v);
			}
		});
		
		viewEditBookPopWindow = LayoutInflater.from(activity).inflate(R.layout.wordbook_popwindow_edit_book, null);
		
		popWindowEditBook = new PopupWindow(viewEditBookPopWindow, Util.getScreenWidth() * 3/5, LayoutParams.WRAP_CONTENT);
		popWindowEditBook.setFocusable(true);
		popWindowEditBook.setOutsideTouchable(true);
		popWindowEditBook.setBackgroundDrawable(new BitmapDrawable()); 
		popWindowEditBook.update();		
		
		editBookRecyclerViewAdapter = new EditBookRecyclerViewAdapter(activity, getWordbookList(), mDragStartListener);
		recyclerViewEditBook = (RecyclerView) viewEditBookPopWindow.findViewById(R.id.wordbook_recyclerview_edit_book);
		recyclerViewEditBook.setLayoutManager(new MyLayoutManager(activity));
		recyclerViewEditBook.setAdapter(editBookRecyclerViewAdapter);
		recyclerViewEditBook.setItemAnimator(new DefaultItemAnimator());  
		recyclerViewEditBook.setHasFixedSize(true);
		
		editBookRecyclerViewAdapter.setOnItemClickListener(new EditBookRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
			
			@Override
			public void onItemClick(View view, final int position, final String tableName) {
				LayoutInflater layoutInflater = LayoutInflater.from(activity);  
		        View viewDlgEditBook = layoutInflater.inflate(R.layout.wordbook_dlg_create_book, null);  
		        final EditText edtBook = (EditText)viewDlgEditBook.findViewById(R.id.wordbook_edt_create_book);
		        edtBook.setText(tableName);
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(activity); 
		        builder.setTitle("编辑单词本")
		                .setIcon(R.mipmap.ic_launcher)
		                .setView(viewDlgEditBook)  
		                .setPositiveButton("提交", new DialogInterface.OnClickListener() {  
		  
		                    @Override  
		                    public void onClick(DialogInterface dialog, int which) { 
		                    	String newName = edtBook.getText().toString();
		                    	if ( WordsManager.alterTableName(tableName, newName) ) {
		                    		editBookRecyclerViewAdapter.updateItem(position, newName);
		                    		WordsManager.editTableInfo(tableName, newName);
		                    		bookMenuRecyclerViewAdapter.updateItem(position, newName);
		                    		Toast.makeText(activity, "修改成功", Toast.LENGTH_SHORT).show();
		                    	}
		                    	else 
		                    		Toast.makeText(activity, "请勿重复命名", Toast.LENGTH_SHORT).show();
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
		});
		
		callback = new SimpleItemTouchHelperCallback(editBookRecyclerViewAdapter);
		touchHelper = new ItemTouchHelper(callback);
		touchHelper.attachToRecyclerView(recyclerViewEditBook);
		
		btnCreateBook = (Button) viewEditBookPopWindow.findViewById(R.id.wordbook_btn_add_book);
		btnCreateBook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LayoutInflater layoutInflater = LayoutInflater.from(getActivity());  
		        View viewDlgAddBook = layoutInflater.inflate(R.layout.wordbook_dlg_create_book, null);  
		        final EditText edtCreateBook = (EditText)viewDlgAddBook.findViewById(R.id.wordbook_edt_create_book);
		        
		        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext()); 
		        builder.setTitle("请输入新的单词本名称")
		                .setIcon(R.mipmap.ic_launcher)
		                .setView(viewDlgAddBook)  
		                .setPositiveButton("提交", new DialogInterface.OnClickListener() {  
		  
		                    @Override  
		                    public void onClick(DialogInterface dialog, int which) {  
		                    	createBook(edtCreateBook.getText().toString());
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
		});
	}

	/**
	 * <p>Title: getDataset</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-18 上午11:13:45
	 */
	private static List<WordCls> getWordDataset(String tableName) {
		if ( hmDataset.get(tableName) == null || hmIsDatasetEdit.get(tableName)) {
    		wordsDataset = WordsManager.getWordsDataset(tableName);
    		hmDataset.put(tableName, wordsDataset);
    		hmIsDatasetEdit.put(tableName, false);
    	} else {
    		wordsDataset = hmDataset.get(tableName);
    	}
		return wordsDataset;
	}
		
	/**
     * <p>Title: initSpinner</p>
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8
     */
    private void initSpinner() {  

        spnWordbook = (Spinner) actionbarLayout.findViewById(R.id.wordbook_actionbar_spinner);  
          
//        initSpinnerDataMethod1();  
          
        initSpinnerDataMethod2();  
        
        tableName = prefSettings.getString(KEY_WORDBOOK_SELECTED_TABLENAME, getWordbookList().get(0));
        spnSelectedPosition = getWordbookList().indexOf(tableName);
        spnWordbook.setSelection(spnSelectedPosition);
          
        spnWordbook.setOnItemSelectedListener(new OnItemSelectedListener() {
        	
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				tableName= parent.getItemAtPosition(position).toString(); 
	        	prefEditorSettings.putString(KEY_WORDBOOK_SELECTED_TABLENAME, tableName);
	        	prefEditorSettings.commit();
	        	
	        	getWordDataset(tableName);
	        	
	        	if ( ! isWordRecyclerInit ) {
	        		initWordRecyclerView();
	        		isWordRecyclerInit = true;
	        	}
	        	
	        	wordCardAdapter.updateDataset(wordsDataset);
	        	Toast.makeText(activity, "你点击的是:"+tableName, Toast.LENGTH_SHORT).show();   
	        	
	        	
	        	restoreWordRecyclerViewPosition(tableName);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});
          
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
	private static List<String> getWordbookList(){          
	    wordbookList = WordsManager.getTableList();  
	    return wordbookList;  
	}

    /**
	 * <p>Title: initRecyclerView</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-10 下午10:15:11
	 */
	private static void initWordRecyclerView() {
		
		isWordRecyclerInit = true;
		recyclerViewWord = (RecyclerView) view.findViewById(R.id.wordbook_recycler_list); 
	
		wordCardAdapter = new WordRecyclerViewAdapter(activity, tableName, wordsDataset);  
		recyclerViewWord.setAdapter(wordCardAdapter); 
		
		recyclerViewWidth = recyclerViewWord.getWidth();
		
		recyclerViewWord.addOnScrollListener(new OnScrollListener() {
			
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
	            	switch (wordCardAdapter.getItemViewType()) {
					case TYPE_VIEW_HORIZON:
						scrollToCenter(recyclerView);
						break;
					case TYPE_VIEW_VERTICAL:
					default:
						break;
					}
	            	
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
		
	    recyclerViewWord.setItemAnimator(new DefaultItemAnimator());  
	    recyclerViewWord.setHasFixedSize(true);
	
	    wordCardAdapter.setOnItemClickListener(new WordRecyclerViewAdapter.OnRecyclerViewItemClickListener() {
	
			@Override
			public void onItemClick(View view, int position, WordCls wordCls) {
				switch (wordCardAdapter.getItemViewType(position)) {
				case TYPE_VIEW_HORIZON:
					Log.i(tableName, "onItemClick——" + String.valueOf(position) + "——" + wordCls.getWord());
					//水平为全屏卡片模式，单击发音
					Toast.makeText(activity, "发音" + wordCls.getWord(), Toast.LENGTH_SHORT).show();
					break;
				case TYPE_VIEW_VERTICAL:
					//垂直为小卡片list模式，单击进入大卡片的该单词position，联网获取完整释义
				default:
					setRecyclerViewWordType(TYPE_VIEW_HORIZON);
					recyclerViewWord.scrollToPosition(position);
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
	 * <p>Title: scrollToCenter</p>
	 * <p>Description: </p>
	 * @param recyclerView
	 * @author bubble
	 * @date 2015-9-17
	 */
	private static void scrollToCenter(RecyclerView recyclerView) {
		firstVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
		lastVisibleItemPosition = ((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
		
		if ( firstVisibleItemPosition < lastVisibleItemPosition ) {
			recyclerViewWidth = recyclerView.getWidth();
			firstVisibleChild = recyclerView.getChildAt(0);
			firstChildVisibleWidth = firstVisibleChild.getRight();
			if ( firstChildVisibleWidth > ( recyclerViewWidth / 2 ) )
				recyclerView.smoothScrollToPosition(firstVisibleItemPosition);
			else if ( firstChildVisibleWidth < ( recyclerViewWidth / 2 ) )
				recyclerView.smoothScrollToPosition(lastVisibleItemPosition);
		}
	}

	/**
	 * <p>Title: saveInstanceState</p>
	 * <p>Description: </p>
	 * @param recyclerViewWord
	 * @author bubble
	 * @date 2015-9-11 
	 */
	private static void saveRecyclerViewPosition(String tableName) {
		recyclerViewState = recyclerViewWord.getLayoutManager().onSaveInstanceState();
		hmRecyclerViewState.put(tableName, recyclerViewState);
		
		firstVisibleItemPosition = ((LinearLayoutManager)recyclerViewWord.getLayoutManager()).findFirstVisibleItemPosition();
		
		if (firstVisibleItemPosition > -1) {
			firstVisibleChild = recyclerViewWord.getChildAt(0);
			//dy正，手指将列表往上拉
			//dy负，手指将列表往下拉
			dyTop = firstVisibleChild.getHeight() - firstVisibleChild.getBottom(); 
			dyBottom = recyclerViewWord.getHeight() - firstVisibleChild.getBottom();
			
			prefEditorSettings.putInt(KEY_RECYCLERVIEW_SCROLL_POSITION + tableName, firstVisibleItemPosition);
			prefEditorSettings.putInt(KEY_RECYCLERVIEW_SCROLL_DY_TOP + tableName, dyTop);
			prefEditorSettings.putInt(KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM + tableName, dyBottom);
			prefEditorSettings.commit();
		}
	}
	
	/**
	 * <p>Title: restoreInstanceState</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-11 
	 */
	private static void restoreWordRecyclerViewPosition(String tableName) {
		recyclerViewState = hmRecyclerViewState.get(tableName);
		if (recyclerViewState != null) {
			recyclerViewWord.getLayoutManager().onRestoreInstanceState(recyclerViewState);
		} else {
			if ( recyclerViewWord != null) {
				savedFirstVisiblePosition = prefSettings.getInt(KEY_RECYCLERVIEW_SCROLL_POSITION + tableName, 0);
				dyTop = prefSettings.getInt(KEY_RECYCLERVIEW_SCROLL_DY_TOP + tableName, 0);
				dyBottom = prefSettings.getInt(KEY_RECYCLERVIEW_SCROLL_DY_BOTTOM + tableName, 0);
				
				currentFirstVisiblePosition =  ((LinearLayoutManager)recyclerViewWord.getLayoutManager()).findFirstVisibleItemPosition();
				
				recyclerViewWord.scrollToPosition(savedFirstVisiblePosition);
				
				if(currentFirstVisiblePosition > -1) {
					if (currentFirstVisiblePosition >= savedFirstVisiblePosition) {	//savedFirstVisiblePosition在顶部
						recyclerViewWord.scrollBy(0, dyTop);
					} else if (currentFirstVisiblePosition < savedFirstVisiblePosition){	//savedFirstVisiblePosition在底部
						if (savedFirstVisiblePosition > 4)
							recyclerViewWord.scrollBy(0, dyBottom);				
						else {	//第一页的item用handler/smoothScrollBy会有跳转动作显示，暂时会找到合适的办法
							recyclerViewWord.scrollBy(0, recyclerViewWord.getHeight());
							recyclerViewWord.scrollToPosition(savedFirstVisiblePosition);
							recyclerViewWord.smoothScrollBy(0, dyTop);
						}
					} 
				} else {	//第一次打开，还未出现界面，会有滑动现象
					recyclerViewWord.scrollToPosition(savedFirstVisiblePosition);
					recyclerViewWord.smoothScrollBy(0, dyTop);
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
	private static void setRecyclerViewWordType(int type) {
		switch (type) {
		case TYPE_VIEW_HORIZON:
			prefEditorSettings.putInt(KEY_WORDBOOK_ORIENTATION, type);
			wordCardAdapter.setItemViewType(type);
			recyclerViewWord.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
			MainActivity.setSlidingAboveMode(SlidingMenu.TOUCHMODE_MARGIN);
			break;
		case TYPE_VIEW_VERTICAL:
		default:
			prefEditorSettings.putInt(KEY_WORDBOOK_ORIENTATION, type);
			wordCardAdapter.setItemViewType(type);
			recyclerViewWord.setLayoutManager(new LinearLayoutManager(activity)); 
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
	public static void showCardMenu(final View v, final int position, final WordCls wordCls) {
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
	private static void showEditWordDialog(final View v, final int position, final WordCls wordCls) {
		// 取得自定义View  
        LayoutInflater layoutInflater = LayoutInflater.from(activity);  
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
	private static void showChooseBookDialog(final WordCls wordCls) {
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
                    			hmIsDatasetEdit.put(tableList[i], true);
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
	private static void showDeleteWordDialog(final WordCls wordCls) {
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
	private static void deleteWord(final WordCls wordCls) {
		wordCardAdapter.deleteItem(wordCls);
    	WordsManager.deleteWord(tableName, wordCls.getWord());
    	
    	saveRecyclerViewPosition(tableName);
    	
    	hmIsDatasetEdit.put(tableName, true);
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
	private static void editWord(final int position, final WordCls wordCls,
			final EditText edtWord, final EditText edtPhonetic,
			final EditText edtDefinition) {
		wordCls.setWord(edtWord.getText().toString());
		wordCls.setPhonetic(edtPhonetic.getText().toString());
		wordCls.setDefinition(edtDefinition.getText().toString());
		
		WordsManager.editWord(tableName, wordCls);
		
		wordCardAdapter.updateItem(position, wordCls);
		
		saveRecyclerViewPosition(tableName);
		
		hmIsDatasetEdit.put(tableName, true);
	}
    
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-11 下午11:06:24
	 */
	@Override
	public void onPause() {
		super.onPause();
		Log.i(getTag(), "WordBookFragment——onPause");
		saveRecyclerViewPosition(tableName);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-15 下午8:52:49
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		Log.i(getTag(), "WordBookFragment——onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-15 上午10:48:15
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(getTag(), "WordBookFragment——onDestroy");
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-19 下午12:56:24
	 */
	@Override
	public void onStartDrag(ViewHolder viewHolder) {
		touchHelper.startDrag(viewHolder);
	}

	/**
	 * <p>Title: createBook</p>
	 * <p>Description: </p>
	 * @param edtCreateBook
	 * @author bubble
	 * @date 2015-9-19 下午2:23:51
	 */
	private void createBook(String tableName) {
		WordsManager.createTable(tableName);
		editBookRecyclerViewAdapter.addItem(tableName);
		recyclerViewEditBook.setLayoutManager(new MyLayoutManager(activity));
		bookMenuRecyclerViewAdapter.addItem(tableName);
	}
	
	public static void notifyBookMenuItemMoved(int fromPosition, int toPosition) {
		if ( bookMenuRecyclerViewAdapter != null )
			bookMenuRecyclerViewAdapter.onItemMove(fromPosition, toPosition);
	}
	
	public static void notifyBookMenuItemRemoved(int position) {
		if ( bookMenuRecyclerViewAdapter != null ) {
			bookMenuRecyclerViewAdapter.onItemDismiss(position);
			
			if ( tvDropdownPopMenu.getText().toString().matches(getWordbookList().get(position))) {
				if ( getWordbookList().size() > 1) {
					String newTableName;
					if (position == 0)
						newTableName = getWordbookList().get(1);
					else 
						newTableName = getWordbookList().get(0);
					
					tvDropdownPopMenu.setText(newTableName);
					
					prefEditorSettings.putString(KEY_WORDBOOK_SELECTED_TABLENAME, newTableName);
					prefEditorSettings.commit();
					
					getWordDataset(newTableName);
					
					if ( ! isWordRecyclerInit ) {
						initWordRecyclerView();
						isWordRecyclerInit = true;
					}
					
					wordCardAdapter.updateDataset(wordsDataset);
					
					restoreWordRecyclerViewPosition(newTableName);				
				} else {
					tvDropdownPopMenu.setText("");
					
					prefEditorSettings.putString(KEY_WORDBOOK_SELECTED_TABLENAME, "");
					prefEditorSettings.commit();
					
					wordsDataset.clear();
					wordCardAdapter.updateDataset(wordsDataset);
				}
			} 
			
			WordsManager.deleteTable(getWordbookList().get(position));
		}
	}
}
