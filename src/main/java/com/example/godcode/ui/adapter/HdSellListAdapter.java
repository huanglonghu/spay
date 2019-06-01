package com.example.godcode.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.godcode.bean.SellGoodsOrder;

import java.util.List;

public class HdSellListAdapter extends BaseAdapter {
    private List<SellGoodsOrder.ResultBean> result;
    private String productNumber;
    private Context context;

    public HdSellListAdapter(Context context,List<SellGoodsOrder.ResultBean> result,String productNumber){
        this.context=context;
        this.result=result;
        this.productNumber=productNumber;
    }
    @Override
    public int getCount() {
        return result==null?0:result.size();
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
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
        SellGoodsOrder.ResultBean resultBean = result.get(position);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER_VERTICAL|Gravity.RIGHT;
        textView.setLayoutParams(layoutParams);
        textView.setText(productNumber+"-"+resultBean.getPosition()+"*"+resultBean.getCount());
        convertView=textView;
        return convertView;
    }
}
