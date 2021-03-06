package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.Constraints.TAG
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.nakano.stampcardmvvm.R
import com.nakano.stampcardmvvm.databinding.FragmentStampCardBinding
import com.nakano.stampcardmvvm.viewModel.UserViewModel
import com.nakano.stampcardmvvm.viewModel.UserViewModelFactory
import kotlinx.android.synthetic.main.fragment_stamp_card.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class StampCardFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: UserViewModelFactory by instance()
    private val viewModel: UserViewModel by viewModels { factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentStampCardBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_stamp_card, container, false)
        binding.userViewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)

        // TODO: データ取得するまでの間、viewに値が入っていないので、代わりの値を入れるかsplashを入れるかどちらか対応すること

        viewModel.setBlankStampArea()
        viewModel.getUserFromFirestore()
        viewModel.user.observe(viewLifecycleOwner,
            Observer {
                if (it != null) {
                    viewModel.setStamp()
                }
            })

        button_stamp.setOnClickListener {
            viewModel.isLogin()
            viewModel.isLoginLiveData.observe(viewLifecycleOwner,
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

        swipe_refresh_layout.setColorSchemeResources(R.color.colorAccent)
        swipe_refresh_layout.setOnRefreshListener {
            CoroutineScope(Dispatchers.Default).launch {
                myTask()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        viewModel.isLogin()
        viewModel.isLoginLiveData.observe(viewLifecycleOwner,
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
                val args = bundleOf(getString(R.string.BUNDLE_PAIR_KEY_IS_CHANGE_EMAIL) to false) as Bundle?
                findNavController().navigate(R.id.action_stampCardFragment_to_accountInfoFragment, args)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private suspend fun myTask() {
        try {

            // onPreExecute
            withContext(Dispatchers.Main) {
                viewModel.getUserFromFirestore()
                viewModel.setStamp()
            }

            // doInBackground
            Thread.sleep(500)

            // onPostExecute
            withContext(Dispatchers.Main) {
                swipe_refresh_layout.isRefreshing = false
            }
        } catch (e: Exception) {
            // onCancelled
            Log.e(TAG, "error: ", e)
        }
    }
}
