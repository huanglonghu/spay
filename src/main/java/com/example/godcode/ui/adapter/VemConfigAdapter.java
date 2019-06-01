package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.godcode.R;
import com.example.godcode.bean.BindProduct;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.constant.Constant;
import com.example.godcode.databinding.ItemBindproductBinding;
import com.example.godcode.interface_.VemConfigStrategy;
import com.example.godcode.ui.view.widget.VemConfigDialog;
import java.util.ArrayList;
import java.util.HashMap;

public class VemConfigAdapter extends BaseAdapter {

    private final LayoutInflater inflater;
    private Context context;
    private int count = 20;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private String productNumber;
    private ArrayList<BindProduct.CommodityRoadBeanX.CommodityRoadBean> list = new ArrayList<>();


    public VemConfigAdapter(Context context, String productNumber) {
        this.context = context;
        this.productNumber = productNumber;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (viewMap.get(position) == null) {
            ItemBindproductBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_bindproduct, null, false);
            convertView = binding.getRoot();
            viewMap.put(position, convertView);
            BindProduct.CommodityRoadBeanX.CommodityRoadBean commodityRoadBean = new BindProduct.CommodityRoadBeanX.CommodityRoadBean();
            commodityRoadBean.setCommodityRoadNumber(position + 1);
            binding.setHdBean(commodityRoadBean);
            convertView.setTag(binding);
            binding.setProductNumber(productNumber);
            binding.edit.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_UP:
                            String presentName = "";
                            if (!TextUtils.isEmpty(binding.presentName.getText())) {
                                presentName = binding.presentName.getText().toString();
                            }
                            new VemConfigDialog.Builder()
                                    .context(context)
                                    .position(position)
                                    .commodityRoadBean(commodityRoadBean)
                                    .presentName(presentName)
                                    .vemconfigStrategy(new VemConfigStrategy() {
                                        @Override
                                        public void complete(BindProduct.CommodityRoadBeanX.CommodityRoadBean commodityRoadBean, String presentImgUrl, String presentName, int position) {
                                            list.add(commodityRoadBean);
                                            View view = getView(position, null, null);
                                            ItemBindproductBinding binding = (ItemBindproductBinding) view.getTag();
                                            binding.presentName.setText(presentName);
                                            if (!TextUtils.isEmpty(presentImgUrl)) {
                                                if (!presentImgUrl.contains("http")) {
                                                    presentImgUrl = Constant.baseUrl + presentImgUrl;
                                                }
                                                RxImageLoader.with(context).load(presentImgUrl).into(binding.iv);
                                            }
                                            binding.setHdBean(commodityRoadBean);
                                        }
                                    })
                                    .startIndex(0).build().show();

                    }
                    return true;
                }
            });
        }
        return viewMap.get(position);
    }

    public void refreshCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    public ArrayList<BindProduct.CommodityRoadBeanX.CommodityRoadBean> getList() {
        return list;
    }

    public void setList(ArrayList<BindProduct.CommodityRoadBeanX.CommodityRoadBean> list) {
        this.list = list;
    }

}
