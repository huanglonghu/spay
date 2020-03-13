package com.example.spay.ui.fragment.mainActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spay.R;
import com.example.spay.bean.User;
import com.example.spay.databinding.FragmentMainBinding;
import com.example.spay.greendao.option.FriendOption;
import com.example.spay.greendao.option.UserOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.adapter.MyViewPagerAdapter;
import com.example.spay.constant.Constant;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.StringUtil;
import com.google.gson.Gson;
import java.util.ArrayList;

public class MainFragment extends BaseFragment {
    private FragmentMainBinding binding;
    private ArrayList<BaseFragment> fragments;
    private View view;

    public FragmentMainBinding getBinding() {
        return binding;
    }

    public void setBinding(FragmentMainBinding binding) {
        this.binding = binding;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
            presenter.setMainBinding(binding);
            binding.setNewsCount(0);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.setPosition(0);
            initData();
            initFragments();
            initListener();
            MyViewPagerAdapter adapter = new MyViewPagerAdapter(getChildFragmentManager(), fragments);
            binding.mainViewPager.setAdapter(adapter);
            binding.mainViewPager.setOffscreenPageLimit(3);
        }
        return view;
    }

    private void initData() {
        if (UserOption.getInstance().querryUser(Constant.userId) == null) {
            HttpUtil.getInstance().getUserMsgById(Constant.userId).subscribe(
                    personalStr -> {
                        User mine = new Gson().fromJson(personalStr, User.class);
                        User.ResultBean result = mine.getResult();
                        if (result != null) {
                            if (UserOption.getInstance().querryUser(Constant.userId) == null) {
                                Constant.userName = result.getNickName();
                                Constant.syNum = result.getUserName();
                                com.example.spay.greendao.entity.User user = new com.example.spay.greendao.entity.User();
                                user.setHeadImageUrl(result.getHeadImgUrl());
                                user.setUserName(result.getNickName());
                                user.setSyNumber(result.getUserName());
                                user.setArea(result.getArea());
                                user.setPhoneNumer(result.getPhoneNumber());
                                user.setIsMakeCode(result.isMakeCode());
                                user.setEmailAddress(result.getEmailAddress());
                                user.setIsProxy(result.isProxy());
                                if (result.getSex() == 1) {
                                    user.setSex("男");
                                } else {
                                    user.setSex("女");
                                }
                                user.setUserId(result.getId());
                                UserOption.getInstance().addUser(user);
                                FriendOption.getInstance(activity).querryFriendList(1, true);
                            }
                        }

                    }
            );
        } else {
            com.example.spay.greendao.entity.User user = UserOption.getInstance().querryUser(Constant.userId);
            Constant.userName = user.getUserName();
            Constant.syNum = user.getSyNumber();
        }
    }

    private void initListener() {
        binding.mainViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                binding.setPosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void lazyLoad() {

    }


    public void initFragments() {
        String home = StringUtil.getString(activity, R.string.home);
        String friends = StringUtil.getString(activity, R.string.friends);
        String me = StringUtil.getString(activity, R.string.me);
        String[] titleArray = {home, friends, me};
        binding.setTitleArray(titleArray);
        fragments = new ArrayList<>();
        HomeFragment homeFragment = new HomeFragment();
        FriendFragment friendFragment = new FriendFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        friendFragment.setArguments(bundle);
        MineFragment mineFragment = new MineFragment();
        fragments.add(homeFragment);
        fragments.add(friendFragment);
        fragments.add(mineFragment);
        presenter.setFragments(fragments);
    }


}
