package com.ayvytr.baseadapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by zhy on 16/4/9.
 */
public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected static final int TYPE_DATA = 0;
    protected static final int TYPE_EMPTY = -1;

    protected Context mContext;
    protected List<T> mList;

    protected ItemViewDelegateManager<T> mItemViewDelegateManager;
    protected OnItemClickListener<T> mOnItemClickListener;
    protected OnItemLongClickListener<T> mOnItemLongClickListener;

    protected boolean isItemAnimEnabled;

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
                    int position = viewHolder.getBindingAdapterPosition();
                    mOnItemClickListener.onItemClick(viewHolder, mList.get(position), position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(mOnItemLongClickListener != null) {
                    int position = viewHolder.getBindingAdapterPosition();
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
        mItemViewDelegateManager.convert(holder, getItem(position),
                holder.getBindingAdapterPosition(), payloads);
    }

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }


    public List<T> getData() {
        return mList;
    }

    public boolean isEmpty() {
        return mList.isEmpty();
    }

    public void updateList(List<T> list) {
        int oldCount = mList.size();
        mList.clear();
        mList.addAll(list);
        if(isItemAnimEnabled) {
            if(oldCount == 0) {
                if(!list.isEmpty()) {
                    notifyItemRangeInserted(0, list.size());
                }
            } else {
                int newCount = list.size();
                if(newCount == 0) {
                    notifyItemRangeRemoved(0, oldCount);
                } else {
                    if(newCount == oldCount) {
                        notifyItemRangeChanged(0, newCount);
                    } else if(newCount < oldCount) {
                        notifyItemRangeChanged(0, newCount);
                        notifyItemRangeRemoved(newCount, oldCount);
                    } else {
                        notifyItemRangeChanged(0, oldCount);
                        notifyItemRangeInserted(oldCount, newCount);
                    }
                }
            }
        } else {
            notifyDataSetChanged();
        }
    }

    public void add(@NonNull T t) {
        add(mList.size(), t);
    }

    public void add(@IntRange(from = 0) int index, @NonNull T t) {
        mList.add(index, t);

        if(isItemAnimEnabled) {
            notifyItemInserted(index);
        } else {
            notifyDataSetChanged();
        }
    }

    public void add(@NonNull List<T> list) {
        add(mList.size(), list);
    }

    public void add(@IntRange(from = 0) int index, List<T> list) {
        if(list != null && !list.isEmpty()) {
            mList.addAll(index, list);
            if(isItemAnimEnabled) {
                notifyItemRangeInserted(index, index + list.size());
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void remove(@NonNull T t) {
        int i = mList.indexOf(t);
        boolean removed = mList.remove(t);
        if(removed) {
            if(isItemAnimEnabled) {
                notifyItemRemoved(i);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public void remove(@IntRange(from = 0) int index) {
        if(mList.isEmpty()) {
            return;
        }

        mList.remove(index);
        if(isItemAnimEnabled) {
            notifyItemRemoved(index);
        } else {
            notifyDataSetChanged();
        }
    }

    public void clear() {
        if(isEmpty()) {
            return;
        }

        int count = mList.size();
        mList.clear();
        if(count > 0) {
            if(isItemAnimEnabled) {
                notifyItemRangeRemoved(0, count);
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public T getItem(@IntRange(from = 0) int position) {
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

    public boolean isItemAnimEnabled() {
        return isItemAnimEnabled;
    }

    public void setItemAnimEnabled(boolean itemAnimEnabled) {
        isItemAnimEnabled = itemAnimEnabled;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(RecyclerView.ViewHolder holder, T t, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(RecyclerView.ViewHolder holder, T t, int position);
    }
}
