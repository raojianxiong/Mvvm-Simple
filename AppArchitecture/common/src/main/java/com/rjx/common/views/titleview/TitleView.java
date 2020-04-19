package com.rjx.common.views.titleview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.rjx.base.customview.BaseCustomView;
import com.rjx.common.R;
import com.rjx.common.databinding.TitleViewBinding;
import com.rjx.common.views.WebViewActivity;

public class TitleView extends BaseCustomView<TitleViewBinding,TitleViewViewModel> {
    public TitleView(Context context) {
        super(context);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int setViewLayoutId() {
        return R.layout.title_view;
    }

    @Override
    protected void setDataToView(TitleViewViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    protected void onRootClick(View view) {
        WebViewActivity.startCommonWeb(view.getContext(),"", getViewModel().jumpUri);
    }

}
