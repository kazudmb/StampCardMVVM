package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.util.Utility
import com.nakano.stampcardmvvm.viewModel.QRCodeDisplayViewModel

class QRCodeDisplayFragment : Fragment() {

    private var qrCodeDisplayViewModel: QRCodeDisplayViewModel? = null

    private lateinit var qrCodeImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        binding = FragmentQRCodeDisplayBinding.inflate(inflater, container, false)
//        binding!!.lifecycleOwner = this
//        observeValue()
//        return binding!!.root

        val view = inflater.inflate(R.layout.fragment_q_r_code_display, container, false)

        qrCodeImageView = view.findViewById(R.id.qr_code)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        qrCodeImageView.setImageBitmap(Utility.createQRCode(resources))

    }

    companion object {
        private const val TAG = "QRCodeDisplayActivity"
    }
}