package com.ayvytr.baseadapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.*
import androidx.annotation.*
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Jason on 2018/8/14.
 */
class ViewHolder(private val mContext: Context, val convertView: View):
    RecyclerView.ViewHolder(convertView) {
    private val mViews: SparseArray<View?> = SparseArray()

    fun <T: View?> getView(@IdRes viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    fun setText(@IdRes viewId: Int, @StringRes textId: Int): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.setText(textId)
        return this
    }

    fun setText(@IdRes viewId: Int, text: CharSequence?): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        return this
    }

    fun setImage(@IdRes viewId: Int, resId: Int): ViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageResource(resId)
        return this
    }

    fun setImage(@IdRes viewId: Int, bitmap: Bitmap?): ViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImage(@IdRes viewId: Int, drawable: Drawable?): ViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): ViewHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(@IdRes viewId: Int, @ColorRes textColorRes: Int): ViewHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(mContext.resources.getColor(textColorRes))
        return this
    }

    @SuppressLint("NewApi")
    fun setAlpha(@IdRes viewId: Int, value: Float): ViewHolder {
        val view = getView<View>(viewId)!!
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            view.alpha = value
        } else {
            // Pre-honeycomb hack to set Alpha value
            val alpha = AlphaAnimation(value, value)
            alpha.duration = 0
            alpha.fillAfter = true
            view.startAnimation(alpha)
        }
        return this
    }

    fun setVisible(@IdRes viewId: Int, visible: Int): ViewHolder {
        val view = getView<View>(viewId)!!
        view.visibility = visible
        return this
    }

    fun linkify(@IdRes viewId: Int): ViewHolder {
        val view = getView<TextView>(viewId)!!
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface?, @IdRes vararg viewIds: Int): ViewHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)!!
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(@IdRes viewId: Int, progress: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.progress = progress
        return this
    }

    fun setProgress(@IdRes viewId: Int, progress: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(@IdRes viewId: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        return this
    }

    fun setRating(@IdRes viewId: Int, rating: Float): ViewHolder {
        val view = getView<RatingBar>(viewId)!!
        view.rating = rating
        return this
    }

    fun setRating(@IdRes viewId: Int, rating: Float, max: Int): ViewHolder {
        val view = getView<RatingBar>(viewId)!!
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(@IdRes viewId: Int, tag: Any?): ViewHolder {
        val view = getView<View>(viewId)!!
        view.tag = tag
        return this
    }

    fun setTag(@IdRes viewId: Int, key: Int, tag: Any?): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setTag(key, tag)
        return this
    }

    fun setChecked(@IdRes viewId: Int, checked: Boolean): ViewHolder {
        val view = getView<View>(viewId) as Checkable
        view.isChecked = checked
        return this
    }

    fun setSelected(@IdRes viewId: Int, isSelected: Boolean): ViewHolder {
        val view = getView<View>(viewId)!!
        view.isSelected = isSelected
        return this
    }

    fun setOnClickListener(@IdRes viewId: Int,
                           listener: View.OnClickListener?): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(@IdRes viewId: Int,
                           listener: View.OnTouchListener?): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(@IdRes viewId: Int,
                               listener: View.OnLongClickListener?): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setOnLongClickListener(listener)
        return this
    }

    fun setEnabled(@IdRes viewId: Int, enabled: Boolean): ViewHolder {
        val view = getView<View>(viewId)!!
        view.isEnabled = enabled
        return this
    }

    fun appendText(@IdRes viewId: Int, text: CharSequence?) {
        val tv = getView<TextView>(viewId)!!
        tv.append(text)
    }

    companion object {
        @JvmStatic
        fun createViewHolder(context: Context, itemView: View): ViewHolder {
            return ViewHolder(context, itemView)
        }

        @JvmStatic
        fun createViewHolder(context: Context,
                             parent: ViewGroup?, layoutId: Int): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutId, parent,
                                                                false)
            return ViewHolder(context, itemView)
        }
    }

}