package com.bubble.simpleword.activity;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Source;

import org.json.JSONObject;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bubble.simpleword.R;
import com.bubble.simpleword.adapter.WordRecyclerViewAdapter.HorizonViewHolder;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;

/**
 * <p>Title: Searchable</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-9-17 下午10:15:44
 */
public class SearchResultsActivity extends Activity implements OnClickListener{

	private WordCls wordCls = null;
	private TextView tvBigHint;
	private TextView tvSmallHint;

	private LinearLayout progressBarSmall;
	private LinearLayout progressBarBig;
	
	private TextView tvWord;
	private TextView tvPhonetic;
	private TextView tvDefinition;
	
	private TextView tvDefEN;
	private TextView tvDefCN;
	private LinearLayout llCompleteWord;
	
	private LinearLayout llChinese;
	private TextView tvChinese;
	private TextView tvWordset;
	
	private ParseJsonTask parseJsonTask;
	
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public SearchResultsActivity() {
	}

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_ll_english);
		
		
		tvWord = (TextView) findViewById(R.id.search_tv_word);
		tvPhonetic = (TextView) findViewById(R.id.search_tv_phonetic);
		tvDefinition = (TextView) findViewById(R.id.search_tv_definition);
		
		tvDefEN = (TextView) findViewById(R.id.search_tv_def_en);
		tvDefCN = (TextView) findViewById(R.id.search_tv_def_cn);
		llCompleteWord= (LinearLayout) findViewById(R.id.search_ll_complete_word);
		
		llChinese = (LinearLayout) findViewById(R.id.search_ll_ch);
		tvChinese = (TextView) findViewById(R.id.search_ch_tv_chinese);
		tvWordset = (TextView) findViewById(R.id.search_ch_tv_eng_words);
		
		progressBarSmall = (LinearLayout) findViewById(R.id.search_small_progressbar);
		progressBarBig = (LinearLayout) findViewById(R.id.search_big_progressbar);
		tvBigHint = (TextView) findViewById(R.id.search_tv_big_hint);
		tvSmallHint = (TextView) findViewById(R.id.search_tv_small_hint);
		
