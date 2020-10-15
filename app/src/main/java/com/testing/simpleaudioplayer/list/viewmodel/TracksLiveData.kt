package com.testing.simpleaudioplayer.list.viewmodel

import androidx.lifecycle.LiveData
import com.testing.simpleaudioplayer.model.PlayableTrack

class TracksLiveData(private val state: ListState) : LiveData<List<PlayableTrack>>() {

    override fun onActive() {
        value = state.tracks
    }
 }