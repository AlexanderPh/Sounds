package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope

class SongListViewModel(
     application: Application
) : AndroidViewModel(application) {

     val melodyList = MelodyListLiveData(application, viewModelScope)

}