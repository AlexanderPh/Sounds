package com.testing.simpleaudioplayer.list.viewmodel

import androidx.lifecycle.LiveData
import com.testing.simpleaudioplayer.model.PlayableTrack
import com.testing.simpleaudioplayer.views.PlayingState

class CurrentTrackLiveData(private val state: ListState): LiveData<PlayableTrack?>() {

    override fun onActive() {
        val currentTrack = state.tracks.find {
            it.state != PlayingState.OnStop
        }
        value = currentTrack
    }
}