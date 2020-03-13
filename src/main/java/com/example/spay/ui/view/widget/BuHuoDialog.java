package com.example.spay.ui.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.example.spay.R;
import com.example.spay.databinding.LayoutBuhuoDialogBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.http.InputFilterMinMax;
import com.example.spay.interface_.EtStrategy;
import com.example.spay.presenter.Presenter;
import com.example.spay.utils.ToastUtil;

import java.util.ArrayList;

public class BuHuoDialog extends Dialog {
    private LayoutBuhuoDialogBinding binding;
    private LayoutInflater inflater;
    private Context context;
    private EtStrategy etStragety;
    private String commodityRoads;
    private ArrayList<Long> list;


    public BuHuoDialog(@NonNull Context context, ArrayList<Long> list,String commodityRoads, EtStrategy etStragety) {
        super(context);
        this.context = context;
        this.commodityRoads=commodityRoads;
        this.etStragety=etStragety;
        this.list=list;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setCancelable(false);
        initView(context);
    }

    private void initView(Context context) {
        inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_buhuo_dialog, null, false);
        binding.hdName.setText(commodityRoads);
        binding.kcNum.setFilters(new InputFilter[]{new InputFilterMinMax("0", "99")});
        binding.hdName.setOnClickListener(new View.OnClickListener() {
            boolean flag=true;
            @Override
            public void onClick(View v) {
                if(flag){
                    flag = false;
                    binding.hdName.setEllipsize(null); // 展开
                    binding.hdName.setSingleLine(false);//将TextView设置为不是单行显示的
                }else{
                    flag = true;
                    binding.hdName.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                    binding.hdName.setSingleLine(true);//收缩的时候设置为是单行显示的
                }
            }
        });
        initListen();
        getWindow().setWindowAnimations(R.style.popupStyle);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(true);
    }


    private void initListen() {
        binding.cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        binding.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = binding.kcNum.getText().toString();
                if(TextUtils.isEmpty(s)){
                    ToastUtil.getInstance().showToast("请输出库存数",2000,context);
                    return;
                }
                int costNumber = Integer.parseInt(s);
                HttpUtil.getInstance().buhuo(costNumber,list).subscribe(
                        a->{
                            dismiss();
                            etStragety.etComplete();
                        }
                );

            }
        });
    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth() * 8 / 10;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setAttributes(layoutParams);
    }


}
