package com.rjx.common.arouter.news;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.template.IProvider;

/**
 * @author Jianxiong Rao
 */
public interface INewsService extends IProvider {
    String NEW_ROUTER  = "/news/";
    String NEWS_SERVICE = NEW_ROUTER + "news_service";
    Fragment getHeadlineNewFragment();
}
