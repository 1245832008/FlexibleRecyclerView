package com.lmj.flexiblerecyclerview.expandablerecyclerview.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.BaseItem;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.BaseRecyclerData;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.GroupItem;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.RecyclerViewData;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.holder.BaseViewHolder;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.listener.OnRecyclerViewListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author：Drawthink
 * describe:
 * date: 2017/5/22
 * T :group  data
 * S :child  data
 * VH :ViewHolder
 */

public abstract class BaseRecyclerViewAdapter<T , S, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    public static final String TAG = BaseRecyclerViewAdapter.class.getSimpleName();
//    这个值是需要加载更多的时候,每次请求的数量
    public static final int PAGE_SIZE = 15;

    protected Activity mActivity;

    protected BaseRecyclerData mData;
    /**
     * all data
     */
    private List<RecyclerViewData> allDatas;
    /**
     * showing datas
     */
    private List showingDatas = new ArrayList<>();

    /**
     * child datas
     */
    private List<List<S>> childDatas;

    private OnRecyclerViewListener.OnItemClickListener itemClickListener;
    private OnRecyclerViewListener.OnItemLongClickListener itemLongClickListener;

    private BaseViewHolder mFooterViewHolder;

    public void setOnItemClickListener(OnRecyclerViewListener.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnRecyclerViewListener.OnItemLongClickListener longClickListener) {
        this.itemLongClickListener = longClickListener;
    }

    public BaseRecyclerViewAdapter(Activity activity,BaseRecyclerData data) {
        mActivity = activity;
        mData = data;
        this.allDatas = data.dataList;
        setShowingDatas();
        this.notifyDataSetChanged();
    }

    public void setData(BaseRecyclerData data) {
        mData = data;
        this.allDatas = data.dataList;
        setShowingDatas();
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (shouldLoadMore()) {
            return null == showingDatas ? 0 : showingDatas.size() + 1;
        } else {
            return null == showingDatas ? 0 : showingDatas.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && shouldLoadMore()) {
            return BaseViewHolder.VIEW_TYPE_LOADING;
        } else {
            if (showingDatas.get(position) instanceof GroupItem) {
                if (((GroupItem) showingDatas.get(position)).isFooter()) {
                    return BaseViewHolder.VIEW_TYPE_FOOTER;
                } else {
                    return BaseViewHolder.VIEW_TYPE_PARENT;
                }
            } else {
                return BaseViewHolder.VIEW_TYPE_CHILD;
            }
        }
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case BaseViewHolder.VIEW_TYPE_PARENT:
                view = getGroupView(parent);
                break;
            case BaseViewHolder.VIEW_TYPE_CHILD:
                view = getChildView(parent);
                break;
            case BaseViewHolder.VIEW_TYPE_FOOTER:
                view = getFooterView(parent);
                break;
            case BaseViewHolder.VIEW_TYPE_LOADING:
                view = getLoadingView(parent);
                break;
        }
        return createRealViewHolder(mActivity, view, viewType);
    }


    @Override
    public void onBindViewHolder(final VH holder, final int position) {
        if (shouldLoadMore() && position == getItemCount() - 1) {
            mFooterViewHolder = holder;
            holder.setLoadingListener(new BaseViewHolder.LoadingListener() {
                @Override
                public BaseRecyclerData loadMore() throws Exception {
                    return onLoadMore();
                }
            });
            //防止数据未充满一页时,loadItem不消失
            if (allDatas.size()<PAGE_SIZE){
                startLoadMore();
            }
        } else {
            final Object item = showingDatas.get(position);
            final int gp = getGroupPosition(position);
            final int cp = getChildPosition(gp, position);
            if (item != null && item instanceof GroupItem) {
                if (((GroupItem) item).isFooter()) {
                    onBindFooterHolder(holder, gp, position, (T) ((GroupItem) item).getGroupData());
                } else {
                    onBindGroupHolder(holder, gp, position, (T) ((GroupItem) item).getGroupData());
                    if (((GroupItem) item).isExpand()) {
                        holder.setArrowExpand();
                    } else {
                        holder.setArrowCollapse();
                    }
                    holder.groupView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != itemClickListener) {
                                itemClickListener.onGroupItemClick(position, gp, holder.groupView, ((GroupItem) item).isExpand());
                            }
                            if (((GroupItem) item).isExpand()) {
                                collapseGroup(position);
                            } else {
                                expandGroup(position);
                            }

                        }
                    });
                    holder.groupView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (null != itemLongClickListener) {
                                itemLongClickListener.onGroupItemLongClick(position, gp, holder.groupView);
                            }
                            return true;
                        }
                    });
                }

            } else {
                onBindChildHolder(holder, gp, cp, position, (S) item);
                holder.childView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != itemClickListener) {
                            itemClickListener.onChildItemClick(position, gp, cp, holder.childView);
                        }
                    }
                });
                holder.childView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (null != itemLongClickListener) {
                            int gp = getGroupPosition(position);
                            itemLongClickListener.onChildItemLongClick(position, gp, cp, holder.childView);
                        }
                        return true;
                    }
                });
            }
        }

    }


    /**
     * setup showing datas
     */
    private void setShowingDatas() {
        if (null != showingDatas) {
            showingDatas.clear();
        }
        if (this.childDatas == null) {
            this.childDatas = new ArrayList<>();
        }
        childDatas.clear();
        GroupItem groupItem;
        GroupItem footerItem;
        for (int i = 0; i < allDatas.size(); i++) {
            if (allDatas.get(i).getGroupItem() instanceof GroupItem) {
                groupItem = allDatas.get(i).getGroupItem();
            } else {
                break;
            }
            childDatas.add(i, groupItem.getChildDatas());
            showingDatas.add(groupItem);
            if (groupItem.hasChilds() && groupItem.isExpand()) {
                showingDatas.addAll(groupItem.getChildDatas());
            }
            if (showFooter()) {
                footerItem = (GroupItem) groupItem.clone();
                footerItem.setFooter(true);
                showingDatas.add(footerItem);
            }
        }
    }

    /**
     * expandGroup
     *
     * @param position showingDatas position
     */
    private void expandGroup(int position) {
        Object item = showingDatas.get(position);
        if (null == item) {
            return;
        }
        if (!(item instanceof GroupItem)) {
            return;
        }
        if (((GroupItem) item).isExpand()) {
            return;
        }
        if (!canExpandAll()) {
            for (int i = 0; i < showingDatas.size(); i++) {
                if (i != position) {
                    int tempPositino = collapseGroup(i);
                    if (tempPositino != -1) {
                        position = tempPositino;
                    }
                }
            }
        }

        List<BaseItem> tempChilds;
        ((GroupItem) item).onExpand();
        if (((GroupItem) item).hasChilds()) {
            tempChilds = ((GroupItem) item).getChildDatas();
            if (canExpandAll()) {
                showingDatas.addAll(position + 1, tempChilds);
                notifyItemRangeInserted(position + 1, tempChilds.size());
                notifyItemRangeChanged(position + 1, showingDatas.size() - (position + 1));
            } else {
                int tempPsi = showingDatas.indexOf(item);
                showingDatas.addAll(tempPsi + 1, tempChilds);
                notifyItemRangeInserted(tempPsi + 1, tempChilds.size());
                notifyItemRangeChanged(tempPsi + 1, showingDatas.size() - (tempPsi + 1));
            }
        }
    }

    /**
     * collapseGroup
     *
     * @param position showingDatas position
     */
    private int collapseGroup(int position) {
        Object item = showingDatas.get(position);
        if (null == item) {
            return -1;
        }
        if (!(item instanceof GroupItem)) {
            return -1;
        }
        if (!((GroupItem) item).isExpand()) {
            return -1;
        }
        int tempSize = showingDatas.size();
        List<BaseItem> tempChilds;
        ((GroupItem) item).onExpand();
        if (((GroupItem) item).hasChilds()) {
            tempChilds = ((GroupItem) item).getChildDatas();
            showingDatas.removeAll(tempChilds);
            notifyItemRangeRemoved(position + 1, tempChilds.size());
            notifyItemRangeChanged(position + 1, tempSize - (position + 1));
            return position;
        }
        return -1;
    }

    /**
     * @param position showingDatas position
     * @return GroupPosition
     */
    private int getGroupPosition(int position) {
        Object item = showingDatas.get(position);
        if (item instanceof GroupItem) {
            for (int j = 0; j < allDatas.size(); j++) {
                if (allDatas.get(j).getGroupItem().equals(item)) {
                    return j;
                }
            }
        }
        for (int i = 0; i < childDatas.size(); i++) {
            if (childDatas.get(i).contains(item)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param groupPosition
     * @param showDataPosition
     * @return ChildPosition
     */
    private int getChildPosition(int groupPosition, int showDataPosition) {
        Object item = showingDatas.get(showDataPosition);
        try {
            return childDatas.get(groupPosition).indexOf(item);
        } catch (IndexOutOfBoundsException ex) {
            Log.e(TAG,"getChildPosition:"+ ex.getMessage());
        }
        return 0;
    }

    /**
     * return groupView
     */
    public abstract View getGroupView(ViewGroup parent);

    /**
     * return childView
     */
    public abstract View getChildView(ViewGroup parent);

    /**
     * return footerView
     */
    public abstract View getFooterView(ViewGroup parent);

    /**
     * return loadingView
     */
    public abstract View getLoadingView(ViewGroup parent);

    /**
     * return <VH extends BaseViewHolder> instance
     */
    public abstract VH createRealViewHolder(Context ctx, View view, int viewType);

    /**
     * onBind groupData to groupView
     *
     * @param holder
     * @param position
     */
    public abstract void onBindGroupHolder(VH holder, int groupPos, int position, T groupData);

    /**
     * onBind groupData to footerView
     *
     * @param holder
     * @param position
     */
    public abstract void onBindFooterHolder(VH holder, int groupPos, int position, T footerData);

    /**
     * onBind childData to childView
     *
     * @param holder
     * @param position
     */
    public abstract void onBindChildHolder(VH holder, int groupPos, int childPos, int position, S childData);

    /**
     * if return true Allow all expand otherwise Only one can be expand at the same time
     */
    public boolean canExpandAll() {
        return true;
    }


    /**
     * 需要加载更多则需要重写该方法
     *
     * @return true 表示需要加载更多
     */
    public boolean shouldLoadMore() {
        return false;
    }

    /**
     * 需要加载更多则需要重写该方法
     *
     * @return 加载获取的数据
     * @throws Exception 异常
     */
    public BaseRecyclerData onLoadMore() throws Exception {
        return null;
    }

    /**
     * 是否需要加载
     */
    private boolean hasLoadMore() {
        return mData != null && !mData.atLastPage;
    }

    /**
     * 如果要有底部布局的话,该方法要返回true;
     *
     * @return value
     */
    public boolean showFooter(){
        return false;
    }

    public void startLoadMore() {
        if (hasLoadMore() && mFooterViewHolder.getState() == BaseViewHolder.LOADMORE_STATE_LOADING) {
            mFooterViewHolder.loadMore();
        } else {
            if (!hasLoadMore() && mFooterViewHolder != null)
                mFooterViewHolder.refreshUI(BaseViewHolder.LOADMORE_STATE_NONE);
        }
    }



    /**
     * 对原数据进行增加删除，调用此方法进行notify
     */
    public void notifyRecyclerViewData() {
        setShowingDatas();
        notifyDataSetChanged();
    }


}
