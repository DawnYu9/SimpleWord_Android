package com.bubble.simpleword.db;

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
	
	
	private String definitionEN;
	private String definitionCN;
	private String audioUrlUS;
	
	private String time;	
	
	/**
	 * default value = 0, means "false"
	 * value = 1, means "true"
	 */
	private int isRemembered;
	
	/**
	 * default value = 0, means "false"
	 * value = 1, means "true"
	 */
	private int isLoaded;	
	
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
	public WordCls(String word, String phonetic, String definition, String definitionEN, 
			String definitionCN, String audioUrlUS, int isRemembered, int isLoaded) {
		setWord(word);
		setPhonetic(phonetic);
		setDefinition(definition);
		setDefinitionEN(definitionEN);
		setDefinitionCN(definitionCN);
		setAudioUrlUS(audioUrlUS);
		setRemembered(isRemembered);
		setLoaded(isLoaded);
	}
	
	public String getWord() {
		if ( word == null )
			word = "";
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public String getPhonetic() {
		if ( phonetic == null )
			phonetic = "";
		return phonetic;
	}
	
	public void setPhonetic(String phonetic) {
		this.phonetic = phonetic;
	}
	
	public String getDefinition() {
		if ( definition == null )
			definition = "";
		return definition;
	}
	
	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public String getDefinitionEN() {
		return definitionEN;
	}

	public String getDefinitionCN() {
		return definitionCN;
	}

	public String getAudioUrlUS() {
		return audioUrlUS;
	}

	public void setDefinitionEN(String definitionEN) {
		this.definitionEN = definitionEN;
	}

	public void setDefinitionCN(String definitionCN) {
		this.definitionCN = definitionCN;
	}

	public void setAudioUrlUS(String audioUrlUS) {
		this.audioUrlUS = audioUrlUS;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	/**
	 * <p>Title: isRemembered</p>
	 * <p>Description: </p>
	 * @return 0：false; 1:true
	 * @author bubble
	 * @date 2015-9-16 下午11:34:36
	 */
	public boolean isRemembered() {
		if ( isRemembered == 0 )
			return false;
		else 
			return true;
	}

	/**
	 * <p>Title: setRemembered</p>
	 * <p>Description: </p>
	 * @param isRemembered 0：false; 1:true
	 * @author bubble
	 * @date 2015-9-16 下午11:35:06
	 */
	public void setRemembered(int isRemembered) {
		this.isRemembered = isRemembered;
	}
	
	/**
	 * <p>Title: setRemembered</p>
	 * <p>Description: </p>
	 * @param isRemembered
	 * @author bubble
	 * @date 2015-9-17 下午1:07:16
	 */
	public void setRemembered(boolean isRemembered) {
		if ( isRemembered )
			this.isRemembered = 1;
		else
			this.isRemembered = 0;
	}
	
	/**
	 * <p>Title: isLoaded</p>
	 * <p>Description: </p>
	 * @return 0：false; 1: true
	 * @author bubble
	 * @date 2015-9-16 下午11:35:37
	 */
	public boolean isLoaded() {
		if ( isLoaded == 0 )
			return false;
		else
			return true;
	}

	/**
	 * <p>Title: setLoaded</p>
	 * <p>Description: </p>
	 * @param isLoaded 0：false; 1: true
	 * @author bubble
	 * @date 2015-9-16 下午11:35:47
	 */
	public void setLoaded(int isLoaded) {
		this.isLoaded = isLoaded;
	}
	
	/**
	 * <p>Title: setLoaded</p>
	 * <p>Description: </p>
	 * @param isLoaded
	 * @author bubble
	 * @date 2015-9-17 下午1:05:50
	 */
	public void setLoaded(boolean isLoaded) {
		if ( isLoaded )
			this.isLoaded = 1;
		else 
			this.isLoaded = 0;
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
//		this.htmlString = "<font color='#E91E63'>" + word + "</font><font color='#B6B6B6'>" 
//				+ phonetic + "</font><font color='#727272'>" + definition + "</font>";
		this.htmlString = word + "<font color='#B6B6B6'>" + phonetic + "</font>" + definition;
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
