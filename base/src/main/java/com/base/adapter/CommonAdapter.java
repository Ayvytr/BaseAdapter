package com.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Created by Jason on 2018/8/14.
 * <p>
 * RecycleView通用的adapter
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, final int layoutId) {
        this(context, layoutId, null);
    }

    public CommonAdapter(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position, List<Object> payloads) {
                onBind(holder, t, position, payloads);
            }
        });


    }

    protected abstract void onBind(ViewHolder holder, T t, int position, List<Object> payloads);


}
