package com.rjx.news.ui.head;

import com.rjx.base.viewmodel.MvvmBaseViewModel;
import com.rjx.news.bean.ChannelsModel;

/**
 * @author Jianxiong Rao
 */
public class HeadlineNewsViewModel extends MvvmBaseViewModel<ChannelsModel,ChannelsModel.Channel> {
    public HeadlineNewsViewModel(){
        model = new ChannelsModel();
        model.register(this);
        model.getCachedDataAndLoad();
    }
}
