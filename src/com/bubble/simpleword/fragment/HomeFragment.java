package com.bubble.simpleword.fragment;

import java.io.File;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bubble.simpleword.util.OkHttpClientManager;
import com.bubble.simpleword.util.Util;
import com.squareup.okhttp.Request;

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
	private SharedPreferences cachePref;
	private Editor cacheEditor;
	
	public static final String IS_LOADING = "正在加载……";
	
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
	
	private JsonObjectRequest jsonObjectRequest;
	
	private String engSentence = "";
	private String chSentence = "";
	public static final String ENG_SENTENCE = "eng_sentence";
	public static final String CH_SENTENCE = "ch_sentence";
	
	private String audioURL = "";
	public static final String AUDIO_URL = "audio_url";
	private static Uri uri;
	private static MediaPlayer playerSentence;
	
	private String imgURL = "";
	public static final String IMG_URL = "img_url";
	private ImageRequest imageRequest;
	private ImageGetter imageGetterPlay;
	private BitmapCache bitmapCache;
	private ImageLoader imageLoader;
	private ImageListener imgListener;
	private Bitmap imgBitmap;
	private String imgPath;
	private File imgFile;
	
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
		
		cachePref = Util.getCacheSharedPreferences(getActivity());
		cacheEditor = cachePref.edit();
		
		tvWord = (TextView) view.findViewById(R.id.home_tv_word);
		tvDefinition = (TextView) view.findViewById(R.id.home_tv_definition);
		
		img = (ImageView) view.findViewById(R.id.home_img);
//		img = (NetworkImageView) view.findViewById(R.id.home_img);
		tvEng = (TextView) view.findViewById(R.id.home_tv_english);
		tvCh = (TextView) view.findViewById(R.id.home_tv_chinese);
		
		tvSmallHint = (TextView) view.findViewById(R.id.home_tv_small_hint);
		progressBarSmall = (ProgressBar) view.findViewById(R.id.home_small_progressbar);
		tvBigHint = (TextView) view.findViewById(R.id.home_tv_big_hint);
		progressBarBig = (ProgressBar) view.findViewById(R.id.home_big_progressbar);
		
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
		
		tvEng.setOnClickListener(this);
		tvSmallHint.setOnClickListener(this); 
		tvBigHint.setOnClickListener(this); 
		
		setNetContent();
		
		return view; 
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-12 上午12:31:31
	 */
	@Override
	public void onResume() {
		super.onResume();
	}
	
	/**
	 * <p>Title: getJsonData</p>
	 * <p>Description: 金山词霸每日一句</p>
	 * @author bubble
	 * @date 2015-9-26 下午10:33:16
	 */
	private void getDailySentenceJsonData() {
		jsonObjectRequest = new JsonObjectRequest(MainActivity.DAILYSENTENCE_URL, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						try {
							imgURL = response.getString("picture");
							Util.download(imgURL, MainActivity.CACHE_IMG_DIRECTORY);
							
							audioURL = response.getString("tts");
							try {
								uri = Uri.parse(audioURL);
								playerSentence = new MediaPlayer().create(getActivity(), uri);
							} catch (Exception e) {
								e.printStackTrace();
							}
							Util.download(audioURL, MainActivity.CACHE_SENTENCE_DIRECTORY);
							
							engSentence = response.getString("content");
							chSentence = response.getString("note");
							
							cacheEditor.putString(Util.getCurrentDate() + IMG_URL, imgURL);
							cacheEditor.putString(Util.getCurrentDate() + AUDIO_URL, audioURL);
							cacheEditor.putString(Util.getCurrentDate() + ENG_SENTENCE, engSentence);
							cacheEditor.putString(Util.getCurrentDate() + CH_SENTENCE, chSentence);
							
							cacheEditor.putBoolean(Util.getCurrentDate(), true);
							cacheEditor.commit();
							
							setNetContent();
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
	public ImageView setImg() {
		imgPath = MainActivity.CACHE_IMG_DIRECTORY 
				+ File.separator + Util.getCurrentDate() + ".jpg";
		imgFile = new File(imgPath);
		if ( imgFile.exists() ) {
			imgBitmap = Util.getLoacalBitmap(imgPath);
			img.setImageBitmap(imgBitmap);
			
			tvSmallHint.setVisibility(View.INVISIBLE);
			progressBarSmall.setVisibility(View.INVISIBLE);
		} else {
			getImg(cachePref.getString(Util.getCurrentDate() + IMG_URL, ""));
			setImg();
		}
		
		return img;
	}

	/**
	 * <p>Title: getImg</p>
	 * <p>Description: </p>
	 * @param url
	 * @author bubble
	 * @date 2015-10-11 下午11:11:42
	 */
	private void getImg(String url) {
		if ( ! url.isEmpty() ) {
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
		} else {
			getDailySentenceJsonData();
		}
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
//			if ( playerSentence != null ) {
//				if ( playerSentence.isPlaying() ) {
//					playerSentence.seekTo(0);
//				} else {
//					playerSentence.start();
//				}
//			} else {
//				getDailySentenceJsonData();
//			}
			try {
				Util.pronounceSentence(getActivity(), cachePref.getString(Util.getCurrentDate() + AUDIO_URL, audioURL));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			break;
		case R.id.home_tv_small_hint:
			progressBarSmall.setVisibility(View.VISIBLE);
			tvSmallHint.setText(IS_LOADING);
			setImg();
		case R.id.home_tv_big_hint:
			progressBarBig.setVisibility(View.VISIBLE);
			tvBigHint.setText(IS_LOADING);
			getDailySentenceJsonData();
			break;
		default:
			break;
		}
	}
	
	/**
	 * <p>Title: setContent</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-11 下午9:07:47
	 */
	private void setNetContent() {
		if ( cachePref.getBoolean(Util.getCurrentDate(), false) ) {
			progressBarBig.setVisibility(View.INVISIBLE);
			tvBigHint.setVisibility(View.INVISIBLE);
			progressBarSmall.setVisibility(View.INVISIBLE);
			tvSmallHint.setVisibility(View.INVISIBLE);
			
			setImg();
			
			engSentence = cachePref.getString(Util.getCurrentDate() + ENG_SENTENCE, "");
			chSentence = cachePref.getString(Util.getCurrentDate() + CH_SENTENCE, "");
			tvEng.setText(Html.fromHtml(engSentence + "<img src=\"" + R.drawable.play_gray + "\">", imageGetterPlay, null));
			tvCh.setText(Html.fromHtml(chSentence + "<small><font color='#E91E63'>（金山词霸提供）</font></small>"));
		} else {
			tvBigHint.setVisibility(View.VISIBLE);
			tvBigHint.setText("正在获取数据，请稍等");
			progressBarBig.setVisibility(View.VISIBLE);
			
			getDailySentenceJsonData();
		}
	}

	/**
	 * <p>Title: stopPlayerSentence</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-9 下午8:41:28
	 */
//	public static void stopPlayerSentence() {
//		if ( playerSentence != null ) {
//			if ( playerSentence.isPlaying() )
//				playerSentence.stop();
//		}
//	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-10-12 下午10:22:15
	 */
	@Override
	public void onDestroy() {
		super.onDestroy();
		if ( playerSentence != null ) {
			playerSentence.release();
		}
	}
}
