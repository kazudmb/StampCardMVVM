package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentChangeEmailBinding
import com.nakano.stampcardmvvm.viewModel.StampCardViewModel
import com.nakano.stampcardmvvm.viewModel.StampCardViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ChangeEmailFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: StampCardViewModelFactory by instance()

    private lateinit var viewModel: StampCardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChangeEmailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_change_email, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(StampCardViewModel::class.java)
        binding.stampCardViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}