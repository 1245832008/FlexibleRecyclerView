package com.lmj.flexiblerecyclerview.expandablerecyclerview.bean;

import java.util.List;

/**
 * author：Drawthink
 * describe:
 * date: 2017/5/22
 * T 为group数据对象
 * S 为child数据对象
 */

public class GroupItem<T,S> extends BaseItem implements Cloneable{

    /**head data*/
    private T groupData;

    /** childDatas*/
    private List<S> childDatas;

    /** 是否展开,  默认展开*/
    private boolean isExpand = true;

    private boolean isFooter = false;

    /** 返回是否是父节点*/
    @Override
    public boolean isParent() {
        return true;
    }

    public boolean isExpand(){
        return isExpand;
    }

    public void onExpand() {
        isExpand = !isExpand;
    }

    public boolean isFooter() {
        return isFooter;
    }

    public void setFooter(boolean footer) {
        isFooter = footer;
    }

    public GroupItem(T groupData, List<S> childDatas, boolean isExpand) {
        this.groupData = groupData;
        this.childDatas = childDatas;
        this.isExpand = isExpand;
    }

    public boolean hasChilds(){
        if(getChildDatas() == null || getChildDatas().isEmpty() ){
            return false;
        }
        return true;
    }

    public List<S> getChildDatas() {
        return childDatas;
    }

    public void setChildDatas(List<S> childDatas) {
        this.childDatas = childDatas;
    }

    public void removeChild(int childPosition){

    }

    public T getGroupData() {
        return groupData;
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        }catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
