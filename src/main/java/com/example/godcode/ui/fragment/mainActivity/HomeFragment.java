package com.example.godcode.ui.fragment.mainActivity;

import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.DivideIncome;
import com.example.godcode.databinding.FragmentHomeBinding;
import com.example.godcode.greendao.entity.Notification;
import com.example.godcode.greendao.option.NotificationOption;
import com.example.godcode.handler.WebSocketNewsHandler;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.EtStrategy;
import com.example.godcode.observable.WebSocketNewsObservable;
import com.example.godcode.observable.WebSocketNewsObserver;
import com.example.godcode.service.NetStateReceiver;
import com.example.godcode.ui.activity.MainActivity;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.fragment.deatailFragment.MobileRechargeFragment;
import com.example.godcode.ui.view.widget.NetStateDialog;
import com.example.godcode.utils.FormatUtil;
import com.example.godcode.utils.GsonUtil;
import com.example.godcode.utils.LogUtil;

import java.util.List;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            binding.setPresenter(presenter);
            initListener();
            MainActivity activity = (MainActivity) this.activity;
            WebSocketNewsObservable<WebSocketNewsHandler> webSocketNewsObservable = activity.getWebSocketNewsObservable();
            WebSocketNewsObserver<WebSocketNewsHandler> observer = new WebSocketNewsObserver<WebSocketNewsHandler>() {
                @Override
                public void onUpdate(WebSocketNewsObservable<WebSocketNewsHandler> observable, WebSocketNewsHandler data) {
                    int handlerType = data.getHandlerType();
                    if (handlerType == 1 || handlerType == 4 || handlerType == 6) {
                        refreshData();
                        if (handlerType == 6) {
                            initData();
                        }
                    }
                }
            };
            webSocketNewsObservable.register(observer);
        }
        initBalanceMsg();
        initView();
        return binding.getRoot();
    }

    private void initBalanceMsg() {
        binding.balance.setText(FormatUtil.getInstance().get2double(Constant.balances));
        binding.todayDivide.setText(FormatUtil.getInstance().get2double(Constant.toDayMoney));
        binding.yesterdayDivide.setText(FormatUtil.getInstance().get2double(Constant.yesterDayMoney));
    }

    private void initData() {
        HttpUtil.getInstance().getDivideIncome(Constant.userId).subscribe(
                divideStr -> {
                    DivideIncome divideIncome = GsonUtil.getInstance().fromJson(divideStr, DivideIncome.class);
                    DivideIncome.ResultBean result = divideIncome.getResult();
                    Constant.toDayMoney = result.getToDayMoney();
                    Constant.yesterDayMoney = result.getYesterDayMoney();
                    Constant.balances = result.getBalances();
                    initBalanceMsg();
                }
        );

    }


    private void initView() {
        List<Notification> notifications = NotificationOption.getInstance().getAllNotification(Constant.userId);
        if (notifications.size() > 0) {
            Notification n1 = notifications.get(notifications.size() - 1);
            binding.news1.setText(n1.getContent());
        }
        if (notifications.size() > 1) {
            Notification n2 = notifications.get(notifications.size() - 2);
            binding.news2.setText(n2.getContent());
        }
    }


    private void initListener() {
        NetStateReceiver netStateReceiver = new NetStateReceiver(new EtStrategy() {
            @Override
            public void netChange(boolean isConnect) {
                if (isConnect) {
                    initData();
                } else {
                    NetStateDialog netStateDialog = new NetStateDialog(getContext());
                    netStateDialog.show();
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(netStateReceiver, intentFilter);

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
                presenter.step2Fragment(mobileRechargeFragment, "mobileRecharge");
            }
        });

        binding.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean selected = binding.toggle.isSelected();
                binding.toggle.setSelected(!selected);
                if (!selected) {
                    binding.balance.setInputType(129);
                    binding.todayDivide.setInputType(129);
                    binding.yesterdayDivide.setInputType(129);
                } else {
                    binding.balance.setInputType(128);
                    binding.todayDivide.setInputType(128);
                    binding.yesterdayDivide.setInputType(128);
                }
            }
        });

    }

    @Override
    protected void lazyLoad() {
    }


    public void refreshData() {
        initView();
        binding.setShowNews(true);
    }


}
