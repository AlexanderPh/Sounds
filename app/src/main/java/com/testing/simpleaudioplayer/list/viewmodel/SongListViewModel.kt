package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.testing.core.log
import com.testing.player.PlayerProvider
import com.testing.player.PlayerProviderCallback
import com.testing.simpleaudioplayer.list.recycler.PlayerControlCallback
import com.testing.simpleaudioplayer.model.PlayableMelody
import com.testing.simpleaudioplayer.views.PlayingState
import kotlinx.coroutines.launch

class SongListViewModel(
     application: Application
) : AndroidViewModel(application), PlayerProviderCallback, PlayerControlCallback {

     private val interactor = MelodyInteractor.getInstance(application)
     private val player = PlayerProvider(this)


     val currentPlay = MutableLiveData<PlayableMelody?>(null)
     val tracks = MutableLiveData<MutableList<PlayableMelody>>(mutableListOf())



     fun loadList(resId: Int) = viewModelScope.launch {
          val list = interactor.loadList(resId)
          list?.let {
               tracks.postValue(it)
          }
     }




     //коллбэк от плеера при начала воспроизведения
     override fun onPlayStarted() {
          currentPlay.value?.let { track ->
               val updatedMelody = track.copy(
                    state = PlayingState.Playing
               )
               updateList(updatedMelody)
               currentPlay.setValue(updatedMelody)
          }

     }

     //коллбэк от плеера при паузе воспроизведения
     override fun onPlayerPaused() {

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
          tracks.value?.let {
               val inList = it.find {track ->
                    track.id == updatedMelody.id
               }

               if (inList != null){
                    val indexOf = it.indexOf(inList)
                    it[indexOf] = updatedMelody
                    tracks.postValue(it)
               }
          }
     }



     override fun onError() {
          onPlayStopped()
          Toast.makeText(getApplication(),"ERROR", Toast.LENGTH_LONG).show()
     }


     override fun itemClicked(itemPosition: Int) {
          handleTrack(itemPosition)

     }

     override fun itemClicked() {

     }

     override fun itemClosed() {
     }

     private fun handleTrack(itemPosition: Int) {
          val trackList = tracks.value
          trackList?.let { list ->
               val selectedTrack = list[itemPosition]
               val currentTrack = list.find {
                    it.state == PlayingState.Loading
                            || it.state == PlayingState.Playing
                            || it.state == PlayingState.OnPause
               }
               currentTrack?.let { ct ->
                    if (ct.id == selectedTrack.id){
                         when (ct.state){
                              PlayingState.Playing -> {
                                   pauseTrack(selectedTrack, itemPosition, list)
                              }
                              PlayingState.OnPause -> {
                                   playTrack(selectedTrack, itemPosition, list)
                              }
                              else -> return
                         }
                    } else {
                         switchTrack(selectedTrack, ct, itemPosition, list)
                    }
               } ?: startNewTrack(selectedTrack, itemPosition, list)

          }
     }

     private fun switchTrack(
          selectedTrack: PlayableMelody,
          currentTrack: PlayableMelody,
          itemPosition: Int,
          list: MutableList<PlayableMelody>
     ) {
          val currentTrackPosition = list.indexOf(currentTrack)
          currentTrack.state = PlayingState.OnStop
          currentTrack.progress = 0
          selectedTrack.state = PlayingState.Loading
          list[currentTrackPosition] = currentTrack
          list[itemPosition] = selectedTrack
          currentPlay.postValue(selectedTrack)
          tracks.postValue(list)
          player.playSoundFromUrl(selectedTrack.previewPath)
          log("ON_SWITCH")

     }

     private fun playTrack(
          selectedTrack: PlayableMelody,
          listPosition: Int,
          list: MutableList<PlayableMelody>
     ) {
          selectedTrack.state = PlayingState.Playing
          list[listPosition] = selectedTrack
          currentPlay.postValue(selectedTrack)
          tracks.postValue(list)
          player.play()
          log("ON_PLAY")
     }

     private fun pauseTrack(
          selectedTrack: PlayableMelody,
          listPosition: Int,
          list: MutableList<PlayableMelody>
     ) {
          selectedTrack.state = PlayingState.OnPause
          list[listPosition] = selectedTrack
          currentPlay.postValue(selectedTrack)
          tracks.postValue(list)
          player.pause()
          log("ON_PAUSE")


     }

     private fun startNewTrack(
          selectedTrack: PlayableMelody,
          listPosition: Int,
          list: MutableList<PlayableMelody>
     ) {
          selectedTrack.state = PlayingState.Loading
          list[listPosition] = selectedTrack
          currentPlay.postValue(selectedTrack)
          tracks.postValue(list)
          player.playSoundFromUrl(selectedTrack.previewPath)
          log("ON_START_NEW")

     }

}