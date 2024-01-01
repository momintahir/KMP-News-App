//package momin.tahir.kmp.newsapp.data.sqldelight
//
//import app.cash.sqldelight.coroutines.asFlow
//import app.cash.sqldelight.coroutines.mapToList
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.IO
//import kotlinx.coroutines.flow.map
//
//
//class Database(private val databaseDriverFactory: DatabaseDriverFactory) {
//    private var database: AppDatabase? = null
//
////    private val sourceAdapter = object : ColumnAdapter<Source, String> {
////        override fun decode(databaseValue: String): Source = when (databaseValue) {
////            "Male" -> Gender.MALE
////            "Female" -> Gender.FEMALE
////            "Genderless" -> Gender.GENDERLESS
////            else -> Gender.UNKNOWN
////        }
////
////        override fun encode(value: Source): String = when (value) {
////            Gender.MALE -> "Male"
////            Gender.FEMALE -> "Female"
////            Gender.GENDERLESS -> "Genderless"
////            Gender.UNKNOWN -> "Unknown"
////        }
////    }
//
//    private suspend fun initDatabase() {
//        if (database == null) {
//            database = AppDatabase.invoke(databaseDriverFactory.createDriver())
//        }
//    }
//
//    suspend operator fun <R> invoke(block: suspend (AppDatabase) -> R): R {
//        initDatabase()
//        return block(database!!)
//    }
//
//    fun getAllNews()= database?.appDatabaseQueries?.selectAllNewsFavorite()
//            ?.asFlow()
//            ?.mapToList(Dispatchers.IO)
//            ?.map { list ->
//                list.map { it }
//            }
//
//}