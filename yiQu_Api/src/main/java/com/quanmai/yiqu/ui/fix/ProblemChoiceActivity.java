package com.quanmai.yiqu.ui.fix;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.FileUtil;

public class ProblemChoiceActivity extends BaseActivity {
	ListView mListView;
	ProblemChoiceAdapter mAdapter;
	ProgressBar progressBar;
	View iv_no_data;

	class Problem {
		String problem;
		String problem_id;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_list);
		init();

	}

	private void init() {
		((TextView) findViewById(R.id.tv_title)).setText("选择常见问题");
		iv_no_data = findViewById(R.id.iv_no_data);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		mListView = (ListView) findViewById(R.id.lv_listview);
		mAdapter = new ProblemChoiceAdapter(mContext);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Problem problem = (Problem) parent.getAdapter().getItem(
						position);

				Intent intent = new Intent();
				// // intent.putExtra("id", id);
				intent.putExtra("problem", problem.problem);
				intent.putExtra("problem_id", problem.problem_id);
				// // intent.putExtra("problem_id", fcode);
				setResult(RESULT_OK, intent);
				finish();
			}
		});

		FileUtil.copyAssetsToFilesystem(mContext, "area.db",
				FileUtil.getOrCreateDbdir(mContext) + "area.db");
		select();

	}

	Handler handler = new Handler() {
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			progressBar.setVisibility(View.GONE);
			if (msg.obj != null) {
				mAdapter.Refresh((List<Problem>) msg.obj);
			}
			if(mAdapter.getCount()>0)
			{
				iv_no_data.setVisibility(View.GONE);
			}else {
				iv_no_data.setVisibility(View.VISIBLE);
			}
		}
	};

	public void select() {
		progressBar.setVisibility(View.VISIBLE);
		new Thread(new Runnable() {
			@Override
			public void run() {
				List<Problem> list = new ArrayList<ProblemChoiceActivity.Problem>();
				SQLiteDatabase db = mContext.openOrCreateDatabase("area.db",
						MODE_PRIVATE, null);
				Cursor cursor = db.rawQuery("select * from repairclassinfo",
						null);
				while (cursor.moveToNext()) {
					Problem problem = new Problem();
					problem.problem = cursor.getString(1);
					problem.problem_id = cursor.getString(2);
					// Map<String, String> map = new HashMap<String, String>();
					// map.put("id", cursor.getString(0));
					// map.put("problem", cursor.getString(1));
					// map.put("problem_id", cursor.getString(2));
					// map.put("problem_id", cursor.getString(3));
					list.add(problem);
				}
				cursor.close();
				db.close();
				Message msg = Message.obtain();
				msg.obj = list;
				handler.sendMessage(msg);
			}
		}).start();
	}
	//
	// public void setresult(String id, String classname, String
	// repaircontentcode, String fcode) {
	// System.out.println(classname);
	// Intent intent = new Intent();
	// // intent.putExtra("id", id);
	// intent.putExtra("problem", classname);
	// intent.putExtra("problem_id", repaircontentcode);
	// // intent.putExtra("problem_id", fcode);
	// setResult(RESULT_OK,intent);
	// finish();
	// }

}