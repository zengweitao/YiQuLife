package com.quanmai.yiqu.ui.publish;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class DegreeChoiceActivity extends BaseActivity {
    ListView mListView;
    DegreeChoiceAdapter mAdapter;
    ProgressBar progressBar;
    View iv_no_data;
    int type = 0;
    List<Integer> degreeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        type = getIntent().getIntExtra("type", 0);
        degreeList = new ArrayList<Integer>();
        init();

    }

    private void init() {
        ((TextView) findViewById(R.id.tv_title)).setText("成色");
        iv_no_data = findViewById(R.id.iv_no_data);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mListView = (ListView) findViewById(R.id.lv_listview);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("degree", degreeList.get(position));
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        for (int i = 10; i > 0; i--) {
            degreeList.add(i);
        }
        mAdapter = new DegreeChoiceAdapter(mContext, degreeList);
        mListView.setAdapter(mAdapter);
        progressBar.setVisibility(View.GONE);
    }


}