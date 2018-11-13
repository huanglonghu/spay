package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.bean.ApplyFriend;
import com.example.godcode.bean.ConcurAddFriend;
import com.example.godcode.bean.ConcurAddFriendResponse;
import com.example.godcode.databinding.FragmentNewfriendBinding;
import com.example.godcode.greendao.entity.Friend;
import com.example.godcode.greendao.option.FriendOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.adapter.NewFriendListAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.LogUtil;
import com.google.gson.Gson;
import java.util.HashMap;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
            binding.newFriendToolbar.title.setText("新的朋友");
            binding.newFriendToolbar.tvOption.setText("添加朋友");
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
        adapter = new NewFriendListAdapter(activity, items, NewFriendFragment.this);
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

    @Override
    public void refreshData() {
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
                        presenter.step2Fragment(cdf, "cdf");
                    }
                }
        );
    }

}
