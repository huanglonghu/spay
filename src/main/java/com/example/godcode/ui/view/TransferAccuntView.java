package com.example.godcode.ui.view;

import android.app.Activity;
import android.app.Dialog;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.BankCard;
import com.example.godcode.bean.Transfer;
import com.example.godcode.databinding.LayoutPaySelectBinding;
import com.example.godcode.databinding.LayoutTransferaccountBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.adapter.PayViewPageAdapter;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.fragment.deatailFragment.TransferAccountDetailFragment;
import com.example.godcode.utils.FormatUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TransferAccuntView extends Dialog {
    private LayoutInflater layoutInflater;
    private LayoutTransferaccountBinding binding;
    private Activity activity;
    private LayoutPaySelectBinding binding1;
    private ClickSureListener clickSureListener;
    private double money;

    public TransferAccuntView(@NonNull Activity activity, double money, ClickSureListener clickSureListener) {
        super(activity, R.style.dialog2);
        this.activity = activity;
        this.money = money;
        this.clickSureListener = clickSureListener;
        layoutInflater = LayoutInflater.from(activity);
        initView();
    }

    private void initView() {
        setCancelable(false);
        View view = View.inflate(activity, R.layout.layout_pay, null);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.pay_container);
        binding1 = DataBindingUtil.inflate(layoutInflater, R.layout.layout_pay_select, null, false);

        binding1.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        binding1.lvPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = bankList.get(position);
                viewPager.setCurrentItem(0);
                binding.payWay.setText(s);
            }
        });

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_transferaccount, null, false);

        binding.closeInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                clickSureListener.clickSure();
            }
        });
        binding.transferMoney.setText(FormatUtil.getInstance().get2double(money));
        ArrayList<View> views = new ArrayList<>();
        views.add(binding.getRoot());
        views.add(binding1.getRoot());
        PayViewPageAdapter payViewPageAdapter = new PayViewPageAdapter(views);
        viewPager.setAdapter(payViewPageAdapter);
        getWindow().setContentView(view);
        getWindow().setWindowAnimations(R.style.popupStyle);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private ArrayList<String> bankList = new ArrayList<>();

    private void requestBankCard() {
        if (bankList.size() == 0) {
            HttpUtil.getInstance().getBankCardsByUserID(Constant.userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    bankListStr -> {
                        BankCard bankCard = new Gson().fromJson(bankListStr, BankCard.class);
                        List<BankCard.ResultBean> result = bankCard.getResult();
                        for (int i = 0; i < result.size(); i++) {
                            BankCard.ResultBean resultBean = result.get(i);
                            if (resultBean.getBindType() == 3) {
                                bankList.add(resultBean.getBankName());
                            }
                        }
                        bankList.add("余额");
                        ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity, R.layout.item_bank, bankList);
                        binding1.lvPay.setAdapter(stringArrayAdapter);
                    }, throwable -> {
                    }
            );
        }

    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth();
        layoutParams.height = 1 * (Presenter.getInstance().getWidowHeight()) / 2;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
    }
}
