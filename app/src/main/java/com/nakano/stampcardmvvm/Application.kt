package com.nakano.stampcardmvvm

import com.nakano.stampcardmvvm.model.repository.QRCodeDisplayRepository
import com.nakano.stampcardmvvm.viewModel.QRCodeDisplayViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class Application : android.app.Application() {

    val myModule = module {
        single { QRCodeDisplayRepository() }
        viewModel { QRCodeDisplayViewModel(get()) }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@Application)
            modules(myModule)
        }
    }
}