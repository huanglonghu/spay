package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.ApplyFriend;
import com.example.spay.bean.ConcurAddFriend;
import com.example.spay.bean.ConcurAddFriendResponse;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.databinding.ItemLvNewfriendBinding;
import com.example.spay.constant.Constant;
import com.example.spay.http.HttpUtil;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.fragment.deatailFragment.ContactDetailFragment;
import com.google.gson.Gson;
import java.util.List;

public class NewFriendListAdapter extends BaseListAdapter {
    public NewFriendListAdapter(Context context, List<ApplyFriend.ResultBean.ItemsBean> datas, int res) {
        super(context, datas, res);
    }

    @Override
    View initView(LayoutInflater layoutInflater, int res, List datas, int position, ViewGroup parent) {
        ItemLvNewfriendBinding binding = DataBindingUtil.inflate(layoutInflater, res, parent, false);
        ApplyFriend.ResultBean.ItemsBean itemsBean = (ApplyFriend.ResultBean.ItemsBean) datas.get(position);
        binding.setItem(itemsBean);
        binding.btnAcceptfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptFriend(itemsBean);
            }
        });
        String friendImgPath = itemsBean.getFriendImgPath();
        if (!TextUtils.isEmpty(friendImgPath)) {
            if (!friendImgPath.contains("http")) {
                friendImgPath = Constant.baseUrl + friendImgPath;
            }
            RxImageLoader.with(context).load(friendImgPath).into(binding.itemNewfriendPhoto);
        } else {
            binding.itemNewfriendPhoto.setImageResource(R.drawable.contact_normal);
        }
        return binding.getRoot();
    }


    public void acceptFriend(ApplyFriend.ResultBean.ItemsBean bean) {
        ConcurAddFriend concurAddFriend = new ConcurAddFriend();
        concurAddFriend.setIsConcur(true);
        concurAddFriend.setId(bean.getId());
        HttpUtil.getInstance().concurAddFriend(concurAddFriend).subscribe(
                concurAddFriendStr -> {
                    ConcurAddFriendResponse concurAddFriendResponse = new Gson().fromJson(concurAddFriendStr, ConcurAddFriendResponse.class);
                    if (concurAddFriendResponse.isSuccess()) {
                        ContactDetailFragment cdf = new ContactDetailFragment();
                        cdf.initData(bean.getFK_UserID());
                        Presenter.getInstance().step2Fragment(cdf, "cdf");
                    }
                }
        );
    }
}
