package com.bubble.simpleword.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;
import android.widget.TextView.BufferType;

/**
 * <p>Title: MarqueeTextView</p>
 * <p>Description: 从左向右滚动（暂时未弄懂如何改为从右向左滚动）</p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-31 下午8:04:02
 */
public class MarqueeTextView extends TextView implements Runnable {

	private int currentScrollX;// 当前滚动的位置
    private boolean isStop = false;
    private int textWidth;
    private boolean isMeasure = false;

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if ( ! isMeasure ) { 	// 文字宽度只需获取一次就可以了
            getTextWidth();
            isMeasure = true;
        }
    }

    /**
     * <p>Title: getTextWidth</p>
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8-31 下午8:21:06
     */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }

    /**
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8-31 下午8:21:20
     */
    @Override
    public void run() {
        currentScrollX -= 1;// 滚动速度
        scrollTo(currentScrollX, 0);
        if ( isStop ) {
                return;
        }
        if ( getScrollX() <= -(this.getWidth()) ) {
                scrollTo(textWidth, 0);
                currentScrollX = textWidth;
//              return;
        }
        postDelayed(this, 10);
    }

    /**
     * <p>Title: startScroll</p>
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8-31 下午8:21:47
     */
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }

    /**
     * <p>Title: stopScroll</p>
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8-31 下午8:21:52
     */
    public void stopScroll() {
        isStop = true;
    }

    /**
     * <p>Title: startFor0</p>
     * <p>Description: </p>
     * @author bubble
     * @date 2015-8-31 下午8:21:56
     */
    public void startScrollFromBeginning() {
        currentScrollX = 0;
        startScroll();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        startScroll();
    }

    @Override
    public void destroyDrawingCache() {
        super.destroyDrawingCache();
    }
}
