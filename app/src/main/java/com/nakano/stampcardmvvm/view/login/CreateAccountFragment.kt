package com.nakano.stampcardmvvm.view.login

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
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentEmailBaseBinding
import com.nakano.stampcardmvvm.viewModel.AuthViewModel
import com.nakano.stampcardmvvm.viewModel.AuthViewModelFactory
import kotlinx.android.synthetic.main.fragment_email_base.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

// TODO: High EmailLoginからの遷移の時に、入力しているパスワードは引き継ぐこと、また新規登録からログインへも引き継ぐこと
class CreateAccountFragment : Fragment(), KodeinAware {

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

        login_button.text = getString(R.string.new_registration)
        login_button.setOnClickListener {
            viewModel.createInWithEmailAndPassword(
                field_email.text.toString(),
                field_password.text.toString()
            )
            viewModel.isSuccess.observe(viewLifecycleOwner, Observer {
                progress_bar.visibility = View.INVISIBLE
                if (it) {
                    val action =
                        CreateAccountFragmentDirections.actionCreateAccountFragmentPopUpToStampCardFragment()
                    findNavController().navigate(action)
                    Toast.makeText(
                        context,
                        R.string.create_user_with_email_success,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        R.string.create_user_with_email_failure,
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }

        forgot_password.visibility = View.GONE
        new_registration.visibility = View.GONE
    }
}
