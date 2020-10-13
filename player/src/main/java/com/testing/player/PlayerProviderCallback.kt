package com.testing.player

interface PlayerProviderCallback {
    fun onPlayStarted()
    fun onPlayStopped()
    fun onDataSourcePrepareStarted()
    fun onError()
}