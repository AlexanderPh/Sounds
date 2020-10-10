package com.testing.repository.model

import java.lang.Exception

sealed class Result {
    data class Success(val melodyList: List<Melody>) : Result()
    data class Failed(val exception: Exception) : Result()
}