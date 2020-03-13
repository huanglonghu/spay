package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spay.R;
import com.example.spay.bean.BankCard;
import com.example.spay.databinding.FragmentRechargeSecondBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Recharge_SecondFragment extends BaseFragment {

    private FragmentRechargeSecondBinding binding;
    private View view;
    private RechargeFragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            parentFragment = (RechargeFragment) getParentFragment();
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recharge_second, container, false);
            binding.tvMoney.setText(String.valueOf(parentFragment.getMoney()));
            view = binding.getRoot();
            initData();
            initView();
            initListener();
        }
        return view;
    }

    private void initData() {
        getBankList();
    }

    private void initListener() {
        binding.tvRechargeWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankList.size() > 0) {
                    BankSelectFragment bankSelectFragment = new BankSelectFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("fragmentType", 1);
                    bankSelectFragment.setArguments(bundle);
                    presenter.step2Fragment(bankSelectFragment, "bankSelect");
                } else {
                    presenter.step2Fragment("bankCard");
                }
            }
        });

        binding.btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankList.size() == 0) {
                    //去设置绑定银行卡
                    presenter.step2Fragment("bankCard");
                } else {
                 //   PayPwdSetting.getInstance().isPayPsdSet("充值",parentFragment.getMoney(),view,Recharge_SecondFragment.this,2);
                }
            }
        });
    }


    private ArrayList<BankCard.ResultBean> bankList = new ArrayList<>();

    private void getBankList() {
        HttpUtil.getInstance().getBankCardsByUserID(Constant.userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                bankListStr -> {
                    BankCard bankCard = new Gson().fromJson(bankListStr, BankCard.class);
                    List<BankCard.ResultBean> result = bankCard.getResult();
                    if (bankList.size() > 0) {
                        bankList.clear();
                    }
                    for (int i = 0; i < result.size(); i++) {
                        BankCard.ResultBean resultBean = result.get(i);
                        if (resultBean.getBindType() == 3) {
                            bankList.add(resultBean);
                        }
                    }
                    if (bankList.size() == 0) {
                        binding.tvRechargeWay.setText("添加银行卡支付");
                    } else {
                        binding.tvRechargeWay.setText(bankList.get(0).getBankName());
                    }
                }, throwable -> {
                }
        );
    }


    public void initView() {

    }



    @Override
    protected void lazyLoad() {

    }

//    @Override
//    public void toCheck(String psd) {
//
//            RechargeBody rechargeBody = new RechargeBody();
//            RechargeBody.PayMoneyBean payMoneyBean = new RechargeBody.PayMoneyBean();
//            payMoneyBean.setFeeType("CNY");
//            payMoneyBean.setFK_UserID(Constant.userId);
//            payMoneyBean.setSumTotal(parentFragment.getMoney());
//            rechargeBody.setPayMoney(payMoneyBean);
//            HttpUtil.getInstance().recharge(rechargeBody).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
//                    rechargeStr -> {
//                        PsdPopupWindow.getInstance(activity).exit();
//                        parentFragment.setBank(binding.tvRechargeWay.getText().toString());
//                        parentFragment.toggle(2);
//                    }
//            );
//
//
//    }




}
