package com.testing.player

interface PlayerProviderCallback {
    fun onPlayStarted()
    fun onPlayerPaused()
    fun onPlayStopped()
    fun onDataSourcePrepareStarted()
    fun onProgressUpdated(progress: Int)
    fun onBufferingProgressUpdated(progress: Int)
    fun onError()
}