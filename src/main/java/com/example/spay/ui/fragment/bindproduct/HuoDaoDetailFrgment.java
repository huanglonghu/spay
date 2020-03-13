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
import com.example.spay.bean.CommodityRoadList;
import com.example.spay.bean.MyAssetList;
import com.example.spay.databinding.FragmentHuodaoDetailBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.EtStrategy;
import com.example.spay.ui.adapter.CommdityRoadListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.GsonUtil;

import java.util.List;

public class HuoDaoDetailFrgment extends BaseFragment {

    private FragmentHuodaoDetailBinding binding;
    private View view;
    private int productId;
    private CommdityRoadListAdapter commdityRoadListAdapter;
    private int commodityRoadCount;
    private CommodityRoadList.ResultBean.DataBean[] datas;
    private MyAssetList.ResultBean.DataBean dataBean;
    private boolean isMaster;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_huodao_detail, container, false);
            binding.setPresenter(presenter);
            String title = "货道详情";
            binding.hdDetailToolbar.title.setText(title);
            Bundle arguments = getArguments();
            if (arguments != null) {
                dataBean = (MyAssetList.ResultBean.DataBean) arguments.getSerializable("DataBean");
                productId = dataBean.getFK_ProductID();
                isMaster = arguments.getBoolean("isMaster");
                binding.setIsMaster(isMaster);
                commodityRoadCount = dataBean.getCommodityRoadCount();
                datas = new CommodityRoadList.ResultBean.DataBean[commodityRoadCount];
            }
            initView();
            initListen();
            view = binding.getRoot();
        }
        initData();
        return view;
    }

    private void initData() {
        HttpUtil.getInstance().getCommodityRoadList(productId, null).subscribe(
                hdListStr -> {
                    refreshCommdityRoadList(hdListStr);
                }
        );
    }


    private void refreshCommdityRoadList(String hdListStr) {
        CommodityRoadList commodityRoadList = GsonUtil.fromJson(hdListStr, CommodityRoadList.class);
        CommodityRoadList.ResultBean result = commodityRoadList.getResult();
        List<CommodityRoadList.ResultBean.DataBean> data = result.getData();
        if (data != null) {
            for (int i = 0; i < data.size(); i++) {
                CommodityRoadList.ResultBean.DataBean dataBean = data.get(i);
                int commodityRoadNumber = dataBean.getCommodityRoadNumber();
                datas[commodityRoadNumber - 1] = data.get(i);
            }
        }
        if (commdityRoadListAdapter == null) {
            commdityRoadListAdapter = new CommdityRoadListAdapter(activity,isMaster,datas, productId, dataBean.getProductNumber(), new EtStrategy() {
                @Override
                public void etComplete() {
                    initData();
                }
            });
            binding.lvHd.setAdapter(commdityRoadListAdapter);
        } else {
            commdityRoadListAdapter.refreshData(datas);
        }
    }


    private void initListen() {
        binding.batchSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datas == null || datas.length == 0) {
                    Toast.makeText(activity, "无货道商品可设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                BatchSetupFragment batchSetupFragment = new BatchSetupFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("datas", datas);
                bundle.putInt("productId", productId);
                bundle.putString("productNumber", dataBean.getProductNumber());
                batchSetupFragment.setArguments(bundle);
                presenter.step2Fragment(batchSetupFragment, "batchSetup");
            }
        });

        binding.searchCommdity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchName = binding.searchName.getText().toString();
                if (!TextUtils.isEmpty(searchName)) {
                    HttpUtil.getInstance().getCommodityRoadList(productId, searchName).subscribe(
                            hdStr -> {
                                refreshCommdityRoadList(hdStr);
                            }
                    );
                } else {
                    HttpUtil.getInstance().getCommodityRoadList(productId, null).subscribe(
                            hdListStr -> {
                                refreshCommdityRoadList(hdListStr);
                            }
                    );
                }
            }
        });
    }

    private void initView() {

    }


    @Override
    protected void lazyLoad() {
    }


}
