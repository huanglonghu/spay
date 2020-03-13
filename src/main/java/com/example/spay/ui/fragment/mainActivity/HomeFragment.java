package com.example.spay.ui.fragment.mainActivity;

import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.qzs.voiceannouncementlibrary.VoiceUtils;
import com.example.spay.R;
import com.example.spay.bean.DivideIncome;
import com.example.spay.databinding.FragmentHomeBinding;
import com.example.spay.greendao.entity.Notification;
import com.example.spay.greendao.option.NotificationOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.EtStrategy;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.service.NetStateReceiver;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.fragment.deatailFragment.MobileRechargeFragment;
import com.example.spay.ui.view.widget.NetStateDialog;
import com.example.spay.utils.FormatUtil;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.LogUtil;
import com.google.gson.Gson;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
            binding.setPresenter(presenter);
            initListener();
            isMakeCode();
            RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }
                @Override
                public void onNext(RxEvent rxEvent) {
                    int eventType = rxEvent.getEventType();
                    if (eventType == EventType.EVENTTYPE_REFRESH_NOTIFICATION || eventType == EventType.EVENTTYPE_BALANCE_CHANGE || eventType == EventType.EVENTTYPE_ADDFRIEND) {
                        refreshNotification();
                    }
                    if(eventType ==EventType.EVENTTYPE_DIVIDE_MSG||eventType == EventType.EVENTTYPE_BALANCE_CHANGE ){
                        refreshDivideIncome();
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });

        }
        initBalanceMsg();
        initView();
        return binding.getRoot();
    }

    private void initBalanceMsg() {
        binding.balance.setText(FormatUtil.getInstance().get2double(Constant.balances));
    }


    public void refreshDivideIncome() {
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
                    refreshDivideIncome();
                } else {
                    NetStateDialog netStateDialog = new NetStateDialog(getContext());
                    netStateDialog.show();
                }
            }
        });
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        getActivity().registerReceiver(netStateReceiver, intentFilter);

        binding.ykkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity, "功能尚未开放,敬请期待", Toast.LENGTH_SHORT).show();
            }
        });


        binding.rlNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.setShowNews(false);
                presenter.step2Fragment("serviceRemainder");
            }
        });


    }

    @Override
    protected void lazyLoad() {
    }


    public void refreshNotification() {
        initView();
        binding.setShowNews(true);
    }


    public void isMakeCode() {
        HttpUtil.getInstance().getUserMsgById(Constant.userId).subscribe(
                personalStr -> {
                    com.example.spay.bean.User mine = new Gson().fromJson(personalStr, com.example.spay.bean.User.class);
                    com.example.spay.bean.User.ResultBean result = mine.getResult();
                    if (result != null) {
                        boolean makeCode = result.isMakeCode();
                        binding.setIsMakeCode(makeCode);
                    }
                }
        );
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isVisible()) {
            isMakeCode();
            refreshDivideIncome();
        }
    }


}
