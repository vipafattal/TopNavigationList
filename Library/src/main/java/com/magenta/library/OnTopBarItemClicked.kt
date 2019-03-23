package com.magenta.library

import android.widget.TextView

interface OnTopBarItemClicked {
    fun topItemClicked(textView: TextView, id: Int, position: Int)
}