package com.lmj.flexiblerecyclerview.expandablerecyclerview.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lmj.flexiblerecyclerview.FlexibleApplication;
import com.lmj.flexiblerecyclerview.R;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.BaseRecyclerData;

import java.util.concurrent.Executors;

/**
 * author：Drawthink
 * describe：BaseViewHolder
 * date: 2017/5/22
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public static final int VIEW_TYPE_PARENT = 1;  //父布局
    public static final int VIEW_TYPE_CHILD = 2;    //子布局
    public static final int VIEW_TYPE_FOOTER = 3;    //脚布局
    public static final int VIEW_TYPE_LOADING = 4;    //上拉加载

    public static final int LOADMORE_STATE_LOADING = 0;//加载中
    public static final int LOADMORE_STATE_RETRY = 1;//重试
    public static final int LOADMORE_STATE_NONE = 2;//不显示，隐藏

    public ViewGroup childView;

    public ViewGroup groupView;

    public ViewGroup footerView;

    public ViewGroup loadingView;

    protected Context mContext;

    public View loadmoreContainerLoading;
    public View loadmoreContainerRetry;
    public TextView mTvRetry;
    public ImageView arrow;
    private LoadMoreTask mLoadMoreTask;
    private LoadingListener mLoadingListener;
    private int state = LOADMORE_STATE_LOADING;

    public BaseViewHolder(Context ctx, View itemView, int viewType) {
        super(itemView);
        mContext = ctx;
        switch (viewType) {
            case VIEW_TYPE_PARENT:
                groupView = (ViewGroup)itemView.findViewById(getGroupViewResId());
                arrow = (ImageView) itemView.findViewById(R.id.holder_arrow);
                break;
            case VIEW_TYPE_CHILD:
                childView = (ViewGroup) itemView.findViewById(getChildViewResId());
                break;
            case VIEW_TYPE_FOOTER:
                footerView = (ViewGroup)itemView.findViewById(getFooterViewResId());
                break;
            case VIEW_TYPE_LOADING:
                loadingView = (ViewGroup)itemView.findViewById(getLoadingViewResId());
                loadmoreContainerLoading = itemView.findViewById(R.id.item_loadmore_container_loading);
                loadmoreContainerRetry = itemView.findViewById(R.id.item_loadmore_container_retry);
                mTvRetry = (TextView) itemView.findViewById(R.id.item_loadmore_tv_retry);
                loadmoreContainerRetry.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadMore();
                    }
                });
                break;
        }
    }

    /**
     * return ChildView root layout id
     */
    public abstract int getChildViewResId();

    /**
     * return GroupView root layout id
     * */
    public abstract int getGroupViewResId();

    /**
     * return FooterView root layout id
     * */
    public abstract int getFooterViewResId();

    /**
     * return LoadingView root layout id
     * */
    public abstract int getLoadingViewResId();


    public int getState() {
        return state;
    }

    public void refreshUI(int state) {
        loadmoreContainerLoading.setVisibility(View.GONE);
        loadmoreContainerRetry.setVisibility(View.GONE);
        switch (state) {
            case LOADMORE_STATE_LOADING:
                loadmoreContainerLoading.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_STATE_RETRY:
                loadmoreContainerRetry.setVisibility(View.VISIBLE);
                break;
            case LOADMORE_STATE_NONE:
                loadmoreContainerLoading.setVisibility(View.GONE);
                loadmoreContainerRetry.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    public void loadMore() {
        if (mLoadMoreTask == null) {
            refreshUI(LOADMORE_STATE_LOADING);
            mLoadMoreTask = new LoadMoreTask();
            Executors.newSingleThreadExecutor().execute(mLoadMoreTask);
        }
    }

    private class LoadMoreTask implements Runnable {
        @Override
        public void run() {
            BaseRecyclerData loadMoreData;
            if (mLoadingListener==null){
                state = LOADMORE_STATE_NONE;
            }else {
                try {
                    loadMoreData = mLoadingListener.loadMore();
                    if (loadMoreData != null) {
                        state = LOADMORE_STATE_LOADING;
                    }
                    if (loadMoreData != null && loadMoreData.atLastPage) {
                        state = LOADMORE_STATE_NONE;
                    }
                    if (loadMoreData == null) {
                        state = LOADMORE_STATE_RETRY;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    state = LOADMORE_STATE_RETRY;
                }
            }

           handleUiOnMainThread(new Runnable() {
                @Override
                public void run() {
                    refreshUI(state);
                }
            });
            mLoadMoreTask = null;
        }
    }

    /**
     * 设置加载更多操作
     * @param loadingListener 监听器
     */
    public void setLoadingListener(LoadingListener loadingListener) {
        mLoadingListener = loadingListener;
    }

    public interface LoadingListener {
        BaseRecyclerData loadMore() throws Exception;
    }

    public void setArrowExpand(){
        if (arrow!=null){
            arrow.setRotation(90f);
        }
    }

    public void setArrowCollapse(){
        if (arrow!=null){
            arrow.setRotation(0f);
        }
    }
    private   void handleUiOnMainThread(Runnable task) {
        int curThreadId = android.os.Process.myTid();

        if (curThreadId == FlexibleApplication.getMainTreadId()) {// 如果当前线程是主线程
            task.run();
        } else {// 如果当前线程不是主线程
            FlexibleApplication.getHandler().post(task);
        }
    }
}