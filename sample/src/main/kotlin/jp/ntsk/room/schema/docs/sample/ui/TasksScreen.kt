package jp.ntsk.room.schema.docs.sample.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.ntsk.room.schema.docs.sample.model.Task
import jp.ntsk.room.schema.docs.sample.theme.SampleAppTheme
import java.time.OffsetDateTime

@Composable
fun TasksScreen() {
}

@ExperimentalMaterial3Api
@Composable
private fun TasksScreen(
    modifier: Modifier = Modifier,
    tasks: List<Task>
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(end = 24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                contentDescription = "icon"
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            Text(text = "Tasks")
                        }
                    }
                })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {}) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        TasksLazyColumn(
            modifier = Modifier.padding(innerPadding),
            tasks = tasks
        )
    }
}

@Composable
private fun TasksLazyColumn(
    modifier: Modifier = Modifier,
    tasks: List<Task>
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(tasks) {
            TaskItem(
                task = it
            )
        }
    }
}

@Composable
private fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = task.name,
                fontSize = 21.sp
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = task.description,
                fontSize = 12.sp,
                maxLines = 2
            )
            Spacer(modifier = Modifier.size(16.dp))
            TaskStatusLabel(
                modifier = Modifier.align(Alignment.End),
                status = task.status
            )
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
                .size(12.dp)
                .background(color = status.color, shape = CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = status.label,
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
            tasks = listOf(
                Task(
                    id = 1,
                    name = "Buy groceries",
                    description = "Buy milk, eggs, bread, and some fresh vegetables from the supermarket.",
                    status = Task.Status.TODO,
                    createdAt = OffsetDateTime.now(),
                    dueAt = OffsetDateTime.now().plusDays(3)
                ),
                Task(
                    id = 2,
                    name = "Prepare presentation",
                    description = "Finalize slides and add notes for the client meeting presentation.",
                    status = Task.Status.IN_PROGRESS,
                    createdAt = OffsetDateTime.now(),
                    dueAt = OffsetDateTime.now().plusDays(4)
                ),
                Task(
                    id = 3,
                    name = "Call the plumber",
                    description = "Discuss the leaking pipe issue and arrange a time for repair.",
                    status = Task.Status.DONE,
                    createdAt = OffsetDateTime.now(),
                    dueAt = OffsetDateTime.now().plusDays(5)
                ),
                Task(
                    id = 4,
                    name = "Workout session",
                    description = "Complete a 45-minute cardio workout followed by 15 minutes of stretching.",
                    status = Task.Status.ARCHIVED,
                    createdAt = OffsetDateTime.now(),
                    dueAt = OffsetDateTime.now().plusDays(6)
                )
            )
        )
    }
}