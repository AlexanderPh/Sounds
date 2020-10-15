package com.testing.repository.model

import java.lang.Exception

sealed class Result {
    data class Success(val trackList: List<Track>) : Result()
    data class Failed(val exception: Exception) : Result()
}