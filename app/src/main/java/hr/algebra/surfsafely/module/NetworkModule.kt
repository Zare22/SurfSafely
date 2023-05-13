package hr.algebra.surfsafely.module

import hr.algebra.surfsafely.client.ApiClient
import org.koin.dsl.module

val serviceModule = module {
    single { ApiClient.getService() }
}