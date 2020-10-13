package com.testing.simpleaudioplayer.list.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testing.simpleaudioplayer.model.PlayableMelody
import com.testing.simpleaudioplayer.views.PlayingState

class MelodyListAdapter : RecyclerView.Adapter<MelodyViewHolder>() {

    var currentIndex: Int = -1
    var controlCallback: PlayerControlCallback? = null

    var items: ArrayList<PlayableMelody> =  arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
            // todo implement diffUtil && payloads
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MelodyViewHolder {
        return MelodyViewHolder.create(parent.context)
    }

    override fun onBindViewHolder(
        holder: MelodyViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
    }

    override fun onBindViewHolder(holder: MelodyViewHolder, position: Int) {
        holder.bind(items[position], controlCallback)
    }

    override fun getItemCount(): Int {
        return items.size
    }
}