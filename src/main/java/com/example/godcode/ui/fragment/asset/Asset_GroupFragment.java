package com.example.godcode.ui.fragment.asset;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.godcode.R;
import com.example.godcode.bean.GroupMsg;
import com.example.godcode.bean.WebSocketNews1;
import com.example.godcode.databinding.LayoutAssetGroupBinding;
import com.example.godcode.ui.adapter.GroupListAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.fragment.deatailFragment.Asset_1_Fragment;
import com.example.godcode.utils.LogUtil;

import java.util.ArrayList;
import java.util.Map;

public class Asset_GroupFragment extends BaseFragment {
    private GroupListAdapter groupListAdapter;
    private LayoutAssetGroupBinding binding;
    private Asset_1_Fragment parentFragment;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            parentFragment = (Asset_1_Fragment) getParentFragment();
            binding = DataBindingUtil.inflate(inflater, R.layout.layout_asset_group, null, false);
            initView();
            initData();
            initListener();
        }
        parentFragment.setTitle("我的资产");
        return binding.getRoot();
    }

    private void initListener() {
        binding.lvGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int groupId = groupListAdapter.getGroupId(position);
                String groupName = groupListAdapter.getGroupName(position);
                Asset_DetailFragment fragment = (Asset_DetailFragment) parentFragment.getFragments().get(1);
                fragment.setCurrentGroupUserID(groupId);
                parentFragment.toggleFragment(1);
                parentFragment.setTitle(groupName);
            }
        });
    }

    private void initData() {

    }

    private ArrayList<GroupMsg.ResultBean.bean> groupList = new ArrayList<>();

    private void initView() {
        groupListAdapter = new GroupListAdapter(activity, groupList);
        binding.lvGroup.setAdapter(groupListAdapter);
    }

    @Override
    protected void lazyLoad() {

    }


    public void refreshGroup(Map<String, GroupMsg.ResultBean.bean> data, int periodType) {
        groupListAdapter.clearData(periodType);
        for (String a : data.keySet()) {
            GroupMsg.ResultBean.bean bean = data.get(a);
            bean.setUserName(a);
            groupList.add(bean);
        }
        groupListAdapter.notifyDataSetChanged();
    }

    public void refreshData(WebSocketNews1.DataBean data) {
        String merchantUserIds = data.getMerchantUserIds();
        double scanQRMoney = data.getScanQRMoney();
        double divedeMoney = data.getDivedeMoney();
        for (int i = 0; i < groupList.size(); i++) {
            GroupMsg.ResultBean.bean bean = groupList.get(i);
            double v1 = Double.parseDouble(bean.getScanCodeIncome());
            double v2 = Double.parseDouble(bean.getDivideIncome());
            bean.setScanCodeIncome(v1 + scanQRMoney);
            bean.setDivideIncome(v2 + divedeMoney);
            groupListAdapter.refreshData(i, bean);
            break;

        }
    }

}
