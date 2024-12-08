package jp.ntsk.room.schema.docs.sample.ui.taskdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.ntsk.room.schema.docs.sample.model.Task
import jp.ntsk.room.schema.docs.sample.repository.TasksRepository
import jp.ntsk.room.schema.docs.sample.ui.Screens
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(
    private val tasksRepository: TasksRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val selectedTaskId = savedStateHandle.toRoute<Screens.TaskDetail>().id
    private val taskFlow: Flow<Task?> = tasksRepository.getById(selectedTaskId)
    val uiState: StateFlow<TaskDetailUiState> =
        taskFlow.map { task ->
            if (task == null) {
                TaskDetailUiState.New
            } else {
                TaskDetailUiState.Success(task = task)
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskDetailUiState.Loading
        )

    fun save(task: Task) = viewModelScope.launch {
        tasksRepository.add(task)
    }

    fun delete(task: Task) = viewModelScope.launch {
        tasksRepository.delete(task)
    }
}