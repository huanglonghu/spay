package com.example.godcode.ui.fragment.deatailFragment;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;

import com.example.godcode.R;
import com.example.godcode.bean.YSRecord;
import com.example.godcode.databinding.FragmentYsjlBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.adapter.YsjlListAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.MyDatePickerDialog;
import com.example.godcode.ui.view.MyListView;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatCheckUtil;
import com.example.godcode.utils.LogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class YSJLFragment extends BaseFragment implements MyListView.RefreshData, SelectTimeFragment.TimeSelect {
    private FragmentYsjlBinding binding;
    private View view;
    private List<YSRecord.ResultBean.DataBean> data;
    private YsjlListAdapter ysjlListAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.fragment_ysjl, null, false);
            binding.setPresenter(presenter);
            binding.lvYsjl.setRefreshData(this);
            binding.setType(type);
            view = binding.getRoot();
            data = new ArrayList<>();
            initView();
            initListener();
        }
        data.clear();
        ysjlListAdapter.clearView();
        binding.lvYsjl.setPage(1);
        initData();
        return view;
    }

    private void initView() {
        if (type == 1) {
            binding.ysjlToolbar.title.setText("今日扫码记录");
        } else if (type == 2) {
            binding.ysjlToolbar.title.setText("昨日扫码记录");
        } else if (type == 3) {
            binding.ysjlToolbar.title.setText("本月营收记录");
        } else if (type == 4) {
            binding.tvDate1.setText(time1);
            binding.tvDate2.setText(time2);
            binding.ysjlToolbar.title.setText("总营收记录");
        }
        ysjlListAdapter = new YsjlListAdapter(activity, data);
        binding.lvYsjl.setAdapter(ysjlListAdapter);
    }


    private void initListener() {
        binding.lvYsjl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YSJLDetailFragment ysjlDetailFragment = new YSJLDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("ysjlItem", data.get(position));
                ysjlDetailFragment.setArguments(bundle);
                presenter.step2Fragment(ysjlDetailFragment, "ysjl_detail");
            }
        });

        binding.calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.log("-----click-----");
                SelectTimeFragment selectTimeFragment = new SelectTimeFragment();
                selectTimeFragment.setTimeSelect(YSJLFragment.this);
                presenter.step2Fragment(selectTimeFragment, "selectTime");
            }
        });
    }

    private void initData() {
        querryYsjl();
    }

    private void querryYsjl() {
        LogUtil.log(time2+"---------time--------"+time1);
        HttpUtil.getInstance().getYSRecord(time1, time2, binding.lvYsjl.getPage()).subscribe(
                ysRecordStr -> {
                    YSRecord ysRecord = new Gson().fromJson(ysRecordStr, YSRecord.class);
                    List<YSRecord.ResultBean.DataBean> list = ysRecord.getResult().getData();
                    double allSumMoney = ysRecord.getResult().getAllSumMoney();
                    binding.divideIncomeTotal.setText(FormatCheckUtil.getInstance().get2double(allSumMoney));
                    String incomeSumMoney = FormatCheckUtil.getInstance().get2double(ysRecord.getResult().getIncomeSumMoney());
                    binding.sz.setText("收入¥  " + incomeSumMoney);
                    if (list.size() > 0) {
                        data.addAll(list);
                        binding.lvYsjl.setLoading(true);
                    } else {
                        binding.lvYsjl.setLoading(false);
                    }
                    ysjlListAdapter.notifyDataSetChanged();
                }
        );

    }


    @Override
    protected void lazyLoad() {
    }

    @Override
    public void refreshData() {

    }


    private String time1;
    private String time2;
    private int type;

    public void initData(int type) {
        switch (type) {
            case 1://今日记录
                time1 = DateUtil.getInstance().getToday();
                time2 = time1;
                break;
            case 2://昨日记录
                time1 = DateUtil.getInstance().getYesterDaty();
                time2 = time1;
                break;
            case 3://本月记录
                time1 = DateUtil.getInstance().getMonth();
                time2 = DateUtil.getInstance().getToday();
                break;
            case 4://总记录
                time1 = DateUtil.getInstance().getToday();
                time2 = time1;
                break;
        }
        this.type = type;
    }


    @Override
    public void refreshData(int page) {
        querryYsjl();
    }

    @Override
    public void setDate(String date1, String date2) {
        this.time1 = date1;
        this.time2 = date2;
        binding.tvDate1.setText(time1);
        binding.tvDate2.setText(time2);
        querryYsjl();
    }
}
