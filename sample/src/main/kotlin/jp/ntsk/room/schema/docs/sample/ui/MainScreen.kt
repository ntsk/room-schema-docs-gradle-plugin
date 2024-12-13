package jp.ntsk.room.schema.docs.sample.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.ntsk.room.schema.docs.sample.ui.taskdetail.TaskDetailScreen
import jp.ntsk.room.schema.docs.sample.ui.tasks.TasksScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Tasks,
    ) {
        composable<Screens.Tasks> {
            TasksScreen(
                onClickFab = {
                    navController.navigate(Screens.TaskDetail(0))
                },
                onClickTask = { task ->
                    navController.navigate(Screens.TaskDetail(task.id))
                }
            )
        }

        composable<Screens.TaskDetail> {
            TaskDetailScreen(onClickArrowBack = { navController.popBackStack() })
        }
    }
}