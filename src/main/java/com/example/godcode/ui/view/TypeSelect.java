package com.example.godcode.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.NumberPicker;

import com.example.godcode.R;
import com.example.godcode.databinding.LayoutBankTypeSelectBinding;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.utils.LogUtil;

public class TypeSelect extends Dialog {

    private LayoutBankTypeSelectBinding binding;

    public TypeSelect(@NonNull Context context, String[] data) {
        super(context, R.style.dialog3);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_bank_type_select, null, false);
        binding.typePicker.setDisplayedValues(data);
        binding.typePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        binding.typePicker.setMinValue(0);
        binding.typePicker.setMaxValue(data.length - 1);
        binding.typePicker.setWrapSelectorWheel(false);
        binding.cancelBankType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.sureBankSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                int value = binding.typePicker.getValue();
                if (selectResponse != null) {
                    selectResponse.select(value);
                }
            }
        });
        setCancelable(false);
        setCanceledOnTouchOutside(true);
        setContentView(binding.getRoot());
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.dimAmount = 0.6f;
        getWindow().setWindowAnimations(R.style.popupStyle);
        getWindow().setAttributes(layoutParams);
    }

    public interface SelectResponse {
        void select(int pos);
    }

    private SelectResponse selectResponse;

    public void setSelectResponse(SelectResponse selectResponse) {
        this.selectResponse = selectResponse;
    }
}
