package com.bubble.simpleword.util;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

/**
 * <p>Title: CustomTypefaceSpan</p>
 * <p>Description:apply multiple custom fonts in one TextView
 * example:
 * Typeface font1 = Typeface.createFromAsset(getAssets(), "font/font1.ttf");
 * Typeface font2 = Typeface.createFromAsset(getAssets(), "font/font2.ttf");   
 * SpannableStringBuilder ss = new SpannableStringBuilder("abcdefg");
 * ss.setSpan (new CustomTypefaceSpan("", font1), 0, 4,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
 * ss.setSpan (new CustomTypefaceSpan("", font2), 4, 7,Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
 * textview.setText(ss);
 * </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author http://stackoverflow.com/questions/6612316/how-set-spannable-object-font-with-custom-font
 * @date 2015-9-6 下午12:50:20
 */


public class CustomTypefaceSpan extends TypefaceSpan {

	private final Typeface newType;
	
	public CustomTypefaceSpan(String family, Typeface type) {
	    super(family);
	    newType = type;
	}
	
	@Override
	public void updateDrawState(TextPaint tp) {
	    applyCustomTypeFace(tp, newType);
	}
	
	@Override
	public void updateMeasureState(TextPaint paint) {
	    applyCustomTypeFace(paint, newType);
	}
	
	private static void applyCustomTypeFace(Paint paint, Typeface tf) {
	    int oldStyle;
	    Typeface old = paint.getTypeface();
	    if (old == null) {
	        oldStyle = 0;
	    } else {
	        oldStyle = old.getStyle();
	    }
	
	    int fake = oldStyle & ~tf.getStyle();
	    if ((fake & Typeface.BOLD) != 0) {
	        paint.setFakeBoldText(true);
	    }
	
	    if ((fake & Typeface.ITALIC) != 0) {
	        paint.setTextSkewX(-0.25f);
	    }
	
	    paint.setTypeface(tf);
	}
}