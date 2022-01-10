package com.ayvytr.baseadapter

/**
 * Created by zhy on 16/4/9.
 */
interface ItemViewDelegate<T> {
    fun itemViewLayoutId(): Int
    fun isForViewType(item: T, position: Int): Boolean
    fun convert(holder: ViewHolder, t: T, position: Int, payloads: List<Any>)
}