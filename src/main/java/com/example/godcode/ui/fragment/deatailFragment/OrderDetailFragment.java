package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.godcode.bean.OrderDetail;
import com.example.godcode.bean.ProductScan;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.R;
import com.example.godcode.bean.PayByBalance;
import com.example.godcode.databinding.FragmentOrderdetailBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.ClickSureListener;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.example.godcode.ui.view.KeyBoard;
import com.example.godcode.ui.view.PsdPopupWindow;
import com.example.godcode.utils.DateUtil;
import com.example.godcode.utils.FormatUtil;
import com.example.godcode.utils.PayPsdSetting;

public class OrderDetailFragment extends BaseFragment{

    private FragmentOrderdetailBinding binding;
    private View view;
    private OrderDetail orderDetail;
    private ProductScan.ResultBean productScanResult;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_orderdetail, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.orderDetailToolbar.title.setText("订单详情");
            initData();
            initView();
        }
        return view;
    }


    public void initData() {
        Bundle bundle = getArguments();
        orderDetail = (OrderDetail) bundle.getSerializable("orderDetail");
        productScanResult = (ProductScan.ResultBean) bundle.getSerializable("productScanResult");
    }


    public void initView() {
        OrderDetail.ResultBean result = orderDetail.getResult();
        binding.orderNumber.setText(result.getOrderNumber());
        binding.orderMoney.setText(FormatUtil.getInstance().get2double(result.getSumOrder()));
        String orderDate = DateUtil.getInstance().formatDate(result.getOrderDate());
        binding.orderDate.setText(orderDate);
        if (!TextUtils.isEmpty(productScanResult.getThumbnailImgPath())) {
            String url = productScanResult.getThumbnailImgPath();
            RxImageLoader.with(activity).load(Constant.baseUrl + url).into(binding.productImage);
        } else {
            binding.productImage.setBackgroundResource(R.drawable.contact_normal);
        }
        binding.orderPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double money = result.getSumOrder();
                PayPsdSetting.getInstance().chackPwd(money, new ClickSureListener() {
                    @Override
                    public void isPwdExit(boolean isPwdExit) {
                        if(isPwdExit){
                            KeyBoard keyBoard = new KeyBoard(activity, new ClickSureListener() {
                                @Override
                                public void checkPwd(String pwd) {
                                    toCheck(pwd);
                                }
                            });
                            PsdPopupWindow.getInstance(activity).show("付款", money, view, keyBoard);
                        }else {

                        }
                    }
                });
            }
        });

    }

    @Override
    protected void lazyLoad() {
    }


    public void toCheck(String psd) {

        PayByBalance payByBalance = new PayByBalance();
        payByBalance.setPassword(psd);
        payByBalance.setPayOrderID(orderDetail.getResult().getId());
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
                                paySuccessFragment.initData(productScanResult.getProductName(), orderDetail.getResult().getSumOrder());
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


}
