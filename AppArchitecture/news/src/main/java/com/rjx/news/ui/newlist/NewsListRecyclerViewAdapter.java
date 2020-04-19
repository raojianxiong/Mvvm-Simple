package com.rjx.news.ui.newlist;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rjx.base.customview.BaseCustomViewModel;
import com.rjx.base.recyclerview.BaseViewHolder;
import com.rjx.common.views.picturetitleview.PictureTitleView;
import com.rjx.common.views.picturetitleview.PictureTitleViewModel;
import com.rjx.common.views.titleview.TitleView;
import com.rjx.common.views.titleview.TitleViewViewModel;

import java.util.List;

/**
 * @author Jianxiong Rao
 */
public class NewsListRecyclerViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<BaseCustomViewModel> mItems;
    private final int VIEW_TYPE_PICTURE_TITLE = 1;
    private final int VIEW_TYPE_TITLE = 2;

    public NewsListRecyclerViewAdapter() {
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_PICTURE_TITLE) {
            PictureTitleView pictureTitleView = new PictureTitleView(parent.getContext());
            return new BaseViewHolder(pictureTitleView);
        } else if (viewType == VIEW_TYPE_TITLE) {
            TitleView titleView = new TitleView(parent.getContext());
            return new BaseViewHolder(titleView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        holder.bind(mItems.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof PictureTitleViewModel) {
            return VIEW_TYPE_PICTURE_TITLE;
        } else if (mItems.get(position) instanceof TitleViewViewModel) {
            return VIEW_TYPE_TITLE;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    void setData(List<BaseCustomViewModel> contentList) {
        mItems = contentList;
        notifyDataSetChanged();
    }
}
