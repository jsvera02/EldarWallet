package com.jsvera.eldarwallet.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.data.adapter.ToolbarPagerAdapter
import com.jsvera.eldarwallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapterToolbar: ToolbarPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        adapterToolbar = ToolbarPagerAdapter(supportFragmentManager, lifecycle)
/*        adapterToolbar.addFragment(HomeFragment(), "HomeFragment")
        adapterToolbar.addFragment(BalanceFragment(), "MyListFragment")
        adapterToolbar.addFragment(CardsFragment(), "SearchFragment")
        adapterToolbar.addFragment(AddCardFragment(), "ConfigFragment")
        adapterToolbar.addFragment(QrPaymentFragment(), "ConfigFragment")
        adapterToolbar.addFragment(MakePaymentFragment(), "ConfigFragment")*/

    }
}