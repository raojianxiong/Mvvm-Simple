package com.rjx.news.path;

import android.content.Context;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rjx.common.arouter.news.INewsService;
import com.rjx.news.ui.head.HeadlineNewsFragment;

/**
 * @author Jianxiong Rao
 */
@Route(path = INewsService.NEWS_SERVICE)
public class NewsService implements INewsService {
    @Override
    public Fragment getHeadlineNewFragment() {
        return new HeadlineNewsFragment();
    }

    @Override
    public void init(Context context) {

    }
}
