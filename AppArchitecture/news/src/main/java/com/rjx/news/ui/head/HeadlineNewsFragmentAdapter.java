package com.rjx.news.ui.head;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rjx.news.bean.ChannelsModel;
import com.rjx.news.ui.newlist.NewsListFragment;

import java.util.HashMap;

/**
 * @author Jianxiong Rao
 */
public class HeadlineNewsFragmentAdapter extends FragmentPagerAdapter {

    private ObservableList<ChannelsModel.Channel> mChannels;
    private HashMap<String, Fragment> fragmentHashMap = new HashMap<>();

    public HeadlineNewsFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        String key = mChannels.get(position).channelId + ":" + mChannels.get(position).channelName;
        if (fragmentHashMap.get(key) != null) {
            return fragmentHashMap.get(key);
        }
        Fragment fragment = NewsListFragment.newInstance(mChannels.get(position).channelId, mChannels.get(position).channelName);
        fragmentHashMap.put(key, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return mChannels == null ? 0 : mChannels.size();
    }

    public void setChannels(ObservableList<ChannelsModel.Channel> channels) {
        this.mChannels = new ObservableArrayList<>();
        this.mChannels.addAll(channels);
        notifyDataSetChanged();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return mChannels.get(position).channelName;
    }
}
