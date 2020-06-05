package com.nakano.stampcardmvvm.model.repository

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class QRCodeDisplayRepository {

    fun generateBitmap(): Bitmap {

        lateinit var bitmap : Bitmap

        val dp = 300
        // TODO: getResorcesは、Activityのメソッドなので使用不可
//        val scale = resources.displayMetrics.density
        val scale = 3.0
        val size = (dp * scale + 0.5f).toInt()

        try {
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.encodeBitmap("test", BarcodeFormat.QR_CODE, size, size)
            // TODO: ImageViewをBindingでセットする方法を検討すること（xmlとkt）
//            val imageQr = view?.findViewById<ImageView>(R.id.qr_code)
//            imageQr?.setImageBitmap(bitmap)
//            Log.d(QRCodeDisplayFragment.TAG, getString(R.string.generate_qrcode_success_log))
        } catch (exception: Exception) {
//            Log.w(QRCodeDisplayFragment.TAG, getString(R.string.generate_qrcode_failure_log), exception)
        }

        return bitmap
    }

    companion object {
        private const val TAG = "QRCodeDisplayActivity"
    }
}
