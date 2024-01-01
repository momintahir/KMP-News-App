package momin.tahir.kmp.newsapp.data.sqldelight

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(
            schema = AppDatabase.Schema,
            name = "MyAppSQLDelightDatabase.db"
        )
    }
}