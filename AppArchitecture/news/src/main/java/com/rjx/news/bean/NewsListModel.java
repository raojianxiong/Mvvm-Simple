package com.rjx.news.bean;

import android.annotation.SuppressLint;

import com.rjx.base.customview.BaseCustomViewModel;
import com.rjx.base.model.MvvmBaseModel;
import com.rjx.common.views.picturetitleview.PictureTitleViewModel;
import com.rjx.common.views.titleview.TitleViewViewModel;
import com.rjx.network.AppNetworkApi;
import com.rjx.network.observer.BaseObserver;
import com.rjx.news.api.NewsApiInterface;
import com.rjx.news.api.NewsListBean;

import java.util.ArrayList;

public class NewsListModel extends MvvmBaseModel<NewsListBean, ArrayList<BaseCustomViewModel>> {
    private String mChannelId = "";
    private String mChannelName = "";

    public NewsListModel(String channelId, String channelName) {
        super(NewsListBean.class, true, "pref_key_news_" + channelId, null, 1);
        mChannelId = channelId;
        mChannelName = channelName;
    }

    @Override
    public void refresh() {
        isRefresh = true;
        load();
    }

    public void loadNextPage() {
        isRefresh = false;
        load();
    }


    @SuppressLint("CheckResult")
    @Override
    protected void load() {
        AppNetworkApi.getService(NewsApiInterface.class)
                .getNewsList(mChannelId, mChannelName, String.valueOf(isRefresh ? 1 : pageNumber))
                .compose(AppNetworkApi.getInstance().applySchedulers(new BaseObserver<NewsListBean>(this, this)));

    }

    @Override
    public void onSuccess(NewsListBean t, boolean isFromCache) {

        ArrayList<BaseCustomViewModel> baseViewModels = new ArrayList<>();

        for (NewsListBean.Contentlist source : t.showapiResBody.pagebean.contentlist) {
            if (source.imageurls != null && source.imageurls.size() > 1) {
                PictureTitleViewModel viewModel = new PictureTitleViewModel();
                viewModel.avatarUrl = source.imageurls.get(0).url;
                viewModel.jumpUri = source.link;
                viewModel.title = source.title;
                baseViewModels.add(viewModel);
            } else {
                TitleViewViewModel viewModel = new TitleViewViewModel();
                viewModel.jumpUri = source.link;
                viewModel.title = source.title;
                baseViewModels.add(viewModel);
            }
        }
        loadSuccess(t,baseViewModels,isFromCache);
    }

    @Override
    public void onFailure(Throwable e) {

    }
}
