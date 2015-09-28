package com.bubble.simpleword.adapter;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bubble.simpleword.R;

public class BookMenuRecyclerViewAdapter extends Adapter<BookMenuRecyclerViewAdapter.ViewHolder> {
	
	private Context context;
	private List<String> bookList;
	
	private OnRecyclerViewItemClickListener onItemClickListener = null;
	private View v;
	private ViewHolder viewHolder;
	private String tableName;
	private int position;
	private String prev;
	
	
	public static interface OnRecyclerViewItemClickListener {
	    void onItemClick(View view , int position, String tableName);
	}
	
	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.onItemClickListener = listener;
	}
	
	public BookMenuRecyclerViewAdapter(Context context, List<String> bookList) {
		super();
		this.context = context;
		this.bookList = bookList;
	}

	public class ViewHolder extends RecyclerView.ViewHolder {

		public TextView tvBook;
		
		public ViewHolder(View v) {
			super(v);
			tvBook = (TextView) v.findViewById(R.id.wordbook_recyclerview_edit_book_item_tv_book);
		}

	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-18 下午3:58:38
	 */
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordbook_recyclerview_item_edit_book, parent, false);
		viewHolder = new ViewHolder(v);
		
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
	public void onBindViewHolder(final ViewHolder viewHolder, int position) {
		tableName = bookList.get(position);
		viewHolder.itemView.setTag(tableName);
		viewHolder.tvBook.setText(tableName);
	}

	@Override
	public int getItemCount() {
		return bookList == null ? 0 : bookList.size();
	}
	
	public void removeItem(String tableName) {
	    position = bookList.indexOf(tableName);
	    bookList.remove(position);
	    notifyItemRemoved(position);
	}
	
	public void addItem(String tableName) {
		bookList.add(tableName);
		position = bookList.indexOf(tableName);
		notifyItemInserted(position);
	}
	
	public void updateItem(int position, String tableName) {
		bookList.set(position, tableName);
		notifyItemChanged(position);
	}
	
	public void onItemMove(int fromPosition, int toPosition) {
		prev = bookList.remove(fromPosition);
        bookList.add(toPosition > fromPosition ? toPosition - 1 : toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
	}

	public void onItemDismiss(int position) {
		bookList.remove(position);
	    notifyItemRemoved(position);
	}
	
	public void updateDataset(List<String> newList) {
		bookList = newList;
		notifyDataSetChanged();
	}
	
	public void setItemBackground(int position) {
		
	}
}
