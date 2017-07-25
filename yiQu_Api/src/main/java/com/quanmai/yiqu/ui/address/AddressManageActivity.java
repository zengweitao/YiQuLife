package com.quanmai.yiqu.ui.address;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.AddressApi;
import com.quanmai.yiqu.api.ApiConfig;
import com.quanmai.yiqu.api.vo.AddressInfo;
import com.quanmai.yiqu.api.vo.AddressManageInfo;
import com.quanmai.yiqu.base.BaseActivity;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.ui.booking.BookingSecond2Activity;
import com.quanmai.yiqu.ui.selectpic.ImageFloder;

import com.quanmai.yiqu.ui.views.CustomListView;
import com.quanmai.yiqu.ui.views.SlideItem;

import java.util.ArrayList;

public class AddressManageActivity extends BaseActivity implements View.OnClickListener{

    private CustomListView listview_address;
    private AddressAdapter mAddressAdapter;
    CommonList<AddressInfo> addressList;
    private LayoutInflater inflater;
    int page=0;//请求页数
    int ADDOREDIT_CODE=0;
    private LinearLayout linear_add_address;
    private TextView tv_title;
    private PullToRefreshScrollView pulltorefresh_address;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);
        init();
        getAddressList(page);
    }
    public void init(){
        addressList=new CommonList();
        inflater=LayoutInflater.from(mContext);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("管理地址");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        pulltorefresh_address = (PullToRefreshScrollView) findViewById(R.id.pulltorefresh_address);
        linear_add_address = (LinearLayout) findViewById(R.id.linear_add_address);
        listview_address = (CustomListView) findViewById(R.id.listview_address);
        listview_address.setEnabled(true);
        mAddressAdapter = new AddressAdapter();
        listview_address.setAdapter(mAddressAdapter);
        linear_add_address.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        listview_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("checkaddress",addressList.get(position));
                setResult(203,intent);
                finish();
            }
        });
        pulltorefresh_address.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page=0;
                getAddressList(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                getAddressList(page);
            }
        });
    }

    public void getAddressList(final int page){
        showLoadingDialog("数据加载中...");
        AddressApi.get().AddressList(mContext, page, new ApiConfig.ApiRequestListener<CommonList<AddressInfo>>() {
            @Override
            public void onSuccess(String msg, CommonList<AddressInfo> data) {
                dismissLoadingDialog();
                Log.e("--地址列表","== "+data.size());
                if (data!=null&&data.size()!=0){
                    if (page==0){
                        addressList.clear();
                    }
                    addressList.addAll(data);
                    mAddressAdapter.notifyDataSetChanged();
                    pulltorefresh_address.onRefreshComplete();
                }
                if (data.max_page<=page+1){
                    pulltorefresh_address.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }else {
                    pulltorefresh_address.setMode(PullToRefreshBase.Mode.BOTH);
                }
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
                pulltorefresh_address.onRefreshComplete();
            }
        });
    }

    /**
     * 按地址ID删除地址
     * @param id
     */
    public void DeleteAddress(int id){
        showLoadingDialog("请稍后！");
        AddressApi.get().AddressDelete(mContext, id, new ApiConfig.ApiRequestListener<String>() {
            @Override
            public void onSuccess(String msg, String data) {
                dismissLoadingDialog();
                showCustomToast(msg);
                mAddressAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String msg) {
                dismissLoadingDialog();
                showCustomToast(msg);
            }
        });
    }

    /**
     * 设置默认地址
     */
    public void DefaultAddress(int id){
        AddressApi.get().AddressDefault(mContext,id,
                new ApiConfig.ApiRequestListener<String>() {
                    @Override
                    public void onSuccess(String msg, String data) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                        getAddressList(0);
                    }

                    @Override
                    public void onFailure(String msg) {
                        dismissLoadingDialog();
                        showCustomToast(msg);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        addressList.clear();
        getAddressList(0);
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
    }*/

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_add_address:
                reJump();
                break;
            case R.id.iv_back:
                if (addressList.size()>0){
                    Intent intent=new Intent();
                    intent.putExtra("checkaddress",addressList.get(0));
                    setResult(203,intent);
                }
                finish();
                break;
        }
    }

    public void reJump(){
        Intent intent=new Intent(mContext,AddressEditActivity.class);
        intent.putExtra("EditOrAdd","Add");
        startActivityForResult(intent,ADDOREDIT_CODE);
    }

    class AddressAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return addressList.size();
        }

        @Override
        public Object getItem(int position) {
            return addressList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder=null;
            if (convertView==null){
                View content=inflater.inflate(R.layout.item_address_order_recycle,null);
                View menu=inflater.inflate(R.layout.item_dele_meun,null);
                mViewHolder=new ViewHolder(content,menu);
                SlideItem slideItem=new SlideItem(mContext);
                slideItem.setContentView(content,menu);
                convertView=slideItem;
                mViewHolder.linearLayout=(RelativeLayout) content.findViewById(R.id.relative_order_addess);
                mViewHolder.textView_order_name= (TextView) convertView.findViewById(R.id.textView_order_name);
                mViewHolder.textView_order_phone= (TextView) convertView.findViewById(R.id.textView_order_phone);
                mViewHolder.textView_order_address= (TextView) convertView.findViewById(R.id.textView_order_address);
                mViewHolder.textView_order_mr= (TextView) convertView.findViewById(R.id.textView_order_mr);
                mViewHolder.imageView_order_next= (ImageView) convertView.findViewById(R.id.imageView_order_next);
                mViewHolder.imageView_order_edit= (ImageView) convertView.findViewById(R.id.imageView_order_edit);
                convertView.setTag(mViewHolder);
            }else {
                mViewHolder= (ViewHolder) convertView.getTag();
            }
            mViewHolder.imageView_order_next.setVisibility(View.INVISIBLE);
            mViewHolder.textView_order_mr.setVisibility(View.INVISIBLE);
            mViewHolder.imageView_order_edit.setVisibility(View.VISIBLE);
            if (addressList!=null&&addressList.size()!=0){
                mViewHolder.textView_order_name.setText(addressList.get(position).name);
                mViewHolder.textView_order_phone.setText(addressList.get(position).phone);
                mViewHolder.textView_order_address.setText(addressList.get(position).address);
                if (addressList.get(position).sortfield==1){
                    mViewHolder.textView_order_mr.setVisibility(View.VISIBLE);
                }
            }
            mViewHolder.imageView_order_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,AddressEditActivity.class);
                    Bundle mBundle = new Bundle();
                    mBundle.putSerializable("AddressInfo",addressList.get(position));
                    intent.putExtras(mBundle);
                    intent.putExtra("EditOrAdd","Edit");
                    startActivityForResult(intent,ADDOREDIT_CODE);
                }
            });
            mViewHolder.itemTvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DeleteAddress(addressList.get(position).id);
                    addressList.remove(position);
                }
            });
            mViewHolder.defaultAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DefaultAddress(addressList.get(position).id);
                }
            });
            return convertView;
        }

        class ViewHolder {
            TextView textView_order_name;
            TextView textView_order_phone;
            TextView textView_order_address;
            TextView textView_order_mr;
            ImageView imageView_order_next;
            ImageView imageView_order_edit;
            RelativeLayout linearLayout;
            TextView itemTvDelete;
            TextView defaultAddress;
            public ViewHolder(View center,View menu) {
                this.itemTvDelete = (TextView) menu.findViewById(R.id.delete);
                this.defaultAddress = (TextView) menu.findViewById(R.id.defaultAddress);
            }

        }
    }
}