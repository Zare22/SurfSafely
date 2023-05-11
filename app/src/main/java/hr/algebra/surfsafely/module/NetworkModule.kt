package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.client.ApiClient
import org.koin.dsl.module

val networkModule = module {
    single { ApiClient.getService() }
}