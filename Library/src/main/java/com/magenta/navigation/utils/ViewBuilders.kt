package com.magenta.library.utils

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView

/**
 * Created by ${User} on ${Date}
 */


internal fun View.linearLayout(block: LinearLayout.() -> Unit) = LinearLayout(context).apply {
    block(this)
}


internal fun View.textView(block: TextView.() -> Unit) = TextView(context).apply {
    block(this)
}

internal fun View.horScrollView(block: HorizontalScrollView.() -> Unit) = HorizontalScrollView(context).apply {
    block(this)
}
