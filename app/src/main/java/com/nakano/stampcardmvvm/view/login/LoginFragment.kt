package com.nakano.stampcardmvvm.view.login

import android.content.Intent
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
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentLoginBinding
import com.nakano.stampcardmvvm.util.HelperClass
import com.nakano.stampcardmvvm.viewModel.AuthViewModel
import com.nakano.stampcardmvvm.viewModel.AuthViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

// TODO: 各login処理ごとに分割すること
// TODO: 複数のサービスでログインする場合、アカウントが上書きされているケースがあるので、修正する必要あり
class LoginFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private val viewModel: AuthViewModel by viewModels { factory }
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentLoginBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.authViewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initGoogleSignInClient()

        login_google.setOnClickListener {
            signIn()
        }

        login_twitter.setOnClickListener {
            toastMessage(R.string.toast_message_coming_soon)
//            progress_bar.visibility = View.VISIBLE
//            viewModel.signInWithTwitter()
//            viewModel.isSuccess.observe(
//                viewLifecycleOwner,
//                Observer {
//                    progress_bar.visibility = View.INVISIBLE
//                    if (it) {
//                        goToStampCardFragment()
//                        toastMessage(R.string.sign_in_with_email_success)
//                    } else {
//                        toastMessage(R.string.sign_in_with_email_failure)
//                    }
//                })
        }

        login_facebook.setOnClickListener {
            toastMessage(R.string.toast_message_coming_soon)
        }

        login_email.setOnClickListener {
            val action =
                LoginFragmentDirections.actionLoginFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        login_phone.setOnClickListener {
            toastMessage(R.string.toast_message_coming_soon)
        }

        login_anonymous.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            viewModel.signInAnonymous()
            viewModel.isSuccess.observe(
                viewLifecycleOwner,
                Observer {
                    progress_bar.visibility = View.INVISIBLE
                    if (it) {
                        goToStampCardFragment()
                        toastMessage(R.string.sign_in_with_email_success)
                    } else {
                        toastMessage(R.string.sign_in_with_email_failure)
                    }
                })

        }
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient =
            GoogleSignIn.getClient(context?.applicationContext!!, googleSignInOptions)
    }

    private fun signIn() {
        progress_bar.visibility = View.VISIBLE
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, 123)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 123) {
            val task =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val googleSignInAccount = task.getResult(
                    ApiException::class.java
                )
                googleSignInAccount?.let { getGoogleAuthCredential(it) }
            } catch (e: ApiException) {
                HelperClass.logErrorMessage(e.message)
            }
        }
    }

    private fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential =
            GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        viewModel.signInWithGoogle(googleAuthCredential)
        viewModel.isSuccess.observe(
            viewLifecycleOwner,
            Observer {
                progress_bar.visibility = View.INVISIBLE
                if (it) {
                    goToStampCardFragment()
                    toastMessage(R.string.sign_in_with_email_success)
                } else {
                    toastMessage(R.string.sign_in_with_email_failure)
                }
            })
    }

    private fun toastMessage(resourceId: Int) {
        Toast.makeText(context, resourceId, Toast.LENGTH_LONG)
            .show()
    }

    private fun goToStampCardFragment() {
        val action =
            LoginFragmentDirections.actionLoginPopUpToStampCardFragment()
        findNavController().navigate(action)
    }
}
