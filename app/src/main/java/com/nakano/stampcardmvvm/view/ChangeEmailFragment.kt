package com.nakano.stampcardmvvm.view

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

class ChangeEmailFragment : Fragment(), KodeinAware {

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

        val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUserEmail = firebaseAuth.currentUser?.email

        login_button.text = requireContext().getText(R.string.send)
        login_button.setOnClickListener {
            if (currentUserEmail != null) {
                if (Utility.validateForm(requireContext(), field_email, field_password)) {
                    progress_bar.visibility = View.VISIBLE
                    viewModel.updateEmail(currentUserEmail, field_email.text.toString(), field_password.text.toString())
                    viewModel.isSuccess.observe(viewLifecycleOwner,
                        Observer {
                            progress_bar.visibility = View.INVISIBLE
                            if (it) {
                                // TODO: navigationする時に、新しくなったメールアドレスも渡すこと
                                val args = bundleOf(getString(R.string.BUNDLE_PAIR_KEY_IS_CHANGE_EMAIL) to true)
                                findNavController().navigate(R.id.action_changeEmailFragment_popUpTo_accountInfo, args)
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
