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
import com.example.spay.bean.AddFriend;
import com.example.spay.bean.QurreyFriend;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.databinding.FragmentUserBinding;
import com.example.spay.greendao.entity.Friend;
import com.example.spay.greendao.option.FriendOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.utils.GsonUtil;

public class UserFragment extends BaseFragment {
    private FragmentUserBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false);
            view = binding.getRoot();
            binding.setPresenter(presenter);
            initView();
            initListener();
        }
        return view;
    }

    private void initView() {
        binding.userDetailToolbar.title.setText("详细资料");

        Friend friend = FriendOption.getInstance(activity).querryFriend(id);
        if (friend == null) {
            HttpUtil.getInstance().getUserMsgById(id).subscribe(
                    userStr -> {
                        QurreyFriend qurreyFriend = GsonUtil.getInstance().fromJson(userStr, QurreyFriend.class);
                        QurreyFriend.ResultBean bean = qurreyFriend.getResult();
                        String headImgUrl = bean.getHeadImgUrl();
                        setHeadImg(headImgUrl);
                        binding.tvUserName.setText(bean.getNickName());
                        binding.tvNumber.setText(bean.getUserName());
                        binding.userArea.setText(bean.getArea());
                    }
            );
        } else {
            setHeadImg(friend.getHeadImageUrl());
            binding.tvUserName.setText(friend.getUserName());

        }


    }

    private void setHeadImg(String headImgUrl) {
        if (!TextUtils.isEmpty(headImgUrl)) {
            if (!headImgUrl.contains("http")) {
                headImgUrl = Constant.baseUrl + headImgUrl;
            }
            RxImageLoader.with(activity).load(headImgUrl).into(binding.ivUser);
        }
    }

    private void initListener() {
        binding.btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddFriend addFriend = new AddFriend();
                AddFriend.FriendsBean friendsBean = new AddFriend.FriendsBean();
                friendsBean.setFK_UserID(Constant.userId);
                friendsBean.setToUserID(id);
                addFriend.setFriends(friendsBean);
                HttpUtil.getInstance().addFriend(addFriend).subscribe(
                        addFriendStr -> {
                            Toast.makeText(activity, "已发送好友请求", Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    private int id;

    public void initData(int id) {
        this.id = id;
    }

}
