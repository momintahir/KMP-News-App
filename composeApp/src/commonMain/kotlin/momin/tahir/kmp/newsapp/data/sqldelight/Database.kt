package momin.tahir.kmp.newsapp.data.sqldelight

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.map


class Database(private val databaseDriverFactory: DatabaseDriverFactory) {
    private var database: AppDatabase? = null

    private fun initDatabase() {
        if (database == null) {
            database = AppDatabase.invoke(databaseDriverFactory.createDriver())
        }
    }

    suspend operator fun <R> invoke(block: suspend (AppDatabase) -> R): R {
        initDatabase()
        return block(database!!)
    }
}