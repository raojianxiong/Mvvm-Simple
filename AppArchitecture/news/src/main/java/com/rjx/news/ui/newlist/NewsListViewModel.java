package com.rjx.news.ui.newlist;

import com.rjx.base.customview.BaseCustomViewModel;
import com.rjx.base.viewmodel.MvvmBaseViewModel;
import com.rjx.news.bean.NewsListModel;

/**
 * @author Jianxiong Rao
 */
public class NewsListViewModel extends MvvmBaseViewModel<NewsListModel, BaseCustomViewModel> {
    public NewsListViewModel init(String classId,String lboClassId){
        model = new NewsListModel(classId,lboClassId);
        model.register(this);
        model.getCachedDataAndLoad();
        return this;
    }

    public void tryToLoadNextPage(){
        model.loadNextPage();
    }
}
