package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.example.spay.R;
import com.example.spay.bean.PresentList;
import com.example.spay.catche.Loader.RxImageLoader;
import com.example.spay.constant.Constant;
import com.example.spay.databinding.ItemLvGiftBinding;
import com.example.spay.http.HttpUtil;
import com.example.spay.ui.view.widget.VemConfigDialog;

import java.util.HashMap;
import java.util.List;

public class GiftListAdapter extends BaseAdapter {

    private Context context;
    private final LayoutInflater inflater;
    private HashMap<Integer, View> viewMap = new HashMap<>();
    private VemConfigDialog dialog;
    List<PresentList.ResultBean.ItemsBean> items;

    public GiftListAdapter(Context context, VemConfigDialog dialog, List<PresentList.ResultBean.ItemsBean> items) {
        this.context = context;
        this.dialog = dialog;
        this.items = items;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
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
            ItemLvGiftBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_lv_gift, null, false);
            convertView = binding.getRoot();
            binding.setDialog(dialog);
            PresentList.ResultBean.ItemsBean itemsBean = items.get(position);
            binding.setItem(itemsBean);
            viewMap.put(position, convertView);
            String presentImgUrl = itemsBean.getPresentImgUrl();
            if (!TextUtils.isEmpty(presentImgUrl)) {
                if (!presentImgUrl.contains("http")) {
                    presentImgUrl = Constant.baseUrl + presentImgUrl;
                }
                RxImageLoader.with(context).load(presentImgUrl).into(binding.ivGift);
            }
            binding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HttpUtil.getInstance().deletePresent(itemsBean.getId()).subscribe(
                            a -> {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                items.remove(position);
                                notifyDataSetChanged();
                            }
                    );
                }
            });
        }

        return viewMap.get(position);
    }


    public void refreshData(List<PresentList.ResultBean.ItemsBean> items ) {
        this.items=items;
        viewMap.clear();
        notifyDataSetChanged();
    }
}
