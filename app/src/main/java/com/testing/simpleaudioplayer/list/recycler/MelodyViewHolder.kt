package com.testing.simpleaudioplayer.list.recycler

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import com.testing.core.getDimension
import com.testing.simpleaudioplayer.R
import com.testing.simpleaudioplayer.model.PlayableMelody
import com.testing.simpleaudioplayer.views.InteractableCoverView

private const val COVER_VIEW_ID = 1700
private const val TITLE_VIEW_ID = 1701
private const val PROGRESS_VIEW_ID = 1702

class MelodyViewHolder(
    view: View,
    private val coverView: InteractableCoverView,
    private val melodyTitle: MaterialTextView,
    private val progress: ProgressBar

) : RecyclerView.ViewHolder(view){

    fun bind(melody: PlayableMelody){
        melodyTitle.text = melody.name
        coverView.bind(melody.coverPath)
        progress.progress = melody.progress
    }


    companion object{
        fun create(context: Context) : MelodyViewHolder {
            /*
            * Верстка для Вьюхолдера создается программно, просто для примера
            * Из плюсов - не надо парсить xml-разметку, работает быстрее
            * */


            val horizontalMargin = context.getDimension(
                R.dimen.list_item_horizontal_margin
            ).toInt()

            val horizontalMarginSmall = context.getDimension(
                R.dimen.list_item_horizontal_margin_small
            ).toInt()

            val verticalMargin = context.getDimension(
                R.dimen.list_item_vertical_margin
            ).toInt()

            val rootLayout = RelativeLayout(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

            val coverView = InteractableCoverView(context).apply {
                id = COVER_VIEW_ID
                val coverSize = context.getDimension(R.dimen.list_item_icon_size).toInt()
                layoutParams = RelativeLayout.LayoutParams(
                    coverSize,
                    coverSize
                ).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    addRule(RelativeLayout.ALIGN_PARENT_START)
                    setMargins(
                        horizontalMargin,
                        verticalMargin,
                        horizontalMarginSmall,
                        verticalMargin
                    )
                }

            }

            val melodyTitle = MaterialTextView(context).apply {
                id = TITLE_VIEW_ID
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.END_OF, COVER_VIEW_ID)
                    addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    addRule(RelativeLayout.ALIGN_PARENT_END)
                    setMargins(
                        0,
                        verticalMargin,
                        horizontalMargin,
                        0
                    )
                }

            }

            val progress = ProgressBar(
                context,
                null,
                android.R.attr.progressBarStyleHorizontal
            ).apply {
                id = PROGRESS_VIEW_ID
                layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.END_OF, COVER_VIEW_ID)
                    addRule(RelativeLayout.ABOVE, TITLE_VIEW_ID)
                    addRule(RelativeLayout.ALIGN_PARENT_END)
                }

                max = 100
                progress = 30
            }


            rootLayout.addView(coverView)
            rootLayout.addView(melodyTitle)
            rootLayout.addView(progress)



            return MelodyViewHolder(rootLayout, coverView, melodyTitle, progress)
        }
    }
}

