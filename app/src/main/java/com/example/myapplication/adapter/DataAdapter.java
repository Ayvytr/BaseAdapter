package com.example.myapplication.adapter;

import android.content.Context;

import com.ayvytr.baseadapter.CommonAdapter;
import com.ayvytr.baseadapter.ViewHolder;
import com.example.myapplication.R;
import com.example.myapplication.bean.Bean;

import java.util.List;

/**
 * @author Administrator
 */
public class DataAdapter extends CommonAdapter<Bean> {
    public DataAdapter(Context context, int layoutId) {
        super(context, layoutId);
    }

    @Override
    protected void onBind(ViewHolder holder, Bean bean, int position, List payloads) {
        holder.setText(R.id.tv, bean.text);
    }
}
