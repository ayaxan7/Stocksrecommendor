package com.ayaan.myapplication.di

import com.ayaan.myapplication.data.FundRepository
import com.ayaan.myapplication.data.FundRepositoryImpl
import com.ayaan.myapplication.domain.GetBenchmarkAnalysisUseCase
import com.ayaan.myapplication.domain.GetSmartAdvisorUseCase
import com.ayaan.myapplication.domain.GetTopFundsUseCase
import com.ayaan.myapplication.network.FundApi
import com.ayaan.myapplication.network.FundApiImpl
import com.ayaan.myapplication.network.createHttpClient
import com.ayaan.myapplication.ui.FundListViewModel
import com.ayaan.myapplication.ui.benchmark.BenchmarkAnalysisViewModel
import com.ayaan.myapplication.ui.smartadvisor.SmartAdvisorViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module for common dependencies
 */
val commonModule = module {
    // Network
    single { createHttpClient() }
    single<FundApi> { FundApiImpl(get()) }

    // Repository
    single<FundRepository> { FundRepositoryImpl(get()) }

    // Domain
    single { GetTopFundsUseCase(get()) }
    single { GetBenchmarkAnalysisUseCase(get()) }
    single { GetSmartAdvisorUseCase(get()) }

    // ViewModels
    factory { FundListViewModel(get()) }
    factory { BenchmarkAnalysisViewModel(get()) }
    factory { SmartAdvisorViewModel(get()) }
}

/**
 * Initialize Koin for dependency injection
 */
fun initKoin(appModule: Module = module {}): KoinApplication {
    val koinApplication = KoinApplication.init()
    koinApplication.modules(commonModule, appModule)
    return koinApplication
}
