package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentStampCardBinding
import com.nakano.stampcardmvvm.viewModel.UserViewModel
import com.nakano.stampcardmvvm.viewModel.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_stamp_card.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class StampCardFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()

    private lateinit var viewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStampCardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stamp_card, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(UserViewModel::class.java)
        binding.userViewModel = viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        button_stamp.setOnClickListener {
            viewModel.isLogin()
            viewModel.isLoginLiveData?.observe(viewLifecycleOwner,
                Observer {
                    if (it) {
                        val action =
                            StampCardFragmentDirections.actionStampcardFragmentToQrcodedisplayFragment()
                        findNavController().navigate(action)
                    } else {
                        Toast.makeText(
                            context,
                            getString(R.string.please_login),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                })
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        viewModel.isLogin()
        viewModel.isLoginLiveData?.observe(viewLifecycleOwner,
            Observer {
                if (it) {
                    menu.findItem(R.id.login).isVisible = false
                    menu.findItem(R.id.accountInfo).isVisible = true
                } else {
                    menu.findItem(R.id.login).isVisible = true
                    menu.findItem(R.id.accountInfo).isVisible = false
                }
            })
        super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.login -> {
                val action =
                    StampCardFragmentDirections.actionStampCardFragmentToLoginFragment()
                findNavController().navigate(action)
                return true
            }
            R.id.accountInfo -> {
                val action =
                    StampCardFragmentDirections.actionStampCardFragmentToAccountInfoFragment()
                findNavController().navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
