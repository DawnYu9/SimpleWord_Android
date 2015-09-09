package com.bubble.simpleword.wordbook;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordsManager;

/**
 * <p>Title: MyAdapter</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-11 下午5:44:53
 */
public class WordCardAdapter extends RecyclerView.Adapter<WordCardAdapter.ViewHolder> {  

	private Context mContext;  
	private List<WordCls> wordsList;  
	private String tableName;
	private OnRecyclerViewItemClickListener onItemClickListener = null;
	private OnRecyclerViewItemLongClickListener onItemLongClickListener = null;
	
	public WordCardAdapter( Context context , String tableName, List<WordCls> wordsList) {  
	    this.mContext = context;  
	    this.tableName = tableName;
	    this.wordsList = wordsList;  
	}  
	
	/**
	 * @date 2015-9-9 下午12:42:41
	 */
	public static interface OnRecyclerViewItemClickListener {
	    void onItemClick(View view , int position, WordCls wordCls);
	}
	
	/**
	 * <p>Title: setOnItemClickListener</p>
	 * <p>Description: </p>
	 * @param listener
	 * @author bubble
	 * @date 2015-9-9 下午12:43:02
	 */
	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.onItemClickListener = listener;
	}
	
	/**@date 2015-9-9 下午7:42:22
	 *
	 */
	public static interface OnRecyclerViewItemLongClickListener {
		void onItemLongClick(View view , int position, WordCls wordCls);
	}
	
	/**
	 * <p>Title: setOnItemLongClickListener</p>
	 * <p>Description: </p>
	 * @param listener
	 * @author bubble
	 * @date 2015-9-9 下午7:45:53
	 */
	public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
		this.onItemLongClickListener = listener;
	}
    
	@Override  
	public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int position )  
	{  
	    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wordbook_item_cardview, viewGroup, false);  
	    final ViewHolder viewHolder = new ViewHolder(v);
	    
	    v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
		            onItemClickListener.onItemClick(v, viewHolder.getLayoutPosition(), (WordCls)v.getTag());
		        }
			}
		});
	    
	    v.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (onItemLongClickListener != null) {
		            onItemLongClickListener.onItemLongClick(v, viewHolder.getLayoutPosition(), (WordCls)v.getTag());
		        }
				return false;
			}
		});
	    
	    return viewHolder;  
	}  
	
//	/**
//	 * <p>Description: </p>
//	 * @author bubble
//	 * @date 2015-9-9 下午12:43:14
//	 */
//	@Override
//    public void onClick(View v) {
//        if (onItemClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            onItemClickListener.onItemClick(v,(WordCls)v.getTag());
//        }
//    }
//	
//	/**
//	 * <p>Description: </p>
//	 * @author bubble
//	 * @date 2015-9-9 下午7:47:32
//	 */
//	@Override
//	public boolean onLongClick(View v) {
//		if (onItemLongClickListener != null) {
//            //注意这里使用getTag方法获取数据
//            onItemLongClickListener.onItemLongClick(v,(WordCls)v.getTag());
//        }
//		return false;
//	}
	
	/**
	 * <p>Title: addItem</p>
	 * <p>Description: </p>
	 * @param position
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午2:21:47
	 */
	public void addItem(int position, WordCls wordCls) {
		wordsList.add(position, wordCls);
        notifyItemInserted(position);
    }
	/**
	 * <p>Title: addItem</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午2:24:11
	 */
	public void addItem(WordCls wordCls) {
		wordsList.add(wordCls);
		int position = wordsList.indexOf(wordCls);
		notifyItemInserted(position);
	}
	
	/**
	 * <p>Title: removeItem</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午2:20:55
	 */
	public void removeItem(WordCls wordCls) {
	    int position = wordsList.indexOf(wordCls);
	    wordsList.remove(position);
	    notifyItemRemoved(position);
	}
	
	/**
	 * <p>Title: updateItem</p>
	 * <p>Description: </p>
	 * @param position
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-10 上午12:13:31
	 */
	public void updateItem(int position, WordCls wordCls) {
		wordsList.set(position, wordCls);
		notifyItemChanged(position);
	}
	
	/**
	 * <p>Title: updateList</p>
	 * <p>Description: </p>
	 * @param wordsList
	 * @author bubble
	 * @date 2015-9-9 下午2:20:58
	 */
	public void updateList(List<WordCls> newList) {
		wordsList = newList;
		notifyDataSetChanged();
	}
	
	@Override  
	public void onBindViewHolder( ViewHolder viewHolder, int position )  
	{  
		viewHolder.itemView.setTag(wordsList.get(position));
		
		WordCls wordCls = wordsList.get(position);  
		
	    viewHolder.tvWord.setText(wordCls.getWord());  
	    viewHolder.tvPhonetic.setText(wordCls.getPhonetic());  
	    viewHolder.tvDefinition.setText(wordCls.getDefinition());  
	    viewHolder.imgBtnAddDel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
			}
		});
	}  

	@Override  
	public int getItemCount()  
	{  
	    // 返回数据总数  
	    return wordsList == null ? 0 : wordsList.size();  
	}  

	public static class ViewHolder extends RecyclerView.ViewHolder  {
		public View view;
		public TextView tvWord;  
		public TextView tvPhonetic;  
		public TextView tvDefinition;  
		public ImageButton imgBtnAddDel;
		
	    public ViewHolder( View v) {  
	        super(v); 
            this.view = v;
            
	        tvWord = (TextView) v.findViewById(R.id.wordcard_tv_word);  
	        tvPhonetic = (TextView) v.findViewById(R.id.wordcard_tv_phonetic);  
	        tvDefinition = (TextView) v.findViewById(R.id.wordcard_tv_definition);  
	        imgBtnAddDel = (ImageButton) v.findViewById(R.id.wordcard_imgbtn_add_delete);  
	    }

	}  
}  