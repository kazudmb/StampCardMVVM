package com.nakano.stampcardmvvm.model.repository

import android.content.Context
import android.graphics.Bitmap
import com.nakano.stampcardmvvm.model.model.AppDatabase
import com.nakano.stampcardmvvm.util.Utility

class QRCodeDisplayRepository(
    private val db: AppDatabase,
    private val context: Context
) {

    fun getStampCard() = db.getUserDao().getuser()

}
