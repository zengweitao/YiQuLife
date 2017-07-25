package com.quanmai.yiqu.ui.selectpic;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.fix.FixActivity;
import com.quanmai.yiqu.ui.selectpic.ImageLoader.Type;
import com.quanmai.yiqu.ui.selectpic.ListImageDirPopupWindow.OnImageDirSelected;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SelectPicActivity extends BaseActivity implements OnClickListener, OnImageDirSelected {
    ImageFloder oldfloder;
    String picPath;
    private ProgressDialog mProgressDialog;
    private Context mContext = this;
    private SelectPicActivity c = SelectPicActivity.this;
    int selectedCount = 0;
    /**
     * 存储选中的view，点击gridview取消选中时用于移除view
     */
    Map<String, View> viewMap;

    LinearLayout ll_sure;
    //HorizontalScrollView hs_imgs;
    LinearLayout ll_imgs;
    /**
     * 存储文件夹中的图片数量
     */
    private int mPicsSize;
    /**
     * 图片数量最多的文件夹
     */
    private File mImgDir;
    /**
     * 所有的图片
     */
    private List<String> mImgs2;
    /**
     * 选中的图片
     */
    List<String> selectedList;
    private MyGridView gridview2;
    //MyGallery gallery;
    private GridAdapter mAdapter2;
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashSet<String> mDirPaths = new HashSet<String>();

    /**
     * 扫描拿到所有的图片文件夹
     */
    private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

    private RelativeLayout mBottomLy;

    private TextView mChooseDir, tv_count;
    private TextView mImageCount;
    int totalCount = 0;
    /**
     * 0、1发布商品，2是维修
     */
    public int type;
    public int mScreenHeight, max;

    private ListImageDirPopupWindow mListImageDirPopupWindow;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            // 为View绑定数据
            data2View();
            // 初始化展示文件夹的popupWindw
            initListDirPopupWindw();
//			mProgressDialog.dismiss();
            dismissLoadingDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectpic);
        type = getIntent().getIntExtra("type", 0);
        max = getIntent().getIntExtra("max", 6);

        selectedList = new ArrayList<String>();
//		mImgs1 = new ArrayList<String>();
        mImgs2 = new ArrayList<String>();
        viewMap = new HashMap<String, View>();

        if (getIntent().getStringArrayListExtra("chooseList") != null) {
            selectedList.addAll(getIntent().getStringArrayListExtra("chooseList"));
        }

        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        mScreenHeight = outMetrics.heightPixels;

        initView();
        getImages();

    }

    /**
     * 初始化View
     */
    private void initView() {
        //hs_imgs = (HorizontalScrollView) findViewById(R.id.hs_imgs);
        ll_imgs = (LinearLayout) findViewById(R.id.ll_imgs);

//		gridview1 = (MyGridView) findViewById(R.id.gridview1);
        gridview2 = (MyGridView) findViewById(R.id.gridview2);
        //gallery = (MyGallery) findViewById(R.id.gallery);
        mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
        mImageCount = (TextView) findViewById(R.id.id_total_count);
        tv_count = (TextView) findViewById(R.id.tv_count);
        mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
        mBottomLy.setOnClickListener(this);
        ll_sure = (LinearLayout) findViewById(R.id.ll_sure);
        ll_sure.setOnClickListener(this);
        findViewById(R.id.iv_camera).setOnClickListener(this);

        setCount();
    }

    /**
     * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
     */
    private void getImages() {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Utils.showCustomToast(this, "暂无外部存储");
            return;
        }
        // 显示进度条
