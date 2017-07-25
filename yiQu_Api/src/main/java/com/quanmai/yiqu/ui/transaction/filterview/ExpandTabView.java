package com.quanmai.yiqu.ui.transaction.filterview;

import java.util.ArrayList;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.SortInfo;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

/**
 * 菜单控件头部，封装了下拉动画，动态生成头部按钮个数
 * 
 * @author yueyueniao
 */

public class ExpandTabView extends FrameLayout implements OnClickListener {
	private ArrayList<TabView> mViews = new ArrayList<TabView>();
	private PopupWindow popupWindow;
	ViewRight mRight;
	ViewLeft mLeft;
	ViewMiddle mViewMiddle;
	int current = -1;

	public ExpandTabView(Context context) {
		super(context);
		init(context);
	}

	public ExpandTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	String id_1 = new String();
	String id_2 = new String();
	String id_3 = new String();
	private void init(Context context) {
		View.inflate(context, R.layout.view_sort, this);
		mViews.add((TabView) findViewById(R.id.v_1));
		mViews.add((TabView) findViewById(R.id.v_2));
		mViews.add((TabView) findViewById(R.id.v_3));
		mLeft = new ViewLeft(context, new OnSelectListener() {

			@Override
			public void onSelect(SortInfo info) {
				mViews.get(0).setTextandColor(info.sort_name);
				id_1 = info.sort_id;
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				if (mOnSelectListener != null) {
					mOnSelectListener.onSelect(id_1, id_2, id_3);
				}
			}
		});

		mViewMiddle = new ViewMiddle(context, new OnSelectListener() {

			@Override
			public void onSelect(SortInfo info) {
				mViews.get(1).setTextandColor(info.sort_name);
				id_2 = info.sort_id;
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				if (mOnSelectListener != null) {
					mOnSelectListener.onSelect(id_1, id_2, id_3);
				}
			}
		});

		mRight = new ViewRight(context, new OnSelectListener() {

			@Override
			public void onSelect(SortInfo info) {
				mViews.get(2).setTextandColor(info.sort_name);
				id_3 = info.sort_id;
				if (popupWindow != null) {
					popupWindow.dismiss();
				}
				if (mOnSelectListener != null) {
					mOnSelectListener.onSelect(id_1, id_2, id_3);
				}
			}
		});
		popupWindow = new PopupWindow(this, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, true);
		popupWindow.setAnimationStyle(R.style.PopupWindowAnimation);
		popupWindow.setBackgroundDrawable(new ColorDrawable());
		popupWindow.setFocusable(false);
		popupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				for (int i = 0; i < mViews.size(); i++) {
					mViews.get(i).setOpen(false);
				}

			}
		});

		initView1(mViews.get(0));
		initView2(mViews.get(1));
		initView3(mViews.get(2));
	}

	private void initView1(TabView tabView) {
		tabView.setText("全部地区");
		tabView.setOnClickListener(this);

	}

	private void initView2(TabView tabView) {
		tabView.setText("所有分类");
		tabView.setOnClickListener(this);
	}

	private void initView3(TabView tabView) {
		tabView.setText("排序");
		tabView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (current == v.getId() && popupWindow.isShowing()) {
			popupWindow.dismiss();
			return;
		}
		popupWindow.dismiss();
		current = v.getId();
		switch (v.getId()) {
		case R.id.v_1:
			mViews.get(0).setOpen(true);
			mViews.get(1).setOpen(false);
			mViews.get(2).setOpen(false);
			popupWindow.setContentView(mLeft);
			popupWindow.showAsDropDown(this);
			break;
		case R.id.v_2:
			mViews.get(0).setOpen(false);
			mViews.get(1).setOpen(true);
			mViews.get(2).setOpen(false);
			popupWindow.setContentView(mViewMiddle);
			popupWindow.showAsDropDown(this);
			break;

		case R.id.v_3:
			mViews.get(0).setOpen(false);
			mViews.get(1).setOpen(false);
			mViews.get(2).setOpen(true);
			popupWindow.setContentView(mRight);
			popupWindow.showAsDropDown(this);
			break;

		default:

			break;
		}
	}

	public interface OnSelectListener {
		public void onSelect(SortInfo info);
	}

	public interface OnSelectListener1 {
		public void onSelect(String id1, String id2, String id3);
	}

	OnSelectListener1 mOnSelectListener;

	public void setOnSelectListener(OnSelectListener1 listener) {
		mOnSelectListener = listener;

	}

	//
	// for (int i = 0; i < 3; i++) {
	// final RelativeLayout r = new RelativeLayout(mContext);
	// RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(
	// RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
	// r.addView(viewArray.get(i), rl);
	// mViewArray.add(r);
	// r.setTag(SMALL);
	// ToggleButton tButton = (ToggleButton) inflater.inflate(
	// R.layout.view_sort, this, false);
	// addView(tButton);
	// View line = new View(mContext);
	// line.setBackgroundColor(Color.parseColor("#dddddd"));
	// if (i < viewArray.size() - 1) {
	// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(2,
	// LinearLayout.LayoutParams.FILL_PARENT);
	// addView(line, lp);
	// }
	// mToggleButton.add(tButton);
	// tButton.setTag(i);
	// tButton.setText(mTextArray.get(i));
	// tButton.setTextSize(14f);
	// r.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View v) {
	// onPressBack();
	// }
	// });
	//
	// r.setBackgroundColor(mContext.getResources().getColor(
	// R.color.popup_main_background));
	// tButton.setOnClickListener(new OnClickListener() {
	// @Override
	// public void onClick(View view) {
	// ToggleButton tButton = (ToggleButton) view;
	//
	// if (selectedButton != null && selectedButton != tButton) {
	// selectedButton.setChecked(false);
	// }
	// selectedButton = tButton;
	// selectPosition = (Integer) selectedButton.getTag();
	// startAnimation();
	// if (mOnButtonClickListener != null && tButton.isChecked()) {
	// mOnButtonClickListener.onClick(selectPosition,
	// popupWindow.isShowing());
	// }
	// }
	// });
	// }

	// setOrientation(LinearLayout.HORIZONTAL);
}

