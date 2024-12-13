package jp.ntsk.room.schema.docs.sample.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.ntsk.room.schema.docs.sample.database.AppDatabase
import jp.ntsk.room.schema.docs.sample.database.AppDatabaseFactory
import jp.ntsk.room.schema.docs.sample.database.SubTaskDao
import jp.ntsk.room.schema.docs.sample.database.TaskDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RoomModule {

    @Provides
    @Singleton
    fun provideAppDatabase(appDatabaseFactory: AppDatabaseFactory): AppDatabase {
        return appDatabaseFactory.create()
    }

    @Provides
    @Singleton
    fun provideTaskDao(appDatabase: AppDatabase): TaskDao {
        return appDatabase.taskDao
    }

    @Provides
    @Singleton
    fun provideSubTaskDao(appDatabase: AppDatabase): SubTaskDao {
        return appDatabase.subTaskDao
    }
}