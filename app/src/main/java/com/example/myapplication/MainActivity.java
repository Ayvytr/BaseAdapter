package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ayvytr.baseadapter.MultiItemTypeAdapter;
import com.example.myapplication.adapter.DataEmptyAdapter;
import com.example.myapplication.bean.Bean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private SmartRefreshLayout smartRefreshLayout;
    private RecyclerView rv;
//    private DataAdapter<Bean> adapter;
    private DataEmptyAdapter adapter;
    private View tvClear;
    private View tvAdd;
    private Button btnAnimSwitch;

    private Random random = new Random();
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        rv = findViewById(R.id.rv);
        smartRefreshLayout = findViewById(R.id.smart_refresh_layout);
        tvAdd = findViewById(R.id.tv_add);
        tvClear = findViewById(R.id.tv_clear);
        btnAnimSwitch = findViewById(R.id.btn_anim_switch);

//        smartRefreshLayout.setEnableRefresh(false);
//        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
//        adapter = new DataAdapter(this, R.layout.item);
        adapter = new DataEmptyAdapter(this, R.layout.item, R.layout.item_empty);
        btnAnimSwitch.setText("动画：" + adapter.isItemAnimEnabled());
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<Bean>() {
            @Override
            public void onItemClick(RecyclerView.ViewHolder holder, Bean bean, int position) {
                Toast.makeText(MainActivity.this, position + " " + bean.text, Toast.LENGTH_SHORT)
                        .show();
            }
        });
        rv.setAdapter(adapter);
    }

    public void onClear(View view) {
        adapter.clear();
    }

    public void onAdd(View view) {
        adapter.add(0, new Bean("item:" + random.nextInt(100)));
    }

    public void onAdd10(View view) {
        List<Bean> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new Bean("item:" + i));
        }
        adapter.add(0, list);
    }

    public void onSwitchAnim(View view) {
        boolean enabled = !adapter.isItemAnimEnabled();
        adapter.setItemAnimEnabled(enabled);
        btnAnimSwitch.setText("动画：" + enabled);
    }

    public void onRemove(View view) {
        adapter.remove(0);
    }

    public void onReset10Items(View view) {
        List<Bean> list = new ArrayList<>();
        for(int i = 0; i < 10; i++) {
            list.add(new Bean("new:" + i));
        }
        adapter.updateList(list);
    }

}