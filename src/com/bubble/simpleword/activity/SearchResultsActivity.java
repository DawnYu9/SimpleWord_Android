package com.bubble.simpleword.activity;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.util.Util;

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
	private ActionBar mActionBar;
	
	public static RequestQueue mQueue;  
	private String url;
	private JsonObjectRequest jsonRequest;
	
	private JSONObject data;
	private JSONObject defEN;
	private String definitionEN; 
	private JSONObject defCN;
	private String definitionCN; 
	private String audioUrlUS;
	private String phonetic;
	private String definition;

	private WordCls wordCls = null;
	private TextView tvBigHint;
	private TextView tvSmallHint;

	private LinearLayout progressBarSmall;
	private LinearLayout progressBarBig;
	
	private LinearLayout llEnglish;
	private TextView tvWord;
	private TextView tvPhonetic;
	private TextView tvDefinition;
	
	private TextView tvDefEN;
	private TextView tvDefCN;
	private LinearLayout llCompleteWord;
	
	private LinearLayout llChinese;
	private TextView tvChinese;
	private TextView tvWordset;

	private Bundle bundle;

	private String query;

	private List<WordCls> wordClsList;

	private StringBuilder sb;

	private Bundle appData;

	private String testValue;
	
//	private ParseJsonTask parseJsonTask;
	
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
		mQueue = Volley.newRequestQueue(this);  
		
		mActionBar = getActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);//show back button
		mActionBar.setDisplayShowHomeEnabled(false);//show app's img
		
		llEnglish = (LinearLayout) findViewById(R.id.search_ll_en);
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
		llEnglish.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Util.pronounceWord(wordCls, getApplicationContext());
			}
		});
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
 
    	bundle = getIntent().getExtras();    
        query = bundle.getString("query");
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
        	wordClsList = WordsManager.queryWordCh("GraduateWords", query);
        	if ( wordClsList != null) {
        		tvBigHint.setVisibility(View.INVISIBLE);
        		tvSmallHint.setVisibility(View.INVISIBLE);
        		llChinese.setVisibility(View.VISIBLE);
        		sb = new StringBuilder();
        		for ( int i = 0; i < wordClsList.size(); i++) {
        			wordCls = wordClsList.get(i);
        			sb.append(wordCls.getWord()).append("\n");
        		}
        		if ( ! sb.toString().isEmpty() ) {
            		tvChinese.setText(query);
            		tvWordset.setText(sb.toString());
        		} else {
        			tvBigHint.setVisibility(View.VISIBLE);
        			tvBigHint.setText("本地未搜索到该单词。");
        		}
        	} else {
        		tvBigHint.setVisibility(View.VISIBLE);
        		tvBigHint.setText("本地未搜索到该单词。");
        	}
        }
//        }
        
        appData = intent.getBundleExtra(SearchManager.APP_DATA);
        if (appData != null) {
            testValue = appData.getString("KEY");
        }
    }
    
    private void search(String query) {
        // TODO 自动生成的方法存根
        Toast.makeText(this, "do search", 0).show();
    }
    
