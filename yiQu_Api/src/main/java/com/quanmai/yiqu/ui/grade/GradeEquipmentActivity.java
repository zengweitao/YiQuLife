package com.quanmai.yiqu.ui.grade;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.ChooseEqDetailInfo;
import com.quanmai.yiqu.api.vo.PointPenaltyInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.photopicker.PhotoPickerActivity;
import com.quanmai.yiqu.common.util.PhotoUtil;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.widget.AmountView;
import com.quanmai.yiqu.common.widget.CameraProtectActivity;
import com.quanmai.yiqu.common.widget.MyGridView;
import com.quanmai.yiqu.ui.publish.PublishGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class GradeEquipmentActivity extends BaseActivity implements View.OnClickListener{

    TextView tv_title;
    LinearLayout llCommunity;  //选择设备地址
    TextView tvCommunity,tvEQ;     //小区名，设备名
    RelativeLayout rlAddPicture;  //添加图片
    MyGridView gridview;
    TextView tvEQCount;   //设施应有数量
    LinearLayout llContent;
    Button btnSubmit;

    Dialog chooseDialog; //图片选择dialog
    ArrayList<String> imglist; //图片数组
    private final int TAKE_LOCAL_PHOTO = 201;  //选择照片
    private final int TAKE_PICTURE = 205;//拍照
    PublishGridViewAdapter mAdapter;

    ChooseEqDetailInfo mInfo;
    String commName; //小区名;
    String eqName;  //设备名
    List<List<String>> mScoreList;
    CommonList<PointPenaltyInfo> mDataItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_equipment);

        mInfo = (ChooseEqDetailInfo)getIntent().getSerializableExtra("Info");
        commName = getIntent().getStringExtra("CommName");
        eqName = getIntent().getStringExtra("EQName");


        initView();

        mAdapter.addCloseListener(new PublishGridViewAdapter.OnCloseClick() {
            @Override
            public void changView() {
                rlAddPicture.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
            }

            @Override
            public void onClose(String path) {
                if (imglist!=null){
                    if (imglist.contains(path)){
                        imglist.remove(path);
                    }
                }
            }

            @Override
            public void chooseNewImg() {
                chooseImg();
            }
        });

        //数据丢失恢复
        if (savedInstanceState != null){
            if (imglist!=null){
                imglist.clear();
                if (savedInstanceState.getStringArrayList("list").size()>0){
                    imglist.addAll(savedInstanceState.getStringArrayList("list"));

                    if (mAdapter!=null){
                        mAdapter.add(imglist);
                        gridview.setAdapter(mAdapter);
                        rlAddPicture.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

        GetGradeItem();
    }

    /**
     * 评分view
     * */
    private void initSelectView(CommonList<PointPenaltyInfo> data) {
        llContent.removeAllViews();
        if (data==null||data.size()<0){
            return;
        }
        for (int i=0;i<data.size();i++){
            final PointPenaltyInfo info = data.get(i);
            View mView = LayoutInflater.from(this).inflate(R.layout.grade_item,null);
            TextView tvName = (TextView)mView.findViewById(R.id.tvName);
            AmountView amountView = (AmountView)mView.findViewById(R.id.amountView);

            tvName.setText(info.getCheckname());

            amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
                @Override
                public void onAmountChange(View view, int amount) {
                    List<String> mList = new ArrayList<String>();
                    List<String> idList = new ArrayList<String>();
                    for (int i=0;i<mScoreList.size();i++){
                        idList.add(mScoreList.get(i).get(0));
                    }

                    if (idList.contains(info.getId())){
                        if (amount==0){
                            mScoreList.remove(idList.indexOf(info.getId()));
                        }else {
                            mScoreList.get(idList.indexOf(info.getId())).remove(1);
                            mScoreList.get(idList.indexOf(info.getId())).add(1,amount+"");
                        }
                    }else {
                        if (amount>0){
                            mList.add(info.getId()+"");
                            mList.add(amount+"");
                            mList.add(info.getScore());
                            mScoreList.add(mList);
                        }
                    }

                    Log.e("mark",new Gson().toJson(mScoreList));
                }
            });

            llContent.addView(mView);
        }
    }

    private void initView() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_title.setText("设施评分");
        llCommunity = (LinearLayout)findViewById(R.id.llCommunity);
        tvCommunity = (TextView)findViewById(R.id.tvCommunity);
        tvEQ = (TextView)findViewById(R.id.tvEQ);
        rlAddPicture = (RelativeLayout)findViewById(R.id.rlAddPicture);
        gridview = (MyGridView)findViewById(R.id.gridview);
        tvEQCount = (TextView)findViewById(R.id.tvEQCount);
        llContent = (LinearLayout)findViewById(R.id.llContent);
        btnSubmit = (Button)findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
        llCommunity.setOnClickListener(this);
        rlAddPicture.setOnClickListener(this);
        if (imglist==null){
            imglist = new ArrayList<>();
        }

        mAdapter = new PublishGridViewAdapter(this,imglist);
        mScoreList = new ArrayList<>();
        mDataItem = new CommonList<>();
        gridview.setAdapter(mAdapter);

        tvCommunity.setText(commName);
        tvEQ.setText(eqName);
        tvEQCount.setText(mInfo.getAmenityNum()+"");
    }

    /**
     * 获取扣分项
     * */
    private void GetGradeItem(){
        GradeApi.get().GradeItemList(mContext, mInfo.getClassgistTypeid(), new ApiConfig.ApiRequestListener<CommonList<PointPenaltyInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<PointPenaltyInfo> data) {
                mDataItem.addAll(data);
                initSelectView(data);
            }

            @Override
            public void onFailure(String msg) {
                showCustomToast(msg);
            }
        });
    }

    /**
     * 选择图片
     * */
    private void chooseImg(){
        if (chooseDialog == null) {
            chooseDialog = new Dialog(mContext,R.style.MyDialogStyle);
            chooseDialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(0));
            chooseDialog.setCanceledOnTouchOutside(true);
            chooseDialog.setContentView(R.layout.dialog_choose_head_img);
            chooseDialog.findViewById(R.id.btn_local_photo).setOnClickListener(this);
            chooseDialog.findViewById(R.id.btn_take_photos).setOnClickListener(this);
            chooseDialog.findViewById(R.id.btn_cancel).setOnClickListener(this);

            Window window = chooseDialog.getWindow();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            window.setGravity(Gravity.BOTTOM | Gravity.CENTER);
            window.setAttributes(params);
        }
        chooseDialog.show();
    }

    /**
     * 检查图片是否超过3张
     * */
    public boolean checkList(List<String> list){
        boolean checkList = true;

        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (i==2&&list.get(i)!=null){
                    checkList = false;
                }
            }
        }
        return checkList;
    }

    private List<List<String>> changeScoreList(List<List<String>> list){
        List<List<String>> newList = new ArrayList<>();
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                List<String> childList = list.get(i);
                String score = ((Integer.parseInt(childList.get(1))*Double.parseDouble(childList.get(2))))+"";
                childList.remove(2);
                childList.remove(1);
                childList.add(score);
                newList.add(childList);
            }
        }

        return newList;
    }

    /**
     * 提交打分
     * */
    private void submitScore(){

        if (imglist==null||imglist.size()==0){
            showCustomToast("请添加图片");
            return;
        }

        if (imglist.get(imglist.size() - 1) == null){
            imglist.remove(imglist.size() - 1);
        }


//        if (mScoreList.size()!=mDataItem.size()){
//            showCustomToast("请完成打分");
//            return;
//        }

        showLoadingDialog("请稍候");
        new QiniuUtil(mContext, imglist, new QiniuUtil.OnQiniuUploadListener() {
            @Override
            public void success(String names) {
                GradeApi.get().GradeEquipment(mContext, mInfo.getAmenityAreaId(), mInfo.getAmenityid(),new Gson().toJson(changeScoreList(mScoreList)),names, new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        finish();
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                    }
                });
            }

            @Override
            public void failure() {
                dismissLoadingDialog();
                showShortToast("图片上传失败");
            }
        }).upload();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list",imglist);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case TAKE_LOCAL_PHOTO:{
                    if (intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT).size()>0){
                        rlAddPicture.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                    }
                    imglist.clear();
                    imglist.addAll(intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));

                    if (imglist.size() < 3){
                        imglist.add(null);// 添加满了不显示加号
                    }
                    mAdapter.add(imglist);
                    gridview.setAdapter(mAdapter);
                    break;
                }

                case TAKE_PICTURE:{
                    String picPath = intent.getStringExtra(CameraProtectActivity.IMAGE_PATH);
                    if (intent.getStringArrayListExtra("imgList")!=null){
                        if (imglist.size()>0){
                            imglist.clear();
                        }
                        imglist.addAll(intent.getStringArrayListExtra("imgList"));
                    }
                    if (picPath!=""&&picPath!=null){
                        rlAddPicture.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                    }
                    if (imglist.size()>0){
                        if(imglist.get(imglist.size()-1)==null){
                            imglist.remove(imglist.size() - 1);
                        }
                    }
                    imglist.add(picPath);
                    if (imglist.size() < 3){
                        imglist.add(null);// 添加满了不显示加号
                    }
                    mAdapter.add(imglist);
                    gridview.setAdapter(mAdapter);
                    break;
                }

                default:break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //选择图片
            case R.id.rlAddPicture:{
                chooseImg();
                break;
            }
            //本地选择
            case R.id.btn_local_photo:{
                if (checkList(imglist)){
                    Intent intent = new Intent(this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN,3);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE,PhotoPickerActivity.MODE_MULTI);
                    if (imglist.size()>0){
                        if(imglist.get(imglist.size() - 1) == null) {
                            imglist.remove(imglist.size()-1);
                            intent.putExtra(PhotoPickerActivity.SELECTED_PHOTOS,imglist);
                        }
                    }else {
                        intent.putExtra("max", 3-imglist.size());
                    }
                    startActivityForResult(intent, TAKE_LOCAL_PHOTO);
                }else {
                    showCustomToast("最多只能选6张");
                }
                chooseDialog.cancel();
                break;
            }
            //拍照
            case R.id.btn_take_photos:{
                if (checkList(imglist)){
                    if (PhotoUtil.isSdcardExisting()){
                        Intent intent = new Intent(this,
                                CameraProtectActivity.class);
                        intent.putStringArrayListExtra("imgList",imglist);
                        startActivityForResult(intent, TAKE_PICTURE);
                    }else {
                        showCustomToast("请插入SD卡");
                    }
                }else {
                    showCustomToast("最多只能选6张");
                }

                chooseDialog.cancel();
                break;
            }
            //取消dialog
            case R.id.btn_cancel:{
                chooseDialog.dismiss();
                break;
            }
            //选择设施
            case R.id.llCommunity:{
                finish();
                break;
            }
            //提交打分
            case R.id.btnSubmit:{
                submitScore();
                break;
            }
            default:break;
        }
    }
}
