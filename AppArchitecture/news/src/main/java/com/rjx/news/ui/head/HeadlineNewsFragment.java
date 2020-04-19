package com.rjx.news.ui.head;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.google.android.material.tabs.TabLayout;
import com.rjx.base.model.IBaseModelListener;
import com.rjx.base.model.MvvmBaseModel;
import com.rjx.base.model.PagingResult;
import com.rjx.news.R;
import com.rjx.news.bean.ChannelsModel;
import com.rjx.news.databinding.FragmentHomeBinding;

import java.util.ArrayList;

/**
 * @author Jianxiong Rao
 */
public class HeadlineNewsFragment extends Fragment{
    public HeadlineNewsFragmentAdapter adapter;
    FragmentHomeBinding viewDataBinding;
    ChannelsModel model = new ChannelsModel();
    HeadlineNewsViewModel viewModel = new HeadlineNewsViewModel();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        adapter = new HeadlineNewsFragmentAdapter(getChildFragmentManager());
        viewDataBinding.tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewDataBinding.viewpager.setAdapter(adapter);
        viewDataBinding.tablayout.setupWithViewPager(viewDataBinding.viewpager);
        viewDataBinding.viewpager.setOffscreenPageLimit(1);
        viewModel.dataList.observe(this, new Observer<ObservableList<ChannelsModel.Channel>>() {
            @Override
            public void onChanged(ObservableList<ChannelsModel.Channel> channels) {
                synchronized (this){
                    adapter.setChannels(channels);
                }
            }
        });
        return viewDataBinding.getRoot();

    }


}
