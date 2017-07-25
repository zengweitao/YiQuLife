package com.quanmai.yiqu.ui.transaction.filterview;

import java.util.ArrayList;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.SortInfo;
import com.quanmai.yiqu.ui.transaction.filterview.ExpandTabView.OnSelectListener;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;


public class ViewRight extends FrameLayout {
	private OnSelectListener mOnSelectListener;
	private SortAdapter adapter;


	public ViewRight(Context context,OnSelectListener listener) {
		super(context);
		init(context);
		mOnSelectListener=listener; 
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_right, this, true);
		ListView mListView = (ListView) findViewById(R.id.listView);
		ArrayList<SortInfo> items=new ArrayList<SortInfo>();
		items.add(new SortInfo("1","按价格排序"));
		items.add(new SortInfo("2","按新旧排序"));
		adapter = new SortAdapter(context, items);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.selet(position);
				if (mOnSelectListener != null) {
					mOnSelectListener.onSelect(adapter.getItem(position));
				}
				
			}
			
		});
		
	}


	
}
