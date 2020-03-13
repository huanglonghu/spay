package com.example.spay.ui.view.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.bean.BindProduct;
import com.example.spay.bean.PresentList;
import com.example.spay.bean.PresentOption;
import com.example.spay.bean.UploadResponse;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.LayoutVemBinding;
import com.example.spay.databinding.LayoutVemConfigBinding;
import com.example.spay.databinding.VemConfigAddBinding;
import com.example.spay.databinding.VemConfigEditBinding;
import com.example.spay.handler.ActivityResultHandler;
import com.example.spay.http.HttpUtil;
import com.example.spay.interface_.HandlerStrategy;
import com.example.spay.interface_.VemConfigStrategy;
import com.example.spay.presenter.Presenter;
import com.example.spay.ui.adapter.GiftListAdapter;
import com.example.spay.ui.adapter.PayViewPageAdapter;
import com.example.spay.ui.view.customview.AmountView;
import com.example.spay.utils.GsonUtil;
import com.example.spay.utils.MoneyTextWatcher;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MultipartBody;

public class VemConfigDialog extends Dialog {
    private LayoutVemBinding binding;
    private LayoutInflater inflater;
    private LayoutVemConfigBinding binding1;
    private VemConfigAddBinding binding2;
    private VemConfigEditBinding binding3;
    private Context context;
    private List<PresentList.ResultBean.ItemsBean> itemsBeanList;
    private GiftListAdapter giftListAdapter;
    private BindProduct.CommodityRoadBeanX.CommodityRoadBean commodityRoadBean;
    private String presentName;
    private String presentImgUrl;
    private int position;
    private int startIndex;
    private VemConfigStrategy vemConfigStrategy;


    public VemConfigDialog(Builder builder) {
        super(builder.context, R.style.dialog2);
        this.context = builder.context;
        this.commodityRoadBean = builder.commodityRoadBean;
        this.presentName = builder.presentName;
        this.position = builder.position;
        this.startIndex = builder.startInder;
        this.vemConfigStrategy = builder.vemConfigStrategy;
        init();
    }


    private void init() {
        initView();
        initVemPager();
        initListen();
    }

