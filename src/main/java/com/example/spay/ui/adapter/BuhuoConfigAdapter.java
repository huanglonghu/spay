package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.spay.R;
import com.example.spay.bean.CommodityRoadList;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.FragmentBuhuoConfigBinding;
import com.example.spay.databinding.ItemBuhuoConfigBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.EtStrategy;
import com.example.spay.ui.view.widget.BuHuoDialog;
import com.example.spay.utils.ToastUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BuhuoConfigAdapter extends BaseAdapter {
    private Context context;
    private final LayoutInflater inflater;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private boolean[] checkStateArray;
    private CommodityRoadList.ResultBean.DataBean[] datas;
    private FragmentBuhuoConfigBinding binding2;
    private EtStrategy etStrategy;
    private String productNumber;
    private boolean isBatch;
    private List<CommodityRoadList.ResultBean.DataBean> data;

    public BuhuoConfigAdapter(Context context, String productNumber, CommodityRoadList.ResultBean.DataBean[] datas, FragmentBuhuoConfigBinding binding, EtStrategy etStrategy, List<CommodityRoadList.ResultBean.DataBean> data) {
        this.context = context;
        this.datas = datas;
        this.binding2 = binding;
        this.productNumber = productNumber;
        this.etStrategy = etStrategy;
        this.data = data;
        if (data != null) {
            checkStateArray = new boolean[data == null ? 0 : data.size()];
        }
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return isBatch ? (data != null ? data.size() : 0) : (datas == null ? 0 : datas.length);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemBuhuoConfigBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_buhuo_config, null, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
            binding.setProductNumber(productNumber);
            CommodityRoadList.ResultBean.DataBean dataBean;
            if (isBatch) {
                dataBean = data.get(position);
                binding.setCommodityNumber(dataBean.getCommodityRoadNumber());
            } else {
                dataBean = datas[position];
                binding.setCommodityNumber(position + 1);
            }
            binding.setIsBatch(isBatch);
            binding.setBean(dataBean);
            if (dataBean != null) {
                String imgUrl = dataBean.getImgUrl();
                if (!TextUtils.isEmpty(imgUrl)) {
                    if (!imgUrl.contains("http")) {
                        imgUrl = Constant.baseUrl + imgUrl;
                    }
                    RxImageLoader.with(context).load(imgUrl).into(binding.iv);
                }
                binding.bhConfig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Long> list = new ArrayList<>();
                        list.add((long) dataBean.getId());
                        BuHuoDialog buHuoDialog = new BuHuoDialog(context, list, dataBean.getCommodityRoadNumber() + "", etStrategy);
                        buHuoDialog.show();
                    }
                });

                binding.cleanConfig.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<Long> list = new ArrayList<>();
                        list.add((long) dataBean.getId());
                        HttpUtil.getInstance().buhuo(0, list).subscribe(
                                str -> {
                                    //设置成功 刷新数据
                                    etStrategy.etComplete();
                                }
                        );
                    }
                });

                binding.rlIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean selected = binding.circle.isSelected();
                        binding.circle.setSelected(!selected);
                        checkStateArray[position] = !selected;
                        if (!selected == false) {
                            binding2.selectAll.setChecked(false);
                        }
                    }
                });
            }

            viewMap.put(position, convertView);
        }
        return viewMap.get(position);
    }


    public void batchSet(boolean state) {
        this.isBatch = state;
        viewMap.clear();
        notifyDataSetChanged();
    }

    public void checkAll(boolean state) {
        for (int i = 0; i < data.size(); i++) {
            View view = getView(i, null, null);
            ItemBuhuoConfigBinding binding = (ItemBuhuoConfigBinding) view.getTag();
            binding.circle.setSelected(state);
            checkStateArray[i] = state;
        }
    }


    public void batchConfig(int type, EtStrategy etStragety) {
        ArrayList<Long> list = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < checkStateArray.length; i++) {
            if (checkStateArray[i]) {
                CommodityRoadList.ResultBean.DataBean dataBean = data.get(i);
                list.add((long) dataBean.getId());
                sb.append(dataBean.getCommodityRoadNumber() + ",");
            }
        }
        if (list.size() == 0) {
            ToastUtil.getInstance().showToast("请勾选货道", 2000, context);
            return;
        }
        if (type == 1) {
            String s = sb.toString();
            String commodityRoads = s.substring(0, s.lastIndexOf(","));
            BuHuoDialog buHuoDialog = new BuHuoDialog(context, list, commodityRoads, etStragety);
            buHuoDialog.show();
        } else if (type == 2) {
            HttpUtil.getInstance().buhuo(0, list).subscribe(
                    a -> {
                        etStragety.etComplete();
                    }
            );
        }
    }


    public void refreshData(List<CommodityRoadList.ResultBean.DataBean> data) {
        this.data = data;
        viewMap.clear();
        checkStateArray = new boolean[data == null ? 0 : data.size()];
        notifyDataSetChanged();
    }


}
