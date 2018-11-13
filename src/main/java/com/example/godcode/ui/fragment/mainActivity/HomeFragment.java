package com.example.godcode.ui.fragment.mainActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentHomeBinding;
import com.example.godcode.greendao.entity.Notification;
import com.example.godcode.greendao.option.NotificationOption;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.fragment.deatailFragment.MobileRechargeFragment;

import java.util.List;

public class HomeFragment extends BaseFragment {

    private View view;
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            binding.setPresenter(presenter);
            initListener();
        }
        initView();
        return binding.getRoot();
    }

    private void initView() {

        List<Notification> notifications = NotificationOption.getInstance().getAllNotification(Constant.userId);
        if (notifications.size() > 0) {
            Notification n1 = notifications.get(notifications.size() - 1);
            binding.news1.setText(n1.getContent() + n1.getDate());
        }
        if (notifications.size() > 1) {
            Notification n2 = notifications.get(notifications.size() - 2);
            binding.news2.setText(n2.getContent() + n2.getDate());
        }
    }


    private void initListener() {
        binding.rlNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setShowNews(false);
                presenter.step2Fragment("serviceRemainder");
            }
        });

        binding.phoneRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.ykkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "功能尚未开放,敬请期待", Toast.LENGTH_SHORT).show();
            }
        });

        binding.phoneRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MobileRechargeFragment mobileRechargeFragment = new MobileRechargeFragment();
                presenter.step2Fragment(mobileRechargeFragment,"mobileRecharge");
            }
        });

    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {
        initView();
        binding.setShowNews(true);
    }


}
