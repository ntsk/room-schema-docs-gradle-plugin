package jp.ntsk.room.schema.docs.sample.ui.taskdetail

import android.content.res.Configuration
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.ntsk.room.schema.docs.sample.model.Task
import jp.ntsk.room.schema.docs.sample.theme.SampleAppTheme

@Composable
fun TaskDetailScreen(
    viewModel: TaskDetailViewModel = hiltViewModel(),
    onClickArrowBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TaskDetailScreen(
        uiState = uiState,
        onClickArrowBack = onClickArrowBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDetailScreen(
    uiState: TaskDetailUiState,
    onClickArrowBack: () -> Unit,
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
                    task = uiState.task
                )
            }

            is TaskDetailUiState.New -> {
                TaskDetailColumn(
                    modifier = Modifier.padding(innerPadding),
                    task = null
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
) {
    var title by remember { mutableStateOf(task?.title.orEmpty()) }
    var description by remember { mutableStateOf(task?.description.orEmpty()) }
    var status by remember { mutableStateOf(task?.status ?: Task.Status.None) }
    var dueAt by remember { mutableStateOf(task?.dueAt) }
    var isDropDownExpanded by remember { mutableStateOf(false) }

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
                Text(text = status.label)
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
                            }
                        )
                    }
                }
            }
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
            onClickArrowBack = {}
        )
    }
}
