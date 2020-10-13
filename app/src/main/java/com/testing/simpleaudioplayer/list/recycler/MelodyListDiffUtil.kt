package com.testing.simpleaudioplayer.list.recycler

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.testing.simpleaudioplayer.model.PlayableMelody



class MelodyListDiffUtil(
    private val oldItems: ArrayList<PlayableMelody>,
    private val newItems: ArrayList<PlayableMelody>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]


        return  oldItem.id == newItem.id
                && oldItem.state == newItem.state
                && oldItem.progress == newItem.progress
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        val bundle = Bundle()
        if (oldItem.state != newItem.state){
            bundle.putSerializable(STATE_KEY, newItem.state)
        }
        if (oldItem.progress != newItem.progress){
            bundle.putInt(PROGRESS_KEY, newItem.progress)
        }
        if (bundle.size() == 0) return null
        return bundle
    }

    companion object{
        const val PROGRESS_KEY = "progress_key"
        const val STATE_KEY = "state_key"
    }

}