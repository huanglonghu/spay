package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.databinding.FragmentSelecttimeBinding;
import com.example.spay.ui.base.BaseFragment;

import java.util.Calendar;


public class SelectTimeFragment extends BaseFragment {

    private FragmentSelecttimeBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
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
                if (TextUtils.isEmpty(binding.getDate1())) {
                    Toast.makeText(activity, "请选择开始时间", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(binding.getDate2())) {
                    Toast.makeText(activity, "请选择结束时间", Toast.LENGTH_SHORT).show();
                } else {
                    timeSelect.setDate(binding.getDate1(), binding.getDate2());
                    presenter.back();
                }

            }
        });
    }

    private int selectPosition;

    public void initView() {

        Calendar c = Calendar.getInstance();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH);
        int d = c.get(Calendar.DAY_OF_MONTH);
        long l = System.currentTimeMillis();
        binding.datePicker.setMaxDate(l);
        binding.datePicker.init(y, m + 1, d, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int m, int d) {
                String time = String.format("%d-%d-%d", year, m + 1, d);
                if (selectPosition == 0) {
                    binding.setDate1(time);
                } else {
                    binding.setDate2(time);
                }
            }
        });
    }

    public void select(int index) {
        this.selectPosition = index;
        binding.setSelectPosition(index);
    }


    @Override
    protected void lazyLoad() {
    }



    public interface TimeSelect {
        void setDate(String date1, String date2);
    }

    public TimeSelect timeSelect;

    public void setTimeSelect(TimeSelect timeSelect) {
        this.timeSelect = timeSelect;
    }
}
