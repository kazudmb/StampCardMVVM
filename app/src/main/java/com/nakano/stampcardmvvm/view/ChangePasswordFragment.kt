package com.nakano.stampcardmvvm.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentEmailBaseBinding
import com.nakano.stampcardmvvm.util.Utility
import com.nakano.stampcardmvvm.viewModel.UserViewModel
import com.nakano.stampcardmvvm.viewModel.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_email_base.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ChangePasswordFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()
    private val viewModel: UserViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEmailBaseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_email_base, container, false)
        binding.userViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.saveTmpEmail(field_email.text.toString())
            findNavController().popBackStack()
        }

        viewModel.getTmpEmail()
        val firebaseAuth = FirebaseAuth.getInstance()
        val currentEmail = firebaseAuth.currentUser?.email
        if (currentEmail != null) {
            field_email.setOnKeyListener(null)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                field_email.focusable = View.NOT_FOCUSABLE
            }
        }

        login_button.text = getString(R.string.send)
        login_button.setOnClickListener {
            if (Utility.validateForm(requireContext(), field_email)) {
                progress_bar.visibility = View.VISIBLE
                viewModel.sendPasswordResetEmail(field_email.text.toString())
                viewModel.isSuccess.observe(viewLifecycleOwner,
                    Observer {
                        progress_bar.visibility = View.INVISIBLE
                        if (it) {
                            val action =
                                ChangePasswordFragmentDirections.actionChangePasswordFragmentPopUpToAccountInfo()
                            findNavController().navigate(action)
                            Toast.makeText(
                                context,
                                getString(R.string.email_sent_success),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                context,
                                getString(R.string.email_sent_failure),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }

        explanation.visibility = View.VISIBLE
        password.visibility = View.GONE
        field_password.visibility = View.GONE
        forgot_password.visibility = View.GONE
        new_registration.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.saveTmpEmail(field_email.text.toString())
        return NavigationUI.onNavDestinationSelected(
            item,
            requireView().findNavController()
        ) || super.onOptionsItemSelected(item)
    }
}
