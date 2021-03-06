package com.magenta.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.*
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.appcompat.view.menu.MenuBuilder
import androidx.core.content.res.ResourcesCompat
import com.magenta.library.R
import com.magenta.library.utils.*
import com.magenta.navigation.utils.getDrawableReveal
import com.magenta.navigation.utils.getThemeAccentColor
import kotlin.math.roundToInt

/**
 * Created by ${User} on ${Date}
 */
@Suppress("MemberVisibilityCanBePrivate")
class TopBar : androidx.cardview.widget.CardView {
    private val screenUtils = Screen(context)
    private lateinit var prvSelectedTextView: TextView
    var attribute: AttributeSet? = null
    private var itemClickListener: OnTopBarItemClicked? = null
    private var defaultColor: Int = Color.GRAY
    private var activeColor: Int = getThemeAccentColor(context)
    private var defaultPosition: Int = 0
    private var titlePadding: Float = 0f
    private var titleMaxChars = 8

    private var titleActiveStyle = Typeface.BOLD
    private var titleFontFamily: Int = 0
    private var titleTypeface = Typeface.DEFAULT
    private var titleAlignment = Gravity.CENTER

    private lateinit var menu: Menu

    var textViewSize: Float = 17f
        set(value) {
            if (value != -1f)
                field = screenUtils.pixelsToSp(value)
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attribute = attrs
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        attribute = attrs
        init()
    }

    private fun init() {
        attributeBuilder()
    }

    private fun attributeBuilder() {
        if (attribute == null) {
            return
        }
        val ta = context.obtainStyledAttributes(attribute, R.styleable.TopBar)
        val menuRes = ta.getResourceId(R.styleable.TopBar_menuRes, 0)
        defaultPosition = ta.getInteger(R.styleable.TopBar_default_position, defaultPosition)

        defaultColor = ta.getColor(R.styleable.TopBar_default_color, defaultColor)
        activeColor = ta.getColor(R.styleable.TopBar_active_color, activeColor)

        textViewSize = ta.getDimension(R.styleable.TopBar_titleSize, -1f)
        titlePadding = ta.getDimension(R.styleable.TopBar_titlePadding, titlePadding)
        titleMaxChars = ta.getInteger(R.styleable.TopBar_titleMaxChars, titleMaxChars)

        titleAlignment = ta.getInteger(R.styleable.TopBar_titleGravity, titleAlignment)
        titleFontFamily = ta.getResourceId(R.styleable.TopBar_titleFontFamily, 0)
        titleActiveStyle = ta.getInteger(R.styleable.TopBar_titleActiveStyle,titleActiveStyle)
        if (titleFontFamily != 0)
            titleTypeface = ResourcesCompat.getFont(context, titleFontFamily)

        ta.recycle()

        if (menuRes != 0) setMenu(menuRes)
    }

    fun setMenu(@MenuRes menuRes: Int) {
        val menu = buildMenuView(menuRes)

        val viewParent: ViewGroup = if (menu.size() <= 5) {
            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(screenUtils.screenWidth, MATCH_PARENT)
                topBarBuilder(menu)
            }
        } else
            horScrollView {
                scrollBarSize = 0
                linearLayout {
                    topBarBuilder(menu, this@horScrollView)
                }
            }
        this@TopBar.addView(viewParent)
    }


    @SuppressLint("RestrictedApi")
    private fun buildMenuView(@MenuRes menuRes: Int): Menu {
        menu = MenuBuilder(context)
        MenuInflater(context).inflate(menuRes, menu)
        assert(menu.size() > 0) { "Menu must contains at least one item" }
        assert(defaultPosition < menu.size()) { "defaultPosition must be less then menu size" }
        return menu
    }

    private fun ViewGroup.topBarBuilder(menu: Menu, scrollView: HorizontalScrollView? = null) {
        for (position in 0 until menu.size()) {
            val item = menu.getItem(position)
            val textView = textViewBuilder(item)
            addView(textView)
            textView.setOnClickListener {
                scrollView?.scroll(textView.width, position)
                textView.onItemClick(item)
            }
        }
        //Activating color on default item.
        prvSelectedTextView = getChildAt(defaultPosition) as TextView
        prvSelectedTextView.setTextColor(activeColor)
        prvSelectedTextView.setTypeface(titleTypeface, titleActiveStyle)
    }

    private fun TextView.onItemClick(menuItem: MenuItem) {
        prvSelectedTextView.setTextColor(defaultColor)
        prvSelectedTextView.setTypeface(titleTypeface, Typeface.NORMAL)

        setTextColor(activeColor)
        setTypeface(titleTypeface, titleActiveStyle)

        prvSelectedTextView = this
        itemClickListener?.topItemClicked(menuItem)
    }

    private fun HorizontalScrollView.scroll(x: Int, itemPosition: Int) {
        val modifyPositionBy: Float = when {
            itemPosition > 5 -> itemPosition - 2.5f
            itemPosition >= 2 -> itemPosition - 1.5f
            itemPosition == 1 -> itemPosition - 0.4f
            else -> itemPosition + 0f
        }

        smoothScrollTo((x * modifyPositionBy).roundToInt(), 0)
    }

    private fun textViewBuilder(menuItem: MenuItem): TextView = textView {
        gravity = titleAlignment
        typeface = titleTypeface
        text = menuItem.title
        id = menuItem.itemId
        textSize = textViewSize
        ellipsize = TextUtils.TruncateAt.END
        maxEms = titleMaxChars
        maxLines = 1

        background = getDrawableReveal(context)

        setTextColor(defaultColor)
        layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT, 1f).apply {
            weight = 1f
            if (titlePadding != 0f) {
                val pad = titlePadding.roundToInt()
                setPadding(pad, pad, pad, screenUtils.dp(5f).roundToInt())
            }
            //setMargins(0, 0, screenUtils.dp(10f).roundToInt(), 0)
        }
    }

    fun setOnItemClick(listener: OnTopBarItemClicked) {
        itemClickListener = listener
    }

    fun setCurrentItem(@IdRes itemId: Int) {
        require(::menu.isInitialized) { "No menu passed ot the view" }
        val item = menu.findItem(itemId)
        itemClickListener?.topItemClicked(item)
    }


    interface OnTopBarItemClicked {
        fun topItemClicked(menuItem: MenuItem)
    }
}