package com.bubble.simpleword.adapter;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.drag.ItemTouchHelperAdapter;
import com.bubble.simpleword.drag.ItemTouchHelperViewHolder;
import com.bubble.simpleword.fragment.SettingsFragment;
import com.bubble.simpleword.fragment.WordBookFragment;

/**
 * <p>Title: EditBookRecyclerViewAdapter</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-9-18 下午3:20:53
 */
public class EditBookRecyclerViewAdapter extends Adapter<EditBookRecyclerViewAdapter.ViewHolder> 
			implements ItemTouchHelperAdapter{
	
	private Context context;
	private List<String> bookList;
	
	private OnRecyclerViewItemClickListener onItemClickListener = null;
	
	private final OnStartDragListener mDragStartListener;
	
	/**
	 * @date 2015-9-19 下午9:01:03
	 */
	public static interface OnRecyclerViewItemClickListener {
	    void onItemClick(View view , int position, String tableName);
	}
	
	/**
	 * <p>Title: setOnItemClickListener</p>
	 * <p>Description: </p>
	 * @param listener
	 * @author bubble
	 * @date 2015-9-19 下午9:01:08
	 */
	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.onItemClickListener = listener;
	}
	
	/**
	 * @date 2015-9-19
	 */
	public interface OnStartDragListener {
	    void onStartDrag(RecyclerView.ViewHolder viewHolder);
	}
		
	public EditBookRecyclerViewAdapter(Context context, List<String> bookList, OnStartDragListener dragStartListener) {
		super();
		this.context = context;
		this.bookList = bookList;
		this.mDragStartListener = dragStartListener;
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

		public TextView tvBook;
		
		public ViewHolder(View v) {
			super(v);
			tvBook = (TextView) v.findViewById(R.id.wordbook_recyclerview_edit_book_item_tv_book);
		}

		@Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
		
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-18 下午3:58:38
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordbook_recyclerview_item_edit_book, parent, false);
		final ViewHolder viewHolder = new ViewHolder(v);
		
		v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				if (onItemClickListener != null) {
		            onItemClickListener.onItemClick(v, viewHolder.getLayoutPosition(), (String)v.getTag());
		        }
			}
		});
		
		return viewHolder;
	}

	/**                                          
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-18 下午3:58:38
	 */
	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		String tableName = bookList.get(position);
		holder.itemView.setTag(tableName);
		holder.tvBook.setText(tableName);
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-18 下午3:58:38
	 */
	@Override
	public int getItemCount() {
		return bookList == null ? 0 : bookList.size();
	}

	/**
	 * <p>Title: deleteItem</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-18 下午9:22:45
	 */
	public void deleteItem(String tableName) {
	    int position = bookList.indexOf(tableName);
	    bookList.remove(position);
	    notifyItemRemoved(position);
	}

	/**
	 * <p>Title: addItem</p>
	 * <p>Description: </p>
	 * @param tableName
	 * @author bubble
	 * @date 2015-9-19 下午1:42:28
	 */
	public void addItem(String tableName) {
		bookList.add(tableName);
		int position = bookList.indexOf(tableName);
		notifyItemInserted(position);
	}
	
	public void updateItem(int position, String tableName) {
		bookList.set(position, tableName);
		notifyItemChanged(position);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-18 下午10:15:46
	 */
	@Override
	public void onItemMove(int fromPosition, int toPosition) {
//		Collections.swap(bookList, fromPosition, toPosition);
//	    notifyItemMoved(fromPosition, toPosition);
		String prev = bookList.remove(fromPosition);
        bookList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
        WordBookFragment.notifyBookMenuItemMoved(fromPosition, toPosition);
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-18 下午10:15:46
	 */
	@Override
	public void onItemDismiss(int position) {
		String tableName = bookList.get(position);
		bookList.remove(position);
	    notifyItemRemoved(position);
	    WordBookFragment.notifyBookMenuItemRemoved(position);
	    SettingsFragment.notifySpinnerWordbookItemRemoved(position);
	}
	
	public void updateDataset(List<String> newList) {
		bookList = newList;
		notifyDataSetChanged();
	}
}
