package com.example.spay.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.spay.R;
import com.example.spay.bean.PayByBalance;
import com.example.spay.bean.SellGoodsOrder;
import com.example.spay.bean.YSRecord;
import com.example.spay.databinding.FragmentYsjlDetailBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.ClickSureListener;
import com.example.spay.observable.EventType;
import com.example.spay.observable.RxBus;
import com.example.spay.observable.RxEvent;
import com.example.spay.ui.adapter.HdSellListAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.KeyBoard;
import com.example.spay.ui.view.PsdPopupWindow;
import com.example.spay.utils.DateUtil;
import com.example.spay.utils.FormatUtil;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.PayPwdSetting;
import com.example.spay.utils.StringUtil;
import java.util.List;


public class YSJLDetailFragment extends BaseFragment {
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
            String title = StringUtil.getString(activity, R.string.cpsyjl);
            binding.ysjlDetailToolBar.title.setText(title);
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
                double money = bean.getSumOrder();
                PayPwdSetting.getInstance().checkPwd(money, new ClickSureListener() {
                    @Override
                    public void isPwdExit(boolean isPwdExit) {
                        if(isPwdExit){
                            KeyBoard keyBoard = new KeyBoard(activity, new ClickSureListener() {
                                @Override
                                public void checkPwd(String pwd) {
                                    toCheck(pwd);
                                }
                            });
                            PsdPopupWindow.getInstance(activity).show("退款", money, view, keyBoard);
                        }
                    }
                });
            }
        });
    }

    private void initData() {
        bean = (YSRecord.ResultBean.DataBean) getArguments().getSerializable("ysjlItem");
        if (FormatUtil.getInstance().isBeginWith4G(bean.getProductNumber())) {
            HttpUtil.getInstance().getSellGoodsOrderById(bean.getId()).subscribe(
                    str -> {
                        SellGoodsOrder sellGoodsOrder = GsonUtil.fromJson(str, SellGoodsOrder.class);
                        List<SellGoodsOrder.ResultBean> result = sellGoodsOrder.getResult();
                        if (result != null) {
                            HdSellListAdapter hdSellListAdapter = new HdSellListAdapter(activity, result, bean.getProductNumber());
                            binding.hdList.setAdapter(hdSellListAdapter);
                        }
                    }

            );
        }
        binding.setBean(bean);
        binding.orderMoney.setText(FormatUtil.getInstance().get2double(bean.getSumOrder()));
        binding.divideMoney.setText(FormatUtil.getInstance().get2double(bean.getDivideMoney()));
        String time = DateUtil.getInstance().formatDate(bean.getOrderDate());
        binding.ysjlDetailDate.setText(time);
    }


    public void initView() {
        if (!bean.isIsCapableRefund() || bean.isIsRefund() || !Constant.syNum.equals(bean.getProductOwnerName())) {
            binding.tk.setVisibility(View.GONE);
        }
    }

    @Override
    protected void lazyLoad() {}

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
                        RxBus.getInstance().post(new RxEvent(EventType.EVENTTYPE_REFRESH_YSJL));
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


}
