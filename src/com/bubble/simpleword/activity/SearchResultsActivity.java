package com.bubble.simpleword.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bubble.simpleword.R;

/**
 * <p>Title: Searchable</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-9-17 下午10:15:44
 */
public class SearchResultsActivity extends Activity {

	private ListView listview;
	private TextView tvClear;
	private TextView tv;
	
	
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public SearchResultsActivity() {
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_ll);
		
		listview = (ListView) findViewById(R.id.search_listview);
		
		tvClear = (TextView) findViewById(R.id.search_tv_clear);
		
		tv = (TextView) findViewById(R.id.search_tv);
		
        handleIntent(getIntent());
    }
 
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
 
    private void handleIntent(Intent intent) {
 
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            tv.setText(query);
            //use the query to search your data somehow
        }
        
        // 获得额外递送过来的值
        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            String testValue = appData.getString("KEY");
            System.out.println("extra data = " + testValue);
        }
    }
    
    private void search(String query) {
        // TODO 自动生成的方法存根
        TextView textView = (TextView) findViewById(R.id.search_tv);
        textView.setText(query);
        Toast.makeText(this, "do search", 0).show();
    }
}
