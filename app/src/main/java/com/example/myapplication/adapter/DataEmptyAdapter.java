package com.example.myapplication.adapter;

import android.content.Context;

import com.ayvytr.baseadapter.EmptyAdapter;
import com.ayvytr.baseadapter.ViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.bean.Bean;

import java.util.List;

/**
 * @author Administrator
 */
public class DataEmptyAdapter extends EmptyAdapter<Bean> {
    public DataEmptyAdapter(Context context, int layoutId, int emptyLayoutId) {
        super(context, layoutId, emptyLayoutId);
    }

    @Override
    protected void onBind(ViewHolder holder, Bean bean, int position, List<Object> payloads) {
        holder.setText(R.id.tv, bean.text);
    }
}
