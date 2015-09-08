package com.bubble.simpleword.wordbook;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

import com.bubble.simpleword.util.CustomTypefaceSpan;

/**
 * <p>Title: Words</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-6 下午8:09:23
 */
public class WordCls {

	private String word;	
	private String phonetic;	
	private String definition;	
	private String time;	
	private boolean isRemembered;	
	
	
	private String wholeString;	

	private String htmlString;	

	private Typeface phoneticFont;   
	private SpannableStringBuilder SS;
	private int len1;
	private int len2;
	private int len3;
    
	public WordCls(){
		
	}
	
	public WordCls(String word, String phonetic, String definition) {
		setWord(word);
		setPhonetic(phonetic);
		setDefinition(definition);
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getPhonetic() {
		return phonetic;
	}
	
	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}
	
	public String getDefinition() {
		return definition;
	}
	
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public boolean isRemembered() {
		return isRemembered;
	}

	public void setRemembered(boolean isRemembered) {
		this.isRemembered = isRemembered;
	}
	
	/**
	 * <p>Title: getWholeString</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-9-6 
	 */
	public String getWholeString() {
		this.wholeString = word + phonetic + definition;
		return wholeString;
	}
	
	/**
	 * <p>Title: getHtml</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-9-6 下午9:25:21
	 */
	public Spanned getSpannedHtml() {
		this.htmlString = "<font color='#f50057'>" + word + "</font><font color='#c5cae9'>" + phonetic + "</font>" + definition + "";
		return Html.fromHtml(htmlString);
	}
	
	/**
	 * <p>Title: setSpan</p>
	 * <p>Description: set the font style of the wholeString when displaying in one TextView</p>
	 * @param context
	 * @return
	 * @author bubble
	 * @date 2015-9-6 下午1:47:44
	 */
	public SpannableStringBuilder getSpannableStringBuilder(Context context) {
		phoneticFont = Typeface.createFromAsset(context.getAssets(), "font/TOPhonetic.ttf");   
        SS = new SpannableStringBuilder(getWholeString());
        len1 = word.length();
        len2 = word.length() + phonetic.length();
        len3 = wholeString.length();
        SS.setSpan (new CustomTypefaceSpan("", Typeface.DEFAULT), 0, len1,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SS.setSpan (new CustomTypefaceSpan("", phoneticFont), len1, len2,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        SS.setSpan (new CustomTypefaceSpan("", Typeface.DEFAULT), len2, len3,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return SS;
	}
	
}
