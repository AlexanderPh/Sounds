package com.testing.repository

import android.app.Application
import android.content.res.Resources
import com.google.gson.Gson
import com.testing.repository.model.Melody
import com.testing.repository.model.Result
import java.io.InputStreamReader
import java.lang.Exception

class SoundRepository(private val application: Application) {

    private val resources: Resources = application.resources

    suspend fun loadFromRaw(rawId: Int) : Result {
        return try {
            val melodyList  = arrayListOf<Melody>()
            InputStreamReader(resources.openRawResource(rawId)).use { reader ->
                val melodyData = Gson().fromJson(reader, Array<Melody>::class.java)
                melodyData?.let {
                    melodyList.addAll(it)

                }
            }
            Result.Success(melodyList)
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