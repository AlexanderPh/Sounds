package com.testing.simpleaudioplayer.list.viewmodel

import com.testing.simpleaudioplayer.model.PlayableTrack
import com.testing.simpleaudioplayer.views.PlayingState

data class ListState(
    var tracks: List<PlayableTrack> = emptyList()
){
    fun pause(): ListState{
        val newTracks = mutableListOf<PlayableTrack>()
        tracks.forEach { track ->
            if (track.state == PlayingState.Playing){
                newTracks.add(track.copy(
                    state = PlayingState.OnPause
                ))
            } else {
                newTracks.add(track)
            }
        }
        return ListState(newTracks)
    }

    fun stop(): ListState{
        val newTracks = mutableListOf<PlayableTrack>()
        tracks.forEach { track ->
            if (track.state != PlayingState.OnStop){
                newTracks.add(track.copy(
                    state = PlayingState.OnStop,
                    progress = 0
                ))
            } else {
                newTracks.add(track)
            }
        }
        return ListState(newTracks)
    }

    fun play(): ListState{
        val newTracks = mutableListOf<PlayableTrack>()
        tracks.forEach { track ->
            if (track.state == PlayingState.OnPause || track.state == PlayingState.Loading){
                newTracks.add(track.copy(
                    state = PlayingState.Playing
                ))
            } else {
                newTracks.add(track)
            }
        }
        return ListState(newTracks)
    }

    fun isSomethingPlaying(): Boolean{
        tracks.forEach{
            if (it.state == PlayingState.Playing){
                return true
            }
        }
        return false
    }

    fun isSomethingOnPause(): Boolean{
        tracks.forEach{
            if (it.state == PlayingState.OnPause){
                return true
            }
        }
        return false
    }

    fun updateProgress(progress: Int) : ListState{
        val newTracks = mutableListOf<PlayableTrack>()
        tracks.forEach { track ->
            if (track.state == PlayingState.Playing){
                newTracks.add(track.copy(
                    progress = progress
                ))
            } else {
                newTracks.add(track)
            }
        }
        return ListState(newTracks)
    }

    fun getSelectedTrack(index: Int): PlayableTrack? {
        var track : PlayableTrack? = null
        if (!tracks.isNullOrEmpty()){
            track = tracks[index]
        }
        return track
    }

    fun findCurrentTrack(): PlayableTrack? {
       return tracks.find {
           it.state != PlayingState.OnStop
       }

    }

    fun start(id: Int): ListState {
        val newTracks = mutableListOf<PlayableTrack>()
        tracks.forEach { track ->
            if (track.id == id){
                newTracks.add(track.copy(
                    state = PlayingState.Loading
                ))
            } else {
                newTracks.add(track)
            }
        }
        return ListState(newTracks)
    }
}