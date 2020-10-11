package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.testing.simpleaudioplayer.model.PlayableMelody
import kotlinx.coroutines.CoroutineScope

class MelodyListLiveData(
    val application: Application,
    val viewModelScope: CoroutineScope
) : LiveData<ArrayList<PlayableMelody>>() {



    override fun onActive() {
        super.onActive()
    }

    override fun onInactive() {
        super.onInactive()
    }

}