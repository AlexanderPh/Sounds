package com.testing.simpleaudioplayer.list.recycler

import androidx.recyclerview.widget.DiffUtil
import com.testing.core.log
import com.testing.simpleaudioplayer.model.PlayableTrack



class TrackListDiffUtil(
    private val oldItems: List<PlayableTrack>,
    private val newItems: List<PlayableTrack>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].id == newItems[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        val same = oldItem.state == newItem.state && oldItem.progress == newItem.progress

        log("Same $same")
        return same
    }


    companion object{
        const val PROGRESS_KEY = "progress_key"
        const val STATE_KEY = "state_key"
    }

}