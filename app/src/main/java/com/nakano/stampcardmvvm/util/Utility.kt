package com.nakano.stampcardmvvm.util

import android.content.Context
import android.graphics.Bitmap
import android.text.TextUtils
import android.widget.EditText
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

    fun validateForm(
        context: Context,
        fieldEmail: EditText,
        fieldPassword: EditText = EditText(context)
    ): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = context.resources.getString(R.string.input_text)
            valid = false
        } else {
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password)) {
            fieldPassword.error = context.resources.getString(R.string.input_text)
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }
}