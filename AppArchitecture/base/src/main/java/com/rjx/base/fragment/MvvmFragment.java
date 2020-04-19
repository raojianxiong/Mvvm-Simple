package com.rjx.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.rjx.base.R;
import com.rjx.base.loadsir.EmptyCallback;
import com.rjx.base.loadsir.ErrorCallback;
import com.rjx.base.loadsir.LoadingCallback;
import com.rjx.base.util.ToastUtil;
import com.rjx.base.viewmodel.MvvmBaseViewModel;
import com.rjx.base.viewmodel.ViewStatus;

public abstract class MvvmFragment<V extends ViewDataBinding, VM extends MvvmBaseViewModel, D> extends Fragment implements Observer {
    protected VM viewModel;
    protected V viewDataBinding;
    protected String mFragmentTag = "";
    private LoadService mLoadService;

    public abstract int getBindingVariable();

    public abstract int getLayoutId();

    public abstract VM getViewModel();

    public abstract void onListItemInserted(ObservableList<D> sender);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParameters();
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewDataBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = getViewModel();
        getLifecycle().addObserver(viewModel);
        if (viewModel != null) {
            viewModel.dataList.observe(this, this);
            viewModel.viewStatusLiveData.observe(this, this);
        }
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
            viewDataBinding.executePendingBindings();
        }
    }

    /**
     * 初始化参数
     */
    protected void initParameters() {

    }

    @Override
    public void onChanged(Object o) {
        if (o instanceof ViewStatus && mLoadService != null) {
            switch ((ViewStatus) o) {
                case LOADING:
                    mLoadService.showCallback(LoadingCallback.class);
                    break;
                case EMPTY:
                    mLoadService.showCallback(EmptyCallback.class);
                    break;
                case SHOW_CONTENT:
                    mLoadService.showSuccess();
                    break;
                case NO_MORE_DATA:
                    ToastUtil.show(getString(R.string.no_more_data));
                    break;
                case REFRESH_ERROR:
                    if (((ObservableArrayList) viewModel.dataList.getValue()).size() == 0) {
                        mLoadService.showCallback(ErrorCallback.class);
                    } else {
                        ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    }
                    break;
                case LOAD_MORE_FAILED:
                    ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    break;
                default:
                    break;
            }
        } else if (o instanceof ObservableArrayList) {
            onListItemInserted((ObservableArrayList<D>) o);
        }
    }

    public void setLoadSir(View view) {
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {

            }
        });
    }

    protected abstract void onRetryBtnClick();

    protected void showSuccess() {
        if (mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

    protected void showLoading() {
        if (mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }
}
