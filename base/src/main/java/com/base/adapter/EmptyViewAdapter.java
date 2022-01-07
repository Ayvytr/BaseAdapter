package com.base.adapter;

import java.util.List;

/**
 * Created by Jason on 2018/8/29.
 *
 * Recycleview适配器的包装类
 */

public class EmptyViewAdapter extends EmptyWrapper {
    private MultiItemTypeAdapter mMultiItemTypeAdapter;

    public EmptyViewAdapter(MultiItemTypeAdapter adapter) {
        super(adapter);
        mMultiItemTypeAdapter = adapter;
    }

    public void updateList(List list) {
        mMultiItemTypeAdapter.updateList(list);
        notifyDataSetChanged();
    }



    public void addList(List list) {
        mMultiItemTypeAdapter.addList(list);
        notifyDataSetChanged();
    }

    public void refresh(){
        mMultiItemTypeAdapter.notifyDataSetChanged();
        notifyDataSetChanged();
    }

    public void refreshPosition(int i) {
        mMultiItemTypeAdapter.notifyItemChanged(i);
        notifyDataSetChanged();
    }

    public MultiItemTypeAdapter getAdapter() {
        return mMultiItemTypeAdapter;
    }
}
