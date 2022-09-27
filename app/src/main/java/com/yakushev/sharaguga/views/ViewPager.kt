package com.yakushev.sharaguga.views

import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import java.time.LocalDate

fun ViewPager2.showSideItems(pageMarginPx : Int, offsetPx : Int) {

    clipToPadding = false
    clipChildren = false
    offscreenPageLimit = 3

    setPageTransformer { page, position ->
        val offset = position * -(2 * offsetPx + pageMarginPx)

        if (this.orientation == ViewPager2.ORIENTATION_HORIZONTAL) {
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                page.translationX = -offset
            } else {
                page.translationX = offset
            }
        } else {
            page.translationY = offset
        }
    }
}