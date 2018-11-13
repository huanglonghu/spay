package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.bean.BankCard;
import com.example.godcode.bean.BindBankCard;
import com.example.godcode.databinding.FragmentBankcardBinding;
import com.example.godcode.databinding.LayoutEditProductpriceBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.adapter.BankCardListAdaPter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.DeleteBank;
import com.example.godcode.ui.view.EtItemDialog;
import com.google.gson.Gson;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BankCardFragment extends BaseFragment implements EtItemDialog.EtResponse {
    private FragmentBankcardBinding binding;
    private boolean isPrepared;
    private View view;
    private List<BankCard.ResultBean> result;
    private EtItemDialog etItemDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        initData();
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bankcard, container, false);
            binding.setPresenter(presenter);
            isPrepared = true;
            view = binding.getRoot();
            binding.bankcardToolbar.title.setText("银行卡");
            initView();
            lazyLoad();
            initListener();
        }
        return view;
    }

    private void initData() {
        querryBankCard();
    }

    private void querryBankCard() {
        HttpUtil.getInstance().getBankCardsByUserID(Constant.userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                bankCardStr -> {
                    BankCard bankCard = new Gson().fromJson(bankCardStr, BankCard.class);
                    result = bankCard.getResult();
                    BankCardListAdaPter bankCardListAdaPter = new BankCardListAdaPter(activity, result, this);
                    binding.lvBankcard.setAdapter(bankCardListAdaPter);
                }, throwable -> {
                }
        );
    }

    private void initListener() {
        binding.addBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证是否有支付密码
                HttpUtil.getInstance().hasPsd(Constant.userId).subscribe(
                        str -> {
                            if (str.contains("payPass")) {
                                CheckPayPsdFragment checkPayPsdFragment = new CheckPayPsdFragment();
                                checkPayPsdFragment.initData(2);
                                presenter.step2Fragment(checkPayPsdFragment, "checkPsd");
                            } else {
                                SetPayPsdFragment setPayPsdFragment = new SetPayPsdFragment();
                                setPayPsdFragment.initData(2);
                                presenter.step2Fragment(setPayPsdFragment, "setPayPsd");
                            }
                        }
                );
            }
        });
    }

    public void initView() {
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
    }

    @Override
    public void refreshData() {
        querryBankCard();
    }


    public void clickBank(int bindType, int position) {
        switch (bindType) {
            case 1://待审核
                break;
            case 2://已审核
                etItemDialog = new EtItemDialog(activity, "请输入该银行卡绑定手机号收取的信息金额", "金额","",2);
                etItemDialog.setEtResponse(this);
                etItemDialog.setPosition(position);
                etItemDialog.show();
                break;
            case 3://已绑定
                deleteBank(position);
                break;
            case 4://审核失败
                //点击重新添加
                deleteBank(position);
                break;
        }

    }

    private void deleteBank(int position) {
        BankCard.ResultBean resultBean = result.get(position);
        int bankID = resultBean.getId();
        String bankCardNumber = resultBean.getBankCardNumber();
        String num = bankCardNumber.substring(bankCardNumber.length() - 4, bankCardNumber.length());
        DeleteBank deleteBank = new DeleteBank(activity, num, bankID, this);
        deleteBank.show();
    }

    @Override
    public void hanlderEt(String str,int position) {
        double value= Double.parseDouble(str);
        BindBankCard bindBankCard = new BindBankCard();
        bindBankCard.setMoney(value);
        bindBankCard.setBankCardId(result.get(position).getId());
        HttpUtil.getInstance().bindBankCard(bindBankCard).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                bindBankCardStr -> {
                    Toast.makeText(activity, "绑定成功", Toast.LENGTH_SHORT).show();
                    etItemDialog.dismiss();
                    querryBankCard();
                }
        );
    }
}
