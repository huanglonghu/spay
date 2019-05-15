package com.example.godcode.ui.view.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.WindowManager;
import com.example.godcode.interface_.Strategy;
import com.example.godcode.presenter.Presenter;

public class DeleteDialog extends AlertDialog {

    public DeleteDialog(Context context, String title, Strategy strategy) {
        super(context);
        setMessage(title);
        setCancelable(false);
        setButton(AlertDialog.BUTTON_NEGATIVE, "否", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        setButton(AlertDialog.BUTTON_POSITIVE, "是", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                strategy.sure();
            }
        });
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.height = Presenter.getInstance().getWidowHeight() / 4;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
    }





}
