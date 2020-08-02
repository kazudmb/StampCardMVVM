package com.nakano.stampcardmvvm.model.repository

import com.nakano.stampcardmvvm.model.model.AppDatabase

class StampCardRepository(
    private val db: AppDatabase
) {
    fun getStampCard() = db.getUserDao().getuser()
}
