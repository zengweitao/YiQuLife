package com.quanmai.yiqu.ui.Around;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.content.LocalBroadcastManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.AroundApi;
import com.quanmai.yiqu.api.vo.CouponInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.util.DateUtil;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.SPUtils;
import com.quanmai.yiqu.ui.fragment.PersonalFragment;

public class ShakeActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_left,tv_title;
    Button buttonMyCoupon;
    //摇一摇主界面
    RelativeLayout relativeLayoutShake;
    TextView textViewChanges;
    ImageView imageViewShake; //动画imageView
    //优惠券界面
    RelativeLayout relativeLayoutCoupon;
    TextView textViewCouponChanges;
    //优惠券卡片
    LinearLayout linearLayoutCoupon;
    ImageView imageViewBg;
    TextView textViewTitle;
    TextView textViewPrice;
    TextView textViewDate;
    Button buttonFetchCoupon;
    //积分卡片
    Button buttonFetchIntegration;
    TextView textViewIntegration;
    LinearLayout linearLayoutIntegration;

    private Vibrator vibrator;
    private SensorManager sensorManager;
    private static final int SENSOR_SHAKE = 101;
    private static final int SENSOR_STOP = 102;
    int lastTime = 0;//剩余次数
    String mPoints ;
    String couponId;
    // 两次检测的时间间隔
    private static final int UPTATE_INTERVAL_TIME = 70;
    // 上次检测时间
    private long lastUpdateTime;
    // 手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;

    private int tempStart;
    private int tempEnd;
    int time ;
    long flagTime;
    boolean mflag=false;
    CouponInfo mInfo;
    /**
     * 动作执行
     */
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            AnimationDrawable aniDraw = (AnimationDrawable)imageViewShake.getBackground();

            switch (msg.what) {
                case SENSOR_SHAKE:
                    //调用接口---执行动画
                    aniDraw.start();
                    relativeLayoutShake.setVisibility(View.VISIBLE);
                    relativeLayoutCoupon.setVisibility(View.GONE);
//                    time +=1500;
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            Log.e("mark",time+"---time");
//                            handler.sendEmptyMessage(SENSOR_STOP);
//                        }
//                    },time);
                    break;

                case SENSOR_STOP:{
//                    tempEnd++;
//                    Log.e("mark","shake: "+tempStart+"  stop: "+tempEnd);
//                    if (tempStart==tempEnd){
//
//                    }else {
//                        tempEnd  = tempStart-1;
//                    }
                    mflag = false;
                    flagTime = 0;
                    aniDraw.stop();
                    mInfo = null;
                    if (Utils.isNetworkAvailable(ShakeActivity.this)){
                        ShakeAndShake();
                    }else {
                        Toast.makeText(ShakeActivity.this, "没有网络连接", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake);

        init();
        lastTime = getIntent().getIntExtra("lastTimes",0);

        initTimes(lastTime+"");

    }



    @Override
    protected void onResume() {
        super.onResume();
        CountCouponAmount();
        start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopShake();
    }

    private void stopShake(){
        if (sensorManager != null) {// 取消监听器
            sensorManager.unregisterListener(sensorEventListener);
        }
    }

    private void start(){
        tempEnd=0;
        tempStart=0;
        if (sensorManager != null) {// 注册监听器
            sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
            // 第一个参数是Listener，第二个参数是所得传感器类型，第三个参数值获取传感器信息的频率
        }
    }

    /**
     * 初始化
     * */
    private void init() {
        findViewById(R.id.iv_back).setVisibility(View.GONE);
        tv_left = (TextView) findViewById(R.id.tv_left);
        tv_title = (TextView) findViewById(R.id.tv_title);
        buttonMyCoupon = (Button) findViewById(R.id.buttonMyCoupon);

        relativeLayoutShake = (RelativeLayout)findViewById(R.id.relativeLayoutShake);
        textViewChanges = (TextView)findViewById(R.id.textViewChanges);
        imageViewShake = (ImageView)findViewById(R.id.imageViewShake);
        relativeLayoutCoupon = (RelativeLayout)findViewById(R.id.relativeLayoutCoupon);
        textViewCouponChanges = (TextView)findViewById(R.id.textViewCouponChanges);
        linearLayoutCoupon = (LinearLayout)findViewById(R.id.linearLayoutCoupon);
        imageViewBg = (ImageView)findViewById(R.id.imageViewBg);
        textViewTitle = (TextView)findViewById(R.id.textViewTitle);
        textViewPrice = (TextView)findViewById(R.id.textViewPrice);
        textViewDate = (TextView)findViewById(R.id.textViewDate);
        buttonFetchCoupon = (Button)findViewById(R.id.buttonFetchCoupon);
        buttonFetchIntegration = (Button)findViewById(R.id.buttonFetchIntegration);
        textViewIntegration = (TextView)findViewById(R.id.textViewIntegration);
        linearLayoutIntegration = (LinearLayout)findViewById(R.id.linearLayoutIntegration);

        tv_left.setText("关闭");
        tv_title.setText("摇一摇得优惠");
        buttonMyCoupon.setOnClickListener(this);
        buttonFetchCoupon.setOnClickListener(this);
        buttonFetchIntegration.setOnClickListener(this);
        tv_left.setOnClickListener(this);
        linearLayoutCoupon.setOnClickListener(this);

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    //摇一摇
    public void ShakeAndShake(){
        start();
        AroundApi.getInstance().GetSuperiseByShake(this,"" ,new ApiConfig.ApiRequestListener<CouponInfo>() {
            @Override
            public void onSuccess(String msg, CouponInfo data) {
                mInfo = data;
                int lastFetchTimes =Integer.parseInt(SPUtils.get(mContext,"fetchTimes",0)+"") ;
                SPUtils.put(mContext,"fetchTimes",lastFetchTimes+1);

                int updateLastTimes = Integer.parseInt(SPUtils.get(mContext,"fetchTimes",0)+"") ;
                int maxTimes = Integer.parseInt(SPUtils.get(mContext,"maxTimes",0)+"") ;
                if (updateLastTimes<maxTimes){
                    time=0;
                    relativeLayoutShake.setVisibility(View.GONE);
                    relativeLayoutCoupon.setVisibility(View.VISIBLE);
                    if (data!=null){
                        couponId = data.id;
                        initCoupon(data);
                    }else {
                        mPoints = msg;
                        linearLayoutCoupon.setVisibility(View.GONE);
                        linearLayoutIntegration.setVisibility(View.VISIBLE);
                        try{
                            if (Integer.parseInt(msg)<=0){
                                textViewIntegration.setText("再接再厉");
                            }else {
                                textViewIntegration.setText("奖励积分"+msg+"分");
                            }
                        }catch (NumberFormatException e){
                            textViewIntegration.setText("奖励积分"+msg+"分");
                        }

                        //更新积分
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(new Intent(PersonalFragment.ACTION_NETWORKING_TO_REFRESH_DATA));
                    }
                }
                initTimes((maxTimes-updateLastTimes)+"");
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    //领取积分或优惠券
    private void GetCouponOrIntegration(final String id, final String points){
        AroundApi.getInstance().getMyShakeCoupon(mContext,id,points, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                if (Integer.parseInt(points)>0){
                    showCustomToast("领取成功！");
                }
                relativeLayoutShake.setVisibility(View.VISIBLE);
                relativeLayoutCoupon.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(id)){
                    CountCouponAmount();
                }
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    //统计优惠券数量
    private void CountCouponAmount(){
        AroundApi.getInstance().countCouponAmount(mContext, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                if (!TextUtils.isEmpty(data)){
                    buttonMyCoupon.setText("我的优惠券（"+data+"）");
                }else{
                    buttonMyCoupon.setText("我的优惠券（0）");
                }

            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    private void initCoupon(CouponInfo coupon){
        linearLayoutCoupon.setVisibility(View.VISIBLE);
        linearLayoutIntegration.setVisibility(View.GONE);
        ImageloaderUtil.displayImage(this,coupon.thumbnail,imageViewBg);
        textViewTitle.setText(coupon.privilegeName);
        if (!TextUtils.isEmpty(coupon.privilegePrice)&&!coupon.privilegePrice.equals("null")){
            textViewPrice.setText(coupon.privilegePrice);
        }else {
            textViewPrice.setVisibility(View.GONE);
        }

        textViewDate.setText("有效期至"+ DateUtil.formatToOther(mInfo.endTime,"yyyy-MM-dd","yyyy年MM月dd日"));
    }

    private void initTimes(String times){
        lastTime = Integer.parseInt(times);
        String str = "";
        if (Integer.parseInt(times)>0){
             str = "你还有"+times+"次疯摇机会";
        }else {
            str = "你还有0次疯摇机会";
            showCustomToast("你的疯摇次数已经用完了");
        }
        SpannableStringBuilder spannableString = new SpannableStringBuilder(str);
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f9e71d")),3, 3+(times.length()), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewChanges.setText(spannableString);
        textViewCouponChanges.setText(spannableString);
    }
    /**
     * 重力感应监听
     */
    private SensorEventListener sensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 现在检测时间
            long currentUpdateTime = System.currentTimeMillis();
            // 两次检测的时间间隔
            long timeInterval = currentUpdateTime - lastUpdateTime;
            // 判断是否达到了检测时间间隔
            if (timeInterval < UPTATE_INTERVAL_TIME)
                return;
            // 现在的时间变成last时间
            lastUpdateTime = currentUpdateTime;
            // 传感器信息改变时执行该方法
            float[] values = event.values;
            float x = values[0]; // x轴方向的重力加速度，向右为正
            float y = values[1]; // y轴方向的重力加速度，向前为正
            float z = values[2]; // z轴方向的重力加速度，向上为正

            // 获得x,y,z的变化值
            float deltaX = x - lastX;
            float deltaY = y - lastY;
            float deltaZ = z - lastZ;

            // 将现在的坐标变成last坐标
            lastX = x;
            lastY = y;
            lastZ = z;
            // 一般在这三个方向的重力加速度达到40就达到了摇晃手机的状态。
            int medumValue = 12;// 如果不敏感请自行调低该数值,低于10的话就不行了,因为z轴上的加速度本身就已经达到10了
            double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ* deltaZ)/ timeInterval * 10000;
//            if (Math.abs(x) > medumValue || Math.abs(y) > medumValue) {
//                stop();
//                mFlag=false;
//                long [] pattern = {100,200,300,400};  //震动频率
//                vibrator.vibrate(pattern,-1);
//                Message msg = new Message();
//                msg.what = SENSOR_SHAKE;
//                handler.sendMessage(msg);
//            }else{
//                mFlag=true;
//            }
            if (speed>=1400) {
                mflag = true;
                flagTime = System.currentTimeMillis();
                tempStart++;
                Log.e("mark","start: "+tempStart);
//                long[] pattern = {100, 200, 300, 400};  //震动频率
                vibrator.vibrate(200);
                Message msg = new Message();
                msg.what = SENSOR_SHAKE;
                handler.sendMessage(msg);
            }

            if (mflag&&currentUpdateTime-flagTime>1500){
                if (speed<1400){
                    handler.sendEmptyMessage(SENSOR_STOP);
                }
            }

//            if (timeInterval>1000&&speed<1400){
//                if (flag){
//                    handler.sendEmptyMessage(SENSOR_STOP);
//                }
//            }
//            }else {
//                if (speed>500){
//                    mFlag=true;
//                    Toast.makeText(ShakeActivity.this, "speed: "+speed, Toast.LENGTH_SHORT).show();
//                }
//            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //关闭
            case R.id.tv_left:{
                finish();
                break;
            }
            //我的优惠券
            case R.id.buttonMyCoupon:{
                startActivity(MyCouponActivity.class);
                break;
            }
            //领取优惠券
            case R.id.buttonFetchCoupon:{
                GetCouponOrIntegration(couponId,"");
                break;
            }
            //领取积分
            case R.id.buttonFetchIntegration:{
                GetCouponOrIntegration("",mPoints);
                break;
            }
            case R.id.linearLayoutCoupon:{
                if (mInfo!=null){
                    Intent intent = new Intent(mContext, ShowCouponDirectlyActivity.class);
                    intent.putExtra("type","shake");
                    intent.putExtra("info", mInfo.id);
                    startActivity(intent);
                }
                break;
            }
            default:
                break;
        }
    }


}
