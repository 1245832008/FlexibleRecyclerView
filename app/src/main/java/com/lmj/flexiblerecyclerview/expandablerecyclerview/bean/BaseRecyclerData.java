package com.lmj.flexiblerecyclerview.expandablerecyclerview.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * author: lmj
 * date  : 2018/3/13.
 */

public class BaseRecyclerData {

    //判断是否为最后一页
    public boolean atLastPage;
    //当前页
    public int currentPage =1;
    //总页数
    public int totalPage =0;

    //数据
    public List<RecyclerViewData> dataList = new ArrayList<>();

}
