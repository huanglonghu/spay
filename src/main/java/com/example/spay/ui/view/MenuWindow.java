package com.example.spay.ui.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.example.spay.R;
import com.example.spay.databinding.LayoutMainPopupBinding;
import com.example.spay.presenter.Presenter;
public class MenuWindow extends PopupWindow {
    private View root;
    public MenuWindow(Context context) {
        root = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_main_popup, null, false).getRoot();
        setHeight(Presenter.getInstance().getWidowHeight()/3);
        setContentView(root);
        setFocusable(true);
        setWidth(Presenter.getInstance().getWindowWidth()/3);
        setBackgroundDrawable(context.getResources().getDrawable(android.R.color.transparent));
    }


    public void show(View view) {
        showAsDropDown(view,0,0, Gravity.RIGHT);
    }

    public LayoutMainPopupBinding getBinding1() {
        LayoutMainPopupBinding binding = DataBindingUtil.findBinding(root);
        return binding;
    }


}
