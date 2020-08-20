package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentEmailLoginBinding
import com.nakano.stampcardmvvm.util.Utility
import com.nakano.stampcardmvvm.viewModel.AuthViewModel
import com.nakano.stampcardmvvm.viewModel.AuthViewModelFactory
import kotlinx.android.synthetic.main.fragment_email_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ChangeEmailFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private val viewModel: AuthViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentEmailLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_email_login, container, false)
        binding.authViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserEmail = firebaseAuth.currentUser?.email

        login_button.text = requireContext().getText(R.string.send)
        login_button.setOnClickListener {
            if (currentUserEmail != null) {
                if (Utility.validateForm(requireContext(), field_email, field_password)) {
                    viewModel.updateEmail(currentUserEmail, field_email.text.toString(), field_password.text.toString())
                    viewModel.isSuccess.observe(viewLifecycleOwner,
                        Observer {
                            if (it) {
                                // TODO: navigationする時に、新しくなったメールアドレスも渡すこと
                                val action =
                                    ChangeEmailFragmentDirections.actionChangeEmailFragmentPopUpToAccountInfo()
                                findNavController().navigate(action)
                                // TODO: 表示するtoast内容は、repositoryで返す内容に含んでそれを使用したい
                                Toast.makeText(
                                    context,
                                    getString(R.string.send_email_verification_success),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    getString(R.string.send_email_verification_failure),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        })
                }
            }
        }

        mail_address.text = getString(R.string.mail_address_after_change)
        current_mail_address1.visibility = View.VISIBLE
        current_mail_address2.visibility = View.VISIBLE
        current_mail_address2.text = firebaseAuth.currentUser?.email.toString()
        forgot_password.visibility = View.GONE
        new_registration.visibility = View.GONE
    }
}
