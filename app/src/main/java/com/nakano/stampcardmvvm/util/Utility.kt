package com.nakano.stampcardmvvm.util

import android.content.Context
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

object Utility {

    private val TAG = "Utility"

    fun createQRCode(context: Context, uid: String?) : Bitmap? {
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
}