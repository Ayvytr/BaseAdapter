package com.ayvytr.baseadapter

import android.content.Context
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author Administrator
 */
abstract class EmptyAdapter<T>(context: Context, layoutId: Int, private var emptyLayoutId: Int):
    CommonAdapter<T>(context, layoutId) {

    private val emptyViewDelegate: EmptyItemViewDelegate<T>

    private var isDataChanged = false

    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    private val emptyLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(context)
    }

    private val emptyAdapterDataObserver: RecyclerView.AdapterDataObserver by lazy {
        object: RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                isDataChanged = true

                val lm = recyclerView!!.layoutManager
                if (!mList.isEmpty()) {
                    if (lm !== layoutManager && layoutManager != null) {
                        recyclerView!!.layoutManager = layoutManager
                    }
                } else {
                    if (lm !== emptyLayoutManager) {
                        recyclerView!!.layoutManager = emptyLayoutManager
                    }
                }
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeChanged(positionStart: Int, itemCount: Int,
                                            payload: Any?) {
                onChanged()
            }

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                onChanged()
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                onChanged()
            }
        }
    }

    init {
        emptyViewDelegate = object: EmptyItemViewDelegate<T> {
            override fun itemViewLayoutId(): Int {
                return emptyLayoutId
            }

            /**
             * @return 空布局必须返回false，不然布局加载异常
             */
            override fun isForViewType(item: T, position: Int): Boolean {
                return false
            }

            override fun convert(holder: ViewHolder, t: T, position: Int, payloads: List<Any>) {
            }

            override fun convert(holder: ViewHolder) {
                onBindEmptyView(holder)
            }
        }
        addItemViewDelegate(TYPE_EMPTY, emptyViewDelegate)
        registerDataObserver()
    }

    /**
     * 重写可对空布局设置点击事情.
     */
    abstract fun onBindEmptyView(holder: ViewHolder)

    private fun registerDataObserver() {
        if (emptyLayoutId == 0) {
            unregisterDataObserver()
            return
        }

        registerAdapterDataObserver(emptyAdapterDataObserver)
    }

    override fun getItemViewType(position: Int): Int {
        if (!super.isEmpty() || emptyLayoutId == 0) {
            layoutManager = recyclerView!!.layoutManager
            return super.getItemViewType(position)
        }
        return if (isDataChanged) TYPE_EMPTY else 0
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int,
                                  payloads: MutableList<Any>) {
        if (!mList.isEmpty()) {
            mItemViewDelegateManager.convert(holder, getItem(position),
                                             holder.bindingAdapterPosition, payloads)
        } else {
            emptyViewDelegate.convert(holder)
        }
    }

    override fun getItemCount(): Int {
        if (!mList.isEmpty() || emptyLayoutId == 0) {
            return super.getItemCount()
        }
        return if (isDataChanged) 1 else 0
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = null
    }

    fun clearEmptyLayout() {
        emptyLayoutId = 0
        unregisterDataObserver()
    }

    private fun unregisterDataObserver() {
        try {
            unregisterAdapterDataObserver(emptyAdapterDataObserver)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 涉及空布局必须重写增加item的方法，因为item count=0时，notifyItemRangeInserted，notifyItemInserted
     * 会报错.
     */
    override fun updateList(list: List<T>) {
        val oldCount = mList.size
        mList.clear()
        mList.addAll(list)
        if (isItemAnimEnabled) {
            if (oldCount == 0) {
                if (!list.isEmpty()) {
                    notifyItemChanged(0)
                    notifyItemRangeInserted(1, list.size)
                }
            } else {
                val newCount = list.size
                if (newCount == 0) {
                    notifyItemRangeRemoved(0, oldCount)
                } else {
                    if (newCount == oldCount) {
                        notifyItemRangeChanged(0, newCount)
                    } else if (newCount < oldCount) {
                        notifyItemRangeChanged(0, newCount)
                        notifyItemRangeRemoved(newCount, oldCount)
                    } else {
                        notifyItemRangeChanged(0, oldCount)
                        notifyItemRangeInserted(oldCount, newCount)
                    }
                }
            }
        } else {
            notifyDataSetChanged()
        }
    }

    /**
     * 涉及空布局必须重写增加item的方法，因为item count=0时，notifyItemRangeInserted，notifyItemInserted
     * 会报错.
     */
    override fun add(@IntRange(from = 0) index: Int, t: T) {
        val oldCount = mList.size
        mList.add(index, t)
        if (isItemAnimEnabled) {
            if (oldCount == 0) {
                notifyItemChanged(0)
            } else {
                notifyItemInserted(index)
            }
        } else {
            notifyDataSetChanged()
        }
    }

    /**
     * 涉及空布局必须重写增加item的方法，因为item count=0时，notifyItemRangeInserted，notifyItemInserted
     * 会报错.
     */
    override fun add(@IntRange(from = 0) index: Int, list: List<T>) {
        if (!list.isEmpty()) {
            val oldCount = mList.size
            mList.addAll(index, list)
            if (isItemAnimEnabled) {
                if (oldCount == 0) {
                    notifyItemChanged(0)
                    notifyItemRangeInserted(1, 1 + list.size)
                } else {
                    notifyItemRangeInserted(index, index + list.size)
                }
            } else {
                notifyDataSetChanged()
            }
        }
    }


    fun setEmptyLayoutId(@LayoutRes emptyLayoutId: Int) {
        this.emptyLayoutId = emptyLayoutId
        registerDataObserver()
        notifyDataSetChanged()
    }

}