package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentAccountInfoBinding
import com.nakano.stampcardmvvm.viewModel.UserViewModel
import com.nakano.stampcardmvvm.viewModel.UserViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AccountInfoFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()
    private val viewModel: UserViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentAccountInfoBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account_info, container, false)
        binding.userViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
        viewModel.getUser()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_account_info, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.change_email -> {
                val action =
                    AccountInfoFragmentDirections.actionAccountInfoFragmentToChangeEmailFragment()
                findNavController().navigate(action)
                return true
            }
            R.id.change_password -> {
                val action =
                    AccountInfoFragmentDirections.actionAccountInfoFragmentToChangePasswordFragment()
                findNavController().navigate(action)
                return true
            }
            R.id.logout -> {
                // TODO サインアウトの実装すること、AuthViewModelで処理したいがUserViewModelにいるため要検討
                val firebaseAuth = FirebaseAuth.getInstance()
                firebaseAuth.signOut()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
