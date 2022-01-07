package com.base.adapter;


import java.util.List;

import androidx.annotation.NonNull;

/**
 * Created by Jason on 2018/8/14.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(ViewHolder holder, T t, int position, @NonNull List<Object> payloads);

}
