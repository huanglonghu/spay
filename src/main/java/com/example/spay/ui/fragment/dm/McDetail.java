package com.example.spay.ui.fragment.dm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.bean.McUnlockDetail;
import com.example.spay.databinding.McDetailBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.adapter.McDetailListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.MyListView;
import com.example.spay.utils.GsonUtil;

import java.util.ArrayList;
import java.util.List;

public class McDetail extends BaseFragment implements MyListView.RefreshData {

    private McDetailListAdapter mcDetailListAdapter;
    private McDetailBinding binding;
    private ArrayList<McUnlockDetail.ResultBean.DataBean> datas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.mc_detail, container, false);
        binding.setPresenter(presenter);
        initView();
        binding.lvMcDetail.setRefreshData(this);
        refreshData(1);
        return binding.getRoot();
    }

    private void initView() {
        datas = new ArrayList<>();
        mcDetailListAdapter = new McDetailListAdapter(getContext(), datas, R.layout.mcdetail_item);
        binding.lvMcDetail.setAdapter(mcDetailListAdapter);
    }


    @Override
    protected void lazyLoad() {

    }

    @Override
    public void refreshData(int page) {
        int userId = getArguments().getInt("userId");
        int i = getArguments().getInt("productId");
        Integer productId = null;
        if (i != 0) {
            productId = i;
        }
        HttpUtil.getInstance().getMcUnLockDetail(userId, productId, page).subscribe(
                str -> {
                    McUnlockDetail mcUnlockDetail = GsonUtil.fromJson(str, McUnlockDetail.class);
                    McUnlockDetail.ResultBean result = mcUnlockDetail.getResult();
                    if (result != null) {
                        if(result.getData()!=null&&result.getData().size()>0){
                            binding.lvMcDetail.setState(0);
                            List<McUnlockDetail.ResultBean.DataBean> data = result.getData();
                            datas.addAll(data);
                            mcDetailListAdapter.notifyDataSetChanged();
                        }else {
                            binding.lvMcDetail.setState(1);
                        }
                    }else {
                        binding.lvMcDetail.setState(1);
                    }
                }
        );
    }
}
