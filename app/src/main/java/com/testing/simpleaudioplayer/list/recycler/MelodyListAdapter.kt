package com.testing.simpleaudioplayer.list.recycler

import android.os.Bundle
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.testing.core.log
import com.testing.simpleaudioplayer.model.PlayableMelody
import com.testing.simpleaudioplayer.views.PlayingState

class MelodyListAdapter : RecyclerView.Adapter<MelodyViewHolder>() {

    var controlCallback: PlayerControlCallback? = null

    var items: MutableList<PlayableMelody> =  mutableListOf()

     fun updateItems(newItems: MutableList<PlayableMelody>) {
//         val diffUtil = MelodyListDiffUtil(items, newItems)
//         val result = DiffUtil.calculateDiff(diffUtil)
         items.clear()
         items.addAll(newItems)
            notifyDataSetChanged()
       //  result.dispatchUpdatesTo(this)
     }

//        set(value) {
//            val diffUtil = MelodyListDiffUtil(field, value)
//            val result = DiffUtil.calculateDiff(diffUtil)
//            field.clear()
//            field.addAll(value)
//            result.dispatchUpdatesTo(this)
////            field = value
////            notifyDataSetChanged()
//        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MelodyViewHolder {
        return MelodyViewHolder.create(parent.context)
    }



    override fun onBindViewHolder(
        holder: MelodyViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            payloads.forEach { payload ->
                val bundle = payload as Bundle
                if (bundle.containsKey(MelodyListDiffUtil.PROGRESS_KEY)){
                    val progress = bundle.getInt(MelodyListDiffUtil.PROGRESS_KEY)
                    holder.setProgress(progress)
                }
                if (bundle.containsKey(MelodyListDiffUtil.STATE_KEY)){
                    val state = bundle.getSerializable(MelodyListDiffUtil.STATE_KEY) as PlayingState
                    holder.setState(state)
                }

                log(payload)
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