//		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
        showLoadingDialog("请稍候");

        new Thread(new Runnable() {
            @Override
            public void run() {

                String firstImage = null;
                Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver = SelectPicActivity.this
                        .getContentResolver();

                // 只查询jpeg和png的图片
                Cursor mCursor = mContentResolver.query(mImageUri, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                Utils.E(mCursor.getCount() + "");
                while (mCursor.moveToNext()) {
                    // 获取图片的路径
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Images.Media.DATA));

                    Utils.E(path);
                    // 拿到第一张图片的路径
                    if (firstImage == null)
                        firstImage = path;
                    // 获取该图片的父路径名
                    File parentFile = new File(path).getParentFile();
                    if (parentFile == null)
                        continue;
                    String dirPath = parentFile.getAbsolutePath();
                    ImageFloder imageFloder = null;
                    // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
                    if (mDirPaths.contains(dirPath)) {
                        continue;
                    } else {
                        mDirPaths.add(dirPath);
                        // 初始化imageFloder
                        imageFloder = new ImageFloder();
                        imageFloder.setDir(dirPath);
                        imageFloder.setFirstImagePath(path);
                    }

                    int picSize = parentFile.list(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String filename) {
                            if (filename.endsWith(".jpg")
                                    || filename.endsWith(".png")
                                    || filename.endsWith(".jpeg"))
                                return true;
                            return false;
                        }
                    }).length;
                    totalCount += picSize;

                    imageFloder.setCount(picSize);
                    mImageFloders.add(imageFloder);
                    //初始化时显示的文件夹
                    if (picSize > mPicsSize && parentFile.getAbsolutePath().contains("DCIM")) {
                        mPicsSize = picSize;
                        mImgDir = parentFile;
                    }
                }
                mCursor.close();

                // 扫描完成，辅助的HashSet也就可以释放内存了
                mDirPaths = null;

                // 通知Handler扫描图片完成
                mHandler.sendEmptyMessage(0x110);

            }
        }).start();
    }

    /**
     * 为View绑定数据
     */
    private void data2View() {
        if (mImgDir == null && (mImgDir.list() == null || mImgDir.length() == 0)) {
            return;
        }
        String[] arr = mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg")
                        || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        });
        for (int i = 0; i < arr.length; i++) {

            mImgs2.add(arr[i]);

        }
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
//		mAdapter1 = new GridAdapter(mContext, c, mImgs1,
//				R.layout.item_picgrid, mImgDir.getAbsolutePath(), selectedList);
//		gridview1.setAdapter(mAdapter1);
        mAdapter2 = new GridAdapter(mContext, c, mImgs2,
                R.layout.item_picgrid, mImgDir.getAbsolutePath(), selectedList);
        gridview2.setAdapter(mAdapter2);

        if (selectedList != null && selectedList.size() > 0) {
            addView(selectedList);
        }

        //selectedAdapter = new SelectedAdapter(c, selectedList, mImgDir.getAbsolutePath());
        //gallery.setAdapter(selectedAdapter);
        mImageCount.setText(totalCount + "张");
    }

    ;

    /**
     * 初始化展示文件夹的popupWindw
     */
    private void initListDirPopupWindw() {
        mListImageDirPopupWindow = new ListImageDirPopupWindow(
                LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
                mImageFloders, LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.list_dir, null));

        mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().setAttributes(lp);
            }
        });
        // 设置选择文件夹的回调
        mListImageDirPopupWindow.setOnImageDirSelected(this);
    }


    /**
     * 设置选中数量，以底部选中数量为准
     */
    public void setCount() {
        if (selectedList.size() <= max)
            tv_count.setText(selectedList.size() + "/" + max); //单选ll_imgs.getChildCount()
        else
            Utils.showCustomToast(this, "只能设置" + max + "张图片");
    }

    ImageLoader iImageLoader = ImageLoader.getInstance(3, Type.LIFO);

    public void addView(List<String> data) {
        if (data != null && data.size() > 0) {
            if (data.size() < 10) {
                for (int i = 0; i < data.size(); i++) {
                    View view = View.inflate(getApplicationContext(),
                            R.layout.item_selected, null);
                    ImageView img = (ImageView) view.findViewById(R.id.iv_selectedimage);
                    ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
                    iv_close.setTag(data.get(i));
                    iv_close.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeView((String) v.getTag());
                        }
                    });
                    if (data.get(i).startsWith("http")) {
                        Glide.with(c).load(data.get(i)).override(100, 100).into(img);
                    } else {
                        iImageLoader.loadImage(data.get(i), img);
                    }
                    ll_imgs.addView(view);
                    viewMap.put(data.get(i), view);
                }
                setCount();
            } else {
                Utils.showCustomToast(this, "只能设置" + max + "张图片");
            }
        }
    }


    /**
     * LinearLayout添加view，加入map，设置数量；点击添加、拍照添加
     */
    public void addView(String path) {
//		Utils.showCustomToast(this,path);
        if (selectedList.size() < 10) {
            View view = View.inflate(getApplicationContext(),
                    R.layout.item_selected, null);
            ImageView img = (ImageView) view.findViewById(R.id.iv_selectedimage);
            ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
            iv_close.setTag(path);
            iv_close.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ll_imgs.removeView((View) v.getParent());
                    removeView((String) v.getTag());
                }
            });
            iImageLoader.loadImage(path, img);
            //img.setImageBitmap(iImageLoader.decodeSampledBitmapFromResource(path,100,100));//加载比上一句慢
            ll_imgs.addView(view);
            viewMap.put(path, view);
            selectedList.add(path);
