package momin.tahir.kmp.newsapp.data.sqldelight

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import momin.tahir.kmp.newsapp.MyApplication

actual class DatabaseDriverFactory {

    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = MyApplication.instance,
            name = "MyAppSQLDelightDatabase.db"
        )
    }
}