package com.testing.repository.model

data class Track (
    val id: Int,
    val name: String,
    val path: String?,
    val audio: String?,
    val image: String?
)