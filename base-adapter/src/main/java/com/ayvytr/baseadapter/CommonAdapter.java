package com.ayvytr.baseadapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import androidx.annotation.LayoutRes;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected int mLayoutId;
    protected LayoutInflater mInflater;

    public CommonAdapter(final Context context, @LayoutRes final int layoutId) {
        this(context, layoutId, null);
    }

    public CommonAdapter(final Context context, @LayoutRes final int layoutId, List<T> list) {
        super(context, list);
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
