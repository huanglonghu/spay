package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.spay.R;
import com.example.spay.bean.BindProduct;
import com.example.spay.bean.CommodityRoadList;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.ItemBindproductBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.EtStrategy;
import com.example.spay.interface_.VemConfigStrategy;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.ui.view.widget.VemConfigDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommdityRoadListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context context;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private CommodityRoadList.ResultBean.DataBean[] datas;
    private int productId;
    private String productNumber;
    private EtStrategy etStrategy;
    private boolean isMaster;

    public CommdityRoadListAdapter(Context context, boolean isMaster, CommodityRoadList.ResultBean.DataBean[] datas, int productId, String productNumber, EtStrategy etStrategy) {
        this.context = context;
        this.isMaster = isMaster;
        this.datas = datas;
        this.productId = productId;
        this.productNumber = productNumber;
        this.etStrategy = etStrategy;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public Object getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemBindproductBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_bindproduct, parent, false);
            binding.setProductNumber(productNumber);
            convertView = binding.getRoot();
            convertView.setTag(binding);
            CommodityRoadList.ResultBean.DataBean dataBean = datas[position];
            BindProduct.CommodityRoadBeanX.CommodityRoadBean commodityRoadBean = new BindProduct.CommodityRoadBeanX.CommodityRoadBean();
            commodityRoadBean.setCommodityRoadNumber(position + 1);
            if (dataBean != null) {
                binding.presentName.setText(dataBean.getPresentName());
                String imgUrl = dataBean.getImgUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    if (!imgUrl.contains("http")) {
                        imgUrl = Constant.baseUrl + imgUrl;
                    }
                    RxImageLoader.with(context).load(imgUrl).into(binding.iv);
                }
                commodityRoadBean.setCapacity(dataBean.getCapacity() + "");
                commodityRoadBean.setCurrentStocks(dataBean.getCurrentStocks() + "");
                commodityRoadBean.setFK_PresentID(dataBean.getFK_PresentID());
                commodityRoadBean.setGamePrice(dataBean.getGamePrice() + "");
                commodityRoadBean.setId(dataBean.getId() + "");
                commodityRoadBean.setProbability(dataBean.getProbability());
                commodityRoadBean.setSellPrice(dataBean.getSellPrice() + "");
            }
            binding.setHdBean(commodityRoadBean);
            if(!isMaster){
                binding.edit.setVisibility(View.GONE);
            }else {
                binding.edit.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_UP:
                                String presentName = "";
                                if (!TextUtils.isEmpty(binding.presentName.getText())) {
                                    presentName = binding.presentName.getText().toString();
                                }
                                VemConfigDialog vemConfigDialog = new VemConfigDialog.Builder()
                                        .context(context)
                                        .startIndex(0)
                                        .position(position)
                                        .commodityRoadBean(commodityRoadBean)
                                        .presentName(presentName).vemconfigStrategy(new VemConfigStrategy() {
                                            @Override
                                            public void complete(BindProduct.CommodityRoadBeanX.CommodityRoadBean commodityRoadBean, String presentImgUrl, String presentName, int position) {
                                                View view = getView(position, null, null);
                                                ItemBindproductBinding binding = (ItemBindproductBinding) view.getTag();
                                                binding.presentName.setText(presentName);
                                                if (!TextUtils.isEmpty(presentImgUrl)) {
                                                    if (!presentImgUrl.contains("http")) {
                                                        presentImgUrl = Constant.baseUrl + presentImgUrl;
                                                    }
                                                    RxImageLoader.with(context).load(presentImgUrl).into(binding.iv);
                                                }
                                                BindProduct.CommodityRoadBeanX commodityRoadBeanX = new BindProduct.CommodityRoadBeanX();
                                                List<BindProduct.CommodityRoadBeanX.CommodityRoadBean> commodityRoad = new ArrayList<BindProduct.CommodityRoadBeanX.CommodityRoadBean>();
                                                commodityRoad.add(commodityRoadBean);
                                                commodityRoadBeanX.setCommodityRoad(commodityRoad);
                                                commodityRoadBeanX.setFK_ProductID(productId + "");
                                                String currentStocks = commodityRoadBean.getCurrentStocks();
                                                HttpUtil.getInstance().editCommdityRoad(commodityRoadBeanX).subscribe(
                                                        a -> {
                                                            if (!TextUtils.isEmpty(currentStocks)) {
                                                                int sumStock = 0;
                                                                for (int i = 0; i < datas.length; i++) {
                                                                    CommodityRoadList.ResultBean.DataBean dataBean = datas[i];
                                                                    if (dataBean != null) {
                                                                        sumStock += dataBean.getCurrentStocks();
                                                                    }
                                                                }
                                                                RxEvent rxEvent = new RxEvent(EventType.EVENTTYPE_CURRENSSTOCK_CHANGE);
                                                                Bundle bundle = new Bundle();
                                                                bundle.putInt("kc",sumStock);
                                                                rxEvent.setBundle(bundle);
                                                                RxBus.getInstance().post(rxEvent);
                                                            }
                                                            etStrategy.etComplete();
                                                        }
                                                );
                                                binding.setHdBean(commodityRoadBean);
                                            }
                                        }).build();
                                vemConfigDialog.show();
                                break;
                        }
                        return true;
                    }
                });
            }


            viewMap.put(position, convertView);
        }

        return viewMap.get(position);
    }


    public void refreshData(CommodityRoadList.ResultBean.DataBean[] datas) {
        this.datas = datas;
        viewMap.clear();
        notifyDataSetChanged();
    }


}
