package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.ItemLvMyassetBinding;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.constant.Constant;
import com.example.godcode.utils.FormatUtil;

import java.util.HashMap;
import java.util.List;

public class AssetListAdapter extends BaseAdapter {

    private HashMap<Integer, View> viewMap = new HashMap<>();
    private LayoutInflater layoutInflater;
    private List<MyAssetList.ResultBean.DataBean> assetList;
    private Context context;
    private HashMap<Integer, Integer> categoryMap;
    private String[] incomeType = {"今日", "昨日", "本周", "本月", "总"};


    public AssetListAdapter(Context context, List<MyAssetList.ResultBean.DataBean> assetList, HashMap<Integer, Integer> categoryMap) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.assetList = assetList;
        this.categoryMap = categoryMap;
    }

    @Override
    public int getCount() {
        return assetList == null ? 0 : assetList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemLvMyassetBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_lv_myasset, parent, false);
            binding.setPresenter(Presenter.getInstance());
            binding.setPeriodType(incomeType[periodType - 1]);
            MyAssetList.ResultBean.DataBean dataBean = assetList.get(position);
            if (dataBean.getFK_UserID() == Constant.userId) {
                binding.setIsMaster(true);
            } else {
                binding.setIsMaster(false);
            }
            String productImgUrl = dataBean.getProductImgUrl();
            if (!TextUtils.isEmpty(productImgUrl)) {
                if (!productImgUrl.contains("http")) {
                    productImgUrl = Constant.baseUrl + productImgUrl;
                }
                RxImageLoader.with(context).load(productImgUrl).into(binding.ivZc);
            }

            initData(binding, dataBean);
            Integer purView = categoryMap.get(dataBean.getProductCategoryID());
            if ((1 & purView) != 0) {
                binding.tvJrtb.setVisibility(View.VISIBLE);
            } else {
                binding.tvJrtb.setVisibility(View.GONE);
            }
            if ((1 << 2 & purView) != 0) {
                binding.tvJrtl.setVisibility(View.VISIBLE);
            } else {
                binding.tvJrtl.setVisibility(View.GONE);
            }
            convertView = binding.getRoot();
            convertView.setTag(binding);
            viewMap.put(position, convertView);
        }

        return viewMap.get(position);
    }

    private void initData(ItemLvMyassetBinding binding, MyAssetList.ResultBean.DataBean dataBean) {
        double price = dataBean.getPrice();
        String priceStr = FormatUtil.getInstance().get2double(price);
        binding.price.setText("¥" + priceStr);
        binding.setAssetBean(dataBean);
    }


    private int periodType;

    public void clearView(int periodType) {
        this.periodType = periodType;
        viewMap.clear();
        assetList.clear();
        notifyDataSetChanged();
    }


    public void refreshData(int position, MyAssetList.ResultBean.DataBean dataBean) {
        View view = getView(position, null, null);
        ItemLvMyassetBinding binding = (ItemLvMyassetBinding) view.getTag();
        initData(binding, dataBean);
    }

}
