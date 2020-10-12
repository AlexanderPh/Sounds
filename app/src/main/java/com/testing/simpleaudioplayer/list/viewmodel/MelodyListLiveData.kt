package com.testing.simpleaudioplayer.list.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.testing.simpleaudioplayer.model.PlayableMelody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MelodyListLiveData(
    private val application: Application,
    private val scope: CoroutineScope,
    private val rawResId: Int
) : LiveData<ArrayList<PlayableMelody>>() {

    private val interactor = MelodyInteractor.getInstance(application)
    private var job: Job? = null

    override fun onActive() {
        job = loadList(rawResId)
    }

    private fun loadList(rawResId: Int) = scope.launch {
        val list = interactor.loadList(rawResId)
        list?.let {
            postValue(it)
        }
    }

    override fun onInactive() {
        job?.cancel()
    }

}