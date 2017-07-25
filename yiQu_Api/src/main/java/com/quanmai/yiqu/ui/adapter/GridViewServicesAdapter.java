package com.quanmai.yiqu.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.ServicesInfo;
import com.quanmai.yiqu.base.CommonList;
import com.quanmai.yiqu.common.util.ImageloaderUtil;
import com.quanmai.yiqu.common.util.StringUtil;
import com.quanmai.yiqu.ui.common.WebActivity;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by zjj on 2016/6/29.
 * 便民服务GridView适配器
 */
public class GridViewServicesAdapter extends BaseAdapter {
    private Context mContext;
    private CommonList<ServicesInfo> mServicesInfos = new CommonList<>();

    public GridViewServicesAdapter(Context context, CommonList<ServicesInfo> list) {
        this.mContext = context;
        this.mServicesInfos = list;
    }

    @Override
    public int getCount() {
        if (mServicesInfos.size() > 4) {
            return 4;
        }
        return mServicesInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mServicesInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_grid_view_services, null);
            viewHolder = new ViewHolder();
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvRemarks = (TextView) convertView.findViewById(R.id.tvRemarks);
            viewHolder.mainLayout = (RelativeLayout) convertView.findViewById(R.id.mainLayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final ServicesInfo servicesInfo = mServicesInfos.get(position);

//        if (TextUtils.isEmpty(servicesInfo.icon)){
        ImageloaderUtil.displayImage(mContext, servicesInfo.icon, viewHolder.imgIcon);
//        }
        viewHolder.tvName.setText(servicesInfo.name);
        viewHolder.tvRemarks.setText(StringUtil.stringFilter2(servicesInfo.remarks));
        viewHolder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebActivity.class);
                intent.putExtra("title", servicesInfo.name);
                intent.putExtra("http_url", servicesInfo.href);
                mContext.startActivity(intent);

                switch (position) {
                    case 0: {
                        MobclickAgent.onEvent(mContext,"service1"); //友盟自定义事件统计——便民服务
                        break;
                    }
                    case 1: {
                        MobclickAgent.onEvent(mContext,"service2");
                        break;
                    }
                    case 2: {
                        MobclickAgent.onEvent(mContext,"service3");
                        break;
                    }
                    case 3: {
                        MobclickAgent.onEvent(mContext,"service4");
                        break;
                    }
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        RelativeLayout mainLayout;
        ImageView imgIcon;
        TextView tvName;
        TextView tvRemarks;
    }

}
