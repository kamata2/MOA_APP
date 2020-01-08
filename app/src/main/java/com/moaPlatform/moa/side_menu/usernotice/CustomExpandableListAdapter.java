package com.moaPlatform.moa.side_menu.usernotice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.moaPlatform.moa.R;
import com.moaPlatform.moa.side_menu.usernotice.model.UserNoticeItems;

import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<UserNoticeItems> headerList;

    CustomExpandableListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return listPosition;
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, parent);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.headerList.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return headerList == null ? 0 : headerList.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, parent);
        }
        UserNoticeItems resUserNotiItem = headerList.get(listPosition);
        TextView reviewTitle = convertView.findViewById(R.id.review);
        TextView reviewTitleDay = convertView.findViewById(R.id.day);
        TextView reviewTitleNewBt = convertView.findViewById(R.id.newbtn);
        TextView reviewTitleUser = convertView.findViewById(R.id.user);

        reviewTitle.setText(resUserNotiItem.title);
        reviewTitleDay.setText(resUserNotiItem.creeDt);
        reviewTitleNewBt.setText(resUserNotiItem.chckYn);
        reviewTitleUser.setText(resUserNotiItem.dtlCntnt);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public void setData(List<UserNoticeItems> headerList) {
        this.headerList = headerList;
    }
}
