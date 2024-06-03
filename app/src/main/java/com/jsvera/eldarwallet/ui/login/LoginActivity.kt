package com.jsvera.eldarwallet.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.data.Resource
import com.jsvera.eldarwallet.data.base.BaseActivity
import com.jsvera.eldarwallet.data.base.BaseDialog
import com.jsvera.eldarwallet.databinding.ActivityLoginBinding
import com.jsvera.eldarwallet.ui.home.HomeActivity
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
                    startActivity(Intent(applicationContext, HomeActivity::class.java))
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