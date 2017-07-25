package com.quanmai.yiqu.ui.publish;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.GoodsApi;
import com.quanmai.yiqu.api.vo.GoodsInfo;
import com.quanmai.yiqu.api.vo.UserInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.common.photopicker.PhotoPickerActivity;
import com.quanmai.yiqu.common.util.PhotoUtil;
import com.quanmai.yiqu.common.util.QiniuUtil;
import com.quanmai.yiqu.common.widget.CameraProtectActivity;
import com.quanmai.yiqu.common.widget.MyGridView;
import com.quanmai.yiqu.ui.mys.publishmanage.PublishManageActivity;

import java.util.ArrayList;
import java.util.List;

public class EditUnusedActivity extends BaseActivity implements View.OnClickListener {

    TextView tv_title,tv_right;
    RelativeLayout relativeLayoutAddPicture;
    MyGridView mGridView;
    EditText eidtTextTitle;
    EditText et_description;
    RelativeLayout rt_class,rt_degree;
    TextView textViewClass,textViewDegree;
    TextView tv_yikouprice,tv_donate;
    EditText eidtTextPrice,eidtTextPhone;

    Dialog chooseDialog;
    String goods_id="",class_id="",class_name="";
    int degree=-1,isDonate=0;
    GoodsInfo info;
    ArrayList<String> imglist;
    String picPath = "";
    int limit_imgsize = 6;//最大显示图片数
    EditGridViewAdapter adapter;
    private final int TAKE_PHOTO = 201;  //选择照片
    private final int TAKE_PHOTO_AGAIN = 202;  //增加照片
    private final int GET_CLASS = 203;  //分类
    private final int GET_DEGREE = 204; //成色
    private final int TAKE_PICTURE = 205;//拍照
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_unused);

        init();
        getData();

        eidtTextPrice.addTextChangedListener(new TextWatcher() {
            private boolean isChanged = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        eidtTextPrice.setText(s);
                        eidtTextPrice.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    eidtTextPrice.setText(s);
                    eidtTextPrice.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        eidtTextPrice.setText(s.subSequence(0, 1));
                        eidtTextPrice.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });

        if (savedInstanceState != null){
            if (imglist!=null){
                imglist.clear();
                if (savedInstanceState.getStringArrayList("list").size()>0){
                    imglist.addAll(savedInstanceState.getStringArrayList("list"));

                    if (adapter!=null){
                        adapter.add(imglist);
                        mGridView.setAdapter(adapter);
                        relativeLayoutAddPicture.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }

    }

    private void init() {
        tv_title = (TextView)findViewById(R.id.tv_title);
        tv_right = (TextView)findViewById(R.id.tv_right);
        relativeLayoutAddPicture = (RelativeLayout)findViewById(R.id.relativeLayoutAddPicture);
        mGridView = (MyGridView)findViewById(R.id.gridview);
        eidtTextTitle = (EditText)findViewById(R.id.eidtTextTitle);
        et_description = (EditText)findViewById(R.id.et_description);
        rt_class = (RelativeLayout)findViewById(R.id.rt_class);
        rt_degree = (RelativeLayout)findViewById(R.id.rt_degree);
        textViewClass = (TextView)findViewById(R.id.textViewClass);
        textViewDegree = (TextView)findViewById(R.id.textViewDegree);
        tv_yikouprice = (TextView)findViewById(R.id.tv_yikouprice);
        tv_donate = (TextView)findViewById(R.id.tv_donate);
        eidtTextPrice = (EditText)findViewById(R.id.eidtTextPrice);
        eidtTextPhone = (EditText)findViewById(R.id.eidtTextPhone);

        tv_title.setText("闲置修改");
        tv_right.setText("确认发布");

        tv_right.setOnClickListener(this);
        rt_class.setOnClickListener(this);
        rt_degree.setOnClickListener(this);
        tv_yikouprice.setOnClickListener(this);
        tv_donate.setOnClickListener(this);
        relativeLayoutAddPicture.setOnClickListener(this);
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

    private void getData() {
        showLoadingDialog("请稍候");
        if(getIntent().hasExtra("goods_id")){
            goods_id = getIntent().getStringExtra("goods_id");
        }
        GoodsApi.get().GoodsDetail(this, goods_id, new ApiConfig.ApiRequestListener<GoodsInfo>() {

            @Override
            public void onSuccess(String msg, GoodsInfo data) {
                dismissLoadingDialog();
                info = data;
                class_id = data.class_id;
                class_name = data.class_name;
                degree = data.degree;
                eidtTextTitle.setText(data.name);
                et_description.setText(data.description);
                isDonate = data.type;
                if (isDonate == 0) {
                    tv_yikouprice.setBackgroundResource(R.drawable.bg_green_left_corner);
                    tv_donate.setBackground(null);
                    eidtTextPrice.setText(data.price + "");
                    eidtTextPrice.setEnabled(true);
                } else {
                    tv_donate
                            .setBackgroundResource(R.drawable.bg_green_rigth_corner);
                    tv_yikouprice.setBackground(null);
                    eidtTextPrice.setText("0.00");
                    eidtTextPrice.setEnabled(false);
                }
                textViewClass.setText(data.class_name);
                if (degree == 10) {
                    textViewDegree.setText("全新");
                } else {
                    textViewDegree.setText(degree + "成新");
                }

                eidtTextPhone.setText(data.phone);

                if (data.img.size() > 0) {
                    relativeLayoutAddPicture.setVisibility(View.GONE);
                    mGridView.setVisibility(View.VISIBLE);
                }

                imglist = new ArrayList<String>();
                for (int i = 0; i < data.img.size(); i++) {
                    imglist.add(UserInfo.get().prefixQiNiu + "" + data.img.get(i));
                }
                if (imglist.size() < limit_imgsize)
                    imglist.add(null);


                adapter = new EditGridViewAdapter(EditUnusedActivity.this, imglist);
                mGridView.setAdapter(adapter);

                adapter.addCloseListener(new EditGridViewAdapter.OnCloseClick() {
                    @Override
                    public void changView() {
                        relativeLayoutAddPicture.setVisibility(View.VISIBLE);
                        mGridView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onClose(String path) {
                        if (imglist!=null){
                            if (imglist.contains(path)){
                                imglist.remove(path);
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                // TODO Auto-generated method stub
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case TAKE_PHOTO:{
//                    if (imglist.size()>0){
//                        if(imglist.get(imglist.size()-1)==null){
//                            imglist.remove(imglist.size() - 1);
//                        }
//                    }
                    if (intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT).size()>0){
                        relativeLayoutAddPicture.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                    }
                    imglist.clear();
                    imglist.addAll(intent.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT));

                    if (imglist.size() < limit_imgsize){
                        imglist.add(null);
                    } // 添加满了不显示加号
                    adapter.add(imglist);
                    mGridView.setAdapter(adapter);
                    break;
                }
                case TAKE_PHOTO_AGAIN:{
                    imglist.remove(imglist.size() - 1);
                    imglist.addAll(intent.getStringArrayListExtra("list"));
                    if (imglist.size() < limit_imgsize) // 添加满了不显示加号
                        imglist.add(null);
                    adapter.add(imglist);
                    mGridView.setAdapter(adapter);
                    break;
                }

                case TAKE_PICTURE:{
                    picPath = intent.getStringExtra(CameraProtectActivity.IMAGE_PATH);
                    if (intent.getStringArrayListExtra("imgList")!=null){
                        imglist.clear();
                        imglist.addAll(intent.getStringArrayListExtra("imgList"));
                    }
                    if (picPath!=""&&picPath!=null){
                        relativeLayoutAddPicture.setVisibility(View.GONE);
                        mGridView.setVisibility(View.VISIBLE);
                    }
                    if (imglist.size()>0){
                        if(imglist.get(imglist.size()-1)==null){
                            imglist.remove(imglist.size() - 1);
                        }
                    }
                    imglist.add(picPath);
                    if (imglist.size() < limit_imgsize){
                        imglist.add(null);// 添加满了不显示加号
                    }

                    adapter.add(imglist);
                    mGridView.setAdapter(adapter);
                    break;
                }

                case GET_CLASS:{
                    class_id = intent.getStringExtra("class_id");
                    class_name = intent.getStringExtra("class_name");
                    textViewClass.setText(class_name);
                    break;
                }
                case GET_DEGREE:{
                    degree = intent.getIntExtra("degree", 1);
                    if (degree == 10)
                        textViewDegree.setText("全新");
                    else
                        textViewDegree.setText(degree + "成新");
                    break;
                }
                default:break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent();
        switch (view.getId()){
            //发布
            case R.id.tv_right:{
                release();
                break;
            }
            //添加图片
            case R.id.relativeLayoutAddPicture:{
//                intent = new Intent(this, SelectPicActivity.class);
//                intent.putExtra("type", 1);
//                intent.putExtra("max", 6);
//                startActivityForResult(intent,TAKE_PHOTO);
                if (checkList(imglist)){
                    chooseHeadImg();
                }else {
                    showCustomToast("最多只能选6张");
                }
                break;
            }
            //分类
            case R.id.rt_class:{
                intent.setClass(this, TypeActivity.class);
                startActivityForResult(intent, GET_CLASS);
                break;
            }
            //一口价
            case R.id.tv_yikouprice:{
                if (isDonate == 1) {
                    isDonate = 0;
                    tv_yikouprice
                            .setBackgroundResource(R.drawable.bg_green_left_corner);
                    tv_donate.setBackground(null);
                    eidtTextPrice.setText("");
                    eidtTextPrice.setEnabled(true);
                }
                break;
            }
            //捐赠
            case R.id.tv_donate:{
                if (isDonate == 0) {
                    isDonate = 1;
                    tv_donate
                            .setBackgroundResource(R.drawable.bg_green_rigth_corner);
                    tv_yikouprice.setBackground(null);
                    eidtTextPrice.setText("0.00");
                    eidtTextPrice.setEnabled(false);
                }
                break;
            }
            //成色
            case R.id.rt_degree:{
                intent.setClass(this, DegreeChoiceActivity.class);
                startActivityForResult(intent, GET_DEGREE);
                break;
            }

            //从相册挑选
            case R.id.btn_local_photo:{
                if (checkList(imglist)){
                    intent = new Intent(this, PhotoPickerActivity.class);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, false);
                    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN,6);
                    intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE,PhotoPickerActivity.MODE_MULTI);
                    if (imglist.size()>0){
                        if(imglist.get(imglist.size()-1)==null){
                            intent.putExtra("max", 6);
                            imglist.remove(imglist.size()-1);
                            intent.putExtra(PhotoPickerActivity.SELECTED_PHOTOS,imglist);
                        }
                    }else {
                        intent.putExtra("max", limit_imgsize-imglist.size());
                    }
                    startActivityForResult(intent, TAKE_PHOTO);
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
//                        intent = new Intent();
//                        intent.setAction("android.media.action.IMAGE_CAPTURE");
//                        picPath = Environment.getExternalStorageDirectory()
//                                .getAbsolutePath()+"/"+System.currentTimeMillis() + ".jpg";
//                        File imageFile = new File(picPath);//通过路径创建保存文件
//                        Uri imageFileUri = Uri.fromFile(imageFile);
//                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
//                        startActivityForResult(intent, TAKE_PICTURE);
                        intent = new Intent(EditUnusedActivity.this,
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
            //取消
            case R.id.btn_cancel:{
                chooseDialog.cancel();
                break;
            }

            default:
                break;
        }
    }

    private void release() {

        if (imglist.size() == 0) {
            showShortToast("至少添加一张图片");
            return;
        }

        if (imglist.get(imglist.size() - 1) == null)
            imglist.remove(imglist.size() - 1);

        final String title = eidtTextTitle.getText().toString().trim();
        final String description = et_description.getText().toString().trim();
        final String price = eidtTextPrice.getText().toString().trim();
        final String phone = eidtTextPhone.getText().toString().trim();
        if ("".equals(title) || title == null) {
            showShortToast("标题不能为空");
            return;
        }

        if (title.length()<6){
            showCustomToast("标题不可少于6个字");
            return;
        }

        if ("".equals(description) || description == null) {
            showShortToast("描述不能为空");
            return;
        }

        if (Double.parseDouble(price)>10000){
            showShortToast("价格不能大于10000");
            return;
        }

        if (imglist.size() == 0) {
            showShortToast("至少添加一张图片");
            return;
        }
        if ( "".equals(price)) {
            showShortToast("价格不能为空");
            return;
        }
        if ("".equals(class_name) || class_name == null) {
            showShortToast("分类不能为空");
            return;
        }
        if (degree == -1) {
            showShortToast("成色不能为空");
            return;
        }
        if ("".equals(phone) || phone == null) {
            showShortToast("手机号码不能为空");
            return;
        }
        if (!Utils.isTelNumber(phone)){
            showShortToast("请输入正确的手机号");
            return;
        }
        showLoadingDialog("请稍候");
        new QiniuUtil(mContext, imglist, new QiniuUtil.OnQiniuUploadListener() {

            @Override
            public void success(String names) {
                GoodsApi.get().GoodsEdit(mContext, goods_id, title, description, names, isDonate + "", price, class_id, class_name, degree + "", "", phone, new ApiConfig.ApiRequestListener<String>() {

                    @Override
                    public void onSuccess(String msg, String data) {
                        showShortToast(msg);
                        LocalBroadcastManager.getInstance(mContext).
                                sendBroadcast(new Intent(PublishManageActivity.ACTION_REFRESH_DATA));
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(String msg) {
                        showShortToast(msg);

                    }

                });
            }

            @Override
            public void failure() {
                showShortToast("图片上传失败");
            }
        }).upload();

    }


    public void chooseHeadImg() {
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

    public boolean checkList(List<String> list){
        boolean checkList = true;

        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (i==limit_imgsize-1&&list.get(i)!=null){
                    checkList = false;
                }
            }
        }
        return checkList;
    }

}
