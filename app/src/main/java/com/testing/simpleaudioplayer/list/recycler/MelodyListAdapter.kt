package com.testing.simpleaudioplayer.list.recycler

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testing.simpleaudioplayer.model.PlayableMelody

class MelodyListAdapter : RecyclerView.Adapter<MelodyViewHolder>() {

    var items: ArrayList<PlayableMelody> =  arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()
            // todo implement diffUtil && payloads
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MelodyViewHolder {
        return MelodyViewHolder.create(parent.context)
    }

    override fun onBindViewHolder(holder: MelodyViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }


}