package com.nakano.stampcardmvvm.view.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentEmailBaseBinding
import com.nakano.stampcardmvvm.util.Utility
import com.nakano.stampcardmvvm.viewModel.AuthViewModel
import com.nakano.stampcardmvvm.viewModel.AuthViewModelFactory
import kotlinx.android.synthetic.main.fragment_email_base.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class EmailLoginFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEmailBaseBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_email_base, container, false)
        binding.authViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        login_button.setOnClickListener {
            if (Utility.validateForm(requireContext().applicationContext, field_email, field_password)) {
                progress_bar.visibility = View.VISIBLE
                viewModel.signInWithEmailAndPassword(field_email.text.toString(), field_password.text.toString())
                viewModel.isSuccess.observe(viewLifecycleOwner, Observer {
                        progress_bar.visibility = View.INVISIBLE
                        if (it) {
                            val action =
                                EmailLoginFragmentDirections.actionEmailLoginFragmentPopUpToStampCardFragment()
                            findNavController().navigate(action)
                            Toast.makeText(context, R.string.sign_in_with_email_success, Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, R.string.sign_in_with_email_failure, Toast.LENGTH_LONG).show()
                        }
                    })
            }
        }

        forgot_password.setOnClickListener {
            viewModel.saveTmpEmail(field_email.text.toString())
            findNavController().navigate(R.id.action_loginFragment_to_changePasswordFragment)
        }

        new_registration.setOnClickListener {
            viewModel.saveTmpEmail(field_email.text.toString())
            findNavController().navigate(R.id.action_loginFragment_to_createAccountFragment)
        }
    }
}
