package com.example.spay.ui.fragment.bindproduct;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.MyAssetList;
import com.example.spay.databinding.FragmentJtcsConfigBinding;
import com.example.spay.ui.base.BaseFragment;

public class JtcsConfigFragment extends BaseFragment {

    private FragmentJtcsConfigBinding binding;
    private View view;
    private MyAssetList.ResultBean.DataBean dataBean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_jtcs_config, container, false);
            binding.setPresenter(presenter);
            binding.jtcsConfigToolbar.title.setText("机台参数设置");
            view = binding.getRoot();
            initListen();
            Bundle arguments = getArguments();
            if (arguments != null) {
                dataBean = (MyAssetList.ResultBean.DataBean) arguments.getSerializable("DataBean");
            }
        }
        return view;
    }

    private void initListen() {
//        binding.jtcsConfig.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                JtConfigListFragment jtConfigListFragment = new JtConfigListFragment();
//                Bundle bundle = new Bundle();
//                bundle.putInt("productId",dataBean.getFK_ProductID());
//                jtConfigListFragment.setArguments(bundle);
//                presenter.step2Fragment(jtConfigListFragment, "jtConfigList");
//            }
//        });

        binding.bhConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuohuoConfigFragment buohuoConfigFragment = new BuohuoConfigFragment();
                Bundle b = new Bundle();
                b.putSerializable("DataBean",dataBean);
                buohuoConfigFragment.setArguments(b);
                presenter.step2Fragment(buohuoConfigFragment, "bhConfig");
            }
        });

    }


    public void initView() {
    }

    @Override
    protected void lazyLoad() {
    }

}
