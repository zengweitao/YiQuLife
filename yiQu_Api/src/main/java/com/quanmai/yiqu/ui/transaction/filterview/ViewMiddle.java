package com.quanmai.yiqu.ui.transaction.filterview;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.api.vo.SortInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.transaction.filterview.ExpandTabView.OnSelectListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ViewMiddle extends FrameLayout {
	private OnSelectListener mOnSelectListener;
	private SortAdapter adapter1;
	private SortAdapter adapter2;
	ListView mListView1;
	ListView mListView2;
	Context mContext;

	public ViewMiddle(Context context,OnSelectListener listener) {
		super(context);
		init(context);
		mOnSelectListener=listener; 
	}

	public ViewMiddle(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	

	private void init(Context context) {
		mContext = context;
		View.inflate(context,R.layout.view_middle, this);
		mListView1 = (ListView) findViewById(R.id.listView);
		mListView2 = (ListView) findViewById(R.id.listView2);
		mListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SortAdapter adapter = (SortAdapter) parent.getAdapter();
				SortInfo infos = adapter.getItem(position);
				adapter.selet(position);
				if (infos.haschild) {
					adapter2 = new SortAdapter(mContext, infos.childs);
					mListView2.setAdapter(adapter2);
				} else {
					if (mOnSelectListener != null) {
						mOnSelectListener.onSelect(infos);
					}
				}
			}

		});

		mListView2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SortAdapter adapter = (SortAdapter) parent.getAdapter();
				SortInfo infos = adapter.getItem(position);
				adapter.selet(position);
					if (mOnSelectListener != null) {
						mOnSelectListener.onSelect(infos);
					}
			}

		});
		GoodsScreening();
	}


	
	private void GoodsScreening() {
		Api.get().GoodsScreening(mContext, new ApiRequestListener<CommonList<SortInfo>>() {
			
			@Override
			public void onSuccess(String msg, CommonList<SortInfo> data) {
				data.add(0,new SortInfo("","所有分类",false));
				adapter1 = new SortAdapter(mContext, data);
				mListView1.setAdapter(adapter1);
			}
			
			@Override
			public void onFailure(String msg) {
				// TODO Auto-generated method stub
			}
		});

	}

//	Handler handler = new Handler() {
//		@SuppressWarnings("unchecked")
//		public void handleMessage(Message msg) {
//			Log.e("", "msg.arg1="+msg.arg1);
//			if (msg.obj != null) {
//				List<SortInfo> infos = (List<SortInfo>) msg.obj;
//			
//				switch (msg.arg1) {
//				case 1:
//					
//					adapter1 = new SortAdapter(mContext, infos);
//					mListView1.setAdapter(adapter1);
//					mListView2.setAdapter(null);
//				
//					Log.e("", "adapter1.getCount();="+adapter1.getCount());
//					
//					break;
//				case 2:
//					adapter2 = new SortAdapter(mContext, infos);
//					mListView2.setAdapter(adapter2);
//					break;
//				default:
//					break;
//				}
//			}
//
//		}
//	};

	
	

}
