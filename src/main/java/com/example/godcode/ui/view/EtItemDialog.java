package com.example.godcode.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.godcode.R;
import com.example.godcode.databinding.LayoutEditProductpriceBinding;

public class EtItemDialog extends Dialog implements View.OnClickListener {


    private LayoutEditProductpriceBinding binding;
    private Context context;
    private String hint;
    private int type;

    public EtItemDialog(@NonNull Context context, String title, String hint, String precent, int type) {
        super(context);
        this.context = context;
        this.hint = hint;
        this.type = type;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_edit_productprice, null, false);
        binding.setTitle(title);
        binding.setHint(hint);
        binding.precent.setText(precent);
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.commit.setOnClickListener(this);
        if (type == 1) {
            binding.etValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            binding.etValue.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        setContentView(binding.getRoot());
        setCancelable(false);
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setWindowAnimations(R.style.popupStyle);
        getWindow().setAttributes(layoutParams);
    }

    @Override
    public void onClick(View v) {
        String valueStr = binding.etValue.getText().toString();
        int value = 0;
        if (!TextUtils.isEmpty(valueStr)) {
            etResponse.hanlderEt(valueStr, position);
        } else {
            Toast.makeText(context, "请输入" + hint, Toast.LENGTH_SHORT).show();
        }
    }


    public interface EtResponse {
        void hanlderEt(String value, int position);
    }

    public void setEtResponse(EtResponse etResponse) {
        this.etResponse = etResponse;
    }

    private EtResponse etResponse;

    private int position;


}