    private void initView() {
        inflater = LayoutInflater.from(context);
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_vem, null, false);
        binding.setDialog(this);
        getWindow().setWindowAnimations(R.style.popupStyle);
        setContentView(binding.getRoot());
        setCancelable(false);
    }

    private void initVemPager() {
        binding1 = DataBindingUtil.inflate(inflater, R.layout.layout_vem_config, null, false);
        binding1.etSell.addTextChangedListener(new MoneyTextWatcher(binding1.etSell));
        binding1.etGame.addTextChangedListener(new MoneyTextWatcher(binding1.etGame));
        binding2 = DataBindingUtil.inflate(inflater, R.layout.vem_config_add, null, false);
        binding3 = DataBindingUtil.inflate(inflater, R.layout.vem_config_edit, null, false);
        ArrayList<View> views = new ArrayList<>();
        views.add(binding1.getRoot());
        views.add(binding2.getRoot());
        views.add(binding3.getRoot());
        PayViewPageAdapter pageAdapter = new PayViewPageAdapter(views);
        binding.vemConfigPager.setAdapter(pageAdapter);
        if(startIndex == 0){
            binding1.setHdBean(commodityRoadBean);
            binding1.amountView.setAmount(commodityRoadBean.getCurrentStocks());
            binding1.setPresentName(presentName);
        }else {
            step2add();
        }
    }


    private void initListen() {
        binding1.presentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                step2add();
            }
        });

        binding3.presentPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入系统图库选择图片
                AppCompatActivity activity = (AppCompatActivity) VemConfigDialog.this.context;
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                new ActivityResultHandler.Builder().hadlerStrategy(new HandlerStrategy() {
                    @Override
                    public void onActivityResult(MultipartBody.Part filePart, Bitmap bitmap) {
                        upload(filePart, bitmap);
                    }
                }).requestCode(ActivityResultHandler.REQUEST_SELECT_PHOTO).intent(intent).activity(activity).build().startActivityForResult();
            }
        });

        binding2.lvGift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                PresentList.ResultBean.ItemsBean itemsBean = itemsBeanList.get(i);
                if (startIndex==0) {
                    presentName = itemsBean.getPresentName();
                    presentImgUrl = itemsBean.getPresentImgUrl();
                    binding1.setPresentName(presentName);
                    commodityRoadBean.setFK_PresentID(itemsBean.getId());
                    binding.setCurrentItem(0);
                    binding.vemConfigPager.setCurrentItem(0);
                } else if(startIndex ==1) {
                    dismiss();
                    vemConfigStrategy.complete(itemsBean.getPresentName(),itemsBean.getId(),itemsBean.getPresentImgUrl());
                }

            }
        });

        binding2.searchPresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchName = binding2.searchName.getText().toString();
                if (!TextUtils.isEmpty(searchName)) {
                    HttpUtil.getInstance().searchPresent(searchName).subscribe(
                            presentListStr -> {
                                refreshGiftList(presentListStr);
                            }
                    );
                } else {
                    HttpUtil.getInstance().getPresentListById().subscribe(
                            presentListStr -> {
                                refreshGiftList(presentListStr);
                            }
                    );
                }
            }
        });

        binding1.amountView.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                commodityRoadBean.setCurrentStocks(amount + "");
            }
        });

    }

    private void refreshGiftList(String presentListStr) {
        PresentList presentList = GsonUtil.fromJson(presentListStr, PresentList.class);
        List<PresentList.ResultBean.ItemsBean> items = presentList.getResult().getItems();
        itemsBeanList = items;
        if (giftListAdapter == null) {
            giftListAdapter = new GiftListAdapter(context, VemConfigDialog.this, itemsBeanList);
            binding2.lvGift.setAdapter(giftListAdapter);
        } else {
            giftListAdapter.refreshData(items);
        }
    }

    public void step2Edit(PresentList.ResultBean.ItemsBean bean) {
        PresentOption.PresentBean presentBean = new PresentOption.PresentBean();
        presentBean.setPresentName(bean.getPresentName());
        presentBean.setFK_UserID(Constant.userId);
        if (!TextUtils.isEmpty(bean.getCostPrice())) {
            presentBean.setCostPrice(bean.getCostPrice());
        }
        if (bean.getId() != 0) {
            presentBean.setId(bean.getId() + "");
        }
        String presentImgUrl = bean.getPresentImgUrl();
        if (!TextUtils.isEmpty(presentImgUrl)) {
            presentBean.setPresentImgUrl(bean.getPresentImgUrl());
            if (!presentImgUrl.contains("http")) {
                presentImgUrl = Constant.baseUrl + presentImgUrl;
            }
            RxImageLoader.with(context).load(presentImgUrl).into(binding3.presentPhoto);
        } else {
            binding3.presentPhoto.setImageDrawable(null);
        }
        binding3.setBean(presentBean);
        binding.setCurrentItem(2);
        binding.vemConfigPager.setCurrentItem(2);
    }


    public void clickBtn1(int currentItem) {
        switch (currentItem) {
            case 0:
                cancel();
                break;
            case 1:
                if (startIndex==1) {
                    dismiss();
                } else {
                    binding.setCurrentItem(0);
                    binding.vemConfigPager.setCurrentItem(0);
                }
                break;
            case 2:
                step2add();
                break;
        }
    }

    private void step2add() {
        binding.setCurrentItem(1);
        binding.vemConfigPager.setCurrentItem(1);
        HttpUtil.getInstance().getPresentListById().subscribe(
                presentListStr -> {
                    refreshGiftList(presentListStr);
                }
        );
    }

    public void clickBtn2(int currentItem) {
        switch (currentItem) {
            case 0:
                if (TextUtils.isEmpty(presentName)) {
                    Toast.makeText(context, "请先选择礼品", Toast.LENGTH_SHORT).show();
                    return;
                }

                String sellPrice = binding1.getHdBean().getSellPrice();
                if (TextUtils.isEmpty(sellPrice)) {
                    Toast.makeText(context, "请输入售卖价格", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (sellPrice.equals("0")) {
                    Toast.makeText(context, "售卖价格不能为0", Toast.LENGTH_SHORT).show();
                    return;
                }

                String gamePrice = binding1.getHdBean().getGamePrice();

                if (TextUtils.isEmpty(gamePrice)) {
                    Toast.makeText(context, "请输入游戏单价", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (gamePrice.equals("0")) {
                    Toast.makeText(context, "游戏单价不能为0", Toast.LENGTH_SHORT).show();
                    return;
                }
                vemConfigStrategy.complete(commodityRoadBean, presentImgUrl, presentName, position);
                dismiss();
                break;
            case 1:
                PresentList.ResultBean.ItemsBean bean = new PresentList.ResultBean.ItemsBean();
                step2Edit(bean);
                break;
            case 2://提交礼品
                PresentOption presentOption = new PresentOption();
                PresentOption.PresentBean presentBean = binding3.getBean();
                String presentName = presentBean.getPresentName();
                String presentPrice = presentBean.getCostPrice();
                if (TextUtils.isEmpty(presentName)) {
                    Toast.makeText(context, "请输入礼品名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(presentPrice)) {
                    Toast.makeText(context, "请输入礼品价格", Toast.LENGTH_SHORT).show();
                    return;
                }
                //如果有图片则上传 图片
                presentOption.setPresent(presentBean);
                HttpUtil.getInstance().createOrUpdatePresent(presentOption).subscribe(
                        a ->{
                            Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                            step2add();
                        }
                );
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = Presenter.getInstance().getWindowWidth();
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.5f;
        getWindow().setAttributes(layoutParams);
    }


    public void upload(MultipartBody.Part filePart, Bitmap bitmap) {
        binding3.presentPhoto.setImageBitmap(bitmap);
        HttpUtil.getInstance().upload(filePart, 4).subscribe(
                uploadStr -> {
                    UploadResponse uploadResponse = GsonUtil.getInstance().fromJson(uploadStr, UploadResponse.class);
                    String url = uploadResponse.getResult().get(0);
                    PresentOption.PresentBean bean = binding3.getBean();
                    bean.setPresentImgUrl(url);
                }
        );
    }


    public static class Builder {
        private Context context;
        private int position;
        private BindProduct.CommodityRoadBeanX.CommodityRoadBean commodityRoadBean;
        private String presentName;
        private int startInder;
        private VemConfigStrategy vemConfigStrategy;

        public Builder context(Context context) {
            this.context = context;
            return this;
        }

        public Builder position(int position) {
            this.position = position;
            return this;
        }

        public Builder commodityRoadBean(BindProduct.CommodityRoadBeanX.CommodityRoadBean commodityRoadBean) {
            this.commodityRoadBean = commodityRoadBean;
            return this;
        }

        public Builder presentName(String presentName) {
            this.presentName = presentName;
            return this;
        }

        public Builder startIndex(int startIndex) {
            this.startInder = startIndex;
            return this;
        }

        public Builder vemconfigStrategy(VemConfigStrategy vemConfigStrategy) {
            this.vemConfigStrategy = vemConfigStrategy;
            return this;
        }

        public VemConfigDialog build() {
            return new VemConfigDialog(this);
        }
    }
}
