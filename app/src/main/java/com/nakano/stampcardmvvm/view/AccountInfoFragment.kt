package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.*
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentAccountInfoBinding
import com.nakano.stampcardmvvm.util.Constant
import com.nakano.stampcardmvvm.viewModel.UserViewModel
import com.nakano.stampcardmvvm.viewModel.UserViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

// TODO: High layoutファイルを、マテリアルデザインに準拠させること
// TODO: メールアドレスの認証状況(credential)を取得して、表示するレイアウトに変更すること
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
        // TODO: changeEmailのnavigationで戻ってきている場合は、getUserFromGoogleで変更後のアドレスを取得させる
        viewModel.getUser()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        viewModel.getCurrentProviderId()
        viewModel.currentProviderId.observe(viewLifecycleOwner,
            Observer {
                val status: String = when(it) {
                    EmailAuthProvider.PROVIDER_ID -> Constant.ALL
//                    FacebookAuthProvider.PROVIDER_ID -> Constant.ALL
                    FirebaseAuthProvider.PROVIDER_ID -> Constant.ONLY_LOGOUT
//                    GithubAuthProvider.PROVIDER_ID -> Constant.ALL
                    GoogleAuthProvider.PROVIDER_ID -> Constant.ALL
//                    PhoneAuthProvider.PROVIDER_ID -> Constant.ONLY_LOGOUT
//                    PlayGamesAuthProvider.PROVIDER_ID -> Constant.ALL
//                    TwitterAuthProvider.PROVIDER_ID -> Constant.ALL
                    else -> Constant.ONLY_LOGOUT
                }

                when(status) {
                    Constant.ALL -> {
                        menu.findItem(R.id.change_email).isVisible = true
                        menu.findItem(R.id.change_password).isVisible = true
                        menu.findItem(R.id.logout).isVisible = true
                    }
                    Constant.EMAIL_CHANGE_AND_LOGOUT -> {
                        menu.findItem(R.id.change_email).isVisible = true
                        menu.findItem(R.id.change_password).isVisible = false
                        menu.findItem(R.id.logout).isVisible = true
                    }
                    Constant.ONLY_LOGOUT -> {
                        menu.findItem(R.id.change_email).isVisible = false
                        menu.findItem(R.id.change_password).isVisible = false
                        menu.findItem(R.id.logout).isVisible = true
                    }
                }
            })
        super.onPrepareOptionsMenu(menu)
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
                viewModel.logoutFromGoogle()
                val action =
                    AccountInfoFragmentDirections.actionAccountInfoPopUpToStampCardFragment()
                findNavController().navigate(action)
                Toast.makeText(context, R.string.success_logout, Toast.LENGTH_LONG).show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
