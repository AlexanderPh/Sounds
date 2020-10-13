package com.testing.player

import android.media.MediaPlayer

class PlayerProvider(
    private val callback: PlayerProviderCallback?
) : MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private var currentUrl : String? = null
    private val player = MediaPlayer()
    init {
        player.setOnCompletionListener(this)
        player.setOnPreparedListener(this)
    }

    fun playSoundFromUrl(url: String?){
        if (url != currentUrl){
            currentUrl = url
            currentUrl.let {
                player.reset()
                player.setDataSource(url)
                player.prepareAsync()
                callback?.onDataSourcePrepareStarted()
            }
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        player.start()
        callback?.onPlayStarted()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        player.stop()
        callback?.onPlayStopped()

    }

    fun play(){
        if (!player.isPlaying) {
            player.start()
            callback?.onPlayStarted()
        }
    }

    fun pause(){
        if (player.isPlaying) {
            player.pause()
            callback?.onPlayStopped()
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        callback?.onError()
        return false
    }
}



