package com.testing.simpleaudioplayer.list.recycler

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

class ItemAnimator : SimpleItemAnimator() {
    override fun animateChange(
        oldHolder: RecyclerView.ViewHolder?,
        newHolder: RecyclerView.ViewHolder?,
        fromLeft: Int,
        fromTop: Int,
        toLeft: Int,
        toTop: Int
    ): Boolean {
        dispatchChangeFinished(oldHolder, true);
        dispatchChangeFinished(newHolder, false);
        return false
    }

    override fun runPendingAnimations() {
    }

    override fun endAnimation(item: RecyclerView.ViewHolder) {
    }

    override fun endAnimations() {
    }

    override fun isRunning(): Boolean {
        return false
    }

    override fun animateRemove(holder: RecyclerView.ViewHolder?): Boolean {
        dispatchRemoveFinished(holder);
        return true
    }

    override fun animateAdd(holder: RecyclerView.ViewHolder?): Boolean {
        dispatchAddFinished(holder);
        return true
    }

    override fun animateMove(
        holder: RecyclerView.ViewHolder?,
        fromX: Int,
        fromY: Int,
        toX: Int,
        toY: Int
    ): Boolean {
        dispatchMoveFinished(holder)
        return false
    }
}