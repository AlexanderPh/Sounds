package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.testing.player.PlayerProvider
import com.testing.player.PlayerProviderCallback
import com.testing.simpleaudioplayer.list.recycler.PlayerControlCallback
import com.testing.simpleaudioplayer.model.PlayableMelody
import com.testing.simpleaudioplayer.views.PlayingState
import kotlinx.coroutines.launch

const val INIT_POSITION = -1
class SongListViewModel(
      application: Application
) : AndroidViewModel(application), PlayerProviderCallback, PlayerControlCallback {

     private val interactor = MelodyInteractor.getInstance(application)
     private val player = PlayerProvider(this)


     val currentPlay = MutableLiveData<PlayableMelody?>(null)
     private val currentPlayIndex = MutableLiveData(INIT_POSITION)
     val melodyList = MutableLiveData<MutableList<PlayableMelody>>(mutableListOf())



     fun loadList(resId: Int) = viewModelScope.launch {
          val list = interactor.loadList(resId)
          list?.let {
               melodyList.postValue(it)
          }
     }




     //коллбэк от плеера при начала воспроизведения
     override fun onPlayStarted() {
          currentPlay.value?.let { melody ->
               val updatedMelody = melody.copy(
                    state = PlayingState.Playing
               )
               updateList(updatedMelody)
               currentPlay.setValue(updatedMelody)
          }

     }

     //коллбэк от плеера при паузе воспроизведения
     override fun onPlayerPaused() {
          currentPlay.value?.let { melody ->
               val updatedMelody = melody.copy(
                    state = PlayingState.OnPause
               )
               updateList(updatedMelody)
               currentPlay.setValue(updatedMelody)
          }
     }


     //коллбэк от плеера при окончании воспроизведения
     override fun onPlayStopped() {
          currentPlay.value?.let { melody ->
               val updatedMelody = melody.copy(
                    state = PlayingState.Playing
               )
               updateList(updatedMelody)
               currentPlay.setValue(null)
          }
     }


     //коллбэк от плеера о начале буферизации
     override fun onDataSourcePrepareStarted() {
          currentPlay.value?.let { melody ->
               val updatedMelody = melody.copy(
                    state = PlayingState.Loading
               )
               updateList(updatedMelody)
               currentPlay.setValue(updatedMelody)
          }
     }

     //коллбэк от плеера с прогрессом воспроизведения
     override fun onProgressUpdated(progress: Int) {
          currentPlay.value?.let { melody ->
               val updatedMelody = melody.copy(
                    progress = progress
               )
               updateList(updatedMelody)
               currentPlay.setValue(updatedMelody)
          }
     }

     //коллбэк от плеера с прогрессом буферизации

     override fun onBufferingProgressUpdated(progress: Int) {
          // на всякий, но не имплементируем

     }

     private fun updateList(updatedMelody: PlayableMelody) {
          melodyList.value?.let {
               val index = currentPlayIndex.value
               if (index!! != -1){
                    it[index]= updatedMelody
               }
               melodyList.postValue(it)
          }
     }



     override fun onError() {
          onPlayStopped()
          Toast.makeText(getApplication(),"ERROR", Toast.LENGTH_LONG).show()
     }

     override fun play(itemPosition: Int) {
          if (melodyList.value?.get(itemPosition) != null){
               val melody = melodyList.value?.get(itemPosition)
               if (currentPlay.value?.id == melody?.id){
                    player.play()
               } else {
                    currentPlay.postValue(melody)
                    currentPlayIndex.value = itemPosition
                    player.playSoundFromUrl(melody?.previewPath)
               }
          }
     }

     override fun play() {
          if (currentPlayIndex.value != INIT_POSITION) {
               play(currentPlayIndex.value!!)
          }
     }

     override fun pause(itemPosition: Int) {
          pause()
     }

     override fun pause() {
          player.pause()
     }

     override fun stop() {
     }


}