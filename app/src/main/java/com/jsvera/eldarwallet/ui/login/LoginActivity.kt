package com.jsvera.eldarwallet.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.data.Resource
import com.jsvera.eldarwallet.base.BaseActivity
import com.jsvera.eldarwallet.base.BaseDialog
import com.jsvera.eldarwallet.databinding.ActivityLoginBinding
import com.jsvera.eldarwallet.ui.main.MainActivity
import com.jsvera.eldarwallet.ui.signUp.SignUpActivity
import com.jsvera.eldarwallet.utils.gone
import com.jsvera.eldarwallet.utils.isConnected
import com.jsvera.eldarwallet.utils.visible
import com.jsvera.eldarwallet.viewModel.AuthViewModel

class LoginActivity : BaseActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val authViewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom)
            insets
        }

        with(binding) {
            btnLogin.setOnClickListener {
                val userName = etUser.text.toString()
                val password = etPassword.text.toString()
                if (userName.isEmpty() || password.isEmpty()) {
                    val dialog = BaseDialog.newInstance(
                        getString(R.string.app_name),
                        getString(R.string.incomplete_input),
                        showBtnPositive = true,
                        textBtnPositive = getString(R.string.accept)
                    )
                    dialog.onClickAccept = {
                        dialog.dismiss()
                    }
                    dialog.show(supportFragmentManager, null)
                } else {
                    authViewModel.login(userName, password)
                }
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
        }
    }

    override fun initViewModel() {
        authViewModel.loginLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnLogin.gone()
                }

                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnLogin.visible()
                    startActivity(Intent(applicationContext, MainActivity::class.java))
                    finish()
                }

                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnLogin.visible()

                    if (isConnected()) {
                        resource.e?.let {
                            val dialog = BaseDialog.newInstance(
                                getString(R.string.app_name),
                                getString(R.string.invalid_cred),
                                showBtnPositive = true,
                                textBtnPositive = getString(R.string.accept)
                            )
                            dialog.onClickAccept = {
                                dialog.dismiss()
                            }
                            dialog.show(supportFragmentManager, null)
                        }
                    } else {
                        val dialog = BaseDialog.newInstance(
                            getString(R.string.app_name),
                            getString(R.string.error_internet),
                            showBtnPositive = true,
                            textBtnPositive = getString(R.string.accept)
                        )
                        dialog.onClickAccept = {
                            dialog.dismiss()
                        }
                        dialog.show(supportFragmentManager, null)
                    }
                }
            }
        }
    }
}