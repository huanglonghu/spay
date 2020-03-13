package com.example.spay.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.example.spay.R;
import com.example.spay.databinding.ItemLvServiceremainderBinding;
import com.example.spay.greendao.entity.Notification;
import java.util.HashMap;
import java.util.List;

public class ServiceRemainderListAdapter extends BaseAdapter{
    private HashMap<Integer,View> viewMap=new HashMap<>();
    private List<Notification> notifications;
    private final LayoutInflater layoutInflater;
    private String[] typeArray = {"", "转账支出", "二维码收款", "二维码付款", "商户消费", "充值", "提现", "退款", "转账收入", "商户收入"};


    public ServiceRemainderListAdapter(List<Notification> notifications, Context context){
        this.notifications=notifications;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return notifications.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(viewMap.get(position) == null){
            ItemLvServiceremainderBinding binding=DataBindingUtil.inflate(layoutInflater,R.layout.item_lv_serviceremainder,parent,false);
            Notification notification = notifications.get(position);
            int type = notification.getType();
            binding.serviceRemaimderTitle.setText(notification.getTitle());
            binding.setNotification(notification);
            convertView=binding.getRoot();
            viewMap.put(position,convertView);
        }
        return viewMap.get(position);
    }


}
