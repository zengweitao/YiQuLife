package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.BookingDetailInfo;
import com.quanmai.yiqu.api.vo.GarbageDetailsInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesInfo;
import com.quanmai.yiqu.api.vo.RecycleGarbagesMapInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.ShoppingCar.model.ShopCart;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.ui.booking.BookingSecondActivity;
import com.quanmai.yiqu.ui.selectpic.ImageFloder;
import com.quanmai.yiqu.ui.selectpic.ImageLoader;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by 95138 on 2016/4/19.
 */
public class StatisticsAdapter extends BaseAdapter {

    Map<RecycleGarbagesInfo, Double> mGarbageMap;   //key 回收的垃圾 value 回收的数量
    ShopCart mShopCart;
    CommonList<RecycleGarbagesInfo> mList;
    Context mContext;
    CountListener mCountListener;
    boolean isClick = false;
    BookingCountLeftAdapter mBookingCountLeftAdapter;

    String oldText = null;//保存上次符合正则表达式的内容

    public StatisticsAdapter(Context context, BookingCountLeftAdapter mAdapter, CountListener listener) {
        mContext = context;
        mCountListener = listener;
        mList = new CommonList<>();
        mGarbageMap = new HashMap<>();
        this.mBookingCountLeftAdapter = mAdapter;
    }

    public void addData(CommonList<RecycleGarbagesInfo> list) {
        if (list != null && list.size() > 0) {
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addRecord(CommonList<RecycleGarbagesInfo> mInfoList, ShopCart shopCart) {
        if (mInfoList != null && mInfoList.size() > 0) {
            mShopCart = shopCart;
            mGarbageMap.clear();
            mGarbageMap.putAll(mShopCart.getShoppingSingleMap());
            mList.clear();
            mList.addAll(mInfoList);
            notifyDataSetChanged();
        }
    }

    public void clear() {
        mList.clear();
        if (mList.size() > 0) {
            mList.clear();
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_count, null);
            holder.textViewTitle = (TextView) view.findViewById(R.id.textViewTitles);
//            holder.imageViewMinus = (ImageView)view.findViewById(R.id.imageViewMinus);
//            holder.imageViewAdd = (ImageView)view.findViewById(R.id.imageViewAdd);
            holder.editTextViewAmount = (EditText) view.findViewById(R.id.editTextViewAmount);
//            holder.textViewDescription = (TextView)view.findViewById(R.id.textViewDescription);
            holder.textViewPoints = (TextView) view.findViewById(R.id.textViewPoints);
            holder.imageView = (ImageView) view.findViewById(R.id.imageView);
            holder.imageViewDirty = (ImageView) view.findViewById(R.id.imageViewDirty);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final RecycleGarbagesInfo recycleGarbagesInfo = mList.get(position);
        ImageloaderUtil.displayImage(mContext, recycleGarbagesInfo.getPic(), holder.imageView);
        holder.textViewTitle.setText(recycleGarbagesInfo.getGarbage());
        if (recycleGarbagesInfo.getGarbage().equals("有害垃圾")) {
            holder.textViewPoints.setText(recycleGarbagesInfo.getPoint() + "益币/" + recycleGarbagesInfo.getUnit());
            holder.imageViewDirty.setVisibility(View.VISIBLE);
            holder.editTextViewAmount.setVisibility(View.GONE);
            if (mGarbageMap != null) {
                if (mGarbageMap.containsKey(recycleGarbagesInfo) && mGarbageMap.get(recycleGarbagesInfo) > 0) {
                    holder.imageViewDirty.setBackgroundResource(R.drawable.icon_select_amount_dirty_selected);
                    isClick = true;
                } else {
                    holder.imageViewDirty.setBackgroundResource(R.drawable.icon_select_amount_dirty_unselcted);
                    isClick = false;
                }
            }
            holder.imageViewDirty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isClick = !isClick;
                    int goodsCount;
                    if (isClick) {
                        goodsCount = 1;
                        mList.get(position).setCount(goodsCount);
                        mBookingCountLeftAdapter.notifyDataSetChanged();
                        holder.imageViewDirty.setBackgroundResource(R.drawable.icon_select_amount_dirty_selected);
                        mCountListener.countChange("add", recycleGarbagesInfo,
                                Integer.parseInt(recycleGarbagesInfo.point), recycleGarbagesInfo.unit, 1, position);
                    } else {
                        goodsCount = 0;
                        mList.get(position).setCount(goodsCount);
                        mBookingCountLeftAdapter.notifyDataSetChanged();
                        holder.imageViewDirty.setBackgroundResource(R.drawable.icon_select_amount_dirty_unselcted);
                        mCountListener.countChange("min", recycleGarbagesInfo,
                                Integer.parseInt(recycleGarbagesInfo.point), recycleGarbagesInfo.unit, 0, position);
                    }

                }
            });

        } else {
            holder.imageViewDirty.setVisibility(View.GONE);
            holder.editTextViewAmount.setVisibility(View.VISIBLE);
            holder.textViewPoints.setText(recycleGarbagesInfo.getPoint() + "益币/" + recycleGarbagesInfo.getUnit());
            Log.e("--单位", "==" + recycleGarbagesInfo.getUnit());
            if (mGarbageMap != null && mGarbageMap.size() > 0) {
                if (recycleGarbagesInfo.getUnit().contains("斤") || recycleGarbagesInfo.getUnit().contains("克")) {
                    if (mGarbageMap.containsKey(recycleGarbagesInfo) && mGarbageMap.get(recycleGarbagesInfo) > 0) {
                        if (mGarbageMap.get(recycleGarbagesInfo).toString().endsWith(".0")){
                            holder.editTextViewAmount.setText(Math.round(mGarbageMap.get(recycleGarbagesInfo)) + "");
                        }else {
                            holder.editTextViewAmount.setText(mGarbageMap.get(recycleGarbagesInfo) + "");
                        }
                    }
                } else {
                    if (mGarbageMap.containsKey(recycleGarbagesInfo) && mGarbageMap.get(recycleGarbagesInfo) > 0) {
                        holder.editTextViewAmount.setText(Math.round(mGarbageMap.get(recycleGarbagesInfo)) + "");
                    }
                }
            } else {
                holder.editTextViewAmount.setText("");
            }
            holder.editTextViewAmount.addTextChangedListener(new SearchWather(holder.editTextViewAmount,recycleGarbagesInfo, position));
        }

