package jp.ntsk.room.schema.docs.sample.database

import android.content.Context
import androidx.room.Room
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppDatabaseFactory @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun create(): AppDatabase {
        val builder = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "sample_database"
        )

        return builder
            .fallbackToDestructiveMigration()
            .build()
    }
}