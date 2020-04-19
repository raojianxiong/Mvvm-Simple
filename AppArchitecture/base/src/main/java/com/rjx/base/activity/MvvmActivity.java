package com.rjx.base.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ViewDataBinding;
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


/**
 * @author Jianxiong Rao
 */
public abstract class MvvmActivity<V extends ViewDataBinding, VM extends MvvmBaseViewModel> extends AppCompatActivity implements Observer {

    private VM viewModel;
    private LoadService mLoadService;
    protected V viewDataBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViewModel();
        performDataBinding();
    }

    public void setLoadSir(View view){
        mLoadService = LoadSir.getDefault().register(view, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                onRetryBtnClick();
            }
        });
    }

    private void initViewModel() {
        viewModel = getViewModel();
    }
    protected abstract void onRetryBtnClick();

    protected abstract VM getViewModel();

    protected abstract int getLayoutId();

    protected abstract int getBindingVariable();

    private void performDataBinding() {
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.viewModel = viewModel == null ? getViewModel() : viewModel;
        if (getBindingVariable() > 0) {
            viewDataBinding.setVariable(getBindingVariable(), viewModel);
        }
        viewDataBinding.executePendingBindings();
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
                    if(((ObservableArrayList)viewModel.dataList.getValue()).size() ==0){
                        mLoadService.showCallback(ErrorCallback.class);
                    }else{
                        ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    }
                    break;
                case LOAD_MORE_FAILED:
                    ToastUtil.show(viewModel.errorMessage.getValue().toString());
                    break;
                default:
                    break;
            }
        }
    }
}
