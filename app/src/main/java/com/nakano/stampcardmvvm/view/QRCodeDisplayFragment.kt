package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nakano.stampcardmvvm.databinding.FragmentQRCodeDisplayBinding
import com.nakano.stampcardmvvm.viewModel.QRCodeDisplayViewModel
import com.nakano.stampcardmvvm.viewModel.QRCodeDisplayViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

// TODO: uidからRCodeの生成とセットすること

class QRCodeDisplayFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: QRCodeDisplayViewModelFactory by instance()

    private lateinit var viewModel: QRCodeDisplayViewModel
    private lateinit var qrCodeImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentQRCodeDisplayBinding.inflate(inflater, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(QRCodeDisplayViewModel::class.java)
        binding.qrCodeDisplayViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root

//        val view = inflater.inflate(R.layout.fragment_q_r_code_display, container, false)
//
//        qrCodeImageView = view.findViewById(R.id.qr_code)
//
//        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        qrCodeImageView.setImageBitmap(Utility.createQRCode(resources))

    }

    companion object {
        private const val TAG = "QRCodeDisplayActivity"
    }
}