package com.example.spay.ui.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.example.spay.R;
import com.example.spay.bean.CommodityRoadList;
import com.example.spay.databinding.LayoutHuodaoSelectBinding;
import com.example.spay.databinding.LayoutVemBinding;
import com.example.spay.databinding.LayoutVemConfigBinding;
import com.example.spay.databinding.VemConfigAddBinding;
import com.example.spay.databinding.VemConfigEditBinding;
import com.example.spay.interface_.EtStrategy;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.adapter.HuodaoSelectAdapter;
import com.example.spay.utils.ToastUtil;

import java.util.ArrayList;

public class HuodaoSelectDialog extends Dialog {
    private LayoutHuodaoSelectBinding binding;
    private LayoutInflater inflater;
    private Context context;
    private HuodaoSelectAdapter adapter;
    private CommodityRoadList.ResultBean.DataBean[] datas;
    private EtStrategy etStrategy;
    private String productNumber;


    public HuodaoSelectDialog(@NonNull Context context, CommodityRoadList.ResultBean.DataBean[] datas,String productNumber, EtStrategy etStrategy) {
        super(context, R.style.dialog2);
        this.context = context;
        this.datas=datas;
        this.etStrategy=etStrategy;
        this.productNumber=productNumber;
        initView(context);
    }

    private void initView(Context context) {
        inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_huodao_select, null, false);
        adapter = new HuodaoSelectAdapter(context,datas,productNumber);
        binding.gridview.setAdapter(adapter);
        initListen();
        getWindow().setWindowAnimations(R.style.popupStyle);
        setContentView(binding.getRoot());
        setCancelable(false);
        setCanceledOnTouchOutside(true);
    }


    private void initListen() {
        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean[] checkStateArray = adapter.getCheckStateArray();
                ArrayList<Integer> list = new ArrayList<>();
                for (int i=0;i<checkStateArray.length;i++){
                    if(checkStateArray[i]){
                        list.add(i+1);
                    }
                }
                if(list.size()==0){
                    ToastUtil.getInstance().showToast("请至少选择一个货道",1500,context);
                    return;
                }
                etStrategy.etComplete(list);
                dismiss();

            }
        });

        binding.sbState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                adapter.checkAll(isChecked);
            }
        });

        binding.searchCommdity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchName = binding.searchName.getText().toString();
                if (!TextUtils.isEmpty(searchName)) {

                } else {

                }
            }
        });

    }


    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth();
        layoutParams.height = Presenter.getInstance().getWidowHeight()*2/3;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setAttributes(layoutParams);
    }


}
