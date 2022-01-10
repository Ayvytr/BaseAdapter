package com.example.myapplication.adapter;

import android.content.Context;

import com.ayvytr.baseadapter.EmptyAdapter;
import com.ayvytr.baseadapter.ViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.bean.Bean;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Administrator
 */
public class DataEmptyAdapter extends EmptyAdapter<Bean> {
    public DataEmptyAdapter(@NotNull Context context, int layoutId, int emptyLayoutId) {
        super(context, layoutId, emptyLayoutId);
    }

    @Override
    protected void onBindView(ViewHolder holder, Bean bean, int position, List payloads) {
        holder.setText(R.id.tv, bean.text);
    }


    @Override
    public void onBindEmptyView(@NotNull ViewHolder holder) {

    }
}
