package com.quanmai.yiqu.ui.grade;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.api.vo.ChooseEqAreaInfo;
import com.quanmai.yiqu.api.vo.PointPenaltyInfo;
import com.quanmai.yiqu.api.vo.ScoreCommDetailInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.photopicker.PhotoPickerActivity;
import com.quanmai.yiqu.common.util.PhotoUtil;
import com.quanmai.yiqu.common.widget.CameraProtectActivity;
import com.quanmai.yiqu.common.widget.MyGridView;
import com.quanmai.yiqu.ui.grade.adapter.GradeCleanAdapter;
import com.quanmai.yiqu.ui.publish.PublishGridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 清运评分页面
 */
public class GradeCleanActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvCommunity;       //小区名
    private TextView tvEQ;              //设备名
    private LinearLayout llCommunity;   //选择设备地址
    private RelativeLayout rlAddPicture;//添加图片
    private MyGridView gridview;
    private PullToRefreshListView listView;
    private Button btnSubmit;

    private Dialog chooseDialog;                //图片选择dialog
    private ArrayList<String> imglist;          //图片数组
    private final int TAKE_LOCAL_PHOTO = 201;   //选择照片
    private final int TAKE_PICTURE = 205;       //拍照

    private PublishGridViewAdapter mImgAdapter; //图片适配器
    private GradeCleanAdapter mListAdapter;     //列表适配器

    private ScoreCommDetailInfo mCommInfo;      //小区信息实体
    private ChooseEqAreaInfo mEqAreaInfo;       //设施点信息实体

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grade_clean);
        initView();
        init();
        initData();

        //数据丢失恢复
        if (savedInstanceState != null) {
            if (imglist != null) {
                imglist.clear();
                if (savedInstanceState.getStringArrayList("list").size() > 0) {
                    imglist.addAll(savedInstanceState.getStringArrayList("list"));

                    if (mImgAdapter != null) {
                        mImgAdapter.add(imglist);
                        gridview.setAdapter(mImgAdapter);
                        rlAddPicture.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_title)).setText("清运评分");
        tvCommunity = (TextView) findViewById(R.id.tvCommunity);
        tvEQ = (TextView) findViewById(R.id.tvEQ);
        llCommunity = (LinearLayout) findViewById(R.id.llCommunity);
        rlAddPicture = (RelativeLayout) findViewById(R.id.rlAddPicture);
        gridview = (MyGridView) findViewById(R.id.gridview);
        listView = (PullToRefreshListView) findViewById(R.id.listView);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
        rlAddPicture.setOnClickListener(this);
        llCommunity.setOnClickListener(this);

        if (imglist == null) {
            imglist = new ArrayList<>();
        }

        mImgAdapter = new PublishGridViewAdapter(this, imglist);
        gridview.setAdapter(mImgAdapter);
    }

    private void init() {
        mListAdapter = new GradeCleanAdapter(mContext);
        listView.setAdapter(mListAdapter);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            strings.add("设备点" + i);
        }

        mImgAdapter.addCloseListener(new PublishGridViewAdapter.OnCloseClick() {
            @Override
            public void changView() {
                rlAddPicture.setVisibility(View.VISIBLE);
                gridview.setVisibility(View.GONE);
            }

            @Override
            public void onClose(String path) {
                if (imglist != null) {
                    if (imglist.contains(path)) {
                        imglist.remove(path);
                    }
                }
            }

            @Override
            public void chooseNewImg() {
                chooseImg();
            }
        });
    }

    private void initData() {
        if (getIntent().hasExtra("CommInfo")) {
            mCommInfo = (ScoreCommDetailInfo) getIntent().getSerializableExtra("CommInfo");
            tvCommunity.setText(mCommInfo.getCommname());
        }

        if (getIntent().hasExtra("EqAreaInfo")) {
            mEqAreaInfo = (ChooseEqAreaInfo) getIntent().getSerializableExtra("EqAreaInfo");
            tvEQ.setText(mEqAreaInfo.getAmenityareaName());
        }

        getGradeItem("2");
    }

    /**
     * 选择图片
     */
    private void chooseImg() {
        if (chooseDialog == null) {
            chooseDialog = new Dialog(mContext, R.style.MyDialogStyle);
            chooseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
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
     */
    public boolean checkList(List<String> list) {
        boolean checkList = true;

        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i == 2 && list.get(i) != null) {
                    checkList = false;
                }
            }
        }
        return checkList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("list", imglist);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case TAKE_LOCAL_PHOTO: {
                    if (intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT).size() > 0) {
                        rlAddPicture.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                    }
                    imglist.clear();
                    imglist.addAll(intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));

                    if (imglist.size() < 3) {
                        imglist.add(null);// 添加满了不显示加号
                    }
                    mImgAdapter.add(imglist);
                    gridview.setAdapter(mImgAdapter);
                    break;
                }

                case TAKE_PICTURE: {
                    String picPath = intent.getStringExtra(CameraProtectActivity.IMAGE_PATH);
                    if (intent.getStringArrayListExtra("imgList") != null) {
                        if (imglist.size() > 0) {
                            imglist.clear();
                        }
                        imglist.addAll(intent.getStringArrayListExtra("imgList"));
                    }
                    if (picPath != "" && picPath != null) {
                        rlAddPicture.setVisibility(View.GONE);
                        gridview.setVisibility(View.VISIBLE);
                    }
                    if (imglist.size() > 0) {
                        if (imglist.get(imglist.size() - 1) == null) {
                            imglist.remove(imglist.size() - 1);
                        }
                    }
                    imglist.add(picPath);
                    if (imglist.size() < 3) {
                        imglist.add(null);// 添加满了不显示加号
                    }
                    mImgAdapter.add(imglist);
                    gridview.setAdapter(mImgAdapter);
                    break;
                }

                default:
                    break;
            }
        }
    }

    private void submit() {
        if (imglist == null || imglist.size() <= 0) {
            showCustomToast("请添加图片");
            return;
        }

        if (imglist.get(imglist.size() - 1) == null) {
            imglist.remove(imglist.size() - 1);
        }

        String strImgs = "";
        if (imglist != null && imglist.size() > 0) {
            StringBuffer buffer = new StringBuffer("");
            if (imglist.size() > 0) {
                buffer.append(imglist.get(0));
            }
            for (int i = 1; i < imglist.size(); i++) {
                buffer.append(",");
                buffer.append(imglist.get(i));
            }
            strImgs = buffer.toString();
        }

        List<String[]> scoreList = mListAdapter.getScoreList();
        String strScore = new Gson().toJson(scoreList).toString();


        cleanUpGrade(mCommInfo.getCommcode(), mEqAreaInfo.getId() + "", strScore, strImgs);
    }

    //评分条例列表查询
    private void getGradeItem(String classgistType) {
        showLoadingDialog();
        GradeApi.get().GradeItemList(mContext, classgistType, new ApiConfig.ApiRequestListener<CommonList<PointPenaltyInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<PointPenaltyInfo> data) {
                dismissLoadingDialog();
                if (data == null) {
                    return;
                }
                mListAdapter.add(data);
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    //清运打分
    private void cleanUpGrade(String commNo, String amenityAreaId, String arlist, String amenityImg) {
        GradeApi.get().cleanUpGrade(mContext, commNo, amenityAreaId, arlist, amenityImg, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(data);
                setResult(Activity.RESULT_OK);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back: {
                finish();
            }
            //选择小区
            case R.id.llCommunity: {
                finish();
                break;
            }
            //选择图片
            case R.id.rlAddPicture: {
                chooseImg();
                break;
            }
            //本地选择
            case R.id.btn_local_photo: {
                if (checkList(imglist)) {
                    Intent intent = new Intent(this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, 3);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, PhotoPickerActivity.MODE_MULTI);
                    if (imglist.size() > 0) {
                        if (imglist.get(imglist.size() - 1) == null) {
                            imglist.remove(imglist.size() - 1);
                            intent.putExtra(PhotoPickerActivity.SELECTED_PHOTOS, imglist);
                        }
                    } else {
                        intent.putExtra("max", 3 - imglist.size());
                    }
                    startActivityForResult(intent, TAKE_LOCAL_PHOTO);
                } else {
                    showCustomToast("最多只能选6张");
                }
                chooseDialog.cancel();
                break;
            }
            //拍照
            case R.id.btn_take_photos: {
                if (checkList(imglist)) {
                    if (PhotoUtil.isSdcardExisting()) {
                        Intent intent = new Intent(this,
                                CameraProtectActivity.class);
                        intent.putStringArrayListExtra("imgList", imglist);
                        startActivityForResult(intent, TAKE_PICTURE);
                    } else {
                        showCustomToast("请插入SD卡");
                    }
                } else {
                    showCustomToast("最多只能选6张");
                }

                chooseDialog.cancel();
                break;
            }
            //取消dialog
            case R.id.btn_cancel: {
                chooseDialog.dismiss();
                break;
            }
            //提交
            case R.id.btnSubmit: {
                submit();
                break;
            }
            default:
                break;
        }
    }
}
