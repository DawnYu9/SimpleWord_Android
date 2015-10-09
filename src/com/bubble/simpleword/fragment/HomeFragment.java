package com.bubble.simpleword.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bubble.simpleword.R;
import com.bubble.simpleword.activity.MainActivity;
import com.bubble.simpleword.db.WordsManager;
import com.bubble.simpleword.util.BitmapCache;
import com.bubble.simpleword.util.Util;

/**
 * <p>Title: MainFragment</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-2
 */
public class HomeFragment extends Fragment implements OnClickListener{
	private static boolean isCurrent = false;
	
	private View view;
	private TextView tvWord;
	private TextView tvDefinition;
	
	private ImageView img;
//	private NetworkImageView img;
	private TextView tvEng;
	private TextView tvCh;
	
	private TextView tvSmallHint;
	private TextView tvBigHint;
	private ProgressBar progressBarSmall;
	private ProgressBar progressBarBig;
	
	private static final String dailysentenceURL = "http://open.iciba.com/dsapi/?date=";
	private JsonObjectRequest jsonObjectRequest;
	
	private String engSentence;
	private String chSentence;
	
	private String audioURL;
	private static Uri uri;
	private static MediaPlayer playerSentence;
	
	private String imgURL;
	private ImageRequest imageRequest;
	private ImageGetter imageGetterPlay;
	private BitmapCache bitmapCache;
	private ImageLoader imageLoader;
	private ImageListener imgListener;
	/**
	 * <p>Title: </p>
	 * <p>Description: </p>
	 */
	public HomeFragment(Context context) {
	}
	
	/**
	 * <p>Title: isInit</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-10-9 下午9:14:50
	 */
	public static boolean isCurrent() {
		return isCurrent;
	}
	
	/**
	 * <p>Title: setIsCurrent</p>
	 * <p>Description: </p>
	 * @param b
	 * @author bubble
	 * @date 2015-10-9 下午9:17:36
	 */
	public static void setIsCurrent(boolean b) {
		isCurrent = b;
	}
	
	/**
	 * @author bubble
	 * @date 2015-8-2
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		view=inflater.inflate(R.layout.fg_layout_home,container, false);  
		
		tvWord = (TextView) view.findViewById(R.id.home_tv_word);
		tvDefinition = (TextView) view.findViewById(R.id.home_tv_definition);
		
		img = (ImageView) view.findViewById(R.id.home_img);
//		img = (NetworkImageView) view.findViewById(R.id.home_img);
		tvEng = (TextView) view.findViewById(R.id.home_tv_english);
		tvCh = (TextView) view.findViewById(R.id.home_tv_chinese);
		
		tvWord.setText(WordsManager.getWordCls().getWord());
		tvDefinition.setText(WordsManager.getWordCls().getDefinition());
		
		imageGetterPlay = new ImageGetter() {
			  @Override
			  public Drawable getDrawable(String source) {
			      int resId = Integer.parseInt(source);
			      Drawable drawable = getActivity().getResources()
			              .getDrawable(resId);
			      drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
			              drawable.getIntrinsicHeight());
			      return drawable;
			  }
			};

		
		tvSmallHint = (TextView) view.findViewById(R.id.home_tv_small_hint);
		progressBarSmall = (ProgressBar) view.findViewById(R.id.home_small_progressbar);
		tvBigHint = (TextView) view.findViewById(R.id.home_tv_big_hint);
		progressBarBig = (ProgressBar) view.findViewById(R.id.home_big_progressbar);
		
		tvEng.setOnClickListener(this);
		tvSmallHint.setOnClickListener(this); 
		tvBigHint.setOnClickListener(this); 
		
		tvBigHint.setVisibility(View.VISIBLE);
		tvBigHint.setText("正在获取数据，请稍等");
		progressBarBig.setVisibility(View.VISIBLE);
		
		getDailySentenceJsonData();
		
		return view; 
	}
	
	/**
	 * <p>Title: getJsonData</p>
	 * <p>Description: 金山词霸每日一句</p>
	 * @author bubble
	 * @date 2015-9-26 下午10:33:16
	 */
	private void getDailySentenceJsonData() {
		jsonObjectRequest = new JsonObjectRequest(dailysentenceURL, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							imgURL = response.getString("picture");
							
							audioURL = response.getString("tts");
							try {
								uri = Uri.parse(audioURL);
								playerSentence = new MediaPlayer().create(getActivity(), uri);
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							engSentence = response.getString("content");
							chSentence = response.getString("note");
							
							tvSmallHint.setVisibility(View.INVISIBLE);
							progressBarSmall.setVisibility(View.INVISIBLE);
							setImg(imgURL);
							
//							tvEng.setText(engSentence);
							tvEng.setText(Html.fromHtml(engSentence + "<img src=\"" + R.drawable.play_gray + "\">", imageGetterPlay, null));
							tvCh.setText(Html.fromHtml(chSentence + "<small><font color='#E91E63'>（金山词霸提供）</font></small>"));
							
							progressBarBig.setVisibility(View.INVISIBLE);
							tvBigHint.setVisibility(View.INVISIBLE);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressBarBig.setVisibility(View.INVISIBLE);
						tvBigHint.setVisibility(View.VISIBLE);
						tvBigHint.setText(Util.ERROR);
					}
				});
		MainActivity.mQueue.add(jsonObjectRequest);
	}
	
	/**
	 * @return 
	 */
	public ImageView setImg(String url) {
		imageRequest = new ImageRequest(
				url,
				new Response.Listener<Bitmap>() {
					@Override
					public void onResponse(Bitmap response) {
						img.setImageBitmap(response);
						
						tvSmallHint.setVisibility(View.INVISIBLE);
						progressBarSmall.setVisibility(View.INVISIBLE);
					}
				}, 0, 0, Config.RGB_565, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						progressBarSmall.setVisibility(View.INVISIBLE);
						tvSmallHint.setVisibility(View.VISIBLE);
					}
				});
		MainActivity.mQueue.add(imageRequest);
		
//		if ( bitmapCache == null )
//			bitmapCache = new BitmapCache();
//		if ( imageLoader == null )
//			imageLoader = new ImageLoader(MainActivity.mQueue, bitmapCache);
//		
//		if ( imgListener == imgListener)
//			imgListener = ImageLoader.getImageListener(img,  
//		        R.drawable.play_gray, R.drawable.menu); 
//		
//		imageLoader.get(url, imgListener);
//		
//		img.setDefaultImageResId(R.drawable.play_gray);
//		img.setErrorImageResId(R.drawable.menu);
//		img.setImageUrl(url, imageLoader);
		
		return img;
	}
	
	/**
	 * @author bubble
	 * @date 2015-8-6
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-26 下午9:08:11
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_tv_english:
//			Util.pronounceSentence(getActivity(), audioURL);
			if ( playerSentence != null ) {
				if ( playerSentence.isPlaying() ) {
					playerSentence.seekTo(0);
				} else {
					playerSentence.start();
				}
			}
			
			break;
		case R.id.home_tv_small_hint:
			progressBarSmall.setVisibility(View.VISIBLE);
			tvSmallHint.setText("正在加载……");
			setImg(imgURL);
		case R.id.home_tv_big_hint:
			progressBarBig.setVisibility(View.VISIBLE);
			tvBigHint.setText("正在加载……");
			getDailySentenceJsonData();
			break;
		default:
			break;
		}
	}
	
	/**
	 * <p>Title: stopPlayerSentence</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-9 下午8:41:28
	 */
	public static void stopPlayerSentence() {
		if ( playerSentence.isPlaying() )
			playerSentence.stop();
	}
	
}
