package com.lmj.flexiblerecyclerview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.lmj.flexiblerecyclerview.adapter.LoadAdapter;
import com.lmj.flexiblerecyclerview.adapter.MiddleAdapter;
import com.lmj.flexiblerecyclerview.adapter.NormalAdapter;
import com.lmj.flexiblerecyclerview.expandablerecyclerview.adapter.BaseRecyclerViewAdapter;

/**
 * author: lmj
 * date  : 2018/4/2.
 */

public class RecyclerActivity extends AppCompatActivity {
    public static final String RECYCLER_TYPE = "RECYCLER_TYPE";
    public static final int RECYCLER_NORMAL = 1101;
    public static final int RECYCLER_MIDDLE = 1102;
    public static final int RECYCLER_LOAD_MORE =1103;
    private ExpandData mData;
    private BaseRecyclerViewAdapter mAdapter;
    private int type = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        initData();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        switch (type){
            case RECYCLER_NORMAL:
                mAdapter = new NormalAdapter(this, mData);
                recyclerView.setAdapter(mAdapter);
                break;
            case RECYCLER_MIDDLE:
                mAdapter = new MiddleAdapter(this, mData);
                recyclerView.setAdapter(mAdapter);
                break;
            case RECYCLER_LOAD_MORE:
                mAdapter = new LoadAdapter(this, mData);
                recyclerView.setAdapter(mAdapter);
                recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (isVisBottom(recyclerView)){
                            if (mAdapter != null){
                                mAdapter.startLoadMore();
                            }
                        }
                    }
                });
                break;
        }

    }

    private void initData() {
      mData = new ExpandData();
        if (getIntent()!=null){
            type = getIntent().getIntExtra(RECYCLER_TYPE,0);
        }
    }

    boolean isVisBottom(RecyclerView recyclerView){
        if (recyclerView == null) return false;
        return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange();
    }
}
