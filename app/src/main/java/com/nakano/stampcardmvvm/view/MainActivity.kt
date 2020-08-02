package com.nakano.stampcardmvvm.view

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.nakano.stampcardmvvm.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp()
            = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.loginFragment -> {
                val action =
                    StampCardFragmentDirections.actionStampCardFragmentToLoginFragment()
                findNavController(R.id.nav_host_fragment).navigate(action)
                return true
            }
            R.id.accountInfoFragment -> {
                val action =
                    StampCardFragmentDirections.actionStampCardFragmentToAccountInfoFragment()
                findNavController(R.id.nav_host_fragment).navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}