//    class ParseJsonTask extends AsyncTask<String, Void, Boolean> {
//    	LinearLayout progressBar;
//		WordCls wordCls;
//		Handler handler;
//		
//		String phonetic;
//		String definition;
//		String definitionEN;
//		String definitionCN;
//		String audioUrlUS;
//		
//		
//        public ParseJsonTask(LinearLayout progressBar,
//				WordCls wordCls, Handler handler) {
//			super();
//			this.progressBar = progressBar;
//			this.wordCls = wordCls;
//			this.handler = handler;
//		}
//
//		@Override
//        protected void onPreExecute() {
//			progressBar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//        	Message msg = handler.obtainMessage();
//        	
//        	switch (progressBar.getId()) {
//        	case R.id.search_big_progressbar:
//        		progressBar.setVisibility(View.INVISIBLE);
//			case R.id.search_small_progressbar:
//				progressBarSmall.setVisibility(View.INVISIBLE);
//				break;
//			default:
//				break;
//			}
//            if (result) {
//            	if ( progressBar.getId() == R.id.search_big_progressbar) {
//            		wordCls.setPhonetic(phonetic);
//            		wordCls.setDefinition(definition);
//            	}
//            	wordCls.setDefinitionEN(definitionEN);
//            	wordCls.setDefinitionCN(definitionCN);
//            	wordCls.setAudioUrlUS(audioUrlUS);
//            	wordCls.setLoaded(true);
////            	WordsManager.addWordLoadInfo(tableName, wordCls);
//            	
//            	msg.what = 1;
//            }else {
//            	msg.what = 0;
//            	Log.i(wordCls.getWord(), "获取失败");
//            }
//            
//            handler.sendMessage(msg);
//            
//            super.onPostExecute(result);
//        }
//
//        @Override
//        protected Boolean doInBackground(String... params) {
//            String path = "https://api.shanbay.com/bdc/search/?word=" + wordCls.getWord();
//            try {
//            	URL url = new URL(path);
//                Source source = new Source(url.openConnection());	//jericho-html-3.1.jar
//                String jsonstr = source.toString();
//                 
//                JSONObject jsonObj = new JSONObject(jsonstr);
//                 
//                JSONObject data = jsonObj.getJSONObject("data");
//                 
//                JSONObject defEN = data.getJSONObject("en_definition");
//                definitionEN = defEN.getString("pos") + "." + defEN.getString("defn"); 
//                 
//                JSONObject defCN = data.getJSONObject("cn_definition");
//                definitionCN = defCN.getString("pos") + defCN.getString("defn"); 
//                
//                audioUrlUS = data.getString("us_audio");
//                 
//                
//                phonetic = "[" + data.getString("pronunciation") + "]";
//                
//                
//                definition = data.getString("definition");
//                
//                return true;
//            } catch (Exception e) {
//            	Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//    	}
//	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-22 下午5:30:59
	 */
	@Override
	public void onClick(final View v) {
		if ( wordCls != null ) {
			if ( ! wordCls.isLoaded() ) {
				
				switch (v.getId()) {
				case R.id.search_tv_big_hint:
					progressBarBig.setVisibility(View.VISIBLE);
					break;
				case R.id.search_tv_small_hint:
					progressBarSmall.setVisibility(View.VISIBLE);
					break;
				default:
					break;
				}
				url = MainActivity.URL_SHANBAY + wordCls.getWord(); 
				  
				jsonRequest = new JsonObjectRequest
				        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
				            @Override
				            public void onResponse(JSONObject response) {
				                // the response is already constructed as a JSONObject!
				                try {
				                    data = response.getJSONObject("data");
				                    
				                    defEN = data.getJSONObject("en_definition");
				                    definitionEN = defEN.getString("pos") + "." + defEN.getString("defn"); 
				                     
				                    defCN = data.getJSONObject("cn_definition");
				                    definitionCN = defCN.getString("pos") + defCN.getString("defn"); 
				                    
				                    audioUrlUS = data.getString("us_audio");
				                     
				                    
				                    phonetic = "[" + data.getString("pronunciation") + "]";
				                    
				                    definition = data.getString("definition");
				                    
				                    switch (v.getId()) {
				    				case R.id.search_tv_big_hint:
				    					tvBigHint.setVisibility(View.INVISIBLE);
				    					progressBarBig.setVisibility(View.INVISIBLE);
				    					
				    					wordCls.setPhonetic(phonetic);
				    					wordCls.setDefinition(definition);
				    					
				    					tvWord.setText(wordCls.getWord());
				    					tvPhonetic.setText(wordCls.getPhonetic());
				    					tvDefinition.setText(wordCls.getDefinition());
				    				case R.id.search_tv_small_hint:
				    					progressBarSmall.setVisibility(View.INVISIBLE);
				    					tvSmallHint.setVisibility(View.INVISIBLE);
				    					
				    					wordCls.setDefinitionEN(definitionEN);
				    					wordCls.setDefinitionCN(definitionCN);
				    					wordCls.setAudioUrlUS(audioUrlUS);
				    					wordCls.setLoaded(true);
				    					
				    					tvDefEN.setText(wordCls.getDefinitionEN());
				    					tvDefCN.setText(wordCls.getDefinitionCN());
				    					
//				                    	WordsManager.addWordLoadInfo(tableName, wordCls);
				    					break;
				    				default:
				    					break;
				    				}
				                } catch (JSONException e) {
				                    e.printStackTrace();
				                }
				            }
				        }, new Response.ErrorListener() {
				  
				            @Override
				            public void onErrorResponse(VolleyError error) {
				            	switch (v.getId()) {
			    				case R.id.search_tv_big_hint:
			    					progressBarBig.setVisibility(View.INVISIBLE);
			    					tvBigHint.setText("数据获取失败，请重试");
			    					break;
			    				case R.id.search_tv_small_hint:
			    					progressBarSmall.setVisibility(View.INVISIBLE);
			    					tvSmallHint.setText("数据获取失败，请重试");
			    					break;
			    				default:
			    					break;
			    				}
				                error.printStackTrace();
				            }
				        });
				
				mQueue.add(jsonRequest);
			} 
		}
	}
}
