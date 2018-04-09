package com.lmj.flexiblerecyclerview;

import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.BaseRecyclerData;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.bean.RecyclerViewData;

import java.util.ArrayList;
import java.util.List;

/**
 * author: lmj
 * date  : 2018/4/2.
 */

public class ExpandData extends BaseRecyclerData {


    public ExpandData() {
        totalPage = 3;
        for (int i = 0; i < 10; i++) {
            List<ChildData> list = new ArrayList<>();
            ParentData data = new ParentData("由我来组成第" + i + "个头部", R.mipmap.icon_shoes);
            list.add(new ChildData("我还是一个孩子"));
            if (i % 2 == 0) {
                list.add(new ChildData("我还两个孩子"));
            }
            dataList.add(new RecyclerViewData<>(data, list, true));
        }
    }

    public ExpandData(int count) {

        for (int i = 1; i < count; i++) {
            List<ChildData> list = new ArrayList<>();
            ParentData data = new ParentData("卑鄙的外乡人", R.mipmap.icon_shoes);
            list.add(new ChildData("外乡人的儿子"));
            if (i % 2 == 0) {
                list.add(new ChildData("外乡人的女儿"));
            }
            dataList.add(new RecyclerViewData<>(data, list, true));
        }
    }

    public static class ParentData {
        public String title;
        public int footerId;

        public ParentData(String title, int footerId) {
            this.title = title;
            this.footerId = footerId;
        }
    }

    public static class ChildData {
        public String content;

        public ChildData(String content) {
            this.content = content;
        }
    }
}
