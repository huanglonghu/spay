package com.example.spay.ui.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.example.spay.R;
import com.example.spay.databinding.LayoutKeyboardBinding;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.utils.LogUtil;

public class KeyBoard extends PopupWindow {
    private ClickSureListener clickSureListener;


    public KeyBoard(Context context, ClickSureListener clickSureListener) {
        this.clickSureListener = clickSureListener;
        LayoutKeyboardBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_keyboard, null, false);
        binding.setKeyBoard(this);
        setContentView(binding.getRoot());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(false);
        setFocusable(false);
        setBackgroundDrawable(new BitmapDrawable());
    }


    public void show(View view) {
        getContentView().measure(0, 0);
        int height = getContentView().getMeasuredHeight();
        showAtLocation(view, Gravity.LEFT | Gravity.BOTTOM, 0, -height);
    }


    public interface RefreshPsd {
        void setPsLength(int length);
    }

    public void setRefreshPsd(RefreshPsd refreshPsd) {
        this.refreshPsd = refreshPsd;
    }

    private RefreshPsd refreshPsd;

    private String[] psdArray = new String[6];

    private int currentIndex;

    public void closeKeyBoard() {
        dismiss();
    }

    public void delete() {
        LogUtil.log("=============currentIndex============"+currentIndex);
        if (currentIndex > 0) {
            psdArray[currentIndex - 1] = null;
            currentIndex--;
            refreshPsd.setPsLength(currentIndex);
        }
    }

    public void clickItem(int num) {
        LogUtil.log("clicitem=============currentIndex============"+currentIndex);
        if (currentIndex < 6) {
            psdArray[currentIndex] = String.valueOf(num);
            currentIndex++;
            refreshPsd.setPsLength(currentIndex);
            if (currentIndex == 6) {
                //验证密码 进入绑定银行卡界面
                //通知fragment
                clickSureListener.checkPwd(getPsd());
            }
        }
    }


    public String getPsd() {
        StringBuffer psd = new StringBuffer();
        for (int i = 0; i < psdArray.length; i++) {
            psd.append(psdArray[i]);
        }
        return psd.toString();
    }

    public void clearPsd() {
        currentIndex=0;
        for (int i = 0; i < psdArray.length; i++) {
            psdArray[i] = "";
        }
    }

}
