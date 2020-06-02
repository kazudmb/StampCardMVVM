package com.nakano.stampcardmvvm.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentStampCardBinding
import com.nakano.stampcardmvvm.viewModel.StampCardViewModel

const val TAG_OF_STAMP_CARD_FRAGMENT = "StampCardFragment"

class StampCardFragment : Fragment() {

//    private val viewModel: StampCardViewModel by viewModels()
    private lateinit var binding: FragmentStampCardBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_stamp_card, container, false)
        binding.stampCardViewModel = StampCardViewModel()

        return binding.root
    }
}
