package com.ayvytr.baseadapter

import android.content.Context
import androidx.annotation.LayoutRes

/**
 * Created by zhy on 16/4/9.
 */
abstract class CommonAdapter<T> @JvmOverloads constructor(context: Context,
                                                          @LayoutRes val itemLayoutId: Int,
                                                          list: MutableList<T> = mutableListOf()):
    MultiItemTypeAdapter<T>(context, list) {

    protected abstract fun onBindView(holder: ViewHolder, t: T, position: Int, payloads: List<Any>)

    init {

        addItemViewDelegate(object: ItemViewDelegate<T> {
            override fun itemViewLayoutId(): Int {
                return itemLayoutId
            }

            override fun isForViewType(item: T, position: Int): Boolean {
                return true
            }

            override fun convert(holder: ViewHolder, t: T, position: Int, payloads: List<Any>) {
                onBindView(holder, t, position, payloads)
            }

        })
    }
}