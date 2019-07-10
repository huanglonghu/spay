package com.example.godcode.ui.fragment.bindproduct;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.bean.CommodityRoadList;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.databinding.FragmentBuhuoConfigBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.EtStrategy;
import com.example.godcode.observable.EventType;
import com.example.godcode.observable.RxBus;
import com.example.godcode.observable.RxEvent;
import com.example.godcode.ui.adapter.BuhuoConfigAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.utils.GsonUtil;
import java.util.ArrayList;
import java.util.List;

public class BuohuoConfigFragment extends BaseFragment {

    private FragmentBuhuoConfigBinding binding;
    private View view;
    private BuhuoConfigAdapter buhuoConfigAdapter;
    private int productId;
    private CommodityRoadList.ResultBean.DataBean[] datas;
    private MyAssetList.ResultBean.DataBean dataBean;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_buhuo_config, container, false);
            view = binding.getRoot();
            binding.setPresenter(presenter);
            binding.bhConfigToolbar.title.setText("补货管理");
            initListen();
            Bundle arguments = getArguments();
            if (arguments != null) {
                dataBean = (MyAssetList.ResultBean.DataBean) arguments.getSerializable("DataBean");
                productId = dataBean.getFK_ProductID();
                int commodityRoadCount = dataBean.getCommodityRoadCount();
                datas = new CommodityRoadList.ResultBean.DataBean[commodityRoadCount];
                initData();
            }
        }
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
        ArrayList<CommodityRoadList.ResultBean.DataBean> list = new ArrayList<>();
        if (data != null) {
            int kc = 0;
            for (int i = 0; i < data.size(); i++) {
                CommodityRoadList.ResultBean.DataBean dataBean = data.get(i);
                int commodityRoadNumber = dataBean.getCommodityRoadNumber();
                datas[commodityRoadNumber - 1] = data.get(i);
                kc += dataBean.getCurrentStocks();
            }
            for (int i = 0; i < datas.length; i++) {
                if (datas[i] != null) {
                    list.add(datas[i]);
                }
            }
            RxEvent rxEvent = new RxEvent(EventType.EVENTTYPE_CURRENSSTOCK_CHANGE);
            Bundle bundle = new Bundle();
            bundle.putInt("kc",kc);
            rxEvent.setBundle(bundle);
            RxBus.getInstance().post(rxEvent);
        }
        if (buhuoConfigAdapter == null) {
            buhuoConfigAdapter = new BuhuoConfigAdapter(activity, dataBean.getProductNumber(), datas, binding, new BatchConfigStrategy(), list);
            binding.grid.setAdapter(buhuoConfigAdapter);
        } else {
            buhuoConfigAdapter.refreshData(data);
        }
    }


    private void initListen() {
        binding.batchSupply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBatchSupply = binding.getIsBatchSupply();
                buhuoConfigAdapter.batchSet(!isBatchSupply);
                if (binding.getIsBatchClean()) {
                    binding.setIsBatchClean(false);
                    binding.batchClean.setSelected(false);
                }
                if(!binding.getIsBatchClean()&&!isBatchSupply){
                    binding.selectAll.setChecked(false);
                }
                binding.setIsBatchSupply(!isBatchSupply);
                binding.batchSupply.setSelected(!isBatchSupply);
            }
        });

        binding.batchClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBatchClean = binding.getIsBatchClean();
                buhuoConfigAdapter.batchSet(!isBatchClean);
                if (binding.getIsBatchSupply()) {
                    binding.setIsBatchSupply(false);
                    binding.batchSupply.setSelected(false);
                }
                if(!binding.getIsBatchSupply()&&!isBatchClean){
                    binding.selectAll.setChecked(false);
                }
                binding.setIsBatchClean(!isBatchClean);
                binding.batchClean.setSelected(!isBatchClean);
            }
        });


        binding.selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = binding.selectAll.isChecked();
                buhuoConfigAdapter.checkAll(checked);
            }
        });

        binding.btnConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.getIsBatchSupply()) {
                    buhuoConfigAdapter.batchConfig(1, new BatchConfigStrategy());
                } else if (binding.getIsBatchClean()) {
                    buhuoConfigAdapter.batchConfig(2, new BatchConfigStrategy());
                }
            }
        });


    }


    public void initView() {
    }

    @Override
    protected void lazyLoad() {
    }


    private class BatchConfigStrategy extends EtStrategy {
        @Override
        public void etComplete() {
            configSuccess();
        }
    }

    private void configSuccess() {
        Toast toast = new Toast(activity);
        View view = View.inflate(activity, R.layout.layout_config_success, null);
        toast.setView(view);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        initData();
        buhuoConfigAdapter.batchSet(false);
        binding.setIsBatchSupply(false);
        binding.batchSupply.setSelected(false);
        binding.selectAll.setChecked(false);
        binding.setIsBatchClean(false);
        binding.batchClean.setSelected(false);
    }


}
