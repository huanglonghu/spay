package com.example.spay.ui.fragment.bindproduct;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.databinding.FragmentBindproduct0Binding;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.FormatUtil;
import com.example.spay.utils.LogUtil;

public class BindProduct0Fragment extends BaseFragment {
    private FragmentBindproduct0Binding binding;
    private View view;
    private BindProductFragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bindproduct0, container, false);
            parentFragment = (BindProductFragment) getParentFragment();
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String productNumber = binding.etNumber.getText().toString();
                    if (TextUtils.isEmpty(productNumber)) {
                        Toast.makeText(activity, "请输入产品编号", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    parentFragment.setProductNumber(productNumber);
                    if (FormatUtil.getInstance().isBeginWith4G(productNumber)) {//以4G开头
                        parentFragment.toggleChild(2);
                    } else {
                        parentFragment.toggleChild(1);
                    }
                }
            });
        }
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        LogUtil.log("========resume=========="+parentFragment.getProductNumber());
        binding.etNumber.setText(parentFragment.getProductNumber());
    }


    @Override
    protected void lazyLoad() {
    }

}
