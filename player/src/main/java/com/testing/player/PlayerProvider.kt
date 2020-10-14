package com.testing.player

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

class PlayerProvider(
    private val callback: PlayerProviderCallback?
) : MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private var currentUrl : String? = null
    private val player = MediaPlayer()

    init {
        player.setOnCompletionListener(this)
        player.setOnPreparedListener(this)
        player.setOnErrorListener(this)
    }
    private val handler = Handler(Looper.getMainLooper())

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            val current: Float = player.currentPosition.toFloat()
            val max: Float = player.duration.toFloat()
            val progress : Float = (current / max ) * 100f

            callback?.onProgressUpdated(progress.toInt())
            handler.postDelayed(this, 1000)
        }
    }



    fun playSoundFromUrl(url: String?){
        if (url != currentUrl){
            handler.removeCallbacks(runnable)
            currentUrl = url
            currentUrl.let {
                player.reset()
                player.setDataSource(it)
                player.prepareAsync()
                callback?.onDataSourcePrepareStarted()
            }
        }
    }

    override fun onPrepared(mp: MediaPlayer?) {
        player.start()
        callback?.onPlayStarted()
        runnable.run()
    }

    override fun onCompletion(mp: MediaPlayer?) {
        player.reset()
        handler.removeCallbacks(runnable)
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
            callback?.onPlayerPaused()
        }
    }

    override fun onError(mp: MediaPlayer?, what: Int, extra: Int): Boolean {
        callback?.onError()
        player.reset()
        return false
    }

    fun reset() {
        player.reset()
    }

}



