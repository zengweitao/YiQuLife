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
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig.ApiRequestListener;
import com.quanmai.yiqu.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class TypeActivity extends BaseActivity {
    ListView mListView;
    TypeAdapter mAdapter;
    ProgressBar progressBar;
    View iv_no_data;
    int type = 0;
    List<ProductType> degreeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_list);
        type = getIntent().getIntExtra("type", 0);
        degreeList = new ArrayList<ProductType>();
        initview();
        getdata();
    }

    private void getdata() {
        Api.get().GoodsClassList(this, new ApiRequestListener<List<ProductType>>() {

            @Override
            public void onSuccess(String msg, List<ProductType> data) {
                degreeList = data;
                mAdapter = new TypeAdapter(mContext, degreeList);
                mListView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String msg) {
                showShortToast(msg);
            }
        });

    }

    private void initview() {
        ((TextView) findViewById(R.id.tv_title)).setText("类别");
        iv_no_data = findViewById(R.id.iv_no_data);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mListView = (ListView) findViewById(R.id.lv_listview);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("class_id", degreeList.get(position).getClass_id());
                intent.putExtra("class_name", degreeList.get(position).getClass_name());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }


}