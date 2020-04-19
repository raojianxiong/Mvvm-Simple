package com.rjx.common.views.picturetitleview;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.rjx.base.customview.BaseCustomView;
import com.rjx.common.R;
import com.rjx.common.databinding.PictureTitleViewBinding;
import com.rjx.common.views.WebViewActivity;

public class PictureTitleView extends BaseCustomView<PictureTitleViewBinding,PictureTitleViewModel> {
    public PictureTitleView(Context context) {
        super(context);
    }

    public PictureTitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PictureTitleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PictureTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int setViewLayoutId() {
        return R.layout.picture_title_view;
    }

    @Override
    protected void setDataToView(PictureTitleViewModel data) {
        getDataBinding().setViewModel(data);
    }

    @Override
    protected void onRootClick(View view) {
        WebViewActivity.startCommonWeb(view.getContext(),"",getViewModel().jumpUri);
    }
}
