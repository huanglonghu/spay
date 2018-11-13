package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.RevenueDivideItem;
import com.example.godcode.databinding.ItemLvRevenueconfigBinding;
import com.example.godcode.ui.fragment.deatailFragment.RevenueConfigFragment;
import com.example.godcode.utils.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class RevenueConfigListAdapter extends BaseAdapter {
    private Context context;
    private final LayoutInflater layoutInflater;
    private ArrayList<RevenueDivideItem> divideList;
    private RevenueConfigFragment fragment;

    public void setAdd(boolean add) {
        isAdd = add;
    }

    private boolean isAdd;

    private HashMap<Integer, View> viewMap = new HashMap<>();

    public RevenueConfigListAdapter(Context context, ArrayList<RevenueDivideItem> divideList, RevenueConfigFragment fragment) {
        this.context = context;
        this.divideList = divideList;
        this.fragment = fragment;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return divideList.size();
    }

    @Override
    public Object getItem(int position) {
        return divideList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemLvRevenueconfigBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_revenueconfig, parent, false);
            binding.setFragment(fragment);
            RevenueDivideItem revenueDivideItem = divideList.get(position);
            binding.setRevenueDivideItem(revenueDivideItem);
            binding.setPosition(position);
            convertView = binding.getRoot();
            convertView.setTag(binding);
            if (revenueDivideItem.getId() == null) {
                binding.etDivide.setVisibility(View.GONE);
            }
            viewMap.put(position, convertView);
        }
        return viewMap.get(position);
    }

}
