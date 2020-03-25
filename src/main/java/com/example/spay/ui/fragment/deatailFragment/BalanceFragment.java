package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spay.R;
import com.example.spay.bean.BalanceResponse;
import com.example.spay.bean.DivideIncome;
import com.example.spay.databinding.FragmentBalanceBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.ui.activity.MainActivity;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.utils.DateUtil;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.StringUtil;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class BalanceFragment extends BaseFragment {
    private FragmentBalanceBinding binding;
    private View view;
    private double balances;
    private DecimalFormat decimalFormat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            MainActivity activity = (MainActivity) this.activity;
            RxBus.getInstance().toObservable(RxEvent.class).subscribe(new Observer<RxEvent>() {
                @Override
                public void onSubscribe(Disposable disposable) {

                }

                @Override
                public void onNext(RxEvent rxEvent) {
                    int eventType = rxEvent.getEventType();
                    if (eventType == EventType.EVENTTYPE_BALANCE_CHANGE || eventType == EventType.EVENTTYPE_DIVIDE_MSG) {
                        querryDivide();
                    }
                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }
            });
            initView();
            initListener();
        }
        qurryBalance();
        querryDivide();
        return view;
    }


    private void querryDivide() {
        HttpUtil.getInstance().getDivideIncome(Constant.userId).subscribe(
                divideStr -> {
                    DivideIncome divideIncome = GsonUtil.getInstance().fromJson(divideStr, DivideIncome.class);
                    DivideIncome.ResultBean result = divideIncome.getResult();
                    double todayMoney = result.getToDayMoney();
                    double yesterDayMoney = result.getYesterDayMoney();
                    double balances = result.getBalances();
                    binding.balance.setText(decimalFormat.format(balances));
                }
        );
    }

    private void initListener() {

        binding.tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxFragment txFragment = new TxFragment();
                presenter.step2Fragment(txFragment, "tx");
            }
        });

        binding.recharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.step2Fragment("gathering");
            }
        });

        binding.balanceBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.step2Fragment("bankCard");
            }
        });

        binding.balanceZz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TransferAccountFragment transferAccountFragment = new TransferAccountFragment();
                transferAccountFragment.setType(3);
                presenter.step2Fragment(transferAccountFragment, "transferAccount");
            }
        });

    }

    public void initView() {
        decimalFormat = new DecimalFormat("0.00");
        String title = StringUtil.getString(activity, R.string.balance);
        binding.balanceToolbar.title.setText(title);
        binding.balance.setText(decimalFormat.format(balances));
        String today = DateUtil.getInstance().getToday();
        String yesterDaty = DateUtil.getInstance().getYesterDaty();
        binding.setToday(today);
        binding.setYesterDay(yesterDaty);
    }


    public void qurryBalance() {
        HttpUtil.getInstance().querryBalance(Constant.userId).subscribe(
                balanceStr -> {
                    BalanceResponse balanceResponse = new Gson().fromJson(balanceStr, BalanceResponse.class);
                    BalanceResponse.ResultBean result = balanceResponse.getResult();
                    if (result != null && binding != null) {
                        balances = result.getBalances();
                        binding.balance.setText(decimalFormat.format(balances));
                    }
                }
        );
    }

    @Override
    protected void lazyLoad() {
    }

}
