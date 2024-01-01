package momin.tahir.kmp.newsapp.data.sqldelight

import app.cash.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory() {
    fun createDriver(): SqlDriver
}