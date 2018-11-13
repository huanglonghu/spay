package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.ApplyFriend;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.ItemLvNewfriendBinding;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.fragment.deatailFragment.NewFriendFragment;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2018/6/14.
 */

public class NewFriendListAdapter extends BaseAdapter {
    private Context context;
    private final LayoutInflater layoutInflater;
    private List<ApplyFriend.ResultBean.ItemsBean> items;
    private NewFriendFragment newFriendFragment;

    public NewFriendListAdapter(Context context, List<ApplyFriend.ResultBean.ItemsBean> items, NewFriendFragment newFriendFragment) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.items = items;
        this.newFriendFragment = newFriendFragment;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemLvNewfriendBinding binding;
        if (convertView == null) {
            binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_newfriend, parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ItemLvNewfriendBinding) convertView.getTag();
        }
        ApplyFriend.ResultBean.ItemsBean itemsBean = items.get(position);
        binding.setItem(itemsBean);
        binding.setFragment(newFriendFragment);
        String friendImgPath = itemsBean.getFriendImgPath();
        if (!TextUtils.isEmpty(friendImgPath)) {
            if (!friendImgPath.contains("http")) {
                friendImgPath = Constant.baseUrl + friendImgPath;
            }
            RxImageLoader.with(context).load(friendImgPath).into(binding.itemNewfriendPhoto);
        } else {
            binding.itemNewfriendPhoto.setImageResource(R.drawable.contact_normal);
        }
        return convertView;
    }
}
