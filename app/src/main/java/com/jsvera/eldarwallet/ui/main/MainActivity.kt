package com.jsvera.eldarwallet.ui.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.data.adapter.ToolbarPagerAdapter
import com.jsvera.eldarwallet.databinding.ActivityMainBinding
import com.jsvera.eldarwallet.ui.home.HomeFragment
import com.jsvera.eldarwallet.ui.generateQr.GenerateQrFragment
import com.jsvera.eldarwallet.ui.payWithCard.PaymentFragment

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var adapterToolbar: ToolbarPagerAdapter
    private var homeFragment = HomeFragment()
    private val mBundle: Bundle= Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        binding.apply {
            ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }
            adapterToolbar = ToolbarPagerAdapter(supportFragmentManager, lifecycle)
            adapterToolbar.addFragment(HomeFragment(), "HomeFragment")
            adapterToolbar.addFragment(GenerateQrFragment(), "GenerateQrFragment")
           // adapterToolbar.addFragment(ScanQrFragment(), "ScanQrFragment")
            adapterToolbar.addFragment(PaymentFragment(), "PaymentFragment")

            pagerToolbar.isUserInputEnabled = false

            Handler(Looper.getMainLooper()).post {
                pagerToolbar.adapter = adapterToolbar
            }

            homeFragment.arguments = mBundle
            binding.bottomNav.setOnItemSelectedListener { item ->
                matchFragId(item.itemId)
                true
            }

            binding.bottomNav.setOnItemReselectedListener { item ->
                matchFragIdReselect(item.itemId)
                true
            }

        }
    }
    private fun matchFragId(fragId: Int) {
        when (fragId) {
            R.id.itemHome -> {
                binding.pagerToolbar.setCurrentItem(0, false)
            }

            R.id.itemGenerator -> {
                binding.pagerToolbar.setCurrentItem(1, false)
            }

      /*      R.id.itemScan -> {
                binding.pagerToolbar.setCurrentItem(2, false)
            }*/

            R.id.itemCard -> {
                binding.pagerToolbar.setCurrentItem(2, false)
            }
        }
    }

    private fun matchFragIdReselect(fragId: Int) {
            when (fragId) {
                R.id.itemHome -> {
                    (binding.pagerToolbar.adapter as? ToolbarPagerAdapter)?.scrollToTop(0)
                }

                R.id.itemGenerator -> {
                    (binding.pagerToolbar.adapter as? ToolbarPagerAdapter)?.scrollToTop(1)
                }
/*
                R.id.itemScan -> {
                    (binding.pagerToolbar.adapter as? ToolbarPagerAdapter)?.scrollToTop(2)
                }*/

                R.id.itemCard -> {
                    (binding.pagerToolbar.adapter as? ToolbarPagerAdapter)?.scrollToTop(2)
                }
            }
        }
    }