//			mAdapter1.notifyDataSetChanged();
            mAdapter2.notifyDataSetChanged();
            setCount();
        } else
            Utils.showCustomToast(this, "只能设置" + max + "张图片");
//		System.out.println(selectedList.size()+" "+max);
    }

    /**
     * LinearLayout移除view，selectedlist移除，刷新adapter，设置数量；grid中取消，点插插取消
     */
    public void removeView(String path) {
        for (int i = 0; i < selectedList.size(); i++) {
            if (selectedList.get(i).equals(path)) {
                selectedList.remove(i);
                break;
            }
        }
//		mAdapter1.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();
        ll_imgs.removeView(viewMap.get(path));
        setCount();
    }

    /**
     * 切换目录移除所有view
     */
    public void removeAllView() {
        //selectedList.clear();//单选加上
        setCount();
//		mImgs1.clear();
        mImgs2.clear();
//		mAdapter1.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();
        //ll_imgs.removeAllViews();//单选加上
    }

    @Override
    public void selected(ImageFloder floder) {
        if (floder == oldfloder) { //同一目录不清除
            mListImageDirPopupWindow.dismiss();
            return;
        }
        oldfloder = floder;
        removeAllView();
        mImgDir = new File(floder.getDir());
        if (mImgDir == null && mImgDir.list().length == 0) {
            return;
        }
        String[] arr = mImgDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                if (filename.endsWith(".jpg") || filename.endsWith(".png")
                        || filename.endsWith(".jpeg"))
                    return true;
                return false;
            }
        });

//		if(arr.length <= 2){
//			for (int i = 0; i < arr.length; i++) {
//				mImgs1.add(arr[i]);
//			}
//		}else{
//			for (int i = 0; i < 2; i++) {
//				mImgs1.add(arr[i]);
//			}
        for (int i = 0; i < arr.length; i++) {
            mImgs2.add(arr[i]);
        }
        mAdapter2 = new GridAdapter(mContext, c, mImgs2,
                R.layout.item_picgrid, mImgDir.getAbsolutePath(), selectedList);
        gridview2.setAdapter(mAdapter2);
//		}
        /**
         * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
         */
//		mAdapter1 = new GridAdapter(mContext, c, mImgs1,
//				R.layout.item_picgrid, mImgDir.getAbsolutePath(), selectedList);
//		gridview1.setAdapter(mAdapter1);
        mImageCount.setText(floder.getCount() + "张");
        //mChooseDir.setText(floder.getName());
        mListImageDirPopupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.id_bottom_ly:
                mListImageDirPopupWindow.setAnimationStyle(R.style.anim_popup_dir);
                //mListImageDirPopupWindow.showAsDropDown(ll_sure, 0, 0);
                mListImageDirPopupWindow.showAtLocation(ll_sure, Gravity.BOTTOM, 0, 0);
                // 设置背景颜色变暗
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = .3f;
                getWindow().setAttributes(lp);
                break;
            case R.id.ll_sure:
                if (selectedList.size() == 0) {
                    showShortToast("请至少选择一张图片");
                    return;
                }
                if (type == 0) {
//                    intent.setClass(this, PublishActivity.class);
//                    intent.putStringArrayListExtra("list", (ArrayList<String>) selectedList);
//                    startActivity(intent);
//                    finish();
//                } else if (type == 1) {
//                    intent.setClass(this, PublishActivity.class);
//                    intent.putStringArrayListExtra("list", (ArrayList<String>) selectedList);
//                    setResult(RESULT_OK, intent);
//                    finish();
//                } else {
                    intent.setClass(this, FixActivity.class);
                    intent.putStringArrayListExtra("list", (ArrayList<String>) selectedList);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
            case R.id.iv_camera:
                if (selectedList.size() < 8) {
                    String sdStatus = Environment.getExternalStorageState();
                    if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(c, "SD卡不可用！", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                    intent.setAction("android.media.action.IMAGE_CAPTURE");
                    picPath = Environment.getExternalStorageDirectory() + "/"
                            + System.currentTimeMillis() + ".jpg";
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(picPath)));
                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                    startActivityForResult(intent, 0);
                } else
                    Toast.makeText(this, "只能设置" + max + "张图片", 0).show();
                break;
            default:
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
            return;
        if (requestCode == 0) {
            if (picPath != null) {
                addView(picPath);
            }
        }

    }
}
