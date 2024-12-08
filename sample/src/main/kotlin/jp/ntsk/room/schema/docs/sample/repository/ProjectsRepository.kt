package jp.ntsk.room.schema.docs.sample.repository

import jp.ntsk.room.schema.docs.sample.database.ProjectDao
import jp.ntsk.room.schema.docs.sample.model.Project
import jp.ntsk.room.schema.docs.sample.model.toModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectsRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    fun get(): Flow<List<Project>> =
        projectDao.getProjectsOrderByCreatedAt().map { entities ->
            entities.map { it.toModel() }
        }
}