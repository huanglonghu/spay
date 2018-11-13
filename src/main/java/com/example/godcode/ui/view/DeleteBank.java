package com.example.godcode.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.fragment.deatailFragment.BankCardFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/24.
 */

public class DeleteBank extends AlertDialog {
    public DeleteBank(@NonNull Context context, String num, int id, BankCardFragment bankCardFragment) {
        super(context);

        setMessage("是否解除绑定尾号为" + num + "银行卡?");

        setCancelable(false);

        setButton(AlertDialog.BUTTON_NEGATIVE, "否", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        setButton(AlertDialog.BUTTON_POSITIVE, "是", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                HttpUtil.getInstance().deleteBankCard(id).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread())
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
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
    }
}
