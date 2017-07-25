package com.quanmai.yiqu.ui.transaction.filterview;

import com.quanmai.yiqu.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabView extends LinearLayout {
	TextView textView;
	ImageView imageView;
	Boolean isOpen;

	public TabView(Context context) {
		super(context);
		init(context);
	}

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		View.inflate(context, R.layout.view_sort_child, this);
		textView = (TextView) findViewById(R.id.tv_sort);
		imageView = (ImageView) findViewById(R.id.iv_sort);
	}

	public void setText(String string) {
		textView.setText(string);
	
	}
	public void setTextandColor(String string) {
		textView.setText(string);
		textView.setTextColor(0xff81c146);
	}
	
	

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		if (isOpen) {
			imageView.setImageResource(R.drawable.icon_arrow_up);
		} else {
			imageView.setImageResource(R.drawable.icon_arrow_down);
		
		}

	}
}
