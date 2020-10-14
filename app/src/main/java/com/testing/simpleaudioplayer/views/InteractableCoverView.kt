package com.testing.simpleaudioplayer.views

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.testing.core.getDimension
import com.testing.core.getDrawableCompat
import com.testing.simpleaudioplayer.R

class InteractableCoverView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var playingState: PlayingState = PlayingState.OnPause
        set(value) {
            field = value
            updateState()
        }

    private fun updateState() {
        when (playingState){
            PlayingState.Loading -> setLoading()
            PlayingState.Playing -> setPlaying()
            else -> setOnPause()
        }
    }

    private val glide = Glide.with(context)
    private val cornerRadius = context
        .getDimension(R.dimen.list_item_icon_radius_size)
        .toInt()

    private val background = AppCompatImageView(context).apply {
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
    }
    private val playDrawable = context.getDrawableCompat(R.drawable.ic_play)
    private val pauseDrawable = context.getDrawableCompat(R.drawable.ic_pause)
    private val iconSize = context.getDimension(R.dimen.cover_icon_size).toInt()

    private val icon = AppCompatImageView(context).apply {
        layoutParams = LayoutParams(
            iconSize,
            iconSize
        ).apply {
            gravity = Gravity.CENTER
        }
    }

    private val progressBar = ProgressBar(context).apply {
        layoutParams = LayoutParams(
            iconSize,
            iconSize
        ).apply {
            gravity = Gravity.CENTER
        }
        visibility = INVISIBLE
    }

    init {
        addView(background)
        addView(icon)
        addView(progressBar)
        playingState =PlayingState.OnPause
    }

    fun bind( coverPath: String?) {
        glide.load(coverPath).transform(RoundedCorners(cornerRadius)).into(background)
    }



    private fun setPlaying() {
        icon.visibility = VISIBLE
        icon.setImageDrawable(pauseDrawable)
        progressBar.visibility = View.INVISIBLE
    }

    private fun setOnPause() {
        icon.visibility = VISIBLE
        icon.setImageDrawable(playDrawable)
        progressBar.visibility = View.INVISIBLE
    }

    private fun setLoading() {
        icon.visibility = View.INVISIBLE
        progressBar.visibility = VISIBLE
    }



}