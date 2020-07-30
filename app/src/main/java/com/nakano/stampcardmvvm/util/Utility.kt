package com.nakano.stampcardmvvm.util

import android.content.res.Resources
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

object Utility {

    private val TAG = "Utility"

    fun createQRCode(resources: Resources) : Bitmap? {
        val dp = 300
        val scale = resources.displayMetrics.density
        val size = (dp * scale + 0.5f).toInt()

        val barcodeEncoder = BarcodeEncoder()
        return try {
            val bitmap = barcodeEncoder.encodeBitmap(
                "uid", // TODO: ログインしているuidを入れること
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