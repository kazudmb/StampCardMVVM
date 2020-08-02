package com.nakano.stampcardmvvm.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentStampCardBinding
import com.nakano.stampcardmvvm.viewModel.StampCardViewModel
import com.nakano.stampcardmvvm.viewModel.StampCardViewModelFactory
import kotlinx.android.synthetic.main.fragment_stamp_card.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class StampCardFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: StampCardViewModelFactory by instance()

    private lateinit var viewModel: StampCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStampCardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stamp_card, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(StampCardViewModel::class.java)
        binding.stampCardViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_stamp.setOnClickListener {
            val action =
                StampCardFragmentDirections.actionStampcardFragmentToQrcodedisplayFragment()
            findNavController().navigate(action)
        }
    }
}
