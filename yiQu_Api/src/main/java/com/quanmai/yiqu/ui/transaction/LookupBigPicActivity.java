package com.quanmai.yiqu.ui.transaction;


import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.common.widget.BigPicMap;
import com.quanmai.yiqu.common.widget.BigPicMap.OnItemClickListener;

public class LookupBigPicActivity extends Activity implements OnItemClickListener{
	
	private BigPicMap advMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lookup_big_pic_activity);
		advMap = (BigPicMap) findViewById(R.id.advmap);
		advMap.setOnItemClickListener(this);
		advMap.setAdapter(getIntent().getStringArrayListExtra("piclist"));
		advMap.setCurrentItem(getIntent().getIntExtra("current",0));

	}

	@Override
	public void onItemClick(int position, Object object) {
		finish();
	}
}
