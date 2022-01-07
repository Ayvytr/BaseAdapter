package com.ayvytr.baseadapter

/**
 * @author Administrator
 */
interface EmptyItemViewDelegate<T>: ItemViewDelegate<T> {
    fun convert(holder: ViewHolder)
}