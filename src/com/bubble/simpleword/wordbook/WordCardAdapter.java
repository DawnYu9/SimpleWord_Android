package com.bubble.simpleword.wordbook;

import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
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
public class WordCardAdapter extends RecyclerView.Adapter<WordCardAdapter.ViewHolder>  
{  

	private Context mContext;  
	private List<WordCls> wordsList;  
	private String tableName;
	
	public WordCardAdapter( Context context , String tableName, List<WordCls> wordsList)  
	{  
	    this.mContext = context;  
	    this.tableName = tableName;
	    this.wordsList = wordsList;  
	}  
	
	@Override  
	public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int position )  
	{  
	    // 给ViewHolder设置布局文件  
	    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wordbook_item_cardview, viewGroup, false);  
	    return new ViewHolder(v, tableName);  
	}  
	
	@Override  
	public void onBindViewHolder( ViewHolder viewHolder, int position )  
	{  
		final WordCls word = wordsList.get(position);  

	    viewHolder.tvWord.setText(word.getWord());  
	    viewHolder.tvPhonetic.setText(word.getPhonetic());  
	    viewHolder.tvDefinition.setText(word.getDefinition());  
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

	public static class ViewHolder extends RecyclerView.ViewHolder implements OnLongClickListener,OnMenuItemClickListener {
		public View view;
		public TextView tvWord;  
		public TextView tvPhonetic;  
		public TextView tvDefinition;  
		public ImageButton imgBtnAddDel;
	
		public String tableName;
		
	    public ViewHolder( View v, String tableName) {  
	        super(v); 
	        this.tableName = tableName;
            this.view = v;
            v.setOnLongClickListener(this);
            
	        tvWord = (TextView) v.findViewById(R.id.wordcard_tv_word);  
	        tvPhonetic = (TextView) v.findViewById(R.id.wordcard_tv_phonetic);  
	        tvDefinition = (TextView) v.findViewById(R.id.wordcard_tv_definition);  
	        imgBtnAddDel = (ImageButton) v.findViewById(R.id.wordcard_imgbtn_add_delete);  
	    }


		/**
		 * <p>Description: </p>
		 * @author bubble
		 * @date 2015-9-8 下午4:40:03
		 */
		@Override
		public boolean onLongClick(View v) {
			if ( v == this.view ) {
				showCardMenu(v);
			}
			return false;
		}

		/**
		 * <p>Title: showCardMenu</p>
		 * <p>Description: </p>
		 * @param v
		 * @author bubble
		 * @date 2015-9-8 下午6:15:18
		 */
		public void showCardMenu(View v) {
			PopupMenu popMenu = new PopupMenu(v.getContext(), v);
			popMenu.inflate(R.menu.wordbook_card_menu);
			popMenu.setOnMenuItemClickListener(this);
			popMenu.show();
		}  
		
		
		/**
		 * <p>Description: </p>
		 * @author bubble
		 * @date 2015-9-8
		 */
		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.wordbook_card_menu_edit:
				break;
			case R.id.wordbook_card_menu_delete:
				showDeleteDialog();
				break;
			case R.id.wordbook_card_menu_addtobook:
				break;
			default:
				break;
			}
		    return true;
		}
		
		/**
		 * <p>Title: showDeleteDialog</p>
		 * <p>Description: </p>
		 * @author bubble
		 * @date 2015-9-8 下午5:25:58
		 */
		private void showDeleteDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setTitle("弹出警告框");
            builder.setMessage("确定删除吗？");
            
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
//                	WordsManager.deleteWord(tableName, tvWord.getText().toString());
                	Toast.makeText(view.getContext(), "即将删除" + tvWord.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            builder.show();
		}

	}  
}  