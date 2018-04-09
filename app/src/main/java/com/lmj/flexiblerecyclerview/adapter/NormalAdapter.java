package com.lmj.flexiblerecyclerview.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lmj.flexiblerecyclerview.ExpandData;
import com.lmj.flexiblerecyclerview.R;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.BaseRecyclerData;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.holder.BaseViewHolder;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * author: lmj
 * date  : 2018/4/2.
 */

public class NormalAdapter extends BaseRecyclerViewAdapter<ExpandData.ParentData,ExpandData.ChildData,NormalAdapter.NormalViewHolder> implements OnRecyclerViewListener.OnItemClickListener{

    private LayoutInflater mInflater;

    public NormalAdapter(Activity activity,  BaseRecyclerData data) {
        super(activity, data);
        mInflater = LayoutInflater.from(activity);
        setOnItemClickListener(this);
    }

    @Override
    public View getGroupView(ViewGroup parent) {
        return mInflater.inflate(R.layout.viewholder_parent, parent, false);
    }

    @Override
    public View getChildView(ViewGroup parent) {
        return mInflater.inflate(R.layout.viewholder_child, parent, false);
    }

    @Override
    public View getFooterView(ViewGroup parent) {
        return null;
    }

    @Override
    public View getLoadingView(ViewGroup parent) {
        return null;
    }

    @Override
    public NormalViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new NormalViewHolder(ctx,view,viewType);
    }

    @Override
    public void onBindGroupHolder(NormalViewHolder holder, int groupPos, int position, ExpandData.ParentData groupData) {
        holder.setText(R.id.holder_title,groupData.title);
    }

    @Override
    public void onBindFooterHolder(NormalViewHolder holder, int groupPos, int position, ExpandData.ParentData groupData) {

    }

    @Override
    public void onBindChildHolder(NormalViewHolder holder, int groupPos, int childPos, int position, ExpandData.ChildData childData) {
        holder.setText(R.id.holder_content,childData.content);
    }

    @Override
    public void onGroupItemClick(int position, int groupPosition, View view, boolean isExpand) {
        ImageView arrow = (ImageView) view.findViewById(R.id.holder_arrow);
        if (isExpand){
            ObjectAnimator.ofFloat(arrow, "rotation",90f,0f).setDuration(500).start();
        }else {
            ObjectAnimator.ofFloat(arrow, "rotation",0f,90f).setDuration(500).start();
        }
    }

    @Override
    public void onChildItemClick(int position, int groupPosition, int childPosition, View view) {

    }

    class NormalViewHolder extends BaseViewHolder {

        NormalViewHolder(Context ctx, View itemView, int viewType) {
            super(ctx, itemView, viewType);
        }

        @Override
        public int getChildViewResId() {
            return R.id.holder_child;
        }

        @Override
        public int getGroupViewResId() {
            return R.id.holder_parent;
        }

        @Override
        public int getFooterViewResId() {
            return 0;
        }

        @Override
        public int getLoadingViewResId() {
            return 0;
        }

        void setText(int resId, String str){
            TextView textView = (TextView) itemView.findViewById(resId);
            textView.setText(str);
        }
    }

}
