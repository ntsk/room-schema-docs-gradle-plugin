package jp.ntsk.room.schema.docs.sample.ui

import kotlinx.serialization.Serializable

sealed interface Screens {
    @Serializable
    data object Projects : Screens

    @Serializable
    data object Tasks : Screens

    @Serializable
    data class TaskDetail(val id: Long) : Screens
}