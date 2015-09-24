package com.bubble.simpleword.util;

import java.lang.reflect.Field;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

/**
 * @author https://gist.github.com/ademar111190/7d31dab71502e6a85b8a
 * @date 2015-9-24 下午4:05:21
 */
public class SearchViewFormatter {
    protected int mSearchBackGroundResource = 0;
    protected int mSearchIconResource = 0;
    protected boolean mSearchIconInside = false;
    protected boolean mSearchIconOutside = false;
    protected int mSearchVoiceIconResource = 0;
    protected int mSearchTextColorResource = 0;
    protected int mSearchHintColorResource = 0;
    protected String mSearchHintText = "";
    protected int mSearchHintTextResource = 0;
    protected int mInputType = Integer.MIN_VALUE;
    protected int mSearchCloseIconResource = 0;
    protected TextView.OnEditorActionListener mEditorActionListener;
    protected int mCursorResource;
    
    protected Field mCursorDrawableRes; 
    
    protected Resources mResources;

    public SearchViewFormatter setSearchBackGroundResource(int searchBackGroundResource) {
        mSearchBackGroundResource = searchBackGroundResource;
        return this;
    }

    public SearchViewFormatter setSearchIconResource(int searchIconResource, boolean inside, boolean outside) {
        mSearchIconResource = searchIconResource;
        mSearchIconInside = inside;
        mSearchIconOutside = outside;
        return this;
    }

    public SearchViewFormatter setSearchVoiceIconResource(int searchVoiceIconResource) {
        mSearchVoiceIconResource = searchVoiceIconResource;
        return this;
    }

    public SearchViewFormatter setSearchTextColorResource(int searchTextColorResource) {
        mSearchTextColorResource = searchTextColorResource;
        return this;
    }

    public SearchViewFormatter setSearchHintColorResource(int searchHintColorResource) {
        mSearchHintColorResource = searchHintColorResource;
        return this;
    }

    public SearchViewFormatter setSearchHintText(String searchHintText) {
        mSearchHintText = searchHintText;
        return this;
    }

    public SearchViewFormatter setSearchHintTextResource(int searchHintText) {
        mSearchHintTextResource = searchHintText;
        return this;
    }

    public SearchViewFormatter setInputType(int inputType) {
        mInputType = inputType;
        return this;
    }

    public SearchViewFormatter setSearchCloseIconResource(int searchCloseIconResource) {
        mSearchCloseIconResource = searchCloseIconResource;
        return this;
    }

    public SearchViewFormatter setEditorActionListener(TextView.OnEditorActionListener editorActionListener) {
        mEditorActionListener = editorActionListener;
        return this;
    }
    
    /**
     * <p>Title: setCursorResource</p>
     * <p>Description: </p>
     * @param searchCursorResource
     * @return
     * @author bubble
     * @date 2015-9-24 下午5:32:00
     */
    public SearchViewFormatter setCursorResource(int searchCursorResource) {
    	mCursorResource = searchCursorResource;
    	return this;
    }

    public void format(SearchView searchView) {
        if (searchView == null) {
            return;
        }

        mResources = searchView.getContext().getResources();
        int id;

        if (mSearchBackGroundResource != 0) {
            id = getIdentifier("search_plate");
            View view = searchView.findViewById(id);
            view.setBackgroundResource(mSearchBackGroundResource);

            id = getIdentifier("submit_area");
            view = searchView.findViewById(id);
            view.setBackgroundResource(mSearchBackGroundResource);
        }

        if (mSearchVoiceIconResource != 0) {
            id = getIdentifier("search_voice_btn");
            ImageView view = (ImageView) searchView.findViewById(id);
            view.setImageResource(mSearchVoiceIconResource);
        }

        if (mSearchCloseIconResource != 0) {
            id = getIdentifier("search_close_btn");
            ImageView view = (ImageView) searchView.findViewById(id);
            view.setImageResource(mSearchCloseIconResource);
        }

        id = getIdentifier("search_src_text");
        TextView view = (TextView) searchView.findViewById(id);
        if (mSearchTextColorResource != 0) {
            view.setTextColor(mResources.getColor(mSearchTextColorResource));
        }
        if (mSearchHintColorResource != 0) {
            view.setHintTextColor(mResources.getColor(mSearchHintColorResource));
        }
        if (mInputType > Integer.MIN_VALUE) {
            view.setInputType(mInputType);
        }
        if ( mCursorResource != 0 ) {
            try {
				mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
				mCursorDrawableRes.setAccessible(true);
				mCursorDrawableRes.set(view, mCursorResource);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} //This sets the cursor resource ID to 0 or @null which will make it visible on white background
        }
        
        if (mSearchIconResource != 0) {
            ImageView imageView = (ImageView) searchView.findViewById(getIdentifier("search_mag_icon"));

            if (mSearchIconInside) {
                Drawable searchIconDrawable = mResources.getDrawable(mSearchIconResource);
                int size = (int) (view.getTextSize() * 1.30f);
                searchIconDrawable.setBounds(0, 0, size, size);

                if (mSearchHintTextResource != 0) {
                    mSearchHintText = mResources.getString(mSearchHintTextResource);
                }

                SpannableStringBuilder hintBuilder = new SpannableStringBuilder("   ");
                hintBuilder.append(mSearchHintText);
                hintBuilder.setSpan(
                        new ImageSpan(searchIconDrawable),
                        1,
                        2,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                view.setHint(hintBuilder);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
            }
            if (mSearchIconOutside) {
                int searchImgId = getIdentifier("search_button");
                imageView = (ImageView) searchView.findViewById(searchImgId);

                imageView.setImageResource(mSearchIconResource);
            }
        }

        if (mEditorActionListener != null) {
            view.setOnEditorActionListener(mEditorActionListener);
        }
        
    }

    protected int getIdentifier(String literalId) {
        return mResources.getIdentifier(
                String.format("android:id/%s", literalId),
                null,
                null
        );
    }
}