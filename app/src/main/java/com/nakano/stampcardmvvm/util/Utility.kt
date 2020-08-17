package com.nakano.stampcardmvvm.util

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.nakano.stampcardmvvm.R

object Utility {

    fun createQRCode(context: Context, uid: String): Bitmap? {
        val dp = 300
        val scale = context.resources.displayMetrics.density
        val size = (dp * scale + 0.5f).toInt()

        val barcodeEncoder = BarcodeEncoder()
        return try {
            val bitmap = barcodeEncoder.encodeBitmap(
                uid,
                BarcodeFormat.QR_CODE,
                size,
                size
            )
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun getRank(context: Context, numberOfVisits: Long): String {
        when {
            numberOfVisits < 40 -> return context.getString(R.string.rank_member)
            numberOfVisits < 80 -> return context.getString(R.string.rank_silver)
            numberOfVisits >= 80 -> return context.getString(R.string.rank_gold)
        }
        return context.getString(R.string.rank_member)
    }

    // TODO: Middle グルグルを出すutilを作成すること、出来たらCoroutionを使って実装すること
}