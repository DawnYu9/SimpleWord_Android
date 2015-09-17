package com.bubble.simpleword.fragment;

import java.net.URL;

import net.htmlparser.jericho.Source;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.MyDbHelper;

/**
 * <p>Title: MainFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-2
 */
public class HomeFragment extends Fragment {
	public View mView;
	private MyDbHelper dbHelper;
	SQLiteDatabase db;
	Context mContext;
	
	TextView tv_title,tv_summary,tv_author,tv_tag,tv_pages,tv_price;
	LinearLayout ll_layout;
	
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public HomeFragment(Context context) {
		mContext = context;
	}
	/**
	 * @author bubble
	 * @date 2015-8-2
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		mView=inflater.inflate(R.layout.fg_layout_home,container, false);  
		return mView; 
	}
	
	private void getJsonData() {
        //网络下载数据相对耗时，使用异步加载，减少主线程负担
        new AsyncTask<String, Void, Boolean>() {
            String title;
            String summary ;
            String tag;
            String author,pages,price;
            @Override
            protected void onPreExecute() {
                // TODO Auto-generated method stub
                //显示进度条
                ll_layout.setVisibility(View.VISIBLE);
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Boolean result) {
                // TODO Auto-generated method stub
                //设置进度条消失
                ll_layout.setVisibility(View.INVISIBLE);
                //如果获取到数据则把数据显示到屏幕
                if (result) {
                    tv_title.setText(title);
                    tv_summary.setText(summary);
                    tv_tag.setText(tag);
                    tv_author.setText(author);
                    tv_pages.setText(pages);
                    tv_price.setText(price);
                }else {
                    Toast.makeText(getActivity(),"获取数据失败",0).show();
                }
                super.onPostExecute(result);
            }

            @Override
            protected Boolean doInBackground(String... params) {
                // TODO Auto-generated method stub
                String path = "http://api.douban.com/book/subject/isbn/9787111187776?alt=json";
                try {
                    URL url = new URL(path);
                    Source source = new Source(url.openConnection());//这个需加入jericho-html-3.1.jar包
                 String jsonstr = source.toString();
                 //得到一个JSONObject对象
                 JSONObject jsonObj = new JSONObject(jsonstr);
                 //获取title
                 String titlestr = jsonObj.get("title").toString();
                 JSONObject jsontitle = new JSONObject(titlestr);
                 title = jsontitle.getString("$t");
                 
                 //获取标签
                 String tagstr = jsonObj.getString("db:tag");
                 JSONArray jsonArray = new JSONArray(tagstr);
                 StringBuffer sb = new StringBuffer(); 
                 String tagarr = null;
                 for (int i = 0; i < jsonArray.length(); i++) {
                        tagarr = jsonArray.getString(i);
                        JSONObject jsonObject = new JSONObject(tagarr);
                        tag = jsonObject.getString("@name");
                        sb.append(tag+"/");
                    }
                 tag =sb.substring(0, sb.length()-1); 
                 
                 //获取作者
                 String authorstr = jsonObj.getString("author");
                 JSONArray authorArray = new JSONArray(authorstr);
                 StringBuffer sbauthor = new StringBuffer(); 
                 String authorarr = null;
                 for (int i = 0; i < authorArray.length(); i++) {
                     System.out.println(authorArray.length());
                     authorarr = authorArray.getString(i);
                        JSONObject jsonObject = new JSONObject(authorarr);
                        String authorName = jsonObject.getString("name");
                        jsonObject = new JSONObject(authorName);
                        author = jsonObject.getString("$t");
                        sbauthor.append(author+"/");
                    }
                 author =sbauthor.substring(0, sbauthor.length()-1); 
                 
                 //获取价格与页数
                 String str = jsonObj.getString("db:attribute");
                 JSONArray array = new JSONArray(str);
                 sb = new StringBuffer(); 
                 String bookinfo = null;
                 for (int i = 0; i < array.length(); i++) {
                     bookinfo = array.getString(i);
                     JSONObject jsonObject = new JSONObject(bookinfo);
                        if ("price".equals(jsonObject.getString("@name"))) {
                            price = jsonObject.getString("$t");
                        }
                        if ("pages".equals(jsonObject.getString("@name"))) {
                            pages = jsonObject.getString("$t");
                        }
                    }
                 //获取简介
                 String summarystr = jsonObj.getString("summary");
                 JSONObject jsonsummary = new JSONObject(summarystr);
                 summary = jsonsummary.getString("$t");
                 return true;
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "获取数据失败", 0).show();
                    return false;
                }
            }
        }.execute();
    }
	/**
	 * @author bubble
	 * @date 2015-8-6
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
}
