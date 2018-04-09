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
import com.lmj.flexiblerecyclerview.FlexibleApplication;
import com.lmj.flexiblerecyclerview.R;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.BaseRecyclerData;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.holder.BaseViewHolder;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.listener.OnRecyclerViewListener;

/**
 * author: lmj
 * date  : 2018/4/4.
 */

public class LoadAdapter extends BaseRecyclerViewAdapter<ExpandData.ParentData,ExpandData.ChildData,LoadAdapter.LoadViewHolder> implements OnRecyclerViewListener.OnItemClickListener{

    private LayoutInflater mInflater;

    public LoadAdapter(Activity activity, ExpandData data) {
        super(activity, data);
        mData = data;
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
        return mInflater.inflate(R.layout.viewholder_footer, parent, false);
    }

    @Override
    public View getLoadingView(ViewGroup parent) {
        return  mInflater.inflate(R.layout.viewholder_loadmore, parent, false);
    }

    @Override
    public LoadAdapter.LoadViewHolder createRealViewHolder(Context ctx, View view, int viewType) {
        return new LoadViewHolder(ctx,view,viewType);
    }

    @Override
    public void onBindGroupHolder(LoadViewHolder holder, int groupPos, int position, ExpandData.ParentData groupData) {
        holder.setText(R.id.holder_title,groupData.title);
    }

    @Override
    public void onBindFooterHolder(LoadViewHolder holder, int groupPos, int position, ExpandData.ParentData groupData) {
        holder.setImage(R.id.holder_image,R.mipmap.icon_shoes);
    }

    @Override
    public void onBindChildHolder(LoadViewHolder holder, int groupPos, int childPos, int position, ExpandData.ChildData childData) {
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

    @Override
    public boolean showFooter() {
        return true;
    }

    @Override
    public boolean shouldLoadMore() {
        return true;
    }

    @Override
    public BaseRecyclerData onLoadMore() throws Exception {
        ExpandData data = loadingMore();
        mData.dataList.addAll(data.dataList);
        handleUiOnMainThread(new Runnable() {
            @Override
            public void run() {
                notifyRecyclerViewData();
            }
        });
        return mData;
    }

    private ExpandData loadingMore() throws Exception{
        Thread.sleep(1000);
        ExpandData data = new ExpandData(3);
        if ( mData.currentPage>=mData.totalPage){
            mData.atLastPage = true;
        }
        mData.currentPage++;
        return data;

    }

    /**
     * 在主线程刷新UI
     * @param task 任务
     */
    private void handleUiOnMainThread(Runnable task) {
        int curThreadId = android.os.Process.myTid();

        if (curThreadId == FlexibleApplication.getMainTreadId()) {// 如果当前线程是主线程
            task.run();
        } else {// 如果当前线程不是主线程
            FlexibleApplication.getHandler().post(task);
        }
    }

    class LoadViewHolder extends BaseViewHolder {

        LoadViewHolder(Context ctx, View itemView, int viewType) {
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
            return R.id.holder_footer;
        }

        @Override
        public int getLoadingViewResId() {
            return R.id.holder_loading;
        }

        void setImage(int resId, int imgId){
            ImageView imageView = (ImageView) itemView.findViewById(resId);
            imageView.setImageResource(imgId);
        }

        void setText(int resId, String str){
            TextView textView = (TextView) itemView.findViewById(resId);
            textView.setText(str);
        }
    }
}
