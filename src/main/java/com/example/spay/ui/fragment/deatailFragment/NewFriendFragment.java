package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spay.R;
import com.example.spay.bean.ApplyFriend;
import com.example.spay.databinding.FragmentNewfriendBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.adapter.NewFriendListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.utils.StringUtil;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;

public class NewFriendFragment extends BaseFragment {
    private FragmentNewfriendBinding binding;
    private View view;
    private List<ApplyFriend.ResultBean.ItemsBean> items;
    private NewFriendListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_newfriend, container, false);
            binding.setPresenter(presenter);
            String title = StringUtil.getString(activity, R.string.newfriend);
            String str = StringUtil.getString(activity, R.string.addFriend);
            binding.newFriendToolbar.title.setText(title);
            binding.newFriendToolbar.tvOption.setText(str);
            view = binding.getRoot();
            initListener();
        }
        initData();
        return view;
    }

    private void initListener() {

    }

    private void querryFriendList(int page, boolean isConcur) {
        binding.searchNewFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                presenter.step2Fragment(searchFragment, "search");
            }
        });

        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("Filter", "");
        urlMap.put("sorting", "");
        urlMap.put("UserId", Constant.userId + "");
        urlMap.put("isConcur", isConcur + "");
        HttpUtil.getInstance().getFriendList(urlMap).subscribe(
                getFriendListStr -> {
                    ApplyFriend applyFriend = new Gson().fromJson(getFriendListStr, ApplyFriend.class);
                    items = applyFriend.getResult().getItems();
                    initView();
                }
        );
    }



    private void initData() {
        querryFriendList(1, false);
    }


    public void initView() {
        adapter = new NewFriendListAdapter(activity, items, R.layout.item_lv_newfriend);
        binding.lvNewFriend.setAdapter(adapter);
        binding.newFriendToolbar.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.step2Fragment("addFriend");
            }
        });

    }

    @Override
    protected void lazyLoad() {
    }


}
