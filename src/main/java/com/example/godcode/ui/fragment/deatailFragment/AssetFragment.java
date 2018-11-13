package com.example.godcode.ui.fragment.deatailFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.bean.WebSocketNews1;
import com.example.godcode.databinding.FragmentAssetBinding;
import com.example.godcode.ui.activity.ClipImageActivity;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.fragment.asset.Asset_DetailFragment;
import com.example.godcode.utils.LogUtil;

import java.util.ArrayList;

public class AssetFragment extends BaseFragment {
    private FragmentAssetBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_asset, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.assetToolbar.title.setText("我的资产");
            initListener();
            initView();
        }
        toogleFragment(index);
        return view;
    }


    private void initListener() {
        binding.assetToolbar.toolbar4Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == 0) {
                    Asset_1_Fragment fragment = (Asset_1_Fragment) fragments.get(0);
                    int index = fragment.getIndex();
                    if (index == 1) {
                        fragment.toggleFragment(0);
                    } else {
                        presenter.back();
                    }
                } else {
                    toogleFragment(0);
                }
            }
        });
    }


    private int index;

    public void setIndex(int index) {
        this.index = index;
    }

    private ArrayList<BaseFragment> fragments = new ArrayList<>();

    public void initView() {
        Asset_1_Fragment asset_1_fragment = new Asset_1_Fragment();
        Asset_2_Fragment asset_2_fragment = new Asset_2_Fragment();
        fragments.add(asset_1_fragment);
        fragments.add(asset_2_fragment);
    }

    public void toogleFragment(int index) {
        this.index = index;
        getChildFragmentManager().beginTransaction().replace(R.id.asset_container, fragments.get(index)).commit();
        if (index == 0) {
            binding.assetToolbar.option.setVisibility(View.VISIBLE);
            binding.assetToolbar.ivOption.setImageResource(R.drawable.config);
        } else {
            binding.assetToolbar.option.setVisibility(View.GONE);
        }
    }

    @Override
    protected void lazyLoad() {
    }


    public FragmentAssetBinding getBinding() {
        return binding;
    }

    private MyAssetList.ResultBean.DataBean dataBean;

    public void initData(MyAssetList.ResultBean.DataBean dataBean) {
        this.dataBean = dataBean;
    }

    public MyAssetList.ResultBean.DataBean getDataBean() {
        return dataBean;
    }

    @Override
    public void onKeyDown() {
        if (index == 0) {
            Asset_1_Fragment fragment = (Asset_1_Fragment) fragments.get(0);
            int index = fragment.getIndex();
            if (index == 1) {
                fragment.toggleFragment(0);
            } else {
                presenter.back();
            }
        } else {
            toogleFragment(0);
        }
    }

    @Override
    public void refreshData() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 0:
                gotoClipActivity(data.getData());
                break;
            case REQUEST_CROP_PHOTO:
                if (data != null) {
                    Asset_2_Fragment fragment = (Asset_2_Fragment) fragments.get(1);
                    fragment.a(data);
                }
                break;
        }
    }


    public void gotoClipActivity(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent();
        intent.setClass(activity, ClipImageActivity.class);
        intent.putExtra("type", 2);//
        intent.setData(uri);
        activity.startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    private static final int REQUEST_CROP_PHOTO = 102;


    public void refresh(WebSocketNews1 webSocketNews1) {
        if (fragments.size() > 0) {
            Asset_1_Fragment fragment = (Asset_1_Fragment) fragments.get(0);
            if (fragment.getPeriodType() != 2) {
                WebSocketNews1.DataBean data = webSocketNews1.getData();
                double scanQRMoney = data.getScanQRMoney();
                double paperMoney = data.getPaperMoney();
                int coinCount = data.getCoinCount();

                if (fragment.getIndex() == 1) {
                    Asset_DetailFragment detailfragment = (Asset_DetailFragment) fragment.getFragments().get(1);
                    detailfragment.refreshDivide(webSocketNews1);
                }

            }
        }
    }

}
