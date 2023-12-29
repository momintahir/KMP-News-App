package momin.tahir.kmp.newsapp.domain.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import momin.tahir.kmp.newsapp.data.api.INewsApi
import momin.tahir.kmp.newsapp.data.api.NewsApi
import momin.tahir.kmp.newsapp.data.repository.INewsRepository
import momin.tahir.kmp.newsapp.data.repository.NewsRepositoryImp
import momin.tahir.kmp.newsapp.domain.usecase.GetAllNewsUseCase
import momin.tahir.kmp.newsapp.presentation.screens.news_list.NewsListScreenViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module


fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(
            viewModelModule,
            useCasesModule,
            repositoryModule,
            ktorModule,
            jsonModule
        )
    }

val useCasesModule: Module = module {
    factory { GetAllNewsUseCase(get()) }
}

val ktorModule = module {
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                    }
                )
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
    }

    single { "https://newsapi.org/v2" }
}
val viewModelModule = module {
    factory { NewsListScreenViewModel(get()) }
}
val jsonModule = module {
    single { createJson() }
}

fun createJson() = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
}

val repositoryModule = module {
    single<INewsRepository> { NewsRepositoryImp(get()) }
    single<INewsApi> { NewsApi(get(), get()) }
}
fun initKoin() = initKoin {}