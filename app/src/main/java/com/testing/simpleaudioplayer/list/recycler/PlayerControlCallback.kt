package com.testing.simpleaudioplayer.list.recycler


interface PlayerControlCallback {
    fun itemClicked(itemPosition: Int)
    fun itemClicked()
    fun itemClosed()
}