package com.ayvytr.baseadapter;

import android.content.Context;

import com.ayvytr.logger.L;

import java.util.List;

import androidx.annotation.IntRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Administrator
 */
public abstract class EmptyAdapter<T> extends CommonAdapter<T> {
    private final ItemViewDelegate<T> emptyViewDelegate;

    private boolean isDataChanged;

    private int emptyLayoutId;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    private LinearLayoutManager emptyLayoutManager;

    private RecyclerView.AdapterDataObserver emptyAdapterDataObserver;

    public EmptyAdapter(Context context, int layoutId, int emptyLayoutRes) {
        super(context, layoutId);
        this.emptyLayoutId = emptyLayoutRes;
        if(emptyLayoutRes != 0) {
            emptyLayoutManager = new LinearLayoutManager(context);
        }
        registerDataObserver();

        emptyViewDelegate = new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return emptyLayoutId;
            }

            /**
             * @return 空布局必须返回false，不然布局加载异常
             */
            @Override
            public boolean isForViewType(T item, int position) {
                return false;
            }

            @Override
            public void convert(ViewHolder holder, T bean, int position, List<Object> payloads) {
                convertEmptyView(holder);
            }
        };
        addItemViewDelegate(MultiItemTypeAdapter.TYPE_EMPTY, emptyViewDelegate);
    }

    public void convertEmptyView(ViewHolder holder) {
    }

    private void registerDataObserver() {
        if(emptyLayoutId == 0) {
            unregisterDataObserver();
            return;
        }

        if(emptyAdapterDataObserver == null) {
            emptyAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    isDataChanged = true;
                    L.e();

                    RecyclerView.LayoutManager lm = recyclerView.getLayoutManager();
                    if(!mList.isEmpty()) {
                        if(lm != layoutManager && layoutManager != null) {
                            recyclerView.setLayoutManager(layoutManager);
                        }
                    } else {
                        if(lm != emptyLayoutManager) {
                            recyclerView.setLayoutManager(emptyLayoutManager);
                        }
                    }
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    onChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount,
                        @Nullable Object payload) {
                    onChanged();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    onChanged();
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    onChanged();
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    onChanged();
                }
            };
        }
        registerAdapterDataObserver(emptyAdapterDataObserver);
    }

    @Override
    public int getItemViewType(int position) {
        if(!super.isEmpty() || emptyLayoutId == 0) {
            layoutManager = recyclerView.getLayoutManager();
            return super.getItemViewType(position);
        }

        if(isDataChanged) {
            return MultiItemTypeAdapter.TYPE_EMPTY;
        } else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position,
            @NonNull List<Object> payloads) {
        if(!super.isEmpty()) {
            mItemViewDelegateManager.convert(holder, getItem(position),
                    holder.getBindingAdapterPosition(), payloads);
        } else {
            emptyViewDelegate.convert(holder, null, position, payloads);
        }
    }

    @Override
    public int getItemCount() {
        if(!mList.isEmpty() || emptyLayoutId == 0) {
            return super.getItemCount();
        }

        return isDataChanged ? 1 : 0;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        this.recyclerView = null;
    }

    public void clearEmptyLayout() {
        this.emptyLayoutId = 0;
        unregisterDataObserver();
    }

    private void unregisterDataObserver() {
        if(emptyAdapterDataObserver != null) {
            unregisterAdapterDataObserver(emptyAdapterDataObserver);
        }
    }

    /**
     * 涉及空布局必须重写增加item的方法，因为item count=0时，notifyItemRangeInserted，notifyItemInserted
     * 会报错.
     */
    public void updateList(List<T> list) {
        int oldCount = mList.size();
        mList.clear();
        mList.addAll(list);
        if(isItemAnimEnabled) {
            if(oldCount == 0) {
                if(!list.isEmpty()) {
                    notifyItemChanged(0);
                    notifyItemRangeInserted(1, list.size());
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

    /**
     * 涉及空布局必须重写增加item的方法，因为item count=0时，notifyItemRangeInserted，notifyItemInserted
     * 会报错.
     */
    public void add(@IntRange(from = 0) int index, @NonNull T t) {
        int oldCount = mList.size();
        mList.add(index, t);

        if(isItemAnimEnabled) {
            if(oldCount == 0) {
                notifyItemChanged(0);
            } else {
                notifyItemInserted(index);
            }
        } else {
            notifyDataSetChanged();
        }
    }

    /**
     * 涉及空布局必须重写增加item的方法，因为item count=0时，notifyItemRangeInserted，notifyItemInserted
     * 会报错.
     */
    public void add(@IntRange(from = 0) int index, List<T> list) {
        if(list != null && !list.isEmpty()) {
            int oldCount = mList.size();
            mList.addAll(index, list);
            if(isItemAnimEnabled) {
                if(oldCount == 0) {
                    notifyItemChanged(0);
                    notifyItemRangeInserted(1, 1 + list.size());
                } else {
                    notifyItemRangeInserted(index, index + list.size());
                }
            } else {
                notifyDataSetChanged();
            }
        }
    }

    public int getEmptyLayoutId() {
        return emptyLayoutId;
    }

    public void setEmptyLayoutId(@LayoutRes int emptyLayoutId) {
        this.emptyLayoutId = emptyLayoutId;
        registerDataObserver();
        notifyDataSetChanged();
    }
}
