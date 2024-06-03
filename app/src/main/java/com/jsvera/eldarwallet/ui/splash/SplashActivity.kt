package com.jsvera.eldarwallet.ui.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.data.base.BaseActivity
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.data.local.entities.User
import com.jsvera.eldarwallet.databinding.ActivitySplashBinding
import com.jsvera.eldarwallet.ui.login.LoginActivity
import org.json.JSONObject
import java.security.MessageDigest

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySplashBinding.inflate(layoutInflater) }
    private var splashIntent: Intent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        Handler(Looper.myLooper()!!).postDelayed({
            val userLogged = AppPreferences.getUser()
            if (userLogged != null) {

            } else{
                splashIntent = Intent(this, LoginActivity::class.java)
            }
            startActivity(splashIntent)
        }, 2000)
    }

}