package com.lmj.flexiblerecyclerview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void normalClick(View view){
        Intent intent = new Intent(this,RecyclerActivity.class);
        intent.putExtra(RecyclerActivity.RECYCLER_TYPE,RecyclerActivity.RECYCLER_NORMAL);
        startActivity(intent);
    }

    public void middleClick(View view){
        Intent intent = new Intent(this,RecyclerActivity.class);
        intent.putExtra(RecyclerActivity.RECYCLER_TYPE,RecyclerActivity.RECYCLER_MIDDLE);
        startActivity(intent);
    }

    public void loadClick(View view){
        Intent intent = new Intent(this,RecyclerActivity.class);
        intent.putExtra(RecyclerActivity.RECYCLER_TYPE,RecyclerActivity.RECYCLER_LOAD_MORE);
        startActivity(intent);
    }
}
