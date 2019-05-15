package com.example.godcode.ui.fragment.deatailFragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.godcode.R;
import com.example.godcode.bean.BankCard;
import com.example.godcode.databinding.FragmentBankselectBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.constant.Constant;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BankSelectFragment extends BaseFragment {
    private FragmentBankselectBinding binding;
    private View view;
    private int fragmentType;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            fragmentType = getArguments().getInt("fragmentType");
        }
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bankselect, container, false);
            binding.setPresenter(presenter);
            view = binding.getRoot();

            binding.selectBankToolbar.title.setText("选择银行卡");
            initData();
            initView();
            initListener();
        }
        return view;
    }

    private void initListener() {
        binding.lvBank.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.back();
            }
        });
    }


    private ArrayList<String> bankList = new ArrayList<>();

    private void initData() {
        getBankList();
    }

    private void getBankList() {
        HttpUtil.getInstance().getBankCardsByUserID(Constant.userId).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                bankListStr -> {
                    BankCard bankCard = new Gson().fromJson(bankListStr, BankCard.class);
                    List<BankCard.ResultBean> result = bankCard.getResult();
                    for (int i = 0; i < result.size(); i++) {
                        BankCard.ResultBean resultBean = result.get(i);
                        if (resultBean.getBindType() == 3) {
                            bankList.add(resultBean.getBankName());
                        }
                    }
                    ArrayAdapter<String> stringArrayAdapter = new ArrayAdapter<String>(activity, R.layout.item_bank, bankList);
                    binding.lvBank.setAdapter(stringArrayAdapter);
                }, throwable -> {
                }
        );
    }


    public void initView() {

    }

    @Override
    protected void lazyLoad() {
    }


}
