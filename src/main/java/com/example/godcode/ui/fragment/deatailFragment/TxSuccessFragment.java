package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentTxsuccessBinding;
import com.example.godcode.ui.base.BaseFragment;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TxSuccessFragment extends BaseFragment {
    private FragmentTxsuccessBinding binding;
    private View view;
    private TxFragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            parentFragment = (TxFragment) getParentFragment();
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_txsuccess, container, false);
            view = binding.getRoot();
            initView();
        }
        return view;
    }


    public void initView() {
        binding.txSuccessBank.setText(parentFragment.getBank());
        binding.txSuccessMoney.setText(parentFragment.getMoney()+"");
        //182.255.63.151:8091
        binding.txComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.back();
            }
        });
    }



    @Override
    protected void lazyLoad() {
    }


    public String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date now = new Date();
        long l = now.getTime() + 2 * 60 * 60 * 1000;
        now.setTime(l);
        return sdf.format(now);
    }
}
