package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.testing.core.log
import com.testing.player.PlayerProvider
import com.testing.player.PlayerProviderCallback
import com.testing.simpleaudioplayer.list.recycler.PlayerControlCallback
import com.testing.simpleaudioplayer.list.viewmodel.SongListViewModel.Action.*
import com.testing.simpleaudioplayer.model.PlayableMelody
import com.testing.simpleaudioplayer.views.PlayingState
import kotlinx.coroutines.launch

class SongListViewModel(
     application: Application
) : AndroidViewModel(application), PlayerProviderCallback, PlayerControlCallback {

     private val interactor = MelodyInteractor.getInstance(application)
     private val player = PlayerProvider(this)


     val currentPlay = MutableLiveData<PlayableMelody?>(null)
     val currentPlayIndex = MutableLiveData<Int>(-1)


     val progress : LiveData<Int> = Transformations.map(currentPlay){
          it?.progress
     }
     val state: LiveData<PlayingState> = Transformations.map(currentPlay){
          it?.state
     }

     val melodyList = MutableLiveData<ArrayList<PlayableMelody>>(ArrayList())



     fun onAction(action: Action) {
          when (action){
               is LoadList -> loadList(action.resId)
               is SelectMelody -> selectMelody(action.melody)
          }
     }

     private fun loadList(resId: Int) = viewModelScope.launch {
          val list = interactor.loadList(resId)
          list?.let {
               melodyList.postValue(it)
          }
     }

     private fun selectMelody(melody: PlayableMelody) {
          currentPlay.postValue(melody)
          player.playSoundFromUrl(melody.previewPath)
     }


     sealed class Action{
          class LoadList(val resId: Int) : Action()
          class SelectMelody(val melody: PlayableMelody) : Action()
     }

     override fun onPlayStarted() {
          currentPlay.value?.let { melody ->
               currentPlay.value = melody.copy(
                    state = PlayingState.Playing
               ).also {
                    updateList(it)
               }
          }

     }

     override fun onPlayStopped() {
          currentPlay.value?.let { melody ->
               currentPlay.postValue( melody.copy(
                    state = PlayingState.OnPause
               ).also {
                    updateList(it)
               }
               )
          }
     }

     private fun updateList(updatedMelody: PlayableMelody) {
          melodyList.value?.let {
               val index = currentPlayIndex.value
               if (index!! != -1){
                    it[index] = updatedMelody
               }

               melodyList.postValue(it)
          }
     }

     override fun onDataSourcePrepareStarted() {
          currentPlay.value?.let { melody ->
               currentPlay.value = melody.copy(
                    state = PlayingState.Loading
               ).also {
                    updateList(it)
               }
          }
     }

     override fun onProgressUpdated(progress: Int) {
          log(progress)
          currentPlay.value?.let { melody ->
               currentPlay.value = melody.copy(
                    progress = progress
               ).also {
                    updateList(it)
               }
          }
     }

     override fun onBufferingProgressUpdated(progress: Int) {
     }

     override fun onError() {

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

     override fun pause(itemPosition: Int) {
          player.pause()
     }


}