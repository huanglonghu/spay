package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.bean.OrderResult;
import com.example.godcode.bean.ProductScan;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.R;
import com.example.godcode.bean.OrderDetail;
import com.example.godcode.bean.PayByBalance;
import com.example.godcode.databinding.FragmentOrderdetailBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.ui.view.PsdPopupWindow;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatCheckUtil;
import com.example.godcode.utils.PayPsdSetting;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailFragment extends BaseFragment implements KeyBoard.PsdLengthWatcher {

    private FragmentOrderdetailBinding binding;
    private View view;
    private OrderResult.ResultBean resultBean;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orderdetail, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.orderDetailToolbar.title.setText("订单详情");
            initView();
            initListener();
        }
        return view;
    }

    private ProductScan.ResultBean productScan;

    public void initData(OrderResult orderResult, ProductScan.ResultBean productScan) {
        resultBean = orderResult.getResult();
        this.productScan = productScan;
    }

    private void initListener() {
        binding.orderPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayPsdSetting.getInstance().isPayPsdSet("付款", resultBean.getSumOrder(), view, OrderDetailFragment.this, 1);
            }
        });
    }


    public void initView() {
        binding.setOrderBean(resultBean);
        binding.orderMoney.setText(FormatCheckUtil.getInstance().get2double(resultBean.getSumOrder()));
        long time = DateUtil.getInstance().getStringToDate(resultBean.getOrderDate(), "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS");
        String orderDate = DateUtil.getInstance().formatTime(time);
        binding.orderDate.setText(orderDate);
        if (!TextUtils.isEmpty(productScan.getThumbnailImgPath())) {
            String url = productScan.getThumbnailImgPath();
            RxImageLoader.with(activity).load(Constant.baseUrl + url).into(binding.productImage);
        } else {
            binding.productImage.setBackgroundResource(R.drawable.contact_normal);
        }

    }

    @Override
    protected void lazyLoad() {
    }

    @Override
    public void toCheck(String psd) {

        PayByBalance payByBalance = new PayByBalance();
        payByBalance.setPassword(psd);
        payByBalance.setPayOrderID(resultBean.getId());
        payByBalance.setUserID(Constant.userId);
        HttpUtil.getInstance().payByBalance(payByBalance)
                .subscribe(
                        balanceStr -> {
                            if (balanceStr.contains("\"success\":false")) {
                                PsdPopupWindow.getInstance(activity).clear();
                                Toast.makeText(activity, "密码输入错误，请重新输入", Toast.LENGTH_SHORT).show();
                            } else {
                                PsdPopupWindow.getInstance(activity).exit();
                                PaySuccessFragment paySuccessFragment = new PaySuccessFragment();
                                presenter.step2Fragment(paySuccessFragment);
                                paySuccessFragment.initData(productScan.getProductName(), resultBean.getSumOrder());
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
