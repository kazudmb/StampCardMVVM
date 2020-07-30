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
import org.koin.android.ext.android.inject

class QRCodeDisplayFragment : Fragment() {

    private val viewModel: QRCodeDisplayViewModel by inject()
//    private var binding: FragmentQRCodeDisplayBinding? = null
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

    private fun observeValue() {
        val observer = Observer<String> {
            Log.d(TAG, it)
            try {
                viewModel.generate()
            } catch (e: Exception) {
//                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.qrCode.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.toString())
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        qrCodeImageView.setImageBitmap(Utility.createQRCode(resources))

    }

    companion object {
        private const val TAG = "QRCodeDisplayActivity"
    }
}