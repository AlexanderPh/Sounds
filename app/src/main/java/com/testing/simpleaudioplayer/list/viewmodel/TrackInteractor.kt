package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import com.testing.repository.SoundRepository
import com.testing.repository.model.Track
import com.testing.repository.model.Result
import com.testing.simpleaudioplayer.model.PlayableTrack

class TrackInteractor(
    application: Application
){
    private val repository = SoundRepository(application)


    suspend fun loadList(rawResId: Int) : ArrayList<PlayableTrack>? {
        return when (val result  = repository.loadFromRaw(rawResId)) {
            is Result.Success -> {
                generatePlayable(result.trackList)
            }
            is Result.Failed -> {
                null
            }
        }
    }

    private fun generatePlayable(trackList: List<Track>): ArrayList<PlayableTrack> {
        val playableList = arrayListOf<PlayableTrack>()
        for (track in trackList){
            playableList.add(
                PlayableTrack(
                    id = track.id,
                    name = track.name,
                    previewPath = track.audio,
                    coverPath = track.image,
                    assetPath = track.path
                )
            )
        }
        return playableList
    }


    companion object {
        @Volatile private var instance : TrackInteractor? = null
        fun getInstance(application: Application) : TrackInteractor {
            return instance ?: synchronized(this){
                instance ?: TrackInteractor(application).also {
                    instance = it
                }
            }
        }
    }
}