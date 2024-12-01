package jp.ntsk.room.schema.docs.sample.ui.taskdetail

import jp.ntsk.room.schema.docs.sample.model.Task

sealed interface TaskDetailUiState {
    data object New : TaskDetailUiState

    data class Success(
        val task: Task
    ) : TaskDetailUiState

    data object Loading : TaskDetailUiState
}