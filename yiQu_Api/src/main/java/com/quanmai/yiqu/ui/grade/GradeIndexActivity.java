package com.quanmai.yiqu.ui.grade;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.ui.code.MipcaActivityCapture;

/**
 * 巡检评分界面
 */
public class GradeIndexActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_title,tv_right;
    GradeIndexPopupWindow mWindow;
    LinearLayout llClassification,llEquipment,llClean, llPublic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_index);

        initView();

        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindow.showAsDropDown(tv_right);
            }
        });
    }

    //view初始化
    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_right = (TextView)findViewById(R.id.tv_right);
        tv_title.setText("巡检评分");
        tv_right.setText("评分记录");

        mWindow = new GradeIndexPopupWindow(this);
        llClassification = (LinearLayout)findViewById(R.id.llClassification);
        llEquipment = (LinearLayout)findViewById(R.id.llEquipment);
        llClean = (LinearLayout)findViewById(R.id.llClean);
        llPublic = (LinearLayout)findViewById(R.id.llPublic);

        llClassification.setOnClickListener(this);
        llEquipment.setOnClickListener(this);
        llClean.setOnClickListener(this);
        llPublic.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            //分类评分
            case R.id.llClassification:{
                intent = new Intent(this, MipcaActivityCapture.class);
                intent.putExtra("type", 1);
                startActivity(intent);
                break;
            }
            //设施评分
            case R.id.llEquipment:{
                intent = new Intent(this,ChooseScoreCommunityActivity.class);
                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_EQUIPMENT_SCORE);
                startActivity(intent);
                break;
            }
            //清运评分
            case R.id.llClean:{
                intent = new Intent(this,ChooseScoreCommunityActivity.class);
                intent.putExtra("ScoreType",ChooseScoreCommunityActivity.TYPE_CLEAN_SCORE);
                startActivity(intent);
                break;
            }
            //宣传评分
            case R.id.llPublic:{
                Toast.makeText(getApplicationContext(),"开发中",Toast.LENGTH_SHORT).show();
                break;
            }
            default:break;
        }
    }
}
