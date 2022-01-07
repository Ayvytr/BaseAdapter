package com.ayvytr.baseadapter

/**
 * Created by Jason on 2018/8/14.
 */
interface ItemViewDelegate<T> {
    fun itemViewLayoutId(): Int
    fun isForViewType(item: T, position: Int): Boolean
    fun convert(holder: ViewHolder, t: T, position: Int, payloads: List<Any>)
}