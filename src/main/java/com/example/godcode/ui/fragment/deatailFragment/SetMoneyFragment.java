package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.godcode.R;
import com.example.godcode.databinding.FragmentSetmoneyBinding;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.view.MyEditText;
import com.example.godcode.utils.StringUtil;

public class SetMoneyFragment extends BaseFragment implements MyEditText.MoneyValueListener {

    private FragmentSetmoneyBinding binding;
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setmoney, container, false);
            binding.setPresenter(presenter);
            binding.etSetMoney.setMoneyValueListener(this);
            view = binding.getRoot();
            String title = StringUtil.getString(activity, R.string.moneySetting);
            binding.setMoneyToolbar.title.setText(title);
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.sureSetMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String moneyStr = binding.etSetMoney.getText().toString().trim();
                if (!TextUtils.isEmpty(moneyStr)) {
                    double money = Double.parseDouble(moneyStr);
                    if (money < 0.01) {
                        Toast.makeText(activity, "转账最低金额不能小于0.01元", Toast.LENGTH_SHORT).show();
                    }else {
                        moneyResponse.setMoney(money);
                        presenter.back();
                    }
                } else {
                    Toast.makeText(activity, "请输入正确的金额", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void initView() {

    }

    @Override
    protected void lazyLoad() {
    }


    @Override
    public void setEnable(boolean enable,double money) {

        if (enable) {
            if (!binding.sureSetMoney.isEnabled()) {
                binding.sureSetMoney.setEnabled(true);
            }
        } else {
            if (binding.sureSetMoney.isEnabled()) {
                binding.sureSetMoney.setEnabled(false);
            }
        }
    }

    public interface MoneyResponse {
        void setMoney(double money);
    }

    public void setMoneyResponse(MoneyResponse moneyResponse) {
        this.moneyResponse = moneyResponse;
    }

    private MoneyResponse moneyResponse;

}
