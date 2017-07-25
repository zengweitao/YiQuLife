package com.quanmai.yiqu.ui.fragment.selectaddress;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.Api;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.UserApi;
import com.quanmai.yiqu.api.vo.CommCodeInfo;
import com.quanmai.yiqu.api.vo.CommunityAddressInfo;
import com.quanmai.yiqu.common.Utils;
import com.quanmai.yiqu.ui.fragment.UnusedFragment;
import com.quanmai.yiqu.ui.fragment.UnusedFragment.OnSelectListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yin on 16/3/9.
 */
public class ViewAddressSelect extends LinearLayout {

    Context mContext;
    ListView mListView1;
    ListView mListView2;
    AddressSortAdapter adapter1;
    AddressSortRightAdapter adapter2;
    OnSelectListener listener;
    List<CommunityAddressInfo> parentList;
    Map<String ,List<CommunityAddressInfo>> map ;
    String mCity = "";
    String mCode;

    public ViewAddressSelect(Context context,OnSelectListener onSelectListener) {
        super(context);
        init(context);
        listener = onSelectListener;
    }

    public ViewAddressSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ViewAddressSelect(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        mContext = context;
        View.inflate(context, R.layout.select_address, this);
        parentList = new ArrayList<>();
        map = new HashMap<>();
        mListView1 = (ListView) findViewById(R.id.listView);
        mListView2 = (ListView) findViewById(R.id.listView2);

        mListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                adapter1.selet(position);
                listener.onSelect(parentList.get(position).commname);
                mCode = parentList.get(position).commcode;
                if (map.get(mCode)!=null){
                    adapter2 = new AddressSortRightAdapter(mContext);
                    adapter2.addData(map.get(mCode));
                    mListView2.setAdapter(adapter2);
                }else {
                    getCommunityList(mCode);
                }
            }

        });

        mListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (listener != null) {
                    listener.onSelect(map.get(mCode).get(position));
                }
            }

        });
    }

    private void getCommunityList(final String cityCode) {
        UserApi.get().GetCommunityList(mContext, cityCode, new ApiConfig.ApiRequestListener<CommCodeInfo>() {
            @Override
            public void onSuccess(String msg, CommCodeInfo data) {
                if (adapter2!=null){
                    adapter2.clear();
                }
                map.put(cityCode, data.commCodeList);
                adapter2 = new AddressSortRightAdapter(mContext);
                adapter2.addData(data.commCodeList);
                mListView2.setAdapter(adapter2);
            }

            @Override
            public void onFailure(String msg) {
                if (adapter2!=null){
                    adapter2.clear();
                }
            }
        });
    }

    public void setData(Map<String ,List<CommunityAddressInfo>> map){
        if (map!=null&&map.size()>0){
            this.map = map;
        }
    }

    public void setCity(List<CommunityAddressInfo> parentList){
        this.parentList.clear();
        if (parentList!=null&&parentList.size()>0){
            this.parentList = parentList;
            adapter1 = new AddressSortAdapter(mContext, parentList);
            mListView1.setAdapter(adapter1);
        }
    }

    public void setDefaultCity(String name){
        for (int i=0;i<parentList.size();i++){
           if (parentList.get(i).commname.equals(name)){
               adapter1.selet(i);
               mCode = parentList.get(i).commcode;
               if (map.get(mCode)!=null){
                   adapter2 = new AddressSortRightAdapter(mContext);
                   adapter2.addData(map.get(mCode));
                   mListView2.setAdapter(adapter2);
               }else {
                   getCommunityList(mCode);
               }
           }
        }
    }
}
