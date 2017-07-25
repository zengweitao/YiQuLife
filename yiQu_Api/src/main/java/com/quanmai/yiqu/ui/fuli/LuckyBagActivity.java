package com.quanmai.yiqu.ui.fuli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.MessageApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.integration.IntegralDetailsActivity;

/**
 * 领取福利界面
 */
public class LuckyBagActivity extends BaseActivity {

    Button buttonStart;
    TextView tv_title,tv_right;
    String change = "";
    ImageView imageViewContent;
    String luckyBagUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_bag);

        init();

        /**
         * 获取剩余福袋机会
         * */
        MessageApi.get().GetAnswerQuestionsChange(mContext, new ApiConfig.ApiRequestListener<String>() {

            @Override
            public void onSuccess(String msg, String data) {
                change = data;
            }

            @Override
            public void onFailure(String msg) {
                Utils.showCustomToast(mContext, msg);
            }
        });

        //开始答题
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.setClass(mContext, AnswerQuestionsActivity.class);
                intent.putExtra("url", luckyBagUrl);
                startActivity(intent);
                if (intent.hasExtra("isShowChangeBag")) {
                    finish();
                }
            }
        });
    }

    //初始化
    private void init() {
        buttonStart = (Button) findViewById(R.id.buttonStart);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("领取福利");
        tv_right= (TextView) findViewById(R.id.tv_right);
        tv_right.setText("福袋详情");
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),IntegralDetailsActivity.class);
                intent.putExtra("point_type",3);
                startActivity(intent);
            }
        });
        imageViewContent = (ImageView) findViewById(R.id.imageViewContent);
    }
}
