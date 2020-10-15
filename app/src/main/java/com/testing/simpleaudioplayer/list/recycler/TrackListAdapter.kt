package com.testing.simpleaudioplayer.list.recycler

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.testing.simpleaudioplayer.model.PlayableTrack
import com.testing.simpleaudioplayer.views.PlayingState

class TrackListAdapter : RecyclerView.Adapter<TrackViewHolder>() {

    var controlCallback: PlayerControlCallback? = null

    var items: MutableList<PlayableTrack> =  mutableListOf()

     fun updateItems(newItems: MutableList<PlayableTrack>) {
//         val diffUtil = TrackListDiffUtil(items, newItems)
//         val result = DiffUtil.calculateDiff(diffUtil)
         items.clear()
         items.addAll(newItems)
         notifyDataSetChanged()
       // result.dispatchUpdatesTo(this)
     }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder.create(parent.context)
    }

    //переопределение перегруженного метода позволит контролировать конкретные изменения в полях вью
    override fun onBindViewHolder(
        holder: TrackViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach { payload ->
                val bundle = payload as Bundle
                if (bundle.containsKey(TrackListDiffUtil.PROGRESS_KEY)){
                    val progress = bundle.getInt(TrackListDiffUtil.PROGRESS_KEY)
                    holder.setProgress(progress)
                }
                if (bundle.containsKey(TrackListDiffUtil.STATE_KEY)){
                    val state = bundle.getSerializable(TrackListDiffUtil.STATE_KEY) as PlayingState
                    holder.setState(state)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(items[position], controlCallback)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}