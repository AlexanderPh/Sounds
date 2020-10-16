package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.testing.player.PlayerProvider
import com.testing.player.PlayerProviderCallback
import com.testing.simpleaudioplayer.list.recycler.PlayerControlCallback
import com.testing.simpleaudioplayer.model.PlayableTrack
import com.testing.simpleaudioplayer.views.PlayingState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TrackListViewModel(
     application: Application
) : AndroidViewModel(application), PlayerProviderCallback, PlayerControlCallback {

     private val interactor = TrackInteractor.getInstance(application)
     private val player = PlayerProvider(this)
     private val state = MutableLiveData<ListState>()


     val tracksNew = Transformations.distinctUntilChanged(
          Transformations.switchMap(state){
               TracksLiveData(it)
          }
     )
     val currentTrackNew = Transformations.distinctUntilChanged(
          Transformations.switchMap(state){
               CurrentTrackLiveData(it)
          }
     )

     init {
          state.value = ListState()
     }

     fun loadList(resId: Int) = viewModelScope.launch(Dispatchers.IO) {
          val list = interactor.loadList(resId)
          list?.let {
               state.postValue(ListState(it))
          }
     }


     //коллбэк от плеера при начале воспроизведения
     override fun onPlayStarted() {
          state.value = state.value?.play()
     }

     //коллбэк от плеера при паузе воспроизведения
     override fun onPlayerPaused() {

     }


     //коллбэк от плеера при окончании воспроизведения
     override fun onPlayStopped() {
         state.value = state.value?.stop()
     }


     //коллбэк от плеера о начале буферизации
     override fun onDataSourcePrepareStarted() {

     }

     //коллбэк от плеера с прогрессом воспроизведения
     override fun onProgressUpdated(progress: Int) {
          state.value = state.value?.updateProgress(progress)
     }

     //коллбэк от плеера с прогрессом буферизации
     override fun onBufferingProgressUpdated(progress: Int) {

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
          if (state.value?.isSomethingPlaying() == true){
               state.value = state.value?.pause()
               player.pause()
          } else if (state.value?.isSomethingOnPause() == true){
               state.value = state.value?.play()
               player.play()
          }
     }

     override fun itemClosed() {
          player.reset()
          onPlayStopped()
     }


     private fun handleTrack(index: Int) {
          val currentTrack = state.value?.findCurrentTrack()
          val nextTrack = state.value?.getSelectedTrack(index)

          if (currentTrack == null && nextTrack == null){
               return
          }

          currentTrack?.let { ct ->
               if (ct.id == nextTrack?.id){
                    when (ct.state){
                         PlayingState.Playing -> {
                              pauseTrack()
                         }
                         PlayingState.OnPause -> {
                             playTrack()
                         }
                         else -> return
                    }
               } else {
                    switchTrack(true, nextTrack)
               }
          } ?: switchTrack(false, nextTrack)

     }

     private fun switchTrack(
          switch: Boolean,
          nextTrack: PlayableTrack?,
     ) {
          if (switch){
          state.value = state.value?.stop()
          }
          nextTrack?.let {
               state.value = state.value?.start(it.id)
               player.playSoundFromUrl(nextTrack.previewPath)
          }
     }

     private fun playTrack() {
          state.value = state.value?.play()
          player.play()
     }

     private fun pauseTrack() {
          state.value = state.value?.pause()
          player.pause()
     }


     fun onDestroy() {
          player.release()
     }

     fun onLifeCyclePause(){
          if (state.value?.isSomethingPlaying() == true){
               state.value = state.value?.pause()
               player.pause()
          }
     }

     fun onLifeCycleResume(){
          if (state.value?.isSomethingOnPause() == true){
               state.value = state.value?.play()
               player.play()
          }
     }
}