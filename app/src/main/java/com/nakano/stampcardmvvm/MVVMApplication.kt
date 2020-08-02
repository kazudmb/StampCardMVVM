package com.nakano.stampcardmvvm

import android.app.Application
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.model.repository.QRCodeDisplayRepository
import com.nakano.stampcardmvvm.model.repository.StampCardRepository
import com.nakano.stampcardmvvm.viewModel.QRCodeDisplayViewModelFactory
import com.nakano.stampcardmvvm.viewModel.StampCardViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MVVMApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { AppDatabase(instance()) }
        bind() from singleton { StampCardRepository(instance()) }
        bind() from singleton { QRCodeDisplayRepository(instance(), instance()) }
        bind() from provider { StampCardViewModelFactory(instance()) }
        bind() from provider { QRCodeDisplayViewModelFactory(instance()) }
    }

}