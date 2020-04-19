package com.rjx.base.viewmodel;

import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableList;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.rjx.base.model.IBaseModelListener;
import com.rjx.base.model.MvvmBaseModel;
import com.rjx.base.model.PagingResult;

import java.util.List;

/**
 * @author Jianxiong Rao
 */
public abstract class MvvmBaseViewModel<M extends MvvmBaseModel, D> extends ViewModel implements LifecycleObserver, IBaseModelListener<List<D>> {
    protected M model;
    public MutableLiveData<ObservableList<D>> dataList = new MutableLiveData<>();
    public MutableLiveData<ViewStatus> viewStatusLiveData = new MutableLiveData<ViewStatus>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MvvmBaseViewModel() {
        dataList.setValue(new ObservableArrayList<>());
        viewStatusLiveData.setValue(ViewStatus.LOADING);
        errorMessage.setValue("");
    }

    public void tryToRefresh() {
        if (model != null) {
            model.refresh();
        }
    }

    @Override
    protected void onCleared() {
        if (model != null) {
            model.cancel();
        }
    }

    @Override
    public void onLoadFinish(MvvmBaseModel model, List<D> data, PagingResult... pageResult) {
        if (model == this.model) {
            if (model.isPaging()) {
                if (pageResult[0].isFirstPage) {
                    dataList.getValue().clear();
                }
                if (pageResult[0].isEmpty) {
                    if (pageResult[0].isFirstPage) {
                        viewStatusLiveData.setValue(ViewStatus.EMPTY);
                    } else {
                        viewStatusLiveData.setValue(ViewStatus.NO_MORE_DATA);
                    }
                } else {
                    dataList.getValue().addAll(data);
                    dataList.postValue(dataList.getValue());
                    viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT);
                }
            } else {
                dataList.getValue().clear();
                dataList.getValue().addAll(data);
                dataList.postValue(dataList.getValue());
                viewStatusLiveData.setValue(ViewStatus.SHOW_CONTENT);
            }
            viewStatusLiveData.postValue(viewStatusLiveData.getValue());
        }
    }

    @Override
    public void onLoadFail(MvvmBaseModel model, String prompt, PagingResult... pageResult) {
        errorMessage.setValue(prompt);
        if (model.isPaging() && !pageResult[0].isFirstPage) {
            viewStatusLiveData.setValue(ViewStatus.LOAD_MORE_FAILED);
        } else {
            viewStatusLiveData.setValue(ViewStatus.REFRESH_ERROR);
        }
        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private void onResume() {
        dataList.postValue(dataList.getValue());
        viewStatusLiveData.postValue(viewStatusLiveData.getValue());
    }
}
