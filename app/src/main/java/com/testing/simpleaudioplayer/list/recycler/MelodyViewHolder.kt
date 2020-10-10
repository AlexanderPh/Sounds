package com.testing.simpleaudioplayer.list.recycler

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.testing.simpleaudioplayer.model.PlayableMelody

class MelodyViewHolder(
     view: View

) : RecyclerView.ViewHolder(view){

    fun bind(melody: PlayableMelody){

    }


    companion object{
        fun create(context: Context) : MelodyViewHolder {
            val view = View(context)
            return MelodyViewHolder(view)
        }
    }
}

