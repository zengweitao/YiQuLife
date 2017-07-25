package com.quanmai.yiqu.ui.grade;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GradeApi;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.photopicker.utils.OtherUtils;
import com.quanmai.yiqu.common.util.DialogUtil;
import com.quanmai.yiqu.common.util.PhotoUtil;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.widget.CameraProtectActivity;
import com.quanmai.yiqu.ui.grade.adapter.GridViewPersonalGradeAdapter;

import java.util.ArrayList;

public class PersonalGradesActivity extends BaseActivity implements View.OnClickListener{

    RadioGroup radioGroup_personal_grade1, radioGroup_personal_grade2;
    Button buttonSubmit_personal_grade;
    TextView tv_title;

    LinearLayout linearLayoutPhoto_personal_grade;

    int mScore = 0;

    private final int TAKE_PHOTO = 201;  //选择照片
    private final int TAKE_PICTURE = 202;//拍照

    ArrayList<String> imglist;
    GridView gridView_personal_grade;
    private GridViewPersonalGradeAdapter mGridViewPersonalGradeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_grades);

        init();
        initRadioGroup(-1);
        gridViewEvent();
        mGridViewPersonalGradeAdapter.addCloseListener(new GridViewPersonalGradeAdapter.OnCloseClick() {
            @Override
            public void changView() {
                linearLayoutPhoto_personal_grade.setVisibility(View.VISIBLE);
                gridView_personal_grade.setVisibility(View.GONE);
            }

            @Override
            public void onClose(String path) {

            }

            @Override
            public void getPhotos() {
                Intent intent = new Intent(getApplication(), CameraProtectActivity.class);
                //imglist.clear();
                intent.putStringArrayListExtra("imgList", imglist);
                startActivityForResult(intent, TAKE_PICTURE);
            }

        });
    }

    /**
     * 星星初始化
     * @param temp
     */
    private void initRadioGroup(int temp) {
        for (int j = 0; j < radioGroup_personal_grade2.getChildCount(); j++) {
            final int tempI = j + 5;
            RadioButton radioButton = (RadioButton) radioGroup_personal_grade2.getChildAt(j);
            if (temp > 0 && temp >= 5) {
                if (tempI <= temp) {
                    radioButton.setChecked(true);
                }
            }
            setStar(radioButton, tempI, radioButton.isChecked());
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioGroup_personal_grade1.clearCheck();
                    radioGroup_personal_grade2.clearCheck();
                    mScore = tempI + 1;
                    initRadioGroup(tempI);
                }
            });
        }

        for (int i = 0; i < radioGroup_personal_grade1.getChildCount(); i++) {
            final int tempI = i;
            RadioButton radioButton = (RadioButton) radioGroup_personal_grade1.getChildAt(i);
            if (temp >= 0) {
                if (tempI <= temp) {
                    radioButton.setChecked(true);
                }
            }
            setStar(radioButton, tempI, radioButton.isChecked());
            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    radioGroup_personal_grade2.clearCheck();
                    radioGroup_personal_grade1.clearCheck();
                    mScore = tempI + 1;
                    initRadioGroup(tempI);
                }
            });
        }
    }

    /**
     * 初始化控件
     */
    private void init() {
        imglist = new ArrayList<String>();

        radioGroup_personal_grade1 = (RadioGroup) findViewById(R.id.radioGroup_personal_grade1);
        radioGroup_personal_grade2 = (RadioGroup) findViewById(R.id.radioGroup_personal_grade2);


        buttonSubmit_personal_grade = (Button) findViewById(R.id.buttonSubmit_personal_grade);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("个人评分");
        linearLayoutPhoto_personal_grade = (LinearLayout) findViewById(R.id.linearLayoutPhoto_personal_grade);

        gridView_personal_grade = (GridView) findViewById(R.id.gridView_personal_grade);
        addGridViewAdapter();

        linearLayoutPhoto_personal_grade.setOnClickListener(this);
        buttonSubmit_personal_grade.setOnClickListener(this);

    }

    public void gridViewEvent(){
        gridView_personal_grade.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                showCustomToast("点击了第 "+position+" 张图片");

                /*view.findViewById(R.id.imageview_personal_grade_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imglist.remove(position);
                        if (imglist.size()!=0){
                            gridView_personal_grade.setVisibility(View.VISIBLE);
                            notifyGridViewAdapter();
                            if (imglist.size()==3||imglist.size()>3){
                                relativeLayoutAddPicture_personal_grade.setVisibility(View.GONE);
                            }else{
                                relativeLayoutAddPicture_personal_grade.setVisibility(View.VISIBLE);
                            }
                        }else{
                            gridView_personal_grade.setVisibility(View.GONE);
                        }

                    }
                });*/
            }
        });
    }

    /**
     * 星星选择器
     * @param view
     * @param temp
     * @param isChecked
     */
    private void setStar(RadioButton view, int temp, boolean isChecked) {
        switch (temp) {
            case 0: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.one_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_one);
                }
                break;
            }

            case 1: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.two_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_two);
                }
                break;
            }

            case 2: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.three_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_three);
                }
                break;
            }

            case 3: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.four_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_four);
                }
                break;
            }
            case 4: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.five_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_five);
                }
                break;
            }
            case 5: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.six_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_six);
                }
                break;
            }
            case 6: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.seven_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_seven);
                }
                break;
            }
            case 7: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.eight_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_eight);
                }
                break;
            }
            case 8: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.nine_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_nine);
                }
                break;
            }
            case 9: {
                if (isChecked) {
                    view.setBackgroundResource(R.drawable.ten_start);
                } else {
                    view.setBackgroundResource(R.drawable.start_ten);
                }
                break;
            }
            default:
                break;
        }
    }

    /**
     * 点击事件处理
     * @param view
     */
    @Override
    public void onClick(View view) {
        //imglist.clear();
        if (PhotoUtil.isSdcardExisting()) {
            switch (view.getId()) {
                //添加图片
                case R.id.linearLayoutPhoto_personal_grade: {
                    Intent intent = new Intent(this, CameraProtectActivity.class);
                    //imglist.clear();
                    intent.putStringArrayListExtra("imgList", imglist);
                    startActivityForResult(intent, TAKE_PICTURE);
                    break;
                }
                //提交
                case R.id.buttonSubmit_personal_grade: {
                    if (mScore!=0){
                        if (imglist.size() != 0){
                            if (imglist.contains(null)){
                                imglist.remove(null);
                            }
                            submit();
                        }else {
                            showShortToast("请拍照后再“提交打分”！");
                        }
                    }else{
                        showShortToast("你还没有选择小星星哦！");
                    }

                    break;
                }
                default:
                    break;
            }
        } else {
            showCustomToast("请插入SD卡");
        }
    }

    /**
     * 提交评分
     */
    public void submit() {
        DialogUtil.showConfirmDialog(mContext, "是否确定当前打分为最终分值", new DialogUtil.OnDialogSelectId() {

            @Override
            public void onClick(View v) {
                showLoadingDialog("请稍候");
                new QiniuUtil(mContext, imglist, new QiniuUtil.OnQiniuUploadListener() {
                    @Override
                    public void success(String names) {

                        GradeApi.get().markPersonal(mContext, mScore + "", names, new ApiConfig.ApiRequestListener<String>() {

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
        });

    }

    /**
     * 添加适配器
     */
    public void addGridViewAdapter(){
        mGridViewPersonalGradeAdapter = new GridViewPersonalGradeAdapter(this,imglist);
        gridView_personal_grade.setAdapter(mGridViewPersonalGradeAdapter);
    }

    /**
     * 刷新适配器
     */
    public void notifyGridViewAdapter(){
        if (mGridViewPersonalGradeAdapter!=null){
            mGridViewPersonalGradeAdapter.notifyDataSetChanged();
        }
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            /*case TAKE_PHOTO: {
                if (resultCode == RESULT_OK) {
                    imglist.clear();
                    imglist.addAll(intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));
                    imageViewPicture_personal_grade.setVisibility(View.VISIBLE);
                    linearLayoutPhoto_personal_grade.setVisibility(View.GONE);
                    imageViewPicture_personal_grade.setImageBitmap(ImageCompressUtil.compressByQuality(ImageCompressUtil.compressBySize(imglist.get(0), 400, 400), 200));
                }
                break;
            }*/
            case TAKE_PICTURE: {
                if (resultCode == RESULT_OK) {
                    if (imglist.size()==0){
                        imglist.add(intent.getStringExtra(CameraProtectActivity.IMAGE_PATH));
                        imglist.add(null);
                    }else{
                        imglist.remove(imglist.size()-1);
                        imglist.add(imglist.size()-1,intent.getStringExtra(CameraProtectActivity.IMAGE_PATH));
                        if (imglist.size()<3){
                            imglist.add(null);
                        }
                    }
                    Log.e("  --图像集合","  ==  "+imglist+" == "+intent.getExtras().getInt("tab"));
                    if (imglist.size()!=0){
                        linearLayoutPhoto_personal_grade.setVisibility(View.GONE);
                        gridView_personal_grade.setVisibility(View.VISIBLE);
                        notifyGridViewAdapter();
                    }else{
                        linearLayoutPhoto_personal_grade.setVisibility(View.VISIBLE);
                        gridView_personal_grade.setVisibility(View.GONE);
                    }
                }
                break;
            }
            default:
                break;
        }
    }
}
