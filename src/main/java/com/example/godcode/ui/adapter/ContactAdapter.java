package com.example.godcode.ui.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.catche.Loader.RxImageLoader;
import com.example.godcode.databinding.ItemFootBinding;
import com.example.godcode.databinding.ItemFriendBinding;
import com.example.godcode.databinding.ItemHeadBinding;
import com.example.godcode.greendao.entity.Friend;
import com.example.godcode.presenter.Presenter;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.PingYingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private LayoutInflater mLayoutInflater;
    private Presenter presenter;
    private int type;
    private List<Friend> friendList;
    private int HEAD_COUNT;
    private int FOOT_COUNT;
    private final int TYPE_HEAD = 0;
    private final int TYPE_CONTENT = 1;
    private final int TYPE_FOOTER = 2;
    private final int TYPE_LETTER = 3;
    private Context context;
    private HashMap<Integer,String> charcterMap=new HashMap<>();


    public ContactAdapter(Context context, List<Friend> friendList, Presenter presenter, int type) {
        mLayoutInflater = LayoutInflater.from(context);
        this.friendList = friendList;
        this.presenter = presenter;
        this.type = type;
        this.context = context;
        switch (type) {
            case 1:
                HEAD_COUNT = 1;
                FOOT_COUNT = 1;
                break;
            default:
                HEAD_COUNT = 0;
                FOOT_COUNT = 0;
                break;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            ItemHeadBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_head, parent, false);
            HeadViewHolder headViewHolder = new HeadViewHolder(binding);
            return headViewHolder;
        } else if (viewType == TYPE_FOOTER) {
            ItemFootBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_foot, parent, false);
            FootViewHolder footViewHolder = new FootViewHolder(binding);
            return footViewHolder;
        } else {
            ItemFriendBinding binding = DataBindingUtil.inflate(mLayoutInflater, R.layout.item_friend, parent, false);
            FriendHolder friendHolder = new FriendHolder(binding);
            return friendHolder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadViewHolder) {
            ItemHeadBinding binding = ((HeadViewHolder) holder).getBinding();
            if (position == 0) {
                binding.headName.setText("新的朋友");
                binding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.step2Fragment("newFriend");
                        presenter.showNew(2);
                    }
                });
            }
        } else if (holder instanceof FriendHolder) {
            Friend friend = friendList.get(position - HEAD_COUNT);
            ItemFriendBinding binding = ((FriendHolder) holder).getBinding();
            String headImageUrl = friend.getHeadImageUrl();
            String firstChar = friend.getFirstChar();

            if(!charcterMap.containsValue(firstChar)){
                LogUtil.log("------firstChar---------"+firstChar);
                binding.setCharacter(firstChar);
                charcterMap.put(position,firstChar);
                LogUtil.log("------------charcterList-----------"+firstChar);
            }
            if (!TextUtils.isEmpty(headImageUrl)) {
                if (!headImageUrl.contains("http")) {
                    headImageUrl = Constant.baseUrl + headImageUrl;
                }
                RxImageLoader.with(context).load(headImageUrl).into(binding.friendPhoto);
            } else {
                binding.friendPhoto.setBackgroundResource(R.drawable.contact_normal);
            }
            binding.setType(type);
            binding.setFriend(friend);
            binding.setPresenter(presenter);
        } else if (holder instanceof FootViewHolder) {
            ItemFootBinding binding = ((FootViewHolder) holder).getBinding();
            binding.foot.setText(friendList.size() + "位好友");
        }
    }

    @Override
    public int getItemViewType(int position) {
        int contentSize = friendList.size();
        if (HEAD_COUNT > 0) {
            if (position < HEAD_COUNT) { // 头部
                return TYPE_HEAD;
            } else { // 尾部
                if (position < HEAD_COUNT + contentSize) {
                    return TYPE_CONTENT;
                } else {
                    return TYPE_FOOTER;
                }
            }
        } else {
            return TYPE_CONTENT;
        }

    }

    @Override
    public int getItemCount() {
        return friendList.size() + HEAD_COUNT + FOOT_COUNT;
    }

    private class FriendHolder extends RecyclerView.ViewHolder {
        public ItemFriendBinding getBinding() {
            return binding;
        }

        private ItemFriendBinding binding;

        public FriendHolder(ItemFriendBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private class HeadViewHolder extends RecyclerView.ViewHolder {
        private ItemHeadBinding binding;

        public HeadViewHolder(ItemHeadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemHeadBinding getBinding() {
            return binding;
        }


    }

    private class FootViewHolder extends RecyclerView.ViewHolder {
        private ItemFootBinding binding;

        public FootViewHolder(ItemFootBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public ItemFootBinding getBinding() {
            return binding;
        }
    }


    public int getScrollPosition(String character) {
        int position=-1;
        LogUtil.log(charcterMap.containsValue(character)+"----------ddd-------"+character);
        String s = character.toUpperCase();
        if (charcterMap.containsValue(s)) {
           for (int i:charcterMap.keySet()){
               if(charcterMap.get(i).equals(s)){
                   position=i;
                   break;
               }
           }
        }

        return position; // -1不会滑动
    }




    public void clear(){
        charcterMap.clear();
    }


}