//		if ( wordCls != null ) {
//			if ( wordCls.isLoaded() ) {
//				tvSmallHint.setVisibility(View.INVISIBLE);
//				tvBigHint.setVisibility(View.INVISIBLE);
//				tvDefEN.setText(wordCls.getDefinitionEN());
//				tvDefCN.setText(wordCls.getDefinitionCN());
//			} else {
//				tvSmallHint.setVisibility(View.VISIBLE);
//			}
//		}
		
		tvSmallHint.setOnClickListener(this);
		tvBigHint.setOnClickListener(this);
		
        handleIntent(getIntent());
    }
	
	 /**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-22 下午5:18:03
	 */
	@Override
	protected void onResume() {
		super.onResume();
	}
	
    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }
 
    private void handleIntent(Intent intent) {
 
//        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
//        	String query = intent.getStringExtra(SearchManager.QUERY);
        	Bundle bundle = getIntent().getExtras();    
            String query = bundle.getString("query");
            //use the query to search your data somehow
            if ( query.matches("[a-zA-Z]+") ) {
            	wordCls = WordsManager.queryWordEn("GraduateWords", query);
            	if ( wordCls != null ) {
            		tvBigHint.setVisibility(View.INVISIBLE);
	            	tvWord.setText(wordCls.getWord());
	            	tvPhonetic.setText(wordCls.getPhonetic());
	            	tvDefinition.setText(wordCls.getDefinition());
            	} else {
            		wordCls = new WordCls();
            		wordCls.setWord(query);
            		tvBigHint.setVisibility(View.VISIBLE);
            		tvBigHint.setText("本地未搜索到该单词。请点击联网查询");
            	}
            } else if ( query.matches("[\u4e00-\u9fa5]+") ) {
            	List<WordCls> wordClsList = WordsManager.queryWordCh("GraduateWords", query);
            	if ( wordClsList != null) {
            		tvBigHint.setVisibility(View.INVISIBLE);
            		tvSmallHint.setVisibility(View.INVISIBLE);
            		llChinese.setVisibility(View.VISIBLE);
            		StringBuilder sb = new StringBuilder();
            		for ( int i = 0; i < wordClsList.size(); i++) {
            			wordCls = wordClsList.get(i);
            			sb.append(wordCls.getWord()).append("\n");
            		}
            		
            		tvChinese.setText(query);
            		tvWordset.setText(sb.toString());
            	} else {
            		tvBigHint.setVisibility(View.VISIBLE);
            	}
            }
//        }
        
        // 获得额外递送过来的值
        Bundle appData = intent.getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            String testValue = appData.getString("KEY");
            System.out.println("extra data = " + testValue);
        }
    }
    
    private void search(String query) {
        // TODO 自动生成的方法存根
        Toast.makeText(this, "do search", 0).show();
    }
    
    class ParseJsonTask extends AsyncTask<String, Void, Boolean> {
    	LinearLayout progressBar;
		WordCls wordCls;
		Handler handler;
		
		String phonetic;
		String definition;
		String definitionEN;
		String definitionCN;
		String audioUrlUS;
		
		
        public ParseJsonTask(LinearLayout progressBar,
				WordCls wordCls, Handler handler) {
			super();
			this.progressBar = progressBar;
			this.wordCls = wordCls;
			this.handler = handler;
		}

		@Override
        protected void onPreExecute() {
			progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
        	Message msg = handler.obtainMessage();
        	
        	switch (progressBar.getId()) {
        	case R.id.search_big_progressbar:
        		progressBar.setVisibility(View.INVISIBLE);
			case R.id.search_small_progressbar:
				progressBarSmall.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
            if (result) {
            	if ( progressBar.getId() == R.id.search_big_progressbar) {
            		wordCls.setPhonetic(phonetic);
            		wordCls.setDefinition(definition);
            	}
            	wordCls.setDefinitionEN(definitionEN);
            	wordCls.setDefinitionCN(definitionCN);
            	wordCls.setAudioUrlUS(audioUrlUS);
            	wordCls.setLoaded(true);
//            	WordsManager.addWordLoadInfo(tableName, wordCls);
            	
            	msg.what = 1;
            }else {
            	msg.what = 0;
            	Log.i(wordCls.getWord(), "获取失败");
            }
            
            handler.sendMessage(msg);
            
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String path = "https://api.shanbay.com/bdc/search/?word=" + wordCls.getWord();
            try {
            	URL url = new URL(path);
                Source source = new Source(url.openConnection());	//jericho-html-3.1.jar
                String jsonstr = source.toString();
                 
                JSONObject jsonObj = new JSONObject(jsonstr);
                 
                JSONObject data = jsonObj.getJSONObject("data");
                 
                JSONObject defEN = data.getJSONObject("en_definition");
                definitionEN = defEN.getString("pos") + "." + defEN.getString("defn"); 
                 
                JSONObject defCN = data.getJSONObject("cn_definition");
                definitionCN = defCN.getString("pos") + defCN.getString("defn"); 
                
                audioUrlUS = data.getString("us_audio");
                 
                
                phonetic = "[" + data.getString("pronunciation") + "]";
                
                
                definition = data.getString("definition");
                
                return true;
            } catch (Exception e) {
            	Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
                return false;
            }
    	}
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-22 下午5:30:59
	 */
	@Override
	public void onClick(final View v) {
		if ( wordCls != null ) {
			if ( ! wordCls.isLoaded() ) {
				Handler handler = new Handler(){
					
					@Override
					public void handleMessage(Message msg) {
						switch (msg.what) {
						case 1:
							switch (v.getId()) {
							case R.id.search_tv_big_hint:
								tvBigHint.setVisibility(View.INVISIBLE);
								tvSmallHint.setVisibility(View.INVISIBLE);
								tvWord.setText(wordCls.getWord());
				            	tvPhonetic.setText(wordCls.getPhonetic());
				            	tvDefinition.setText(wordCls.getDefinition());
								break;
							case R.id.search_tv_small_hint:
								tvSmallHint.setVisibility(View.INVISIBLE);
								break;
							default:
								break;
							}
							tvDefEN.setText(wordCls.getDefinitionEN());
							tvDefCN.setText(wordCls.getDefinitionCN());
							break;
						case 0:
							switch (v.getId()) {
							case R.id.search_tv_big_hint:
								tvBigHint.setText("数据获取失败，请重试");
								break;
							case R.id.search_tv_small_hint:
								tvSmallHint.setText("数据获取失败，请重试");
								break;
							default:
								break;
							}
							break;
						default:
							break;
						}
					}
					
				};
				switch (v.getId()) {
				case R.id.search_tv_big_hint:
					parseJsonTask = new ParseJsonTask(progressBarBig, wordCls, handler);
					break;
				case R.id.search_tv_small_hint:
					parseJsonTask = new ParseJsonTask(progressBarSmall, wordCls, handler);
					break;
				default:
					break;
				}
				if ( parseJsonTask != null )
					parseJsonTask.execute();
			} 
		}
	}
}
