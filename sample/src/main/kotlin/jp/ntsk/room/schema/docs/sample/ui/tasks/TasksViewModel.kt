package jp.ntsk.room.schema.docs.sample.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ntsk.room.schema.docs.sample.model.Task
import jp.ntsk.room.schema.docs.sample.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    tasksRepository: TasksRepository
) : ViewModel() {
    private val tasksFlow: Flow<List<Task>> = tasksRepository.get()

    val uiState: StateFlow<TasksUiState> =
        tasksFlow.map {
            TasksUiState.Success(tasks = it)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TasksUiState.Loading
        )
}