package com.testing.core

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable

private fun createColorStateList(color : Int) : ColorStateList {
    return ColorStateList(
        arrayOf(intArrayOf()),
        intArrayOf(color)
    )
}



fun createRippleDrawable(drawable : Drawable, color : Int) : Drawable {
    return RippleDrawable(createColorStateList(color), drawable, null)
}
