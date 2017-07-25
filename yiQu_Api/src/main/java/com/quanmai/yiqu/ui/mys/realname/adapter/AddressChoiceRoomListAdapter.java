package com.quanmai.yiqu.ui.mys.realname.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quanmai.yiqu.R;
import com.quanmai.yiqu.api.vo.BuildInfo;
import com.quanmai.yiqu.api.vo.ChooseRoomInfo;
import com.quanmai.yiqu.api.vo.RoomInfo;
import com.quanmai.yiqu.api.vo.UnitInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.quanmai.yiqu.R.id.tvBuildNo;
import static com.quanmai.yiqu.R.id.tvRoomNo;

/**
 * Created by James on 2016/7/8.
 * 住址选择--》房号列表适配器
 */
public class AddressChoiceRoomListAdapter extends BaseExpandableListAdapter {
    Context context;
    List<ChooseRoomInfo.ArListBean> arList;
    List<String> roomList;

    public AddressChoiceRoomListAdapter(Context context) {
        this.context = context;
        arList=new ArrayList<>();
        roomList=new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return arList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //规避框架自动调用不合法参数的错误
        if(groupPosition>=arList.size()){
            return 0;
        }
        int size = arList.get(groupPosition).getRoomList().size();
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arList.get(groupPosition).getBuildno();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arList.get(groupPosition).getRoomList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHoderGroup mViewHoderGroup=null;
        if (convertView == null) {
            mViewHoderGroup=new ViewHoderGroup();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_address_choice_room, null);
            mViewHoderGroup.tvBuildNo= (TextView) convertView.findViewById(tvBuildNo);
            mViewHoderGroup.relative_visibility= (RelativeLayout) convertView.findViewById(R.id.relative_visibility);
            //convertView = View.inflate(context,R.layout.item_address_choice_room, null);
            convertView.setTag(mViewHoderGroup);
        }else {
            mViewHoderGroup= (ViewHoderGroup) convertView.getTag();
        }
        mViewHoderGroup.tvBuildNo.setVisibility(View.VISIBLE);
        mViewHoderGroup.relative_visibility.setVisibility(View.GONE);
        mViewHoderGroup.tvBuildNo.setText(arList.get(groupPosition).getBuildno());
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHoderChild mViewHoderChild=null;
        if (convertView == null) {
            mViewHoderChild=new ViewHoderChild();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_address_choice_room, null);
            mViewHoderChild.tvBuildNo= (TextView) convertView.findViewById(R.id.tvBuildNo);
            mViewHoderChild.tvRoomNo= (TextView) convertView.findViewById(R.id.tvRoomNo);
            mViewHoderChild.relative_visibility= (RelativeLayout) convertView.findViewById(R.id.relative_visibility);
           // convertView = View.inflate(context,R.layout.item_address_choice_room, null);
            convertView.setTag(mViewHoderChild);
        }else {
            mViewHoderChild = (ViewHoderChild) convertView.getTag();
        }
        mViewHoderChild.tvBuildNo.setVisibility(View.GONE);
        mViewHoderChild.relative_visibility.setVisibility(View.VISIBLE);
        mViewHoderChild.tvRoomNo.setText(arList.get(groupPosition).getRoomList().get(childPosition));
        return convertView;
    }

    class ViewHoderGroup{
        TextView tvBuildNo;
        RelativeLayout relative_visibility;

    }
    class ViewHoderChild{
        TextView tvBuildNo;
        TextView tvRoomNo;
        RelativeLayout relative_visibility;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void clear(){
        arList.clear();
    }

    public void addAll(List<ChooseRoomInfo.ArListBean> dataArlist){
       // List<ChooseRoomInfo.ArListBean> datas=getData(dataArlist);
        arList.addAll(dataArlist);
        notifyDataSetChanged();
    }


    private List<ChooseRoomInfo.ArListBean> getData(List<ChooseRoomInfo.ArListBean> dataArlist){
        //对list进行排序
        if (!dataArlist.isEmpty()) {
            Collections.sort(dataArlist, new Comparator<ChooseRoomInfo.ArListBean>() {
                @Override
                public int compare(ChooseRoomInfo.ArListBean lhs, ChooseRoomInfo.ArListBean rhs) {
                    return (get_StringNum(lhs.getBuildno()).compareTo(get_StringNum(rhs.getBuildno())));
                }
            });
        }
        return dataArlist;
    }

    public String get_StringNum(String str){
            str=str.trim();
            String str2="";
            if(str != null && !"".equals(str)){
                for(int i=0;i<str.length();i++){
                    if(str.charAt(i)>=48 && str.charAt(i)<=57){
                       return str2+=str.charAt(i);
                    }
                }
            }
            return null;
    }




    /*private Context mContext;
    private List<Integer> mBuildPositions;
    private List<RoomInfo> mRoomList;
    private List<UnitInfo> mUnitInfos;

    public AddressChoiceRoomListAdapter(Context context) {
        this.mContext = context;
        this.mBuildPositions = new ArrayList<>();
        this.mRoomList = new ArrayList<>();
        this.mUnitInfos = new ArrayList<>();
    }


    @Override
    public int getCount() {
        return mRoomList.size();
    }

    @Override
    public Object getItem(int position) {
        return mRoomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_address_choice_room, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvBuildNo = (TextView) convertView.findViewById(R.id.tvBuildNo);
            viewHolder.tvRoomNo = (TextView) convertView.findViewById(R.id.tvRoomNo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (mBuildPositions.contains(position)) {
            viewHolder.tvBuildNo.setVisibility(View.VISIBLE);
            viewHolder.tvBuildNo.setText(mRoomList.get(position).buildno);
        }else {
            viewHolder.tvBuildNo.setVisibility(View.GONE);
        }
        viewHolder.tvRoomNo.setText(mRoomList.get(position).room);

        return convertView;
    }

    public void clear() {
        mBuildPositions.clear();
        mUnitInfos.clear();
        mRoomList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<UnitInfo> list) {
        if (list == null || list.size() == 0) return;
        mBuildPositions.clear();
        mUnitInfos.clear();
        mRoomList.clear();
        mUnitInfos.addAll(list);
        for (int i = 0; i < mUnitInfos.size(); i++) {

            for (int j = 0; j < mUnitInfos.get(i).roomList.size(); j++) {
                UnitInfo unitInfo = mUnitInfos.get(i);
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.buildno = unitInfo.buildno;
                roomInfo.room = unitInfo.roomList.get(j);
                mRoomList.add(roomInfo);
            }

            if (i == 0) {
                mBuildPositions.add(0);
            } else {
                mBuildPositions.add(mUnitInfos.get(i-1).roomList.size());
            }
        }
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvBuildNo;
        TextView tvRoomNo;
    }*/
}
