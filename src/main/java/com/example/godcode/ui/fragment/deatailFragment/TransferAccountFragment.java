package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.databinding.FragmentTransferaccountBinding;
import com.example.godcode.greendao.entity.Friend;
import com.example.godcode.greendao.option.FriendOption;
import com.example.godcode.ui.adapter.ContactAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.DividerItemDecoration;
import com.example.godcode.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

public class TransferAccountFragment extends BaseFragment {
    private FragmentTransferaccountBinding binding;
    private View view;
    private ContactAdapter contactAdapter;
    private List<Friend> friendList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transferaccount, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            if(type==3){
                binding.transferAccountToolBar.title.setText("转账");
            }else {
                binding.transferAccountToolBar.title.setText("选择好友");
            }

            initView();
            initListener();
        }
        return view;
    }


    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private void initListener() {
        binding.searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                presenter.step2Fragment(searchFragment, "search");
            }
        });
    }

    public void initView() {
        friendList = FriendOption.getInstance(activity).getAllFriend(Constant.userId);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.transferAccountList.setLayoutManager(layoutManager);
        binding.transferAccountList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        contactAdapter = new ContactAdapter(getActivity(), friendList, presenter, type);
        binding.transferAccountList.setAdapter(contactAdapter);
        FriendOption.getInstance(activity).friendUpdateListener().subscribe(
                update -> {
                    if (update) {
                        LogUtil.log("-------转账好友---更新-----");
                        List<Friend> allFriend = FriendOption.getInstance(activity).getAllFriend(Constant.userId);
                        friendList.clear();
                        allFriend.addAll(allFriend);
                        contactAdapter.notifyDataSetChanged();
                    }
                }
        );
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void refreshData() {

    }


}
