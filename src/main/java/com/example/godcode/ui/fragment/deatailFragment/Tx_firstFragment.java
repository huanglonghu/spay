package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.BankCard;
import com.example.godcode.bean.Tx;
import com.example.godcode.databinding.FragmentTxFirstBinding;
import com.example.godcode.greendao.entity.User;
import com.example.godcode.greendao.option.UserOption;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.ui.view.MyEditText;
import com.example.godcode.ui.view.PsdPopupWindow;
import com.example.godcode.utils.FormatCheckUtil;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.PayPsdSetting;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Tx_firstFragment extends BaseFragment implements KeyBoard.PsdLengthWatcher, MyEditText.MoneyValueListener {

    private FragmentTxFirstBinding binding;
    private View view;
    private double money;
    private TxFragment parentFragment;
    private BankCard.ResultBean resultBean;
    private double rate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            parentFragment = (TxFragment) getParentFragment();
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tx_first, container, false);
            binding.etMoney.setMoneyValueListener(this);
            binding.setBalance(parentFragment.getBalance());
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
        binding.btnTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankList.size() == 0) {
                    presenter.step2Fragment("bankCard");
                } else {
                    money = Double.parseDouble(binding.etMoney.getText().toString());
                    if (money > parentFragment.getBalance()) {
                        Toast.makeText(activity, "超出本次可提现金额", Toast.LENGTH_SHORT).show();
                    } else {
                        PayPsdSetting.getInstance().isPayPsdSet("提现", money, view, Tx_firstFragment.this, 2);
                    }
                }
            }
        });


        binding.txAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etMoney.setText((parentFragment.getBalance() - (rate * parentFragment.getBalance() * 0.01) + ""));
            }
        });

        binding.rlBankSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankList.size() > 0) {
                    BankSelectFragment bankSelectFragment = new BankSelectFragment();
                    presenter.step2Fragment(bankSelectFragment, "bankSelect");
                } else {
                    presenter.step2Fragment("bankCard");
                }
            }
        });
    }

    public void initView() {
        rate = parentFragment.getWithdrawRate();
        LogUtil.log("--------rate------"+rate);
        binding.txRate.setText("(收取" +FormatCheckUtil.getInstance().get2double(rate*100) + "%服务费)");
        binding.setTx(true);
        binding.useBalance.setText("可用余额" + parentFragment.getBalance());
    }

    private ArrayList<BankCard.ResultBean> bankList = new ArrayList<>();

    private void getBankList() {
        HttpUtil.getInstance().getBankCardsByUserID(Constant.userId).subscribe(
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
                        binding.txBank.setText("添加新卡提现");
                    } else {
                        resultBean = bankList.get(0);
                        String bankName = resultBean.getBankName();
                        String last4Num = FormatCheckUtil.getInstance().getLast4Num(resultBean.getBankCardNumber());
                        binding.txBank.setText(bankName + "(" + last4Num + ")");
                    }
                }
        );
    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void toCheck(String psd) {
        Tx tx = new Tx();
        tx.setPassword(psd);
        Tx.PutMoneyDtoBean putMoneyDtoBean = new Tx.PutMoneyDtoBean();
        putMoneyDtoBean.setFeeType("CNY");
        putMoneyDtoBean.setFK_UserID(Constant.userId);
        putMoneyDtoBean.setSumTotal(money);
        putMoneyDtoBean.setFK_BankCardID(resultBean.getId());
        tx.setPutMoneyDto(putMoneyDtoBean);
        HttpUtil.getInstance().tx(tx).subscribe(
                txStr -> {
                    if (txStr.contains("\"success\":false")) {
                        PsdPopupWindow.getInstance(activity).clear();
                        Toast.makeText(activity, "密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        PsdPopupWindow.getInstance(activity).exit();
                        parentFragment.setMoney(Double.parseDouble(binding.etMoney.getText().toString()));
                        parentFragment.setBank(binding.txBank.getText().toString());
                        parentFragment.toggle(1);
                    }
                }
        );
    }

    @Override
    public void onKeyDown() {
        if (PsdPopupWindow.getInstance(activity) != null && PsdPopupWindow.getInstance(activity).isShowing()) {
            PsdPopupWindow.getInstance(activity).exit();
        } else {
            presenter.back();
        }

    }

    @Override
    public void refreshData() {
    }

    @Override
    public void setEnable(boolean enable, double money) {
        if (money <= parentFragment.getBalance() - money * rate / 100) {
            binding.setTx(true);
            if (enable) {
                binding.btnTx.setEnabled(true);
                if (money <= 100) {
                    binding.useBalance.setText("服务费" + (100*rate) + "元");
                } else {
                    binding.useBalance.setText("服务费" + FormatCheckUtil.getInstance().get2double(rate * money) + "元");
                }

            } else {
                binding.btnTx.setEnabled(false);
                binding.useBalance.setText("可用余额" + (parentFragment.getBalance() - money) + "");
            }
        } else {
            binding.setTx(false);
            binding.btnTx.setEnabled(false);
            binding.useBalance.setText("超过可用余额");
        }

    }


}
