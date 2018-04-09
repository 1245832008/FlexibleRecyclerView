package com.lmj.flexiblerecyclerview;

import android.content.Context;
import android.view.View;

import com.lmj.flexiblerecyclerview.expandablerecyclerview.holder.BaseViewHolder;

/**
 * author: lmj
 * date  : 2018/4/2.
 */

public class ExpandViewHolder extends BaseViewHolder{

    public ExpandViewHolder(Context ctx, View itemView, int viewType) {
        super(ctx, itemView, viewType);
    }

    @Override
    public int getChildViewResId() {
        return 0;
    }

    @Override
    public int getGroupViewResId() {
        return 0;
    }

    @Override
    public int getFooterViewResId() {
        return 0;
    }

    @Override
    public int getLoadingViewResId() {
        return 0;
    }
}
