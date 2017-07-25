package com.quanmai.yiqu.ui.grade;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ScoreRecordInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.transaction.LookupBigPicActivity;

import java.util.ArrayList;
import java.util.List;

public class ClassificationDetailsActivity extends BaseActivity {

    TextView tv_title;
    TextView tvAddress;
    TextView tvScore;
    TextView tvNumber;
    TextView tvTime;
    TextView textview_comment_show;
    LinearLayout llContent;

    ScoreRecordInfo mInfo;
    ArrayList<String> imgs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification_details);

        mInfo = (ScoreRecordInfo)getIntent().getSerializableExtra("Info");
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvScore = (TextView) findViewById(R.id.tvScore);
        tvNumber = (TextView) findViewById(R.id.tvNumber);
        tvTime = (TextView) findViewById(R.id.tvTime);
        textview_comment_show = (TextView) findViewById(R.id.textview_comment_show);
        llContent = (LinearLayout)findViewById(R.id.llContent);
        tv_title.setText("分类评分详情");

        if (mInfo!=null){
            tvAddress.setText(mInfo.address);
            tvScore.setText("总评分： "+mInfo.score);
            tvNumber.setText("编号： "+mInfo.barcode);
            if (!getIntent().hasExtra("time")){
                tvTime.setText(mInfo.opetime);
            }else {
                tvTime.setText(getIntent().getStringExtra("time"));
            }
            Log.e("--巡检员评分","--"+mInfo.ctext);
            if (mInfo.ctext.equals("")){
                textview_comment_show.setText("巡检员未点评！");
            }else{
                textview_comment_show.setText(mInfo.ctext);
            }


            String[] imgList = mInfo.img.split(",");
            imgs = new ArrayList<>();
            for (int i=0;i<imgList.length;i++){
                imgs.add(imgList[i]);
            }

            for (int j=0;j<imgs.size();j++){
                ImageView imageView = new ImageView(mContext);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setBackgroundColor(Color.parseColor("#f2f2f2"));
                LinearLayout.LayoutParams params =
                        new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                Utils.dp2px(mContext, 194));
                params.topMargin = Utils.dp2px(mContext, 10);
                imageView.setTag(j);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(mContext, LookupBigPicActivity.class);
                        intent.putExtra("current", (Integer) view.getTag());
                        intent.putStringArrayListExtra("piclist", imgs);
                        startActivity(intent);
                    }
                });
                llContent.addView(imageView, params);
                ImageloaderUtil.displayImage(mContext, imgs.get(j) + "?",
                        imageView);
            }
        }
    }

}
