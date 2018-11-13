package com.example.godcode.ui.view;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.godcode.R;
import com.example.godcode.databinding.LayoutInputPsdBinding;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.utils.FormatCheckUtil;
import com.example.godcode.utils.LogUtil;


public class PsdPopupWindow extends PopupWindow {

    private LayoutInputPsdBinding binding;
    private Activity activity;
    private static PsdPopupWindow defaultInstance;

    public static PsdPopupWindow getInstance(Activity activity) {
        PsdPopupWindow psdPopupWindow = defaultInstance;
        if (defaultInstance == null) {
            synchronized (PsdPopupWindow.class) {
                if (defaultInstance == null) {
                    psdPopupWindow = new PsdPopupWindow(activity);
                    defaultInstance = psdPopupWindow;
                }
            }
        }
        return psdPopupWindow;
    }

    private PsdPopupWindow(Activity activity) {
        this.activity = activity;
        binding = DataBindingUtil.inflate(activity.getLayoutInflater(), R.layout.layout_input_psd, null, false);
        binding.txPayPsdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!keyBoard.isShowing()) {
                    keyBoard.show(view);
                }
            }
        });
        binding.closeInputDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        setContentView(binding.getRoot());
        setWidth(Presenter.getInstance().getWindowWidth() - 100);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(false);
        setBackgroundDrawable(new BitmapDrawable());
    }


    private View view;

    public void show(String name, double money, View view, KeyBoard keyBoard) {
        this.keyBoard = keyBoard;
        this.view = view;
        binding.setName(name);
        binding.money.setText(FormatCheckUtil.getInstance().get2double(money));
        showAtLocation(view, Gravity.TOP, 0, 300);
        keyBoard.show(view);
        keyBoard.setRefreshPsd(binding.txPayPsdView);
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.6f;
        activity.getWindow().setAttributes(lp);
    }

    private KeyBoard keyBoard;

    public void exit() {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1f;
        activity.getWindow().setAttributes(lp);
        binding.txPayPsdView.setPsLength(0);
        if (keyBoard != null) {
            keyBoard.clearPsd();
            keyBoard.dismiss();

        }
        dismiss();
    }

    public void clear() {
        keyBoard.clearPsd();
        binding.txPayPsdView.setPsLength(0);
    }

}
