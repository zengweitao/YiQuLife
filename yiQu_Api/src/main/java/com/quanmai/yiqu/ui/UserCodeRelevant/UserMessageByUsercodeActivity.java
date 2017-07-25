package com.quanmai.yiqu.ui.UserCodeRelevant;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.UserDetailsByHouseCodeInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.widget.PullToZoomScrollView;
import com.quanmai.yiqu.common.widget.XCRoundImageView;
import com.quanmai.yiqu.ui.grade.FetchBagRecordActivity;
import com.quanmai.yiqu.ui.grade.GradeManagerActivity;
import com.quanmai.yiqu.ui.integration.GiveIntegrationActivity;
import com.quanmai.yiqu.ui.integration.IntegralDetailsActivity;
import com.quanmai.yiqu.ui.recycle.RecycleScoreRecordActivity;
import com.quanmai.yiqu.ui.ycoin.GiftGivingActivity;
import com.quanmai.yiqu.ui.ycoin.GivingConfirmActivity;

import java.util.List;

/**
 * 扫描户码获取用户详情页面
 */
public class UserMessageByUsercodeActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_title,textViewName,textViewLeft,textViewRight,
                      textview_headusername,textview_communityname,textview_phonecode,textview_doorplatecode;
    private ImageView iv_back;
    private XCRoundImageView imageViewHeadPortrait;
    private Button button_1_month,button_2_month,button_3_month,button_4_month,
                    button_5_month,button_6_month,button_7_month,button_8_month,
                    button_9_month,button_10_month,button_11_month,button_12_month;
    private LinearLayout linear_integral_details,linear_ycoin_details,linear_give_present,
                          linear_googbag_details,linear_getbag_record,linear_grade_record,
                          linear_give_ycoin,linear_give_integral;
    private Intent intent;
    private ImageView imageViewSex;
    private Button tvLevel;
    private PullToZoomScrollView pull_zoom_scroll_usermessage;
    private View line2;
    private TextView textViewRight2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_message_by_usercode);
        init();
        showView((UserDetailsByHouseCodeInfo)getIntent().getSerializableExtra("UserDetailsByHouseCodeInfo"));
    }

    /**
     * 初始化控件
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void init() {
        pull_zoom_scroll_usermessage = (PullToZoomScrollView) findViewById(R.id.pull_zoom_scroll_usermessage);
        //标题
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("用户信息");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        //头部信息
        imageViewHeadPortrait = (XCRoundImageView) findViewById(R.id.imageViewHeadPortrait);
        imageViewHeadPortrait.setImageResource(R.drawable.default_header);
        textViewName = (TextView) findViewById(R.id.textViewName);
        imageViewSex = (ImageView) findViewById(R.id.imageViewSex);
        tvLevel = (Button) findViewById(R.id.tvLevel);
        textViewLeft = (TextView) findViewById(R.id.textViewLeft);
        textViewRight = (TextView) findViewById(R.id.textViewRight);
        line2 = findViewById(R.id.line2);
        line2.setVisibility(View.VISIBLE);
        textViewRight2 = (TextView) findViewById(R.id.textViewRight2);
        textViewRight2.setVisibility(View.VISIBLE);
        //用户详情
        textview_headusername = (TextView) findViewById(R.id.textview_headusername);
        textview_communityname = (TextView) findViewById(R.id.textview_communityname);
        textview_phonecode = (TextView) findViewById(R.id.textview_phonecode);
        textview_doorplatecode = (TextView) findViewById(R.id.textview_doorplatecode);
        //福袋领取记录
        button_1_month = (Button) findViewById(R.id.button_1_month);
        button_2_month = (Button) findViewById(R.id.button_2_month);
        button_3_month = (Button) findViewById(R.id.button_3_month);
        button_4_month = (Button) findViewById(R.id.button_4_month);
        button_5_month = (Button) findViewById(R.id.button_5_month);
        button_6_month = (Button) findViewById(R.id.button_6_month);
        button_7_month = (Button) findViewById(R.id.button_7_month);
        button_8_month = (Button) findViewById(R.id.button_8_month);
        button_9_month = (Button) findViewById(R.id.button_9_month);
        button_10_month = (Button) findViewById(R.id.button_10_month);
        button_11_month = (Button) findViewById(R.id.button_11_month);
        button_12_month = (Button) findViewById(R.id.button_12_month);
        //功能模块
        linear_integral_details = (LinearLayout) findViewById(R.id.linear_integral_details);
        linear_ycoin_details = (LinearLayout) findViewById(R.id.linear_ycoin_details);
        linear_give_present = (LinearLayout) findViewById(R.id.linear_give_present);
        linear_googbag_details = (LinearLayout) findViewById(R.id.linear_googbag_details);
        linear_getbag_record = (LinearLayout) findViewById(R.id.linear_getbag_record);
        linear_grade_record = (LinearLayout) findViewById(R.id.linear_grade_record);
        linear_give_ycoin = (LinearLayout) findViewById(R.id.linear_give_ycoin);
        linear_give_integral = (LinearLayout) findViewById(R.id.linear_give_integral);

        linear_integral_details.setOnClickListener(this);
        linear_ycoin_details.setOnClickListener(this);
        linear_give_present.setOnClickListener(this);
        linear_googbag_details.setOnClickListener(this);
        linear_getbag_record.setOnClickListener(this);
        linear_grade_record.setOnClickListener(this);
        linear_give_ycoin.setOnClickListener(this);
        linear_give_integral.setOnClickListener(this);

        pull_zoom_scroll_usermessage.setOnRefreshListener(new PullToZoomScrollView.OnRefreshListener() {
            @Override
            public void onPullDownToRefresh() {
                getUserDetails(getIntent().getStringExtra("usercode"));
            }
        });
        /*pull_zoom_scroll_usermessage.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Log.e("-- 滑动测试","-"+scrollX+"-"+scrollY+"-"+oldScrollX+"-"+oldScrollY);
            }
        });*/
    }

    /**
     *界面填充数据
     * @param data
     */
    public void showView(UserDetailsByHouseCodeInfo data){
        if (UserDetailsByHouseCodeInfo.get()!=null){
            if (data.imgurl!=null||!data.imgurl.equals("")){
                ImageloaderUtil.displayImage(mContext, data.getImgurl(),
                        imageViewHeadPortrait ,
                        ImageloaderUtil.getDisplayImageOptions(R.drawable.default_header,R.drawable.default_header,R.drawable.default_header));
            }
            textViewName.setText(data.getNickname());
            if (data.getGender().equals("1")){
                imageViewSex.setImageResource(R.drawable.ic_sex_man);
            }else{
                imageViewSex.setImageResource(R.drawable.ic_sex_women);
            }
            if (data.getVipname().contains("LV")){
                tvLevel.setVisibility(View.VISIBLE);
                tvLevel.setText(data.getVipname());
            }
            textViewLeft.setText("积分："+data.getScore());
            textViewRight.setText("益币："+data.getAmount());
            textViewRight2.setText("福袋："+data.getFreefednums()+"");
            textview_headusername.setText(data.getUserName());
            textview_communityname.setText(data.getCommunity());
            textview_phonecode.setText(data.getUserPhone());
            textview_doorplatecode.setText(data.getHoursenum());
            if (data.getConcernsinfoRespList()!=null){
                fillFDNumToMonth(data.getConcernsinfoRespList());
            }
        }
    }

    /**
     *获取用户信息
     * @param code
     */
    public void getUserDetails(String code){
        showLoadingDialog("玩命加载中！");
        UserApi.get().getUserDetailsByHouseCode(this, code, new ApiConfig.ApiRequestListener<UserDetailsByHouseCodeInfo>() {
            @Override
            public void onSuccess(String msg, UserDetailsByHouseCodeInfo data) {
                dismissLoadingDialog();
                showView(data);
            }
            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    /**
     * 显示每月使用的福袋数
     * @param mData
     */
    public void fillFDNumToMonth(List<UserDetailsByHouseCodeInfo.ConcernsinfoRespListBean> mData){
        for (int i=0;i<mData.size();i++){
            if (mData.get(i).getMonths()!=null){
                String str=null;
                if (mData.get(i).getMonths().contains("-")){
                    str=mData.get(i).getMonths().substring(mData.get(i).getMonths().indexOf("-"),mData.get(i).getMonths().length());
                }else {
                    str=mData.get(i).getMonths();
                }
                switch (str){
                    case "01":
                        button_1_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_1_month.setText(mData.get(i).getFednums()+"\n1月");
                        button_1_month.setTextColor(Color.WHITE);
                        break;
                    case "02":
                        button_2_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_2_month.setText(mData.get(i).getFednums()+"\n2月");
                        button_2_month.setTextColor(Color.WHITE);
                        break;
                    case "03":
                        button_3_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_3_month.setText(mData.get(i).getFednums()+"\n3月");
                        button_3_month.setTextColor(Color.WHITE);
                        break;
                    case "04":
                        button_4_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_4_month.setText(mData.get(i).getFednums()+"\n4月");
                        button_4_month.setTextColor(Color.WHITE);
                        break;
                    case "05":
                        button_5_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_5_month.setText(mData.get(i).getFednums()+"\n5月");
                        button_5_month.setTextColor(Color.WHITE);
                        break;
                    case "06":
                        button_6_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_6_month.setText(mData.get(i).getFednums()+"\n6月");
                        button_6_month.setTextColor(Color.WHITE);
                        break;
                    case "07":
                        button_7_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_7_month.setText(mData.get(i).getFednums()+"\n7月");
                        button_7_month.setTextColor(Color.WHITE);
                        break;
                    case "08":
                        button_8_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_8_month.setText(mData.get(i).getFednums()+"\n8月");
                        button_8_month.setTextColor(Color.WHITE);
                        break;
                    case "09":
                        button_9_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_9_month.setText(mData.get(i).getFednums()+"\n9月");
                        button_9_month.setTextColor(Color.WHITE);
                        break;
                    case "10":
                        button_10_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_10_month.setText(mData.get(i).getFednums()+"\n10月");
                        button_10_month.setTextColor(Color.WHITE);
                        break;
                    case "11":
                        button_11_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_11_month.setText(mData.get(i).getFednums()+"\n11月");
                        button_11_month.setTextColor(Color.WHITE);
                        break;
                    case "12":
                        button_12_month.setBackground(this.getResources().getDrawable(R.drawable.bg_btn_theme_radius_34px));
                        button_12_month.setText(mData.get(i).getFednums()+"\n12月");
                        button_12_month.setTextColor(Color.WHITE);
                        break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.linear_integral_details://积分详情 <2>
                intent = new Intent(this,IntegralDetailsActivity.class);
                intent.putExtra("point_type",2);
                intent.putExtra("code",getIntent().getStringExtra("usercode"));
                startActivity(intent);
                break;
            case R.id.linear_ycoin_details://益币详情 <1>
                intent=new Intent(this,IntegralDetailsActivity.class);
                intent.putExtra("point_type",1);
                intent.putExtra("code",getIntent().getStringExtra("usercode"));
                startActivity(intent);
                break;
            case R.id.linear_give_present://赠送礼品
                if (UserInfo.get().usertype.contains(UserInfo.USER_ALL)||UserInfo.get().usertype.contains(UserInfo.USER_GIFT_GIVING)){
                    intent = new Intent(this,GiftGivingActivity.class);
                    intent.putExtra("wherecome","notscan");
                    intent.putExtra("code",getIntent().getStringExtra("usercode"));
                    startActivity(intent);
                }else {
                    showShortToast("您没有赠送礼品权限！");
                }
                break;
            case R.id.linear_googbag_details://福袋详情 <3>
                intent=new Intent(this,IntegralDetailsActivity.class);
                intent.putExtra("point_type",3);
                intent.putExtra("code",getIntent().getStringExtra("usercode"));
                startActivity(intent);
                break;
            case R.id.linear_getbag_record://取袋记录
                intent=new Intent(this,FetchBagRecordActivity.class);
                intent.putExtra("code",getIntent().getStringExtra("usercode"));
                intent.putExtra("phone",((UserDetailsByHouseCodeInfo)getIntent().getSerializableExtra("UserDetailsByHouseCodeInfo")).getUserPhone());
                startActivity(intent);
                break;
            case R.id.linear_grade_record://垃圾分类评分记录
                intent = new Intent(mContext,RecycleScoreRecordActivity.class);
                intent.putExtra("code",getIntent().getStringExtra("usercode"));
                startActivity(intent);
                break;
            case R.id.linear_give_ycoin://赠送益币
                if (UserInfo.get().usertype.contains(UserInfo.USER_ALL)||UserInfo.get().usertype.contains(UserInfo.USER_MANAGER)){
                    intent = new Intent(this,GiveIntegrationActivity.class);
                    intent.putExtra("type","ycoin");
                    intent.putExtra("giftType","1");
                    intent.putExtra("code",getIntent().getStringExtra("usercode"));
                    intent.putExtra("houseCode",((UserDetailsByHouseCodeInfo)getIntent().getSerializableExtra("UserDetailsByHouseCodeInfo")).getHouseCode());
                    intent.putExtra("phone",((UserDetailsByHouseCodeInfo)getIntent().getSerializableExtra("UserDetailsByHouseCodeInfo")).getUserPhone());
                    startActivity(intent);
                }else {
                    showShortToast("您没有赠送益币权限！");
                }
                break;
            case R.id.linear_give_integral://赠送积分
                if (UserInfo.get().usertype.contains(UserInfo.USER_ALL)||UserInfo.get().usertype.contains(UserInfo.USER_MANAGER)){
                    intent = new Intent(this,GiveIntegrationActivity.class);
                    intent.putExtra("giftType","2");
                    intent.putExtra("type","ycoin");
                    intent.putExtra("code",getIntent().getStringExtra("usercode"));
                    intent.putExtra("houseCode",((UserDetailsByHouseCodeInfo)getIntent().getSerializableExtra("UserDetailsByHouseCodeInfo")).getHouseCode());
                    intent.putExtra("phone",((UserDetailsByHouseCodeInfo)getIntent().getSerializableExtra("UserDetailsByHouseCodeInfo")).getUserPhone());
                    startActivity(intent);
                }else {
                    showShortToast("您没有赠送积分权限！");
                }
                break;
        }
    }
}
