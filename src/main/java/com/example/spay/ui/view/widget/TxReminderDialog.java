package com.example.spay.ui.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.spay.R;
import com.example.spay.databinding.LayoutReminderTxBinding;
import com.example.spay.interface_.Strategy;
import com.example.spay.presenter.Presenter;

public class TxReminderDialog extends Dialog {
    private Context context;
    private Strategy strategy;

    public TxReminderDialog(Context context, Strategy strategy) {
        super(context);
        this.context = context;
        this.strategy = strategy;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
    }

    public void show() {
        super.show();
        LayoutReminderTxBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_reminder_tx, null, false);
        binding.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strategy.sure();
            }
        });
        setContentView(binding.getRoot());
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth() * 8 / 10;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
    }
}
