package com.rjx.base.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseCustomView<T extends ViewDataBinding, S extends BaseCustomViewModel> extends LinearLayout implements ICustomView<S>, View.OnClickListener {
    private T dataBinding;
    private S viewModel;
    private ICustomViewActionListener mListener;

    public BaseCustomView(Context context) {
        super(context);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseCustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public BaseCustomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (setViewLayoutId() != 0) {
            dataBinding = DataBindingUtil.inflate(inflater, setViewLayoutId(), this, false);
            dataBinding.getRoot().setOnClickListener(
                    new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mListener != null) {
                                mListener.onAction(ICustomViewActionListener.ACTION_ROOT_VIEW_CLICKED, v, viewModel);
                            }
                            onRootClick(v);
                        }
                    }
            );
            this.addView(dataBinding.getRoot());
        }
    }

    @Override
    public View getRootView() {
        return dataBinding.getRoot();
    }

    @Override
    public void setData(S data) {
        viewModel = data;
        setDataToView(viewModel);
        if (dataBinding != null) {
            dataBinding.executePendingBindings();
        }
        onDataUpdated();
    }

    protected void onDataUpdated() {
    }

    @Override
    public void setActionListener(ICustomViewActionListener listener) {
        this.mListener = listener;
    }

    @Override
    public void onClick(View v) {
    }

    protected T getDataBinding() {
        return dataBinding;
    }

    protected S getViewModel() {
        return viewModel;
    }

    @Override
    public void setStyle(int resId) {

    }

    protected abstract int setViewLayoutId();

    protected abstract void setDataToView(S data);

    protected abstract void onRootClick(View view);
}
