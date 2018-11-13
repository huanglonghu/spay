package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.bean.PayByBalance;
import com.example.godcode.bean.YSRecord;
import com.example.godcode.databinding.FragmentYsjlDetailBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.ui.view.PsdPopupWindow;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatCheckUtil;

import com.example.godcode.utils.PayPsdSetting;


public class YSJLDetailFragment extends BaseFragment implements KeyBoard.PsdLengthWatcher {
    private FragmentYsjlDetailBinding binding;
    private View view;
    private YSRecord.ResultBean.DataBean bean;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ysjl_detail, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.ysjlDetailToolBar.title.setText("产品收益记录");
            initData();
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.tk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPsdSetting.getInstance().isPayPsdSet("退款", bean.getSumOrder(), view, YSJLDetailFragment.this, 1);
            }
        });
    }

    private void initData() {
        bean = (YSRecord.ResultBean.DataBean) getArguments().getSerializable("ysjlItem");
        binding.setBean(bean);
        binding.orderMoney.setText(FormatCheckUtil.getInstance().get2double(bean.getSumOrder()));
        binding.divideMoney.setText(FormatCheckUtil.getInstance().get2double(bean.getDivideMoney()));
        long stringToDate = DateUtil.getInstance().getStringToDate(bean.getOrderDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        String time = DateUtil.getInstance().formatTime(stringToDate);
        binding.ysjlDetailDate.setText(time);
    }


    public void initView() {
        if (!bean.isIsCapableRefund()||bean.isIsRefund()||!bean.getProductOwnerName().equals(Constant.syNum)) {
            binding.tk.setVisibility(View.GONE);
        }
    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void toCheck(String psd) {
        PayByBalance payByBalance = new PayByBalance();
        payByBalance.setUserID(Constant.userId);
        payByBalance.setPassword(psd);
        payByBalance.setPayOrderID(bean.getId());
        HttpUtil.getInstance().refundPayment(payByBalance).subscribe(
                refoundStr -> {
                    if (refoundStr.contains("\"success\":false")) {
                        PsdPopupWindow.getInstance(activity).clear();
                        Toast.makeText(activity, "密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity, "退款成功", Toast.LENGTH_SHORT).show();
                        PsdPopupWindow.getInstance(activity).exit();
                        presenter.back();
                    }
                }
        );

    }


    @Override
    public void onKeyDown() {
        if (PsdPopupWindow.getInstance(activity) != null && PsdPopupWindow.getInstance(activity).isShowing()) {
            PsdPopupWindow.getInstance(activity).exit();
        } else {
            presenter.back();
        }

    }

    @Override
    public void refreshData() {

    }

}
