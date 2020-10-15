package com.testing.repository

import android.app.Application
import android.content.res.Resources
import com.google.gson.Gson
import com.testing.repository.model.Track
import com.testing.repository.model.Result
import java.io.InputStreamReader
import java.lang.Exception

class SoundRepository(private val application: Application) {

    private val resources: Resources = application.resources

    suspend fun loadFromRaw(rawId: Int) : Result {
        return try {
            val trackList  = arrayListOf<Track>()
            InputStreamReader(resources.openRawResource(rawId)).use { reader ->
                val trackData = Gson().fromJson(reader, Array<Track>::class.java)
                trackData?.let {
                    trackList.addAll(it)

                }
            }
            Result.Success(trackList)
        } catch (ex: Exception){
            Result.Failed(ex)
        }
    }

    companion object {
        @Volatile private var instance : SoundRepository? = null
        fun getInstance(application: Application) : SoundRepository {
            return instance ?: synchronized(this){
                instance ?: SoundRepository(application).also {
                    instance = it
                }
            }
        }
    }
}