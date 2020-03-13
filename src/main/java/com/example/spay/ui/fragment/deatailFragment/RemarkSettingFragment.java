package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spay.R;
import com.example.spay.bean.UpdateFriend;
import com.example.spay.databinding.FragmentRemarksettingBinding;
import com.example.spay.greendao.entity.Friend;
import com.example.spay.greendao.option.FriendOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.fragment.mainActivity.FriendFragment;
import com.example.spay.utils.StringUtil;

public class RemarkSettingFragment extends BaseFragment {
    private FragmentRemarksettingBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_remarksetting, null, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            initView();
            initListener();
        }
        return view;
    }

    private void initView() {
        binding.remarkSettingToolBar.title.setText("设置备注");
        binding.remarkSettingToolBar.tvOption.setText("确定");
        if(!TextUtils.isEmpty(friend.getUserName())){
            binding.remark.setText(friend.getUserName());
            binding.remark.setSelection(friend.getUserName().length());
        }

    }

    private void initListener() {
        binding.remarkSettingToolBar.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(binding.remark.getText())) {
                    UpdateFriend updateFriend = new UpdateFriend();
                    UpdateFriend.FriendsBean friendsBean = new UpdateFriend.FriendsBean();
                    friendsBean.setFK_UserID(Constant.userId);
                    friendsBean.setId(friend.getId_());
                    friendsBean.setToUserID(friend.getUserId());
                    friendsBean.setIsConcur(true);
                    friendsBean.setNickName(binding.remark.getText().toString());
                    updateFriend.setFriends(friendsBean);
                    HttpUtil.getInstance().updateFriend(updateFriend).subscribe(
                            str -> {
                                String remark = binding.remark.getText().toString();
                                friend.setUserName(remark);
                                if (!TextUtils.isEmpty(remark)) {
                                    String firstChar = StringUtil.getPingYin(remark).substring(0, 1);
                                    friend.setFirstChar(firstChar.toUpperCase());
                                }
                                FriendOption.getInstance(activity).updateFriend(friend);
                                //通知其他界面更新昵称
                                remarkCallBack.remarkUpdate(remark);
                                FriendFragment friendFragment= (FriendFragment) presenter.getFragments().get(3);
                                friendFragment.refreshData();
                                presenter.back();
                            }
                    );

                } else {
                    Toast.makeText(activity, "请设置备注", Toast.LENGTH_SHORT).show();
                }

            }
        });

        binding.remarkSettingToolBar.toolbar3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.back();
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }


    private Friend friend;

    public void initData(Friend friend) {
        this.friend = friend;
    }

    public interface RemarkCallBack {
        public void remarkUpdate(String remark);
    }

    private RemarkCallBack remarkCallBack;


    public void setRemarkCallBack(RemarkCallBack remarkCallBack) {
        this.remarkCallBack = remarkCallBack;
    }
}
