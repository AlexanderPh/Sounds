package com.testing.simpleaudioplayer.list.recycler


interface PlayerControlCallback {
    fun play(itemPosition: Int)
    fun pause(itemPosition: Int)
    fun play()
    fun pause()
    fun stop()
}