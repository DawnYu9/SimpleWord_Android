package com.bubble.simpleword.wordbook;

import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bubble.simpleword.R;

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

	private List<WordsClass> wordsList;  
	
	private Context mContext;  
	
	public WordCardAdapter( Context context , List<WordsClass> wordsList)  
	{  
	    this.mContext = context;  
	    this.wordsList = wordsList;  
	}  
	
	@Override  
	public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int position )  
	{  
	    // 给ViewHolder设置布局文件  
	    View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wordbook_item_cardview, viewGroup, false);  
	    return new ViewHolder(v);  
	}  
	
	@Override  
	public void onBindViewHolder( ViewHolder viewHolder, int i )  
	{  
	    // 给ViewHolder设置元素  
	    WordsClass word = wordsList.get(i);  
	    viewHolder.mTextView.setText(word.toString());  
	}  

@Override  
	public int getItemCount()  
	{  
	    // 返回数据总数  
	    return wordsList == null ? 0 : wordsList.size();  
	}  

// 重写的自定义ViewHolder  
	public static class ViewHolder extends RecyclerView.ViewHolder  
	{  
	    public TextView mTextView;  
	
	
	    public ViewHolder( View v )  
	    {  
	        super(v);  
	        mTextView = (TextView) v.findViewById(R.id.wordcard_tv);  
	    }  
	}  
}  