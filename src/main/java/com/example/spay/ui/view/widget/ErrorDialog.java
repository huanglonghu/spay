package com.example.spay.ui.view.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;
import com.example.spay.presenter.Presenter;

public class ErrorDialog extends AlertDialog {
    private Context context;

    public ErrorDialog(Context context) {
        super(context);
        this.context = context;
        setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
        TextView textView = new TextView(context);
        textView.setPadding(40,40,40,40);
        textView.setText("产品正在升级，请稍后");
        textView.setTextSize(18);
        textView.setGravity(Gravity.CENTER);
        setContentView(textView);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth() * 8 / 10;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
    }

}
