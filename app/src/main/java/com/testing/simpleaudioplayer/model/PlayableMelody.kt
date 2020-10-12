package com.testing.simpleaudioplayer.model

data class PlayableMelody(
    val id: Int,
    val name: String,
    val previewPath: String?,
    val coverPath: String?,
    var isPlaying: Boolean = false,
    var progress: Int = 0
)