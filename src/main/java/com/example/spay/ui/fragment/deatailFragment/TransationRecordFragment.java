package com.example.spay.ui.fragment.deatailFragment;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.spay.R;
import com.example.spay.bean.Teansantion;
import com.example.spay.bean.TeansantionType;
import com.example.spay.databinding.FragmentTransactionrecordBinding;
import com.example.spay.greendao.option.TransationOption;
import com.example.spay.greendao.option.TransationTypeOption;
import com.example.spay.http.HttpUtil;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.adapter.TransationRecordListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.widget.BottomDialog;
import com.example.spay.ui.view.MyListView;
import com.example.spay.utils.LogUtil;
import com.example.spay.utils.StringUtil;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class TransationRecordFragment extends BaseFragment implements BottomDialog.TypeSelect, MyListView.RefreshData, SelectTimeFragment.TimeSelect {
    private FragmentTransactionrecordBinding binding;
    private View view;
    private List<TeansantionType.ResultBean> teansantionTypeList;
    private List<Teansantion.DataBean> data;
    private TransationRecordListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transactionrecord, container, false);
            binding.setPresenter(presenter);
            binding.lvTransationrecord.setRefreshData(this);
            view = binding.getRoot();
            String title = StringUtil.getString(activity, R.string.transactionsDetail);
            String sx = StringUtil.getString(activity, R.string.sx);
            binding.transationrecordToolbar.title.setText(title);
            binding.transationrecordToolbar.tvOption.setText(sx);
            data = new ArrayList<>();
            typeList = new ArrayList<>();
            initView();
            querryTeansationType();
            initListener();
        } else {
            refreshData(1);
        }
        data.clear();
        return view;
    }


    private void initListener() {
        binding.lvTransationrecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransationRecordDetailFragment trdf = new TransationRecordDetailFragment();
                Bundle bundle = new Bundle();
                Teansantion.DataBean dataBean = data.get(position);
                bundle.putInt("id", dataBean.getId());
                bundle.putInt("relatedKey", dataBean.getRelatedKey());
                bundle.putInt("transactionType", dataBean.getTransactionType());
                trdf.setArguments(bundle);
                presenter.step2Fragment(trdf, "trdf");
            }
        });

    }

    private ArrayList<String> typeList;

    private void querryTeansationType() {
        HttpUtil.getInstance().getTeansantionTypeList().subscribe(
                teanstantionTypeStr -> {
                    TeansantionType teansantionType = new Gson().fromJson(teanstantionTypeStr, TeansantionType.class);
                    teansantionTypeList = teansantionType.getResult();
                    for (int i = 0; i < teansantionTypeList.size(); i++) {
                        TeansantionType.ResultBean resultBean = teansantionTypeList.get(i);
                        TransationTypeOption.getInstance().insertTransationType(resultBean.getKey(), resultBean.getValue());
                        typeList.add(resultBean.getKey());
                    }
                    type = teansantionTypeList.get(0).getValue();
                    querryTeansationName();
                }
        );

    }

    private String type;

    private void querryTeansationName() {
        HttpUtil.getInstance().getTypeList2().subscribe(
                typeStr -> {
                    TeansantionType teansantionType = new Gson().fromJson(typeStr, TeansantionType.class);
                    List<TeansantionType.ResultBean> result = teansantionType.getResult();
                    for (int i = 0; i < result.size(); i++) {
                        TeansantionType.ResultBean resultBean = result.get(i);
                        String value = resultBean.getValue();
                        String key = resultBean.getKey();
                        TransationOption.getInstance().insertTransationName(key, value);
                    }
                    refreshData(1);
                }
        );


    }

    public void initView() {
        initTime();
        adapter = new TransationRecordListAdapter(activity, data, R.layout.item_lv_transationrecord);
        binding.lvTransationrecord.setAdapter(adapter);
        binding.transationrecordToolbar.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = Integer.parseInt(type);
                BottomDialog bottomDialog = new BottomDialog(activity, typeList, position, "请选择交易类型");
                bottomDialog.setTypeSelect(TransationRecordFragment.this);
                bottomDialog.show();
            }
        });
    }

    private void initTime() {
        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        date1 = y + "-" + (m + 1) + "-" + d;
        binding.setDate1(date1);
        binding.recordDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectTimeFragment selectTimeFragment = new SelectTimeFragment();
                selectTimeFragment.setTimeSelect(TransationRecordFragment.this);
                Presenter.getInstance().step2Fragment(selectTimeFragment, "selectDate");
            }
        });
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void selectType(int position) {
        type = teansantionTypeList.get(position).getValue();
        binding.lvTransationrecord.setState(2);
    }

    private boolean isMonth;
    private String date1;
    private String date2;
    private String date3;

    @Override
    public void refreshData(int page) {
        HashMap<String, Object> urlMap = new HashMap<>();
        urlMap.put("UserId", Constant.userId);
        urlMap.put("type", type);
        urlMap.put("page", page);
        urlMap.put("limit", 20);
        if (isMonth) {
            urlMap.put("IsMonth", true);
            urlMap.put("Time", date3);
        } else {
            urlMap.put("IsMonth", false);
            urlMap.put("StartTime", date1);
            if (!TextUtils.isEmpty(date2)) {
                urlMap.put("EndTime", date2);
            }
        }
        LogUtil.log("==================refreshData=================" + urlMap.toString());
        HttpUtil.getInstance().getTeansantion(urlMap).subscribe(
                teansantionStr -> {
                    Teansantion teansantion = new Gson().fromJson(teansantionStr, Teansantion.class);
                    List<Teansantion.DataBean> list = teansantion.getData();
                    if (page == 1 && data.size() > 0) {
                        data.clear();
                        adapter.clearView();
                    }

                    if (list != null && list.size() > 0) {
                        data.addAll(list);
                        binding.setTeansation(teansantion);
                        binding.lvTransationrecord.setState(0);
                    } else {
                        binding.lvTransationrecord.setState(1);
                    }
                    adapter.notifyDataSetChanged();
                }
        );
    }

    @Override
    public void setDate(String date1, String date2) {
        isMonth = false;
        if(getTime(date2)-getTime(date1)>0){
            this.date1 = date1;
            this.date2 = date2;
        }else {
            this.date1 = date2;
            this.date2 = date1;
        }
        if (date1.equals(date2)) {
            binding.setDate1(date1);
        } else {
            binding.setDate1(this.date1);
            binding.setDate2(this.date2);
        }
        binding.lvTransationrecord.setState(2);
    }

    @Override
    public void setDate(String date3) {
        isMonth = true;
        this.date3 = date3;
        this.date1=null;
        this.date2=null;
        binding.setDate1(date3);
        binding.setDate2(date2);
        binding.lvTransationrecord.setState(2);
    }



    public long getTime(String user_time) {
        long time=0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try {
            d = sdf.parse(user_time);
            time = d.getTime();
        } catch (ParseException e) {
        }
        return time;
    }

}
