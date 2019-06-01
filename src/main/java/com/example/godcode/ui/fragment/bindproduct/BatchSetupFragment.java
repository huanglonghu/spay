package com.example.godcode.ui.fragment.bindproduct;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.bean.BatchSettingBody;
import com.example.godcode.bean.CommodityRoadList;
import com.example.godcode.bean.MyAssetList;
import com.example.godcode.databinding.FragmentBatchSetupBinding;
import com.example.godcode.http.HttpUtil;
import com.example.godcode.interface_.EtStrategy;
import com.example.godcode.interface_.VemConfigStrategy;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.view.customview.AmountView;
import com.example.godcode.ui.view.widget.HuodaoSelectDialog;
import com.example.godcode.ui.view.widget.VemConfigDialog;
import com.example.godcode.utils.MoneyTextWatcher;
import com.example.godcode.utils.ToastUtil;

import java.util.List;


public class BatchSetupFragment extends BaseFragment {
    private FragmentBatchSetupBinding binding;
    private View view;
    private BatchSettingBody batchSettingBody;
    private CommodityRoadList.ResultBean.DataBean[] datas;
    private int productId;
    private String productNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_batch_setup, container, false);
            binding.setFragment(this);
            binding.batchSetupToolbar.title.setText("货道批量设置");
            binding.setPresenter(presenter);
            view = binding.getRoot();
            Bundle bundle = getArguments();
            if (bundle != null) {
                datas = (CommodityRoadList.ResultBean.DataBean[]) bundle.getSerializable("datas");
                productId = bundle.getInt("productId");
                productNumber = bundle.getString("productNumber");
            }
            binding.sellPrice.addTextChangedListener(new MoneyTextWatcher(binding.sellPrice));
            binding.gamePrice.addTextChangedListener(new MoneyTextWatcher(binding.gamePrice));
            batchSettingBody = new BatchSettingBody();
            batchSettingBody.setFK_ProductID(productId);
            binding.amountView.setAmount("0");
            initListen();
        }
        return view;
    }

    private void initListen() {
        binding.hdSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HuodaoSelectDialog huodaoSelectDialog = new HuodaoSelectDialog(activity, datas, productNumber, new EtStrategy() {
                    @Override
                    public void etComplete(List<Integer> list) {
                        batchSettingBody.setCommodityRoadNumberList(list);
                    }
                });
                huodaoSelectDialog.show();
            }
        });

        binding.selectPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new VemConfigDialog.Builder().context(activity).startIndex(1).vemconfigStrategy(new VemConfigStrategy() {
                    @Override
                    public void complete(String presentName, int presentId,String presentImgUrl) {
                        binding.present.setText(presentName);
                        batchSettingBody.setFK_PresentID(presentId+"");
                        batchSettingBody.setPresentName(presentName);
                        batchSettingBody.setPresentImgUrl(presentImgUrl);
                    }
                }).build().show();
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.getIsConfig1() && !binding.getIsConfig2() && !binding.getIsConfig3() && !binding.getIsConfig4() && !binding.getIsConfig5()) {
                    ToastUtil.getInstance().showToast("请至少设置一项", 1500, activity);
                    return;
                }
                List<Integer> commodityRoadNumberList = batchSettingBody.getCommodityRoadNumberList();
                if (commodityRoadNumberList == null) {
                    ToastUtil.getInstance().showToast("请选择货道", 1500, activity);
                    return;
                } else {
                    for (int i = 0; i < commodityRoadNumberList.size(); i++) {
                        int commodityNumber = commodityRoadNumberList.get(i);
                        if (datas[commodityNumber - 1] == null && !binding.getIsConfig1()) {
                            ToastUtil.getInstance().showToast("有未编辑的货道,请选择礼品", 1500, activity);
                            return;
                        }
                    }
                }

                if (binding.getIsConfig1()) {
                    String s = binding.present.getText().toString();
                    if (TextUtils.isEmpty(s)) {
                        ToastUtil.getInstance().showToast("请选择礼品", 1500, activity);
                        return;
                    }
                }
                if (binding.getIsConfig2()) {
                    String sellPrice = binding.sellPrice.getText().toString();
                    if (TextUtils.isEmpty(sellPrice)) {
                        ToastUtil.getInstance().showToast("请输入销售价格", 1500, activity);
                        return;
                    }
                    batchSettingBody.setSellPrice(sellPrice);
                }

                if (binding.getIsConfig3()) {
                    String gamePrice = binding.gamePrice.getText().toString();
                    if (TextUtils.isEmpty(gamePrice)) {
                        ToastUtil.getInstance().showToast("请输入货道单价", 1500, activity);
                        return;
                    }
                    batchSettingBody.setGamePrice(gamePrice);
                }

                if (binding.getIsConfig4()) {
                    String probability = binding.probablity.getText().toString();
                    if (TextUtils.isEmpty(probability)) {
                        ToastUtil.getInstance().showToast("请输入商品获得概率", 1500, activity);
                        return;
                    }
                    batchSettingBody.setProbability(probability);
                }

                if (binding.getIsConfig5()) {
                    batchSettingBody.setStock(binding.amountView.getAmount() + "");
                }

                HttpUtil.getInstance().batchSetting(batchSettingBody).subscribe(
                       a->{
                           ToastUtil.getInstance().showToast("设置成功",1500,activity);
                           presenter.back();
                       }
                );
            }
        });

        binding.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                batchSettingBody.setStock(amount + "");
            }
        });

    }


    public void initView() {
    }

    @Override
    protected void lazyLoad() {
    }


    public void switchConfig(View view, boolean isConfig) {
        isConfig = !isConfig;
        switch (view.getId()) {
            case R.id.switch_giftConfig:
                binding.setIsConfig1(isConfig);
                break;
            case R.id.switch_priceConfig:
                binding.setIsConfig2(isConfig);
                break;
            case R.id.switch_gameConfig:
                binding.setIsConfig3(isConfig);
                break;
            case R.id.switch_glConfig:
                binding.setIsConfig4(isConfig);
                break;
            case R.id.switch_numConfig:
                binding.setIsConfig5(isConfig);
                break;
        }

    }


}
