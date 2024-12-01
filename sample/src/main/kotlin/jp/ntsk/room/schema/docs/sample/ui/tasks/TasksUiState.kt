package jp.ntsk.room.schema.docs.sample.ui.tasks

import jp.ntsk.room.schema.docs.sample.model.Task

sealed interface TasksUiState {

    data class Success(
        val tasks: List<Task>
    ) : TasksUiState

    data object Loading : TasksUiState
}
