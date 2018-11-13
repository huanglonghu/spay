package com.example.godcode.ui.fragment.asset;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.godcode.R;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.bean.ProductCategory;
import com.example.godcode.bean.WebSocketNews1;
import com.example.godcode.databinding.ItemLvMyassetBinding;
import com.example.godcode.databinding.LayoutAssetDetailBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.adapter.AssetListAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.fragment.deatailFragment.AssetFragment;
import com.example.godcode.ui.fragment.deatailFragment.Asset_1_Fragment;
import com.example.godcode.ui.view.AssetSelectDialog;
import com.example.godcode.ui.view.MyListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Asset_DetailFragment extends BaseFragment implements AssetSelectDialog.AssetSelect, MyListView.RefreshData {
    private AssetListAdapter assetListAdapter;
    private LayoutAssetDetailBinding binding;
    private Asset_1_Fragment parentFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            parentFragment = (Asset_1_Fragment) getParentFragment();
            binding = DataBindingUtil.inflate(inflater, R.layout.layout_asset_detail, null, false);
            initView();
            initListener();
        }
        initParameter(2);
        initData();
        return binding.getRoot();
    }


    public void initParameter(int status) {
        this.status = status;
        binding.lvDetail.setPage(1);
        assetListAdapter.clearView(parentFragment.getPeriodType());
        UserNameOrAddress = "";
        selectPosition = 0;
        productType = "";
    }

    private void initListener() {
        binding.lvDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ItemLvMyassetBinding binding = DataBindingUtil.findBinding(view);
                if (binding.getIsMaster()) {
                    MyAssetList.ResultBean.DataBean dataBean = assetList.get(position);
                    AssetFragment fragment = (AssetFragment) parentFragment.getParentFragment();
                    fragment.initData(dataBean);
                    fragment.toogleFragment(1);
                }
            }
        });

        AssetFragment fragment = (AssetFragment) parentFragment.getParentFragment();
        fragment.getBinding().assetToolbar.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AssetSelectDialog assetSelectDialog = new AssetSelectDialog(activity, typeMap);
                assetSelectDialog.setAssetSelect(Asset_DetailFragment.this);
                assetSelectDialog.show();
            }
        });

    }

    private void initView() {
        assetListAdapter = new AssetListAdapter(activity, assetList, categoryMap);
        binding.lvDetail.setAdapter(assetListAdapter);
    }

    @Override
    protected void lazyLoad() {
    }

    private void initData() {
        getProductCategorys();
    }


    private List<ProductCategory.ResultBean.ItemsBean> categoryList;
    private HashMap<Integer, Integer> categoryMap = new HashMap<>();
    private HashMap<String, Integer> typeMap = new HashMap<>();

    private void getProductCategorys() {
        HttpUtil.getInstance().getAllProductCategorys().subscribe(
                productCategory -> {
                    ProductCategory category = new Gson().fromJson(productCategory, ProductCategory.class);
                    categoryList = category.getResult().getItems();
                    for (int i = 0; i < categoryList.size(); i++) {
                        ProductCategory.ResultBean.ItemsBean itemsBean = categoryList.get(i);
                        int purview = itemsBean.getPurview();
                        int id = itemsBean.getId();
                        String productType = itemsBean.getProductType();
                        categoryMap.put(id, purview);
                        typeMap.put(productType, id);
                    }
                    requestAssetList();
                }
        );

    }

    @Override
    public void refreshData() {

    }

    private int currentGroupUserID;
    private String productType;
    private int selectPosition;
    private String UserNameOrAddress;
    private int status;

    public void setCurrentGroupUserID(int currentGroupUserID) {
        this.currentGroupUserID = currentGroupUserID;
    }

    public void requestAssetList() {
        HashMap<String, String> urlMap = new HashMap<>();
        urlMap.put("UserId", String.valueOf(Constant.userId));
        urlMap.put("limit", "20");
        urlMap.put("PeriodType", parentFragment.getPeriodType() + "");
        urlMap.put("CurrentGroupUserID", currentGroupUserID + "");
        if (selectPosition == 1) {
            urlMap.put("isMaster", true + "");
        } else if (selectPosition == 2) {
            urlMap.put("isMaster", false + "");
        }
        urlMap.put("isValid", status + "");
        if (!TextUtils.isEmpty(UserNameOrAddress)) {
            urlMap.put("UserNameOrAddress", UserNameOrAddress);
        }
        if (!TextUtils.isEmpty(productType)) {
            urlMap.put("ProductType", productType);
        }

        urlMap.put("page", binding.lvDetail.getPage() + "");
        HttpUtil.getInstance().getGroupById(urlMap).subscribe(
                assetStr -> {
                    MyAssetList myAssetList = new Gson().fromJson(assetStr, MyAssetList.class);
                    MyAssetList.ResultBean result = myAssetList.getResult();
                    List<MyAssetList.ResultBean.DataBean> datas = result.getData();
                    parentFragment.refreshAsset(result.getCoinCount(), result.getCount(), result.getNormalCount()
                            , result.getErrorCount(), result.getDividedMoney()
                    );
                    if (datas.size() > 0) {
                        binding.lvDetail.setLoading(true);
                        for (int i = 0; i < datas.size(); i++) {
                            MyAssetList.ResultBean.DataBean dataBean = datas.get(i);
                            assetMap.put(dataBean.getProductNumber(), i);
                            assetList.add(dataBean);
                        }
                    } else {
                        binding.lvDetail.setLoading(false);
                    }
                    assetListAdapter.notifyDataSetChanged();
                }
        );
    }

    public void refreshDivide(WebSocketNews1 webSocketNews1) {
        WebSocketNews1.DataBean data = webSocketNews1.getData();
        String productNumber = data.getProductNumber();
        if (assetMap.containsKey(productNumber)) {
            int position = assetMap.get(productNumber);
            MyAssetList.ResultBean.DataBean dataBean = assetList.get(position);
            int coinCount = data.getCoinCount();
            int paperMoney = (int) data.getPaperMoney();
            dataBean.setScanCodeIncome(Double.parseDouble(dataBean.getScanCodeIncome()) + data.getScanQRMoney());
            dataBean.setTodayBanknote(dataBean.getTodayBanknote() + paperMoney);
            dataBean.setTodayCoin(dataBean.getTodayCoin() + coinCount);
            assetListAdapter.refreshData(position, dataBean);
        }

    }


    private HashMap<String, Integer> assetMap = new HashMap<>();

    private List<MyAssetList.ResultBean.DataBean> assetList = new ArrayList<>();

    @Override
    public void sureSelect(int position, String productType, String UserNameOrAddress) {
        assetList.clear();
        assetListAdapter.clearView(parentFragment.getPeriodType());
        this.selectPosition = position;
        this.productType = productType;
        this.UserNameOrAddress = UserNameOrAddress;
        binding.lvDetail.setPage(1);
        requestAssetList();
    }

    @Override
    public void refreshData(int page) {
        if (page == 1) {
            assetList.clear();
            assetListAdapter.clearView(parentFragment.getPeriodType());
            assetListAdapter.notifyDataSetChanged();
            binding.lvDetail.setPage(1);
        }
        requestAssetList();
    }
}
