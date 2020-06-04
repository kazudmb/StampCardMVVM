package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.nakano.stampcardmvvm.R

class QRCodeDisplayFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_q_r_code_display, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // TODO:画像の生成処理なので、viewで実施したくない（repoからuidを取得し、viewmodel内でQRコードを生成してfragmentに渡す感じが良いと思う）

        val dp = 300
        val scale = resources.displayMetrics.density
        val size = (dp * scale + 0.5f).toInt()

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap = barcodeEncoder.encodeBitmap("test", BarcodeFormat.QR_CODE, size, size)
            val imageQr = view?.findViewById<ImageView>(R.id.qr_code)
            imageQr?.setImageBitmap(bitmap)
            Log.d(TAG, getString(R.string.generate_qrcode_success_log))
        } catch (exception: Exception) {
            Log.w(TAG, getString(R.string.generate_qrcode_failure_log), exception)
        }
    }

    companion object {
        private const val TAG = "QRCodeDisplayActivity"
    }
}