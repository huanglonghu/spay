package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.spay.R;
import com.example.spay.bean.MobileRechargeList;
import com.example.spay.databinding.LayoutGridItemBinding;
import com.example.spay.databinding.MobileRechargeItemBinding;
import com.example.spay.utils.LogUtil;

import java.util.HashMap;
import java.util.List;


public class MobileRechargeAdapter extends BaseAdapter {

    private HashMap<Integer, View> viewMap = new HashMap<>();
    private LayoutInflater layoutInflater;
    private List<MobileRechargeList.ResultBean> datas;

    public MobileRechargeAdapter(List<MobileRechargeList.ResultBean> datas, Context context) {
        layoutInflater = LayoutInflater.from(context);
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            MobileRechargeItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.mobile_recharge_item, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
            MobileRechargeList.ResultBean resultBean = datas.get(position);
            binding.setBean(resultBean);
            viewMap.put(position, convertView);
        }
        return viewMap.get(position);
    }


    private int selectPosition = -1;

    public void select(int position) {
        LogUtil.log("==============select==============" + position);
        if (selectPosition != -1) {
            View view = viewMap.get(selectPosition);
            view.setSelected(false);
        }
        View view = viewMap.get(position);
        view.setSelected(true);
        selectPosition = position;
    }

}
