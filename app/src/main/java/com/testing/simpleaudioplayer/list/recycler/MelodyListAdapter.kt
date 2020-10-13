package com.testing.simpleaudioplayer.list.recycler

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.testing.core.log
import com.testing.simpleaudioplayer.model.PlayableMelody
import com.testing.simpleaudioplayer.views.PlayingState

class MelodyListAdapter : RecyclerView.Adapter<MelodyViewHolder>() {

    var currentIndex: Int = -1
    var controlCallback: PlayerControlCallback? = null

    private var items: ArrayList<PlayableMelody> =  arrayListOf()

    fun setNewItems(newList: ArrayList<PlayableMelody>){
        val diffUtil = MelodyListDiffUtil(items, newList)
        val result = DiffUtil.calculateDiff(diffUtil)
        items.clear()
        items.addAll(newList)
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MelodyViewHolder {
        return MelodyViewHolder.create(parent.context)
    }



    override fun onBindViewHolder(
        holder: MelodyViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val bundle = payloads[0] as Bundle
            if (bundle.containsKey(MelodyListDiffUtil.PROGRESS_KEY)){
                val progress = bundle.getInt(MelodyListDiffUtil.PROGRESS_KEY)
                holder.setProgress(progress)
            }
            if (bundle.containsKey(MelodyListDiffUtil.STATE_KEY)){
                val state = bundle.getSerializable(MelodyListDiffUtil.STATE_KEY) as PlayingState
                holder.setState(state)
            }
        }
    }

    override fun onBindViewHolder(holder: MelodyViewHolder, position: Int) {
        holder.bind(items[position], controlCallback)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}