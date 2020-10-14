package com.testing.simpleaudioplayer.model

import com.testing.simpleaudioplayer.views.PlayingState

data class PlayableMelody(
    val id: Int,
    val name: String,
    val previewPath: String?,
    val coverPath: String?,
    var state: PlayingState = PlayingState.OnStop,
    var progress: Int = 0
)