package com.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Jason on 2018/8/14.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected static final int TYPE_DATA = 0;
    protected static final int TYPE_EMPTY = -1;

    protected Context mContext;
    protected List<T> mList;

    protected ItemViewDelegateManager<T> mItemViewDelegateManager;
    protected OnItemClickListener<T> mOnItemClickListener;
    protected OnItemLongClickListener<T> mOnItemLongClickListener;

    public MultiItemTypeAdapter(Context context, List<T> list) {
        mContext = context;
        mList = list;
        if(mList == null) {
            mList = new ArrayList<>();
        }
        mItemViewDelegateManager = new ItemViewDelegateManager();
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if(!useItemViewDelegateManager()) {
            return super.getItemViewType(position);
        }
        return mItemViewDelegateManager.getItemViewType(mList.get(position), position);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewDelegate<T> itemViewDelegate = mItemViewDelegateManager
                .getItemViewDelegate(viewType);
        int layoutId = itemViewDelegate.getItemViewLayoutId();
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, layoutId);
        setListener(parent, holder, viewType);
        return holder;
    }

    protected void setListener(ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if(viewType == TYPE_EMPTY) {
            return;
        }

        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(viewHolder, mList.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnItemLongClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemLongClickListener
                            .onItemLongClick(viewHolder, mList.get(position), position);
                }
                return false;
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        onBindViewHolder(holder, position, new ArrayList<Object>(0));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,
            @NonNull List<Object> payloads) {
        mItemViewDelegateManager.convert(holder, getItem(position), holder.getAdapterPosition(),
                payloads);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


    public List<T> getData() {
        return mList;
    }

    public void setData(List<T> list) {
        if(list == null) {
            list = new ArrayList<>(0);
        }
        mList.clear();
        mList = list;
    }

    public boolean isEmpty() {
        return mList.isEmpty();
    }

    public void updateList(ArrayList<T> list) {
        if(list == null) {
            return;
        }
        mList = list;
        notifyDataSetChanged();
    }

    public void updateList(List<T> list) {
        if(list == null) {
            list = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(ArrayList<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addList(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public void addFirst(T entity) {
        mList.add(0, entity);
        notifyDataSetChanged();
    }

    public void addFirstList(List<T> list) {
        mList.addAll(0, list);
        notifyDataSetChanged();
    }

    public void addList(T t) {
        mList.add(t);
        notifyDataSetChanged();
    }

    public void removeList(T t) {
        mList.remove(t);
        notifyDataSetChanged();
    }

    public void removeList(int t) {
        mList.remove(t);
        notifyDataSetChanged();
    }

    public void clear() {
        mList.clear();
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mList.get(position);
    }

    public MultiItemTypeAdapter<T> addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter<T> addItemViewDelegate(int viewType,
            ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate);
        return this;
    }

    protected boolean useItemViewDelegateManager() {
        return mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(RecyclerView.ViewHolder holder, T t, int position);
//        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(RecyclerView.ViewHolder holder, T t, int position);
    }
}
