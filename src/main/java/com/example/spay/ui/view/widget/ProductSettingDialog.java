package com.example.spay.ui.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.SeekBar;
import com.example.spay.databinding.LayoutProductSettingBinding;
import com.example.spay.R;
import com.example.spay.http.InputFilterMinMax;
import com.example.spay.interface_.ProductSettingStrategy;
import com.example.spay.presenter.Presenter;
import com.example.spay.utils.Dp2Px;

public class ProductSettingDialog extends Dialog {
    private LayoutProductSettingBinding binding;
    private String title;
    private Context context;
    private int type;
    private ProductSettingStrategy strategy;
    private int value;

    public ProductSettingDialog(Builder builder) {
        super(builder.context);
        this.context = builder.context;
        this.type = builder.type;
        this.title = builder.title;
        this.value = builder.value;
        this.strategy = builder.strategy;
        initView();
        initListener();
    }


    public void show() {
        super.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth() * 8 / 10;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.6f;
        getWindow().setAttributes(layoutParams);
    }

    private void initView() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.layout_product_setting, null, false);
        setContentView(binding.getRoot());
        binding.setTitle(title);
        binding.setType(type);
        initValue(value);
    }

    private void initListener() {
        binding.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        binding.etCoin.setFilters(new InputFilter[]{new InputFilterMinMax("0", "255")});
        binding.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = 0;
                switch (type) {
                    case 1:
                        value = binding.seekbar.getProgress();
                        break;
                    case 2:
                        value = binding.seekbar.getProgress();
                        break;
                    case 3:
                        if (!TextUtils.isEmpty(binding.etCoin.getText().toString())) {
                            value = Integer.parseInt(binding.etCoin.getText().toString());
                        }
                        break;
                }
                strategy.valueSetting(value);
                dismiss();
            }
        });
    }


    private void initValue(int value) {
        int width = Presenter.getInstance().getWindowWidth() * 8 / 10 - Dp2Px.dp2px(context, 30);
        float moveStep = (float) ((width / 10) * 0.9);
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.moveText.layout((int) (progress * moveStep), 20, width, 80);
                binding.moveText.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        if (type == 1) {
            binding.seekbar.setProgress(value);
        } else if (type == 2) {
            binding.seekbar.setProgress(value);
        } else if (type == 3) {
            binding.etCoin.setText(value + "");
        }
    }


    public static class Builder {
        private String title;
        private Context context;
        private int type;
        private ProductSettingStrategy strategy;
        private int value;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder value(int value) {
            this.value = value;
            return this;
        }


        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }

        public Builder strategy(ProductSettingStrategy strategy) {
            this.strategy = strategy;
            return this;
        }

        public ProductSettingDialog build() {
            return new ProductSettingDialog(this);
        }
    }


}
