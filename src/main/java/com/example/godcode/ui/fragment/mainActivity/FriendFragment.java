package com.example.godcode.ui.fragment.mainActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.godcode.R;
import com.example.godcode.databinding.FragmentFriendBinding;
import com.example.godcode.databinding.ItemHeadBinding;
import com.example.godcode.greendao.entity.Friend;
import com.example.godcode.greendao.option.FriendOption;
import com.example.godcode.ui.adapter.ContactAdapter;
import com.example.godcode.ui.base.BaseFragment;
import com.example.godcode.ui.base.Constant;
import com.example.godcode.ui.fragment.deatailFragment.SearchFragment;
import com.example.godcode.ui.view.DividerItemDecoration;
import com.example.godcode.ui.view.LetterView;
import com.example.godcode.utils.LogUtil;
import com.example.godcode.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class FriendFragment extends BaseFragment {
    private View view;
    private RecyclerView contactList;
    private int type;
    private FragmentFriendBinding binding;
    private ContactAdapter adapter;
    private List<Friend> friendList;
    private LinearLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false);
            view = binding.getRoot();
            initData();
            initView();
            initListener();
        }
        return view;
    }

    private void initData() {
        FriendOption.getInstance(activity).querryFriendList(1, true);
        FriendOption.getInstance(activity).friendUpdateListener().subscribe(
                update -> {
                    if (update) {
                        LogUtil.log("-------好友---更新-----");
                        List<Friend> allFriend = FriendOption.getInstance(activity).getAllFriend(Constant.userId);
                        adapter.clear();
                        layoutManager.removeAllViews();
                        friendList.clear();
                        friendList.addAll(allFriend);
                        for (int i = 0; i < friendList.size(); i++) {
                            LogUtil.log("======firstChar========" + friendList.get(i).getFirstChar());
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
        );
    }


    private void initListener() {
        binding.searchFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchFragment searchFragment = new SearchFragment();
                presenter.step2Fragment(searchFragment, "search");
            }
        });

    }

    private void initView() {
        friendList = new ArrayList<>();
        contactList = (RecyclerView) view.findViewById(R.id.friend_list);
        LetterView letterView = (LetterView) view.findViewById(R.id.letter_view);
        layoutManager = new LinearLayoutManager(getActivity());
        adapter = new ContactAdapter(getActivity(), friendList, presenter, type);
        contactList.setLayoutManager(layoutManager);
        contactList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        contactList.setAdapter(adapter);
        letterView.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                ToastUtil.getInstance().showToast(character, 1000, activity);
                int scrollPosition = adapter.getScrollPosition(character);
                LogUtil.log("-------scrllPosition-------=" + scrollPosition);
//                if (scrollPosition != -1) {
//                    View view = layoutManager.findViewByPosition(scrollPosition);
//                    int top = view.getTop();
//                    layoutManager.scrollToPositionWithOffset(scrollPosition, top);
//                }


            }

            @Override
            public void clickArrow() {
                layoutManager.scrollToPositionWithOffset(0, 0);
            }
        });

    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void refreshData() {
        friendList.clear();
        friendList.addAll(FriendOption.getInstance(activity).getAllFriend(Constant.userId));
        for (int i = 0; i < friendList.size(); i++) {
            LogUtil.log("======firstChar========" + friendList.get(i).getFirstChar());
        }
        adapter.notifyDataSetChanged();
    }

    public void showNews(int count) {
        View view = layoutManager.findViewByPosition(0);
        ItemHeadBinding binding = DataBindingUtil.findBinding(view);
        binding.setNewsCount(count);
    }

}
