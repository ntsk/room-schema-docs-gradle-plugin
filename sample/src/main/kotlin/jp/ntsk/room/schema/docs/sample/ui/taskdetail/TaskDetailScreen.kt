package jp.ntsk.room.schema.docs.sample.ui.taskdetail

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.ntsk.room.schema.docs.sample.model.Task
import jp.ntsk.room.schema.docs.sample.theme.SampleAppTheme
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = hiltViewModel(),
    onClickArrowBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TaskDetailScreen(
        uiState = uiState,
        onClickArrowBack = onClickArrowBack,
        onSaved = { viewModel.save(it) },
        onDelete = { viewModel.delete(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDetailScreen(
    uiState: TaskDetailUiState,
    onClickArrowBack: () -> Unit,
    onSaved: (Task) -> Unit,
    onDelete: (Task) -> Unit,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onClickArrowBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        when (uiState) {
            is TaskDetailUiState.Success -> {
                TaskDetailColumn(
                    modifier = Modifier.padding(innerPadding),
                    task = uiState.task,
                    onSaved = {
                        onSaved(it)
                        onClickArrowBack()
                    },
                    onDelete = {
                        onDelete(it)
                        onClickArrowBack()
                    }
                )
            }

            is TaskDetailUiState.New -> {
                TaskDetailColumn(
                    modifier = Modifier.padding(innerPadding),
                    task = null,
                    onSaved = {
                        onSaved(it)
                        onClickArrowBack()
                    },
                    onDelete = {
                        onDelete(it)
                        onClickArrowBack()
                    }
                )
            }

            is TaskDetailUiState.Loading -> {
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
private fun TaskDetailColumn(
    modifier: Modifier = Modifier,
    task: Task?,
    onSaved: (Task) -> Unit,
    onDelete: (Task) -> Unit,
) {
    var editedTask by remember { mutableStateOf(task) }
    var title by remember { mutableStateOf(task?.title.orEmpty()) }
    var description by remember { mutableStateOf(task?.description.orEmpty()) }
    var status by remember { mutableStateOf(task?.status ?: Task.Status.None) }
    var dueAt by remember { mutableStateOf(task?.dueAt ?: OffsetDateTime.now().plusDays(7)) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = title,
            onValueChange = { title = it },
            label = {
                Text(text = "Title")
            },
            minLines = 1,
        )

        Spacer(modifier = Modifier.size(16.dp))

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = description,
            onValueChange = { description = it },
            label = {
                Text(text = "Description")
            },
            minLines = 4,
        )

        Spacer(modifier = Modifier.size(16.dp))

        StatusDropDownContent(
            currentStatus = status,
            onStatusChanged = {
                status = it
            }
        )

        Spacer(modifier = Modifier.size(16.dp))

        DatePickerContent(
            label = "DueAt",
            currentDate = dueAt,
            onSelectedDateChanged = {
                dueAt = it
            }
        )
        Spacer(modifier = Modifier.size(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val saveTask = editedTask
                if (saveTask == null) {
                    onSaved(
                        Task(
                            id = 0,
                            title = title,
                            description = description,
                            status = status,
                            dueAt = dueAt,
                            project = null, // TODO
                            createdAt = OffsetDateTime.now(),
                        )
                    )
                } else {
                    onSaved(
                        saveTask.copy(
                            title = title,
                            description = description,
                            status = status,
                            dueAt = dueAt,
                        )
                    )
                }
                editedTask = saveTask
            }
        ) {
            Text("Save")
        }

        Spacer(modifier = Modifier.size(8.dp))

        OutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                val deleteTask = editedTask
                if (deleteTask == null) {
                    onDelete(
                        Task(
                            id = 0,
                            title = title,
                            description = description,
                            status = status,
                            dueAt = dueAt,
                            project = null, // TODO
                            createdAt = OffsetDateTime.now(),
                        )
                    )
                } else {
                    onDelete(
                        deleteTask.copy(
                            title = title,
                            description = description,
                            status = status,
                            dueAt = dueAt,
                        )
                    )
                }
                editedTask = deleteTask
            }
        ) {
            Text("Delete")
        }
    }
}

@Composable
private fun StatusDropDownContent(
    currentStatus: Task.Status,
    onStatusChanged: (Task.Status) -> Unit
) {
    var status by remember { mutableStateOf(currentStatus) }
    var isDropDownExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    isDropDownExpanded = true
                }
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            Text(text = "Status")
            Spacer(modifier = Modifier.size(16.dp))
            Text(text = currentStatus.label)
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "dropdown")

            DropdownMenu(
                expanded = isDropDownExpanded,
                onDismissRequest = {
                    isDropDownExpanded = false
                }
            ) {
                Task.Status.entries.forEach { s ->
                    DropdownMenuItem(
                        text = {
                            Text(text = s.label)
                        },
                        onClick = {
                            isDropDownExpanded = false
                            status = s
                            onStatusChanged(s)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerContent(
    label: String,
    currentDate: OffsetDateTime,
    onSelectedDateChanged: (OffsetDateTime) -> Unit
) {
    var openDateTimeDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label)
        Spacer(modifier = Modifier.size(16.dp))
        Text(currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE))
        IconButton(onClick = {
            openDateTimeDialog = true
        }) {
            Icon(imageVector = Icons.Default.DateRange, contentDescription = "date")
        }
    }

    if (openDateTimeDialog) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = Color(0xFFF5F0FF),
            ),
            onDismissRequest = {
                openDateTimeDialog = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDateTimeDialog = false
                        val selectedDateMillis = datePickerState.selectedDateMillis
                            ?: System.currentTimeMillis()
                        val selectedDate = Instant
                            .ofEpochMilli(selectedDateMillis)
                            .atOffset(ZoneOffset.UTC)
                        onSelectedDateChanged(selectedDate)
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDateTimeDialog = false
                    }
                ) {
                    Text("CANCEL")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors()
            )
        }
    }
}

@Composable
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun TaskDetailScreenPreview() {
    SampleAppTheme {
        TaskDetailScreen(
            uiState = TaskDetailUiState.New,
            onClickArrowBack = {},
            onSaved = {},
            onDelete = {}
        )
    }
}
