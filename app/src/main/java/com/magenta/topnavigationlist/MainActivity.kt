package com.magenta.topnavigationlist

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.magenta.library.OnTopBarItemClicked
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var prvTextView: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
     /*   val colorAccent = ContextCompat.getColor(this, R.color.colorAccent)

        for (i in 0 until text_parent.childCount) {
            val textView = text_parent.getChildAt(i) as TextView
            textView.setTextColor(Color.GRAY)

            //this lambda function is not invoked until user click.
            textView.setOnClickListener {
                prvTextView.setTextColor(Color.GRAY)
                prvTextView.typeface = Typeface.DEFAULT
                textView.setTextColor(Color.BLACK)
                textView.typeface = Typeface.DEFAULT_BOLD
                Toast.makeText(this, textView.text, Toast.LENGTH_SHORT).show()
                prvTextView = textView
            }
        }

        val firstTextView = text_parent.getChildAt(0) as TextView
        firstTextView.setTextColor(Color.BLACK)
        firstTextView.typeface = Typeface.DEFAULT_BOLD
        prvTextView = firstTextView*/
    }

    fun toast(str: String) {
        Toast.makeText(this, "clicked view ID:$str", Toast.LENGTH_SHORT).show()
    }
}
