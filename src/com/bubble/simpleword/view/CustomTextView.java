package com.bubble.simpleword.view;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * <p>Title: CustomTextView</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author http://gundumw100.iteye.com/blog/1131358
 * @date 2015-9-25 上午7:20:42
 */

public class CustomTextView extends TextView {

	private static float DEFAULT_MIN_TEXT_SIZE = 10;
	private static float DEFAULT_MAX_TEXT_SIZE = 10;

	// Attributes
	private Paint testPaint;
	private float minTextSize, maxTextSize;

	public CustomTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initialise();
	}

	private void initialise() {
		testPaint = new Paint();
		testPaint.set(this.getPaint());

		// max size defaults to the intially specified text size unless it is
		// too small
		maxTextSize = this.getTextSize();

		if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
			maxTextSize = DEFAULT_MAX_TEXT_SIZE;
		}

		minTextSize = DEFAULT_MIN_TEXT_SIZE;
	};

	/**
	 * Re size the font so the specified text fits in the text box * assuming
	 * the text box is the specified width.
	 */
	private void refitText(String text, int textWidth) {
		if (textWidth > 0) {
			int availableWidth = textWidth - this.getPaddingLeft()
					- this.getPaddingRight();
			float trySize = maxTextSize;
			testPaint.setTextSize(trySize);
			while ((trySize > minTextSize)
					&& (testPaint.measureText(text) > availableWidth)) {
				trySize -= 1;
				if (trySize <= minTextSize) {
					trySize = minTextSize;
					break;
				}
				testPaint.setTextSize(trySize);
			}
			this.setTextSize(trySize);
		}
	};

	@Override
	protected void onTextChanged(CharSequence text, int start, int before,
			int after) {
		super.onTextChanged(text, start, before, after);
		refitText(text.toString(), this.getWidth());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (w != oldw) {
			refitText(this.getText().toString(), w);
		}
	}
}
