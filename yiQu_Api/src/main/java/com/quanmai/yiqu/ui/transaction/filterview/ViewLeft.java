package com.quanmai.yiqu.ui.transaction.filterview;

import java.util.ArrayList;
import java.util.List;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.SortInfo;
import com.quanmai.yiqu.ui.transaction.filterview.ExpandTabView.OnSelectListener;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ViewLeft extends LinearLayout {
	private OnSelectListener mOnSelectListener;
	private SortAdapter adapter1;
	private SortAdapter adapter2;
	private SortAdapter adapter3;
	ListView mListView1;
	ListView mListView2;
	ListView mListView3;
	Context mContext;

	public ViewLeft(Context context,OnSelectListener listener) {
		super(context);
		init(context);
		mOnSelectListener=listener; 
	}

	

	private void init(Context context) {
		mContext = context;
		View.inflate(context, R.layout.view_left, this);
		mListView1 = (ListView) findViewById(R.id.listView);
		mListView2 = (ListView) findViewById(R.id.listView2);
		mListView3 = (ListView) findViewById(R.id.listView3);
		mListView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SortAdapter adapter = (SortAdapter) parent.getAdapter();
				SortInfo infos = adapter.getItem(position);
				adapter.selet(position);
				if (infos.haschild) {
					Refresh(infos.sort_id, 2);
				} else {
					mListView2.setAdapter(null);
					mListView3.setAdapter(null);
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
				if (infos.haschild) {
					Refresh(infos.sort_id, 3);
				} else {
					mListView3.setAdapter(null);
					if (mOnSelectListener != null) {
						mOnSelectListener.onSelect(infos);
					}
				}
			}

		});

		mListView3.setOnItemClickListener(new OnItemClickListener() {
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
		Refresh("0", 1);
	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {

			switch (msg.arg1) {
			case 1:
				if (msg.obj != null) {
					List<SortInfo> infos = (List<SortInfo>) msg.obj;
					adapter1 = new SortAdapter(mContext, infos);
					mListView1.setAdapter(adapter1);
				}

				mListView2.setAdapter(null);
				mListView3.setAdapter(null);

				break;
			case 2:
				if (msg.obj != null) {
					List<SortInfo> infos = (List<SortInfo>) msg.obj;
					adapter2 = new SortAdapter(mContext, infos);
					mListView2.setAdapter(adapter2);
				} else {
					mListView2.setAdapter(null);
				}
				mListView3.setAdapter(null);
				break;
			case 3:
				if (msg.obj != null) {
					List<SortInfo> infos = (List<SortInfo>) msg.obj;
					adapter3 = new SortAdapter(mContext, infos);
					mListView3.setAdapter(adapter3);
				} else {
					mListView3.setAdapter(null);
				}
				break;
			default:
				break;
			}

		}
	};

	private void Refresh(final String id, final int level) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<SortInfo> list = new ArrayList<SortInfo>();
				if(level==1)
				{
					list.add(new SortInfo("","全部地区", false));
				}
				SQLiteDatabase db = mContext.openOrCreateDatabase("area.db",
						Context.MODE_PRIVATE, null);
				Cursor cursor = db.rawQuery(
						"select *  from areacodeinfo where parentId = " + id,
						null);
				while (cursor.moveToNext()) {
					SortInfo areaInfo = new SortInfo(cursor.getString(1),
							cursor.getString(3), cursor.getInt(5) == 1);
					list.add(areaInfo);
				}
				cursor.close();
				db.close();
				Message msg = Message.obtain();
				msg.arg1 = level;
				msg.obj = list;
				handler.sendMessage(msg);
			}
		}).start();

	}



}
