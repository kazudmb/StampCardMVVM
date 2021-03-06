package com.nakano.stampcardmvvm

import android.app.Application
import com.nakano.stampcardmvvm.model.repository.AuthRepository
import com.nakano.stampcardmvvm.model.repository.UserRepository
import com.nakano.stampcardmvvm.viewModel.UserViewModelFactory
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

        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { AuthRepository(instance()) }
        bind() from provider { UserViewModelFactory(instance(), instance()) }
    }

}