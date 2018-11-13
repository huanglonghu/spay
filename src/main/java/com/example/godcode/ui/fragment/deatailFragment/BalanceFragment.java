package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.bean.BalanceResponse;
import com.example.godcode.bean.DivideIncome;
import com.example.godcode.databinding.FragmentBalanceBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.GsonUtil;
import com.google.gson.Gson;

import java.text.DecimalFormat;


public class BalanceFragment extends BaseFragment {
    private FragmentBalanceBinding binding;
    private View view;
    private double balances;
    private DecimalFormat decimalFormat;
    private double withdrawRate;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_balance, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
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
                    double todayMoney = divideIncome.getResult().getToDayMoney();
                    double yesterDayMoney = divideIncome.getResult().getYesterDayMoney();
                    binding.balanceDivide.incomeToday.setText(decimalFormat.format(todayMoney));
                    binding.balanceDivide.incomeYesterDay.setText(decimalFormat.format(yesterDayMoney));
                }
        );
    }

    private void initListener() {

        binding.tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TxFragment txFragment = new TxFragment();
                Bundle bundle = new Bundle();
                bundle.putDouble("balance", balances);
                bundle.putDouble("withdrawRate",withdrawRate);
                txFragment.setArguments(bundle);
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
        binding.balanceToolbar.title.setText("余额");
        binding.balance.setText(decimalFormat.format(balances));
        String today = DateUtil.getInstance().getToday();
        String yesterDaty = DateUtil.getInstance().getYesterDaty();
        binding.setToday(today);
        binding.setYesterDay(yesterDaty);
    }

    public double getWithdrawRate() {
        return withdrawRate;
    }

    public void setWithdrawRate(double withdrawRate) {
        this.withdrawRate = withdrawRate;
    }

    public void qurryBalance() {
        HttpUtil.getInstance().querryBalance(Constant.userId).subscribe(
                balanceStr -> {
                    BalanceResponse balanceResponse = new Gson().fromJson(balanceStr, BalanceResponse.class);
                    BalanceResponse.ResultBean result = balanceResponse.getResult();
                    if(result!=null){
                        balances = result.getBalances();
                        withdrawRate = result.getWithdrawRate();
                        binding.balance.setText(decimalFormat.format(balances));
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
