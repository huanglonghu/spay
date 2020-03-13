package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.spay.R;
import com.example.spay.bean.GroupMsg;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.databinding.ItemLvGroupBinding;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.widget.EditNameDialog;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/11/9.
 */

public class GroupListAdapter extends BaseAdapter {
    private ArrayList<GroupMsg.ResultBean.Bean> groupList;
    private Context context;
    private LayoutInflater layoutInflater;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private int periodType;
    private String[] incomeType = {"今日", "昨日", "本周", "本月", "总"};

    public GroupListAdapter(Context context, ArrayList<GroupMsg.ResultBean.Bean> groupList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.groupList = groupList;
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (groupList.size() > 0 && viewMap.get(position) == null) {
            ItemLvGroupBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_group, null, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
            binding.setPeriodType(incomeType[periodType - 1]);
            GroupMsg.ResultBean.Bean bean = groupList.get(position);
            String headImgUrl = bean.getHeadImgUrl();
            if (!TextUtils.isEmpty(headImgUrl)) {
                if (!headImgUrl.contains("http")) {
                    headImgUrl = Constant.baseUrl + headImgUrl;
                }
                RxImageLoader.with(context).load(headImgUrl).into(binding.ivPhoto);
            }else {
                binding.ivPhoto.setImageResource(R.drawable.contact_normal);
            }
            binding.setBean(bean);
            binding.editGroupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditNameDialog editNameDialog = new EditNameDialog(context, bean.getFK_UserID(), binding.groupName);
                    editNameDialog.show();
                }
            });
            viewMap.put(position, convertView);
        }
        return viewMap.get(position);
    }

    public void clearData(int periodType) {
        this.periodType = periodType;
        viewMap.clear();
        groupList.clear();
        notifyDataSetChanged();
    }

    public int getGroupId(int position) {
        GroupMsg.ResultBean.Bean bean = groupList.get(position);
        return bean.getFK_UserID();
    }

    public String getGroupName(int position) {
        GroupMsg.ResultBean.Bean bean = groupList.get(position);
        return bean.getUserName();
    }


    public void refreshData(int position, GroupMsg.ResultBean.Bean bean) {
        View view = getView(position, null, null);
        ItemLvGroupBinding binding = (ItemLvGroupBinding) view.getTag();
        binding.setBean(bean);

    }
}
