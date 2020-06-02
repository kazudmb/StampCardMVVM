package com.nakano.stampcardmvvm.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nakano.stampcardmvvm.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val fragment = StampCardFragment()

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment, TAG_OF_STAMP_CARD_FRAGMENT)
                .commit()
        }
    }
}