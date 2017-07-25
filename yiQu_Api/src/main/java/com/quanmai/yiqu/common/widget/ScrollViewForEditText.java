package com.quanmai.yiqu.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by yin on 16/3/25.
 */
public class ScrollViewForEditText extends ScrollView {

    private ScrollViewListener scrollViewListener = null;

    public ScrollViewForEditText(Context context) {
        super(context);
    }

    public ScrollViewForEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewForEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged();
        }
    }

}
