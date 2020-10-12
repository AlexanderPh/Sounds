package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.testing.simpleaudioplayer.list.viewmodel.SongListViewModel.Action.LoadList

class SongListViewModel(
     application: Application
) : AndroidViewModel(application) {


     private val listId = MutableLiveData<Int?>(null)

     val melodyList = Transformations.switchMap(listId
     ) {
          it?.let {
               MelodyListLiveData(application, viewModelScope, it)

          }
     }


     fun onAction(action: Action) {
          when (action){
               is LoadList -> listId.postValue(action.resId)
          }
     }


     sealed class Action{
          class LoadList(val resId: Int) : Action()
     }
}