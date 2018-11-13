package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.godcode.R;
import com.example.godcode.bean.GroupMsg;
import com.example.godcode.databinding.FragmentMyassetBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.fragment.asset.Asset_DetailFragment;
import com.example.godcode.ui.fragment.asset.Asset_GroupFragment;
import com.example.godcode.utils.GsonUtil;
import java.util.ArrayList;

public class Asset_1_Fragment extends BaseFragment {
    private FragmentMyassetBinding binding;
    private View root;
    private AssetFragment parentFragment;
    private String[] incomeType = {"今日", "昨日", "本周", "本月", "总"};
    private Asset_GroupFragment asset_groupFragment;
    private Asset_DetailFragment asset_detailFragment;
    private ArrayList<BaseFragment> fragments;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_myasset, container, false);
            binding.setFragment(this);
            binding.setIntcomeType(incomeType);
            binding.setPresenter(presenter);
            root = binding.getRoot();
            parentFragment = (AssetFragment)getParentFragment();
            initListener();
            initView();
        }
        toggleFragment(0);
        return root;
    }

    public void querryByStatus(int status) {
        if(index == 1){
            asset_detailFragment.initParameter(status);
            asset_detailFragment.requestAssetList();
        }
    }

    private void initView() {
        fragments = new ArrayList<>();
        asset_groupFragment = new Asset_GroupFragment();
        asset_detailFragment = new Asset_DetailFragment();
        fragments.add(asset_groupFragment);
        fragments.add(asset_detailFragment);
    }

    public ArrayList<BaseFragment> getFragments() {
        return fragments;
    }

    private int index;

    public int getIndex(){
        return index;
    }

    public void toggleFragment(int index) {
        this.index = index;
        if (index == 0) {
            selectDate(1);
            parentFragment.getBinding().assetToolbar.option.setVisibility(View.GONE);
        }else {
            parentFragment.getBinding().assetToolbar.option.setVisibility(View.VISIBLE);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.asset_pager, fragments.get(index)).commit();
    }

    private void initListener() {

    }

    private int periodType;

    public int getPeriodType() {
        return periodType;
    }

    public void selectDate(int periodType) {
        this.periodType = periodType;
        binding.setSelectPosition(periodType);
        if (index == 0) {
            HttpUtil.getInstance().getGroup(periodType).subscribe(
                    groupStr -> {
                        GroupMsg groupMsg = GsonUtil.fromJson(groupStr, GroupMsg.class);
                        GroupMsg.ResultBean result = groupMsg.getResult();
                        int normalCount = result.getNormalCount();
                        int errorCount = result.getErrorCount();
                        refreshAsset(result.getCoinCount(), normalCount+errorCount,normalCount, result.getErrorCount(), result.getDividedMoney());
                        asset_groupFragment.refreshGroup(result.getData(), periodType);
                    }
            );
        } else {
            asset_detailFragment.initParameter(2);
            asset_detailFragment.requestAssetList();
        }

    }

    public void refreshAsset(int coinCount,int count,int normalCount, int errorCount, String divideMoney) {
        binding.setCoinCount(coinCount);
        binding.setCount(count);
        binding.setNormalCount(normalCount);
        binding.setErrorCount(errorCount);
        binding.divideIncome.setText(divideMoney);
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void refreshData() {

    }


}
