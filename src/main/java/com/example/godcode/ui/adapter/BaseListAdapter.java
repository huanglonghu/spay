package com.example.godcode.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.HashMap;
import java.util.List;

public  abstract class BaseListAdapter<T> extends BaseAdapter{

    private List<T> datas;
    public Context context;
    private HashMap<Integer,View> viewMap=new HashMap<>();
    private final LayoutInflater layoutInflater;
    private int res;

    public BaseListAdapter(Context context, List<T> datas,int res){
          this.datas=datas;
          this.context=context;
          this.res=res;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public int getCount() {
        return datas==null?0:datas.size();
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
            View view = initView(layoutInflater,res,datas,position);
            viewMap.put(position,view);
        }
        return viewMap.get(position);
    }

     abstract View initView(LayoutInflater layoutInflater,int res,List<T> datas,int position);


    public void clearView(){
        viewMap.clear();
    }




}
