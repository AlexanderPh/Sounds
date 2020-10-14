package com.testing.simpleaudioplayer.views

import android.animation.ValueAnimator
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.widget.AppCompatImageView
import com.google.android.material.textview.MaterialTextView
import com.testing.core.*
import com.testing.simpleaudioplayer.R
import com.testing.simpleaudioplayer.list.recycler.PlayerControlCallback
import com.testing.simpleaudioplayer.model.PlayableMelody


private const val COVER_VIEW_ID = 929
private const val TITLE_VIEW_ID = 930
private const val PROGRESS_VIEW_ID = 931
private const val CLOSE_VIEW_ID = 932

private const val EXPANDED_MULTIPLIER = 1f
private const val COLLAPSED_MULTIPLIER = 0f

class CurrentPlayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var controlCallback : PlayerControlCallback? = null


    private var targetMultiplier = 0f
    private var multiplier : Float = 0f
        set(value) {
            field = value
            requestLayout()
        }


    private val animator: ValueAnimator = ValueAnimator().apply {
        duration = 300
        interpolator = AccelerateDecelerateInterpolator()
        addUpdateListener {
            multiplier = it.animatedValue as Float
        }
    }

    private val titleText = context.getString(R.string.now_playing_title)
    private val horizontalMargin = context.getDimension(
        R.dimen.list_item_horizontal_margin
    ).toInt()
    private val verticalMargin = context.getDimension(
        R.dimen.list_item_vertical_margin
    ).toInt()
    private val iconSize = context.getDimension(
        R.dimen.close_icon_size
    ).toInt()

    private val coverView = InteractableCoverView(context).apply {
        id = COVER_VIEW_ID
        val coverSize = context.getDimension(R.dimen.list_item_icon_size).toInt()
        layoutParams = LayoutParams(
            coverSize,
            coverSize
        ).apply {
            addRule(ALIGN_PARENT_TOP)
            addRule(ALIGN_PARENT_START)
            setMargins(
                horizontalMargin,
                verticalMargin,
                horizontalMargin,
                verticalMargin
            )
        }

    }

    private val closeView = AppCompatImageView(context).apply {
        id = CLOSE_VIEW_ID
        layoutParams = LayoutParams(
            iconSize,
            iconSize
        ).apply {
            // addRule(ALIGN_PARENT_TOP)
            addRule(ALIGN_PARENT_END)
            addRule(CENTER_VERTICAL)
            setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin)
        }
        setImageDrawable(context.getDrawableCompat(R.drawable.ic_close))
    }

    private val melodyTitle = MaterialTextView(context).apply {
        id = TITLE_VIEW_ID
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(END_OF, COVER_VIEW_ID)
            addRule(ALIGN_PARENT_TOP)
            addRule(START_OF, CLOSE_VIEW_ID)
            setMargins(
                0,
                verticalMargin,
                0,
                0
            )
        }

        setTextSize(
            TypedValue.COMPLEX_UNIT_PX,
            context.getDimension(R.dimen.list_item_text_size)
        )

        setTextColor(context.color(R.color.colorWhite))
        typeface = context.getFontCompat(R.font.roboto_regular)
        maxLines = 1
        setLines(1)
        ellipsize = TextUtils.TruncateAt.END
        text = titleText
    }

    private val progress = ProgressBar(
        context,
        null,
        android.R.attr.progressBarStyleHorizontal
    ).apply {
        id = PROGRESS_VIEW_ID
        val progressHeight = context.getDimension(R.dimen.progress_bar_height).toInt()

        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            progressHeight
        ).apply {
            addRule(END_OF, COVER_VIEW_ID)
            addRule(BELOW, TITLE_VIEW_ID)
            addRule(START_OF, CLOSE_VIEW_ID)
            setMargins(
                0,
                verticalMargin,
                0,
                verticalMargin
            )
        }

        max = 100
        progressDrawable = context.getDrawableCompat(R.drawable.list_item_progress_drawable)
    }


    init {
        addView(coverView)
        addView(closeView)
        addView(melodyTitle)
        addView(progress)

        closeView.onClick {
            controlCallback?.itemClosed()
            collapse()
        }

        coverView.onClick {
            controlCallback?.itemClicked()

        }

    }
    fun bind(melody: PlayableMelody) {
        melodyTitle.text = "$titleText ${melody.name}"
        coverView.playingState = melody.state
        progress.progress = melody.progress
        expand()
    }

    fun collapse(){
        if (targetMultiplier == COLLAPSED_MULTIPLIER) return
        targetMultiplier = COLLAPSED_MULTIPLIER
        animator.cancel()
        applyAnimation(multiplier, targetMultiplier)

    }
    fun expand(){
        if (targetMultiplier == EXPANDED_MULTIPLIER) return
        targetMultiplier = EXPANDED_MULTIPLIER
        animator.cancel()
        applyAnimation(multiplier, targetMultiplier)
    }



    private fun applyAnimation(start: Float, end: Float) {
        animator.setFloatValues(start, end)
        animator.start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)


        val w = (measuredWidth * multiplier).toInt()
        val h = (measuredHeight * multiplier).toInt()

        setMeasuredDimension(w, h)

    }

}