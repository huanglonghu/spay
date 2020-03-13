package com.example.spay.ui.fragment.dm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.spay.R;
import com.example.spay.bean.ScoreOptionRecord;
import com.example.spay.databinding.McDetailBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.adapter.ScoreOptionListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.ui.view.MyListView;
import com.example.spay.utils.GsonUtil;
import java.util.ArrayList;
import java.util.List;

public class ScoreOptionDetail extends BaseFragment implements MyListView.RefreshData {

    private McDetailBinding binding;
    private ArrayList<ScoreOptionRecord.ResultBean.DataBean> datas;
    private ScoreOptionListAdapter scoreOptionListAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.mc_detail, container, false);
        binding.setPresenter(presenter);
        binding.lvMcDetail.setRefreshData(this);
        initView();
        refreshData(1);
        return binding.getRoot();
    }

    private void initView() {
        datas = new ArrayList<>();
        scoreOptionListAdapter = new ScoreOptionListAdapter(getContext(), datas, R.layout.mcdetail_item);
        binding.lvMcDetail.setAdapter(scoreOptionListAdapter);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void refreshData(int page) {
        HttpUtil.getInstance().getScoreOptionRecord(page).subscribe(
                str -> {
                    ScoreOptionRecord scoreOptionRecord = GsonUtil.fromJson(str, ScoreOptionRecord.class);
                    ScoreOptionRecord.ResultBean result = scoreOptionRecord.getResult();
                    if (result != null) {
                        List<ScoreOptionRecord.ResultBean.DataBean> data = result.getData();
                        if (data != null && data.size() > 0) {
                            binding.lvMcDetail.setState(0);
                            datas.addAll(data);
                            scoreOptionListAdapter.notifyDataSetChanged();
                        } else {
                            binding.lvMcDetail.setState(1);
                        }
                    } else {
                        binding.lvMcDetail.setState(1);
                    }
                }


        );
    }
}