        return view;
    }

    public class ViewHolder {
        TextView textViewTitle;
        EditText editTextViewAmount;
        TextView textViewPoints;
        ImageView imageView;
        ImageView imageViewDirty;
    }

    public interface CountListener {
        void countChange(String addOrminus, RecycleGarbagesInfo recycleGarbagesInfo, int count, String unit, double goodsCount, int postion);
    }

    class SearchWather implements TextWatcher {

        //监听改变的文本框
        private EditText editText;
        double editCount = 0;
        int postion;
        RecycleGarbagesInfo recycleGarbagesInfo;

        /**
         * 构造函数
         */
        public SearchWather(EditText editText,RecycleGarbagesInfo recycleGarbagesInfo, int postion) {
            this.editText = editText;
            this.postion = postion;
            this.recycleGarbagesInfo = recycleGarbagesInfo;
        }

        @Override
        public void onTextChanged(CharSequence ss, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            String editable = editText.getText().toString();
            String str;
            if (mList.get(postion).getUnit().contains("斤") || mList.get(postion).getUnit().contains("克")) {
                str = stringFilter(editable);
            } else {
                str = stringFilter2(editable);
            }
            if (!editable.equals(str)) {
                editText.setText(str);
                //设置新的光标所在位置
                editText.setSelection(str.length());
            }
            if (!str.toString().equals("") && !str.toString().equals("null") && !str.toString().equals(null) && str.toString() != "") {
                editCount = Double.parseDouble(str.toString());
                Log.e("--float数量", "== " + editCount);
            } else {
                editCount = 0;
            }
            int goodsCount;
            if (editable.length() > 0) {
                goodsCount = 1;
                mList.get(postion).setCount(goodsCount);
                mBookingCountLeftAdapter.notifyDataSetChanged();
            } else {
                goodsCount = 0;
                mList.get(postion).setCount(goodsCount);
                mBookingCountLeftAdapter.notifyDataSetChanged();
            }
            mCountListener.countChange("writ", recycleGarbagesInfo,
                    Integer.parseInt(recycleGarbagesInfo.point), recycleGarbagesInfo.unit, editCount, postion);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

    }


    public String stringFilter(String str) throws PatternSyntaxException {
        if (str.length() > 0) {
            String regEx = "(^([0-9]{1})$)|((^([1-9]{1})([0-9]{1})$))|" +
                    "(^([0-9]{2})([.])([1-9]{1})$)|" +
                    "(^([0-9]{1})([.])([1-9]{1})$)|(^([0-9]{1})([.])$)|" +
                    "(^([0-9]{2})([.])$)";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            if (m.matches()) {
                oldText = str;
                return str;
            } else {
                return oldText;
            }
        }
        return "";
    }

    public String stringFilter2(String str) throws PatternSyntaxException {
        if (str.length() > 0) {
            String regEx = "(^([1-9]{1})$)|((^([1-9]{1})([0-9]{1})$))";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            if (m.matches()) {
                oldText = str;
                return str;
            } else {
                return oldText;
            }
        }
        return "";
    }

}

