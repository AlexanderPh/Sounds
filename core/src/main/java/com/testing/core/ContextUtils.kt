package com.testing.core

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat

fun Context.getDrawableCompat(drawable: Int) : Drawable? {
    return AppCompatResources.getDrawable(this, drawable)
}

fun Context.getDimension(dimension: Int) : Float {
    return resources.getDimension(dimension)
}

fun Context.color(color: Int) : Int {
    return ContextCompat.getColor(this, color)
}


fun Context.getFontCompat(resource : Int): Typeface {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        this.resources.getFont(resource)
    } else {
        ResourcesCompat.getFont(this, resource)!!
    }
}