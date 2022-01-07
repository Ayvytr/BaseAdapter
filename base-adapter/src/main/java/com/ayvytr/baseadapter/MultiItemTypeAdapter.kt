package com.ayvytr.baseadapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntRange
import androidx.recyclerview.widget.RecyclerView
import java.util.*

/**
 * Created by zhy on 16/4/9.
 */
open class MultiItemTypeAdapter<T>(val context: Context,
                                   val mList: MutableList<T> = mutableListOf()):
    RecyclerView.Adapter<ViewHolder>() {
    protected val mItemViewDelegateManager = ItemViewDelegateManager<T>()
    protected var mOnItemClickListener: OnItemClickListener<T>? = null
    protected var mOnItemLongClickListener: OnItemLongClickListener<T>? = null

    var isItemAnimEnabled = false

    override fun getItemViewType(position: Int): Int {
        return if (!useItemViewDelegateManager()) {
            super.getItemViewType(position)
        } else mItemViewDelegateManager.getItemViewType(mList[position], position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemViewDelegate = mItemViewDelegateManager
            .getItemViewDelegate(viewType)
        val layoutId = itemViewDelegate!!.itemViewLayoutId()
        val holder = ViewHolder.createViewHolder(context, parent, layoutId)
        setListener(parent, holder, viewType)
        return holder
    }

    protected fun setListener(parent: ViewGroup?, viewHolder: ViewHolder, viewType: Int) {
        if (viewType == TYPE_EMPTY) {
            return
        }
        viewHolder.convertView.setOnClickListener {
            mOnItemClickListener?.apply {
                val position = viewHolder.bindingAdapterPosition
                onItemClick(viewHolder, mList[position], position)
            }
        }
        viewHolder.convertView.setOnLongClickListener(View.OnLongClickListener {
            mOnItemLongClickListener?.apply {
                val position = viewHolder.bindingAdapterPosition
                return@OnLongClickListener onItemLongClick(viewHolder, mList[position], position)
            }
            false
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindViewHolder(holder, position, ArrayList(0))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        mItemViewDelegateManager.convert(holder, getItem(position),
                                         holder.bindingAdapterPosition, payloads)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun getData() = mList

    fun isEmpty() = mList.isEmpty()

    open fun updateList(list: List<T>) {
        val oldCount = mList.size
        mList.clear()
        mList.addAll(list)
        if (isItemAnimEnabled) {
            if (oldCount == 0) {
                if (!list.isEmpty()) {
                    notifyItemRangeInserted(0, list.size)
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

    fun add(t: T) {
        add(mList.size, t)
    }

    open fun add(@IntRange(from = 0) index: Int, t: T) {
        mList.add(index, t)
        if (isItemAnimEnabled) {
            notifyItemInserted(index)
        } else {
            notifyDataSetChanged()
        }
    }

    fun add(list: List<T>) {
        add(mList.size, list)
    }

    open fun add(@IntRange(from = 0) index: Int, list: List<T>) {
        if (!list.isEmpty()) {
            mList.addAll(index, list)
            if (isItemAnimEnabled) {
                notifyItemRangeInserted(index, index + list.size)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun remove(t: T) {
        val i = mList.indexOf(t)
        val removed = mList.remove(t)
        if (removed) {
            if (isItemAnimEnabled) {
                notifyItemRemoved(i)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun remove(@IntRange(from = 0) index: Int) {
        if (mList.isEmpty()) {
            return
        }
        mList.removeAt(index)
        if (isItemAnimEnabled) {
            notifyItemRemoved(index)
        } else {
            notifyDataSetChanged()
        }
    }

    fun clear() {
        if (isEmpty()) {
            return
        }
        val count = mList.size
        mList.clear()
        if (count > 0) {
            if (isItemAnimEnabled) {
                notifyItemRangeRemoved(0, count)
            } else {
                notifyDataSetChanged()
            }
        }
    }

    fun getItem(@IntRange(from = 0) position: Int): T {
        return mList[position]
    }

    fun addItemViewDelegate(itemViewDelegate: ItemViewDelegate<T>): MultiItemTypeAdapter<T> {
        return addItemViewDelegate(TYPE_DATA, itemViewDelegate)
    }

    fun addItemViewDelegate(viewType: Int,
                            itemViewDelegate: ItemViewDelegate<T>): MultiItemTypeAdapter<T> {
        mItemViewDelegateManager.addDelegate(viewType, itemViewDelegate)
        return this
    }

    protected fun useItemViewDelegateManager(): Boolean {
        return mItemViewDelegateManager.itemViewDelegateCount > 0
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>?) {
        mOnItemClickListener = onItemClickListener
    }

    fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>?) {
        mOnItemLongClickListener = onItemLongClickListener
    }

    interface OnItemClickListener<T> {
        fun onItemClick(holder: RecyclerView.ViewHolder, t: T, position: Int)
    }

    interface OnItemLongClickListener<T> {
        fun onItemLongClick(holder: RecyclerView.ViewHolder, t: T, position: Int): Boolean
    }

    companion object {
        @JvmStatic
        val TYPE_DATA = 0
        @JvmStatic
        val TYPE_EMPTY = -1
    }

}