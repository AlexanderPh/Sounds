package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.testing.player.PlayerProvider
import com.testing.player.PlayerProviderCallback
import com.testing.simpleaudioplayer.list.recycler.PlayerControlCallback
import com.testing.simpleaudioplayer.model.PlayableTrack
import com.testing.simpleaudioplayer.views.PlayingState
import kotlinx.coroutines.launch

class TrackListViewModel(
     application: Application
) : AndroidViewModel(application), PlayerProviderCallback, PlayerControlCallback {

     private val interactor = TrackInteractor.getInstance(application)
     private val player = PlayerProvider(this)


     val currentTrack = MutableLiveData<PlayableTrack?>(null)
     val tracks = MutableLiveData<MutableList<PlayableTrack>>(mutableListOf())



     fun loadList(resId: Int) = viewModelScope.launch {
          val list = interactor.loadList(resId)
          list?.let {
               tracks.postValue(it)
          }
     }


     //коллбэк от плеера при начале воспроизведения
     override fun onPlayStarted() {
          currentTrack.value?.let { track ->
               val updatedTrack = track.copy(
                    state = PlayingState.Playing
               )
               updateList(updatedTrack)
               currentTrack.value = updatedTrack
          }
     }

     //коллбэк от плеера при паузе воспроизведения
     override fun onPlayerPaused() {

     }


     //коллбэк от плеера при окончании воспроизведения
     override fun onPlayStopped() {
          currentTrack.value?.let { track ->
               val updatedTrack = track.copy(
                    state = PlayingState.OnStop,
                    progress = 0
               )
               updateList(updatedTrack)
               currentTrack.value = null
          }
     }


     //коллбэк от плеера о начале буферизации
     override fun onDataSourcePrepareStarted() {

     }

     //коллбэк от плеера с прогрессом воспроизведения
     override fun onProgressUpdated(progress: Int) {
          currentTrack.value?.let { track ->
               val updatedTrack = track.copy(
                    progress = progress
               )
               updateList(updatedTrack)
               currentTrack.setValue(updatedTrack)
          }
     }

     //коллбэк от плеера с прогрессом буферизации

     override fun onBufferingProgressUpdated(progress: Int) {

     }

     private fun updateList(updatedTrack: PlayableTrack) {
          tracks.value?.let {
               val inList = it.find {track ->
                    track.id == updatedTrack.id
               }

               if (inList != null){
                    val indexOf = it.indexOf(inList)
                    it[indexOf] = updatedTrack
                    tracks.value = it
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
          selectedTrack: PlayableTrack,
          currentTrack: PlayableTrack,
          itemPosition: Int,
          list: MutableList<PlayableTrack>
     ) {
          val currentTrackPosition = list.indexOf(currentTrack)
          currentTrack.state = PlayingState.OnStop
          currentTrack.progress = 0
          selectedTrack.state = PlayingState.Loading
          list[currentTrackPosition] = currentTrack
          list[itemPosition] = selectedTrack
          this.currentTrack.value = selectedTrack
          tracks.value = list
          player.playSoundFromUrl(selectedTrack.previewPath)
       //   log("ON_SWITCH")

     }

     private fun playTrack(
          selectedTrack: PlayableTrack,
          listPosition: Int,
          list: MutableList<PlayableTrack>
     ) {
          selectedTrack.state = PlayingState.Playing
          list[listPosition] = selectedTrack
          currentTrack.value = selectedTrack
          tracks.value = list
          player.play()
       //   log("ON_PLAY")
     }

     private fun pauseTrack(
          selectedTrack: PlayableTrack,
          listPosition: Int,
          list: MutableList<PlayableTrack>
     ) {
          selectedTrack.state = PlayingState.OnPause
          list[listPosition] = selectedTrack
          currentTrack.value = selectedTrack
          tracks.value = list
          player.pause()
         // log("ON_PAUSE")


     }

     private fun startNewTrack(
          selectedTrack: PlayableTrack,
          listPosition: Int,
          list: MutableList<PlayableTrack>
     ) {
          selectedTrack.state = PlayingState.Loading
          list[listPosition] = selectedTrack
          currentTrack.value = selectedTrack
          tracks.value = list
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