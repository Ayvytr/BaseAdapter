package com.ayvytr.baseadapter

/**
 * @author Ayvytr ['s GitHub](https://github.com/Ayvytr)
 * @since 0.1.0
 */
interface EmptyItemViewDelegate<T>: ItemViewDelegate<T> {
    fun convert(holder: ViewHolder)
}