// /**
// * 根据选择的位置设置tabitem显示的值
// */
// public void setTitle(String valueText, int position,ArrayList<View>
// mViewArray) {
// if (position < mToggleButton.size()) {
// mToggleButton.get(position).setText(valueText);
// mToggleButton.get(position).setTextColor(Color.parseColor("#ff8162"));
// }
// }
//
// public void setTitle(String title){
//
// }
// /**
// * 根据选择的位置获取tabitem显示的值
// */
// public String getTitle(int position) {
// if (position < mToggleButton.size() &&
// mToggleButton.get(position).getText() != null) {
// return mToggleButton.get(position).getText().toString();
// }
// return "";
// }
//
// /**
// * 设置tabitem的个数和初始值
// */
// public void setValue(ArrayList<String> textArray, ArrayList<View>
// viewArray) {
// if (mContext == null) {
// return;
// }
// LayoutInflater inflater = (LayoutInflater)
// mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
// mTextArray = textArray;
// for (int i = 0; i < viewArray.size(); i++) {
// final RelativeLayout r = new RelativeLayout(mContext);
// RelativeLayout.LayoutParams rl = new
// RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
// maxHeight);
// r.addView(viewArray.get(i), rl);
// mViewArray.add(r);
// r.setTag(SMALL);
// ToggleButton tButton = (ToggleButton)
// inflater.inflate(R.layout.toggle_button, this, false);
// addView(tButton);
// View line = new View(mContext);
// line.setBackgroundColor(Color.parseColor("#dddddd"));
// if (i < viewArray.size() - 1) {
// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(2,
// LinearLayout.LayoutParams.FILL_PARENT);
// addView(line, lp);
// }
// mToggleButton.add(tButton);
// tButton.setTag(i);
// tButton.setText(mTextArray.get(i));
// tButton.setTextSize(14f);
// r.setOnClickListener(new OnClickListener() {
// @Override
// public void onClick(View v) {
// onPressBack();
// }
// });
//
// r.setBackgroundColor(mContext.getResources().getColor(R.color.popup_main_background));
// tButton.setOnClickListener(new OnClickListener() {
// @Override
// public void onClick(View view) {
// ToggleButton tButton = (ToggleButton) view;
//
// if (selectedButton != null && selectedButton != tButton) {
// selectedButton.setChecked(false);
// }
// selectedButton = tButton;
// selectPosition = (Integer) selectedButton.getTag();
// startAnimation();
// if (mOnButtonClickListener != null && tButton.isChecked()) {
// mOnButtonClickListener.onClick(selectPosition,popupWindow.isShowing());
// }
// }
// });
// }
// }
//

//
// private void showPopup(int position) {
// View tView = mViewArray.get(selectPosition).getChildAt(0);
// if (tView instanceof ViewBaseAction) {
// ViewBaseAction f = (ViewBaseAction) tView;
// f.show();
// }
// if (popupWindow.getContentView() != mViewArray.get(position)) {
// popupWindow.setContentView(mViewArray.get(position));
// }
// popupWindow.showAsDropDown(this, 0, 1);
// }
//
// /**
// * 如果菜单成展开状态，则让菜单收回去
// */
// public boolean onPressBack() {
// if (popupWindow != null && popupWindow.isShowing()) {
// popupWindow.dismiss();
// hideView();
// if (selectedButton != null) {
// selectedButton.setChecked(false);
// }
// return true;
// } else {
// return false;
// }
//
// }
//
// private void hideView() {
// View tView = mViewArray.get(selectPosition).getChildAt(0);
// if (tView instanceof ViewBaseAction) {
// ViewBaseAction f = (ViewBaseAction) tView;
// f.hide();
// }
// }
//
//
//
// @Override
// public void onDismiss() {
// showPopup(selectPosition);
// popupWindow.setOnDismissListener(null);
// }
//
// private OnButtonClickListener mOnButtonClickListener;
//
// /**
// * 设置tabitem的点击监听事件
// */
// public void setOnButtonClickListener(OnButtonClickListener l) {
// mOnButtonClickListener = l;
// }
//
// /**
// * 自定义tabitem点击回调接口
// */
// public interface OnButtonClickListener {
// public void onClick(int selectPosition,boolean popIsShow);
// }

// }
