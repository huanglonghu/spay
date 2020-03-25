package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.databinding.FragmentSelecttimeBinding;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.utils.LogUtil;

import java.util.Calendar;

public class SelectTimeFragment extends BaseFragment {

    private FragmentSelecttimeBinding binding;
    private View view;
    private DatePicker.OnDateChangedListener onDateChangedListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selecttime, container, false);
            binding.setPresenter(presenter);
            binding.setFragment(this);
            view = binding.getRoot();
            binding.selectTimeToolBar.title.setText("选择时间");
            binding.selectTimeToolBar.tvOption.setText("完成");
            binding.setSelectPosition(selectPosition);
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.selectTimeToolBar.option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.getType() == 0) {
                    if (TextUtils.isEmpty(binding.getDate1())) {
                        Toast.makeText(activity, "请选择开始时间", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(binding.getDate2())) {
                        Toast.makeText(activity, "请选择结束时间", Toast.LENGTH_SHORT).show();
                    } else {
                        timeSelect.setDate(binding.getDate1(), binding.getDate2());
                        presenter.back();
                    }
                } else {
                    timeSelect.setDate(binding.getDate3());
                    presenter.back();
                }
            }
        });

        binding.selectWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = binding.getType();
                if (type == 0) {
                    ((ViewGroup) ((ViewGroup) binding.datePicker.
                            getChildAt(0)).
                            getChildAt(0)).
                            getChildAt(2).
                            setVisibility(View.GONE);
                    binding.setType(1);
                } else {
                    ((ViewGroup) ((ViewGroup) binding.datePicker.
                            getChildAt(0)).
                            getChildAt(0)).
                            getChildAt(2).
                            setVisibility(View.VISIBLE);
                    binding.setType(0);
                }
            }
        });
    }

    private int selectPosition;

    public void initView() {
        Calendar c = Calendar.getInstance();
        int y0 = c.get(Calendar.YEAR);
        int m0 = c.get(Calendar.MONTH);
        int d0 = c.get(Calendar.DAY_OF_MONTH);
        long l = System.currentTimeMillis();
        binding.datePicker.setMaxDate(l);
        String date1 = String.format("%d-%d-%d", y0, m0 + 1, d0);
        String date3 = String.format("%d-%d", y0, m0 + 1);
        binding.setDate1(date1);
        binding.setDate2(date1);
        binding.setDate3(date3);
        binding.datePicker.init(y0, m0 + 1, d0, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int m, int d) {

                String time;
                if (binding.getType() == 0) {
                    if (update) {
                        if (selectPosition == 0) {
                            time = binding.getDate1();
                        } else {
                            time = binding.getDate2();
                        }
                        update = false;
                    } else {
                        time = String.format("%d-%d-%d", year, m + 1, d);
                    }
                    if (selectPosition == 0) {
                        binding.setDate1(time);
                    } else {
                        binding.setDate2(time);
                    }
                } else {
                    time = String.format("%d-%d", year, m + 1);
                    binding.setDate3(time);
                }
            }
        });
    }

    private boolean update;

    public void select(int index) {
        this.selectPosition = index;
        binding.setSelectPosition(index);
        update = true;
        switch (index) {
            case 0:
                String date1 = binding.getDate1();
                String[] times = date1.split("-");
                int y = Integer.parseInt(times[0]);
                int m = Integer.parseInt(times[1]);
                int d = Integer.parseInt(times[2]);
                LogUtil.log("1111111============XXXXXXXXXXXXX================" + d);
                binding.datePicker.updateDate(y, m, d);
                break;
            case 1:
                String date2 = binding.getDate2();
                String[] times2 = date2.split("-");
                int y2 = Integer.parseInt(times2[0]);
                int m2 = Integer.parseInt(times2[1]);
                int d2 = Integer.parseInt(times2[2]);
                LogUtil.log("222222============XXXXXXXXXXXXX================" + d2);
                binding.datePicker.updateDate(y2, m2, d2);
                break;

        }
    }

    @Override
    protected void lazyLoad() {
    }


    public interface TimeSelect {
        void setDate(String date1, String date2);

        void setDate(String date3);
    }

    public TimeSelect timeSelect;

    public void setTimeSelect(TimeSelect timeSelect) {
        this.timeSelect = timeSelect;
    }
}
