package com.rjx.news.ui.newlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableList;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rjx.base.customview.BaseCustomViewModel;
import com.rjx.base.fragment.MvvmFragment;
import com.rjx.base.model.IBaseModelListener;
import com.rjx.base.model.MvvmBaseModel;
import com.rjx.base.model.PagingResult;
import com.rjx.news.R;
import com.rjx.news.bean.NewsListModel;
import com.rjx.news.databinding.FragmentNewsBinding;
import com.scwang.smartrefresh.header.WaterDropHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;


/**
 * @author Jianxiong Rao
 */
public class NewsListFragment extends MvvmFragment<FragmentNewsBinding, NewsListViewModel, BaseCustomViewModel> {
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_ID = "bundle_key_param_channel_id";
    protected final static String BUNDLE_KEY_PARAM_CHANNEL_NAME = "bundle_key_param_channel_name";
    private String mChanneId = "";
    private String mChannelName = "";

    private NewsListRecyclerViewAdapter adapter;

    public static NewsListFragment newInstance(String channelId, String channelName) {
        NewsListFragment fragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_ID, channelId);
        bundle.putString(BUNDLE_KEY_PARAM_CHANNEL_NAME, channelName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_news;
    }

    @Override
    public int getBindingVariable() {
        return 0;
    }

    @Override
    public NewsListViewModel getViewModel() {
        if(viewModel == null){
            viewModel = new ViewModelProvider(this).get(getFragmentTag(),NewsListViewModel.class).init(mChanneId,mChannelName);
        }
        return viewModel;
    }

    @Override
    public void onListItemInserted(ObservableList<BaseCustomViewModel> sender) {
        adapter.setData(sender);
        viewDataBinding.refreshLayout.finishLoadMore();
        viewDataBinding.refreshLayout.finishRefresh();
        showSuccess();
    }

    @Override
    protected void initParameters() {
        if(getArguments() != null){
            mChanneId = getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_ID);
            mChannelName = getArguments().getString(BUNDLE_KEY_PARAM_CHANNEL_NAME);
            mFragmentTag = mChannelName;
        }
    }

    @Override
    protected void onRetryBtnClick() {
        viewModel.tryToRefresh();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        adapter = new NewsListRecyclerViewAdapter();

        viewDataBinding.recyclerView.setHasFixedSize(true);
        viewDataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewDataBinding.recyclerView.setAdapter(adapter);

        viewDataBinding.refreshLayout.setRefreshHeader(new WaterDropHeader(getContext()));
        viewDataBinding.refreshLayout.setRefreshFooter(new BallPulseFooter(getContext()).setSpinnerStyle(SpinnerStyle.Scale));
        viewDataBinding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                viewModel.tryToRefresh();
            }
        });
        viewDataBinding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                viewModel.tryToLoadNextPage();
            }
        });
        setLoadSir(viewDataBinding.refreshLayout);

        showLoading();
    }


    protected String getFragmentTag() {
        return mChannelName;
    }
}
