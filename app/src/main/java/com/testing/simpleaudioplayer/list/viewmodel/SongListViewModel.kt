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


     val currentTrack = MutableLiveData<PlayableMelody?>(null)
     val tracks = MutableLiveData<MutableList<PlayableMelody>>(mutableListOf())



     fun loadList(resId: Int) = viewModelScope.launch {
          val list = interactor.loadList(resId)
          list?.let {
               tracks.postValue(it)
          }
     }


     //коллбэк от плеера при начале воспроизведения
     override fun onPlayStarted() {
          currentTrack.value?.let { track ->
               val updatedMelody = track.copy(
                    state = PlayingState.Playing
               )
               updateList(updatedMelody)
               currentTrack.postValue(updatedMelody)
          }
     }

     //коллбэк от плеера при паузе воспроизведения
     override fun onPlayerPaused() {

     }


     //коллбэк от плеера при окончании воспроизведения
     override fun onPlayStopped() {
          currentTrack.value?.let { melody ->
               val updatedMelody = melody.copy(
                    state = PlayingState.OnStop,
                    progress = 0
               )
               updateList(updatedMelody)
               currentTrack.postValue(null)
          }
     }


     //коллбэк от плеера о начале буферизации
     override fun onDataSourcePrepareStarted() {

     }

     //коллбэк от плеера с прогрессом воспроизведения
     override fun onProgressUpdated(progress: Int) {
          currentTrack.value?.let { melody ->
               val updatedMelody = melody.copy(
                    progress = progress
               )
               updateList(updatedMelody)
               currentTrack.setValue(updatedMelody)
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
          handleCurrentTrack()
     }

     private fun handleCurrentTrack() {
          val track = currentTrack.value
          track?.let { tr ->
               tracks.value?.let { list ->
                    val inList = list.find {
                         tr.id == it.id
                    }
                    val indexOF = list.indexOf(inList)
                    when (tr.state){
                         PlayingState.Playing -> pauseTrack(tr, indexOF, list)
                         PlayingState.OnPause -> playTrack(tr, indexOF, list)
                         else -> return
                    }

               }
          }

     }

     override fun itemClosed() {
          player.reset()
          onPlayStopped()
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
          this.currentTrack.postValue(selectedTrack)
          tracks.postValue(list)
          player.playSoundFromUrl(selectedTrack.previewPath)
       //   log("ON_SWITCH")

     }

     private fun playTrack(
          selectedTrack: PlayableMelody,
          listPosition: Int,
          list: MutableList<PlayableMelody>
     ) {
          selectedTrack.state = PlayingState.Playing
          list[listPosition] = selectedTrack
          currentTrack.postValue(selectedTrack)
          tracks.postValue(list)
          player.play()
       //   log("ON_PLAY")
     }

     private fun pauseTrack(
          selectedTrack: PlayableMelody,
          listPosition: Int,
          list: MutableList<PlayableMelody>
     ) {
          selectedTrack.state = PlayingState.OnPause
          list[listPosition] = selectedTrack
          currentTrack.postValue(selectedTrack)
          tracks.postValue(list)
          player.pause()
         // log("ON_PAUSE")


     }

     private fun startNewTrack(
          selectedTrack: PlayableMelody,
          listPosition: Int,
          list: MutableList<PlayableMelody>
     ) {
          selectedTrack.state = PlayingState.Loading
          list[listPosition] = selectedTrack
          currentTrack.postValue(selectedTrack)
          tracks.postValue(list)
          player.playSoundFromUrl(selectedTrack.previewPath)
        //  log("ON_START_NEW")

     }

     fun onDestroy() {
          player.release()
     }

     fun onLifeCyclePause(){
          val track = currentTrack.value
          track?.let { tr ->
               tracks.value?.let { list ->
                    val inList = list.find {
                         tr.id == it.id
                    }
                    val indexOF = list.indexOf(inList)
                    when (tr.state){
                         PlayingState.Playing -> pauseTrack(tr, indexOF, list)
                         else -> return
                    }

               }
          }
     }

     fun onLifeCycleResume(){
          val track = currentTrack.value
          track?.let { tr ->
               tracks.value?.let { list ->
                    val inList = list.find {
                         tr.id == it.id
                    }
                    val indexOF = list.indexOf(inList)
                    when (tr.state){
                         PlayingState.OnPause -> playTrack(tr, indexOF, list)
                         else -> return
                    }

               }
          }
     }

}