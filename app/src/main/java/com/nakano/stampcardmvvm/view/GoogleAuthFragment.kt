package com.nakano.stampcardmvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentGoogleAuthBinding
import com.nakano.stampcardmvvm.model.model.UserFirebase
import com.nakano.stampcardmvvm.util.HelperClass
import com.nakano.stampcardmvvm.viewModel.AuthViewModelFactory
import com.nakano.stampcardmvvm.viewModel.AuthViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class GoogleAuthFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentGoogleAuthBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_google_auth, container, false)
        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        binding.authViewModelKotlin = viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSignInButton()
        initAuthViewModel()
        initGoogleSignInClient()
    }

    fun initSignInButton() {
        val googleSignInButton: SignInButton =
            requireView().findViewById(R.id.google_sign_in_button)
        googleSignInButton.setOnClickListener { v: View? -> signIn() }
    }

    fun initAuthViewModel() {
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    fun initGoogleSignInClient() {
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        googleSignInClient = GoogleSignIn.getClient(context?.applicationContext!!, googleSignInOptions)
    }

    fun signIn() {
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

    fun getGoogleAuthCredential(googleSignInAccount: GoogleSignInAccount) {
        val googleTokenId = googleSignInAccount.idToken
        val googleAuthCredential =
            GoogleAuthProvider.getCredential(googleTokenId, null)
        signInWithGoogleAuthCredential(googleAuthCredential)
    }

    fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        viewModel.signInWithGoogle(googleAuthCredential)
        viewModel.authenticatedUserLiveData?.observe(viewLifecycleOwner,
            Observer { authenticatedUser: UserFirebase ->
                if (authenticatedUser.isNew!!) {
                    createNewUser(authenticatedUser)
                } else {
                    goToStampCardFragment()
                }
            }
        )
    }

    fun createNewUser(authenticatedUser: UserFirebase) {
        viewModel.createUser(authenticatedUser)
        viewModel.createdUserLiveData?.observe(
            viewLifecycleOwner,
            Observer { user: UserFirebase ->
                if (user.isCreated!!) {
                    toastMessage(user.name)
                }
                goToStampCardFragment()
            }
        )
    }

    fun toastMessage(name: String?) {
        Toast.makeText(
            context,
            "Hi $name!\nYour account was successfully created.",
            Toast.LENGTH_LONG
        ).show()
    }

    fun goToStampCardFragment() {
        val action =
            GoogleAuthFragmentDirections.actionGoogleAuthFragmentPopUpToStampCardFragment()
        findNavController().navigate(action)
    }
}
