package com.rjx.base.recyclerview;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.rjx.base.customview.BaseCustomViewModel;
import com.rjx.base.customview.ICustomView;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    ICustomView view;
    public BaseViewHolder(@NonNull ICustomView itemView) {
        super((View) itemView);
        this.view = itemView;
    }

    public void bind(@Nullable BaseCustomViewModel item){
        view.setData(item);
    }
}
