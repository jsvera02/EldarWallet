package com.jsvera.eldarwallet.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.databinding.ActivitySplashBinding
import com.jsvera.eldarwallet.ui.home.HomeActivity
import com.jsvera.eldarwallet.ui.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()

        Handler(Looper.getMainLooper()).postDelayed({
            val userLogged = AppPreferences.getUser()
            val splashIntent: Intent = if (userLogged != null) {
                Intent(this, HomeActivity::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            startActivity(splashIntent)
            finish()
        }, 2000)
    }
}
