package com.example.spay.ui.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.bean.BankCard;
import com.example.spay.databinding.LayoutBankConfigBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.fragment.deatailFragment.BankCardFragment;
import com.example.spay.ui.fragment.deatailFragment.EditBankCardFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/24.
 */

public class BankConfigDialog extends Dialog {
    private BankCard.ResultBean bean;
    public BankConfigDialog(@NonNull Context context, BankCard.ResultBean bean, BankCardFragment bankCardFragment) {
        super(context);
        String bankCardNumber = bean.getBankCardNumber();
        String num = bankCardNumber.substring(bankCardNumber.length() - 4, bankCardNumber.length());
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.bean=bean;
        LayoutInflater inflater = LayoutInflater.from(context);
        LayoutBankConfigBinding binding = DataBindingUtil.inflate(inflater, R.layout.layout_bank_config, null, false);
        binding.title.setText("银行卡尾号:"+num);
        setCancelable(false);
        setContentView(binding.getRoot());
        binding.deleteBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                HttpUtil.getInstance().deleteBankCard(bean.getId()).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                deletBankStr -> {
                                    Toast.makeText(context, "成功解除绑定", Toast.LENGTH_SHORT).show();
                                    //刷新界面
                                    bankCardFragment.refreshData();
                                }, throwable -> {
                                }
                        );
            }
        });

        binding.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.editBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditBankCardFragment editBankCardFragment = new EditBankCardFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("bankCardBean",bean);
                editBankCardFragment.setArguments(bundle);
                Presenter.getInstance().step2Fragment(editBankCardFragment,"editBankCard");
                dismiss();
            }
        });


    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.3f;
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.getDecorView().setPadding(20, 0, 20, 20);
        getWindow().setAttributes(layoutParams);
    }
}
