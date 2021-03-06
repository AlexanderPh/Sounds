package com.testing.simpleaudioplayer.model

import com.testing.simpleaudioplayer.views.PlayingState

data class PlayableTrack(
    val id: Int,
    val name: String,
    val previewPath: String?,
    val coverPath: String?,
    val assetPath: String?,
    var state: PlayingState = PlayingState.OnStop,
    var progress: Int = 0
)