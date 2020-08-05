package com.nakano.stampcardmvvm.model.repository

import com.nakano.stampcardmvvm.model.model.AppDatabase

class UserRepository(
    private val db: AppDatabase
) {
    fun getStampCard() = db.getUserDao().getuser()
}
