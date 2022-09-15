package com.ayvytr.baseadapter

interface ItemViewDelegate<T> {
    fun itemViewLayoutId(): Int
    fun isForViewType(item: T, position: Int): Boolean
    fun convert(holder: ViewHolder, t: T, position: Int, payloads: List<Any>)
}