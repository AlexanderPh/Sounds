package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import com.testing.repository.SoundRepository
import com.testing.repository.model.Melody
import com.testing.repository.model.Result
import com.testing.simpleaudioplayer.model.PlayableMelody

class MelodyInteractor(
    private val application: Application
){
    private val repository = SoundRepository(application)


    suspend fun loadList(rawResId: Int) : ArrayList<PlayableMelody>? {
        val result  = repository.loadFromRaw(rawResId)
        return when (result) {
            is Result.Success -> {
                generatePlayable(result.melodyList)
            }
            is Result.Failed -> {
                null
            }
        }


    }

    private fun generatePlayable(melodyList: List<Melody>): ArrayList<PlayableMelody> {
        val playableList = arrayListOf<PlayableMelody>()
        for (melody in melodyList){
            playableList.add(
                PlayableMelody(
                    id = melody.id,
                    name = melody.name,
                    previewPath = melody.previewPath,
                    coverPath = melody.coverPath
                )
            )
        }
        return playableList
    }


    companion object {
        @Volatile private var instance : MelodyInteractor? = null
        fun getInstance(application: Application) : MelodyInteractor {
            return instance ?: synchronized(this){
                instance ?: MelodyInteractor(application).also {
                    instance = it
                }
            }
        }
    }
}