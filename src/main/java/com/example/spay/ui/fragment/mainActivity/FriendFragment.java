package com.example.spay.ui.fragment.mainActivity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.spay.R;
import com.example.spay.databinding.FragmentFriendBinding;
import com.example.spay.databinding.ItemHeadBinding;
import com.example.spay.databinding.LayoutMainPopupBinding;
import com.example.spay.greendao.entity.Friend;
import com.example.spay.greendao.option.FriendOption;
import com.example.spay.ui.adapter.ContactAdapter;
import com.example.spay.ui.base.BaseFragment;
import com.example.spay.constant.Constant;
import com.example.spay.ui.view.LetterView;
import com.example.spay.ui.view.MenuWindow;
import com.example.spay.utils.ToastUtil;
import java.util.List;

public class FriendFragment extends BaseFragment {
    private View view;
    private int type;
    private FragmentFriendBinding binding;
    private ContactAdapter adapter;
    private MenuWindow menuWindow;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_friend, container, false);
            view = binding.getRoot();
            binding.setTitle("朋友");
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
                        adapter.clear();
                        List<Friend> friendList = FriendOption.getInstance(activity).getAllFriend(Constant.userId);
                        adapter.refreshData(friendList);
                    }
                }
        );
    }


    private void initListener() {

        binding.friendToolBar.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow = new MenuWindow(activity);
                LayoutMainPopupBinding binding = menuWindow.getBinding1();
                binding.setFragment(FriendFragment.this);
                menuWindow.show(v);
            }
        });

    }

    private void initView() {
        LetterView letterView = (LetterView) view.findViewById(R.id.letter_view);
        adapter = new ContactAdapter(getActivity(), presenter, type);
        binding.friendList.setAdapter(adapter);
        letterView.setCharacterListener(new LetterView.CharacterClickListener() {
            @Override
            public void clickCharacter(String character) {
                ToastUtil.getInstance().showToast(character, 1000, activity);
                int scrollPosition = adapter.getScrollPosition(character);
                if (scrollPosition != -1) {
                    binding.friendList.setSelection(scrollPosition);
                }
            }

            @Override
            public void clickArrow() {
                binding.friendList.setSelection(0);
            }
        });

    }

    @Override
    protected void lazyLoad() {

    }

    public void refreshData() {
        adapter.clear();
        List<Friend> friendList = FriendOption.getInstance(activity).getAllFriend(Constant.userId);
        adapter.refreshData(friendList);
    }

    public void showNews(int count) {
        View view = adapter.getView(0, null, null);
        ItemHeadBinding binding = DataBindingUtil.findBinding(view);
        binding.setNewsCount(count);
    }


    public void config(View view) {
        switch (view.getId()) {
            case R.id.mainPopup_addFriend:
                presenter.step2Fragment("addFriend");
                break;
            case R.id.mainPopup_sys:
                presenter.sys();
                break;
            case R.id.mainPopup_gathering:
                presenter.step2Fragment("gathering");
                break;
        }
        menuWindow.dismiss();
    }

}
