package jp.ntsk.room.schema.docs.sample.ui.tasks

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.ntsk.room.schema.docs.sample.model.Task
import jp.ntsk.room.schema.docs.sample.theme.SampleAppTheme
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    onClickTask: (Task) -> Unit,
    onClickFab: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TasksScreen(
        uiState = uiState,
        onClickFab = onClickFab,
        onClickTask = onClickTask
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TasksScreen(
    uiState: TasksUiState,
    onClickFab: () -> Unit,
    onClickTask: (Task) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "Tasks",
                            textAlign = TextAlign.Center,
                        )
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onClickFab
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        when (uiState) {
            is TasksUiState.Success -> {
                TasksLazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    tasks = uiState.tasks,
                    onClickTask = onClickTask,
                )
            }

            is TasksUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(48.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun TasksLazyColumn(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    onClickTask: (Task) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
    ) {
        items(tasks) {
            TaskItem(
                task = it,
                onClickTask = onClickTask
            )
        }
    }
}

@Composable
private fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    onClickTask: (Task) -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickTask(task) },
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                modifier = Modifier.align(Alignment.CenterVertically),
                imageVector =
                if (task.status == Task.Status.Done || task.status == Task.Status.Archived) {
                    Icons.Default.TaskAlt
                } else {
                    Icons.Outlined.Circle
                },
                contentDescription = "Localized description"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(
                    text = task.title,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.size(4.dp))
                Text(
                    text = task.description,
                    fontSize = 10.sp,
                    maxLines = 2,
                )
                Spacer(modifier = Modifier.size(4.dp))
                Row {
                    TaskStatusLabel(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        status = task.status
                    )
                    Spacer(modifier = Modifier.size(4.dp))
                    TaskDueDateLabel(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        dueDate = task.dueAt
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskStatusLabel(
    modifier: Modifier = Modifier,
    status: Task.Status
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color = status.color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = status.label,
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun TaskDueDateLabel(
    modifier: Modifier = Modifier,
    dueDate: OffsetDateTime
) {
    Row(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(16.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            modifier = Modifier.size(16.dp),
            imageVector = Icons.Default.DateRange, contentDescription = "date"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = dueDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
            fontSize = 10.sp
        )
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalMaterial3Api
private fun TasksScreenPreview() {
    SampleAppTheme {
        TasksScreen(
            uiState =
            TasksUiState.Success(
                listOf(
                    Task(
                        id = 1,
                        title = "Buy groceries",
                        description = "Buy milk, eggs, bread, and some fresh vegetables from the supermarket.",
                        status = Task.Status.Todo,
                        project = null,
                        createdAt = OffsetDateTime.now(),
                        dueAt = OffsetDateTime.now().plusDays(3)
                    ),
                    Task(
                        id = 2,
                        title = "Prepare presentation",
                        description = "Finalize slides and add notes for the client meeting presentation.",
                        status = Task.Status.InProgress,
                        project = null,
                        createdAt = OffsetDateTime.now(),
                        dueAt = OffsetDateTime.now().plusDays(4)
                    ),
                    Task(
                        id = 3,
                        title = "Call the plumber",
                        description = "Discuss the leaking pipe issue and arrange a time for repair.",
                        status = Task.Status.Done,
                        project = null,
                        createdAt = OffsetDateTime.now(),
                        dueAt = OffsetDateTime.now().plusDays(5)
                    ),
                    Task(
                        id = 4,
                        title = "Workout session",
                        description = "Complete a 45-minute cardio workout followed by 15 minutes of stretching.",
                        status = Task.Status.Archived,
                        project = null,
                        createdAt = OffsetDateTime.now(),
                        dueAt = OffsetDateTime.now().plusDays(6)
                    )
                )
            ),
            onClickFab = {},
            onClickTask = {}
        )
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@ExperimentalMaterial3Api
private fun TasksScreenLoadingPreview() {
    SampleAppTheme {
        TasksScreen(
            uiState = TasksUiState.Loading,
            onClickFab = {},
            onClickTask = {}
        )
    }
}
