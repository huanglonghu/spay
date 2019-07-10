package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import com.example.godcode.R;
import com.example.godcode.bean.BankBean;
import com.example.godcode.databinding.FragmentBankselectBinding;
import com.example.godcode.observable.EventType;
import com.example.godcode.observable.RxBus;
import com.example.godcode.observable.RxEvent;
import com.example.godcode.ui.adapter.BankSelectAdapter;
import com.example.godcode.ui.base.BaseFragment;
import java.util.ArrayList;

public class BankSelectFragment extends BaseFragment {
    private FragmentBankselectBinding binding;
    private View view;
    private BankSelectAdapter bankSelectAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bankselect, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();
            binding.selectBankToolbar.title.setText("选择银行卡");
            initView();
            initListener();
        }

        return view;
    }

    private void initListener() {
        binding.lvBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bankSelectAdapter.selectItem(position);
                RxEvent rxEvent = new RxEvent(EventType.EVENTTYPE_SELECT_BANK);
                rxEvent.setId(position);
                RxBus.getInstance().post(rxEvent);
                presenter.back();
            }
        });
    }


    private ArrayList<BankBean> bankList;

    private int index;

    public void setBankMsg(ArrayList<BankBean> bankList, int index) {
        this.bankList = bankList;
        this.index = index;
    }


    public void initView() {
        bankSelectAdapter = new BankSelectAdapter(getContext(), bankList, R.layout.lv_selectbank_item);
        binding.lvBank.setAdapter(bankSelectAdapter);
        bankSelectAdapter.selectItem(index);
    }

    @Override
    protected void lazyLoad() {
    }


}
