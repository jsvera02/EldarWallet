package com.jsvera.eldarwallet.ui.signUp

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
import com.jsvera.eldarwallet.databinding.ActivitySignUpBinding
import com.jsvera.eldarwallet.ui.main.MainActivity
import com.jsvera.eldarwallet.viewModel.AuthViewModel

class SignUpActivity : BaseActivity() {

    private val binding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }
    private val authViewModel by viewModels<AuthViewModel>()

    override fun initViewModel() {
        authViewModel.signUpLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {}
                is Resource.Success -> {
                    val dialog = BaseDialog.newInstance(
                        title = getString(R.string.app_name),
                        description = getString(R.string.account_created),
                        showBtnPositive = true,
                        showBtnNegative = false,
                        textBtnPositive = getString(R.string.accept)
                    )
                    dialog.onClickAccept = {
                        dialog.dismiss()
                        startActivity(
                            Intent(this@SignUpActivity, MainActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        )
                    }
                    dialog.show(supportFragmentManager, null)
                }

                is Resource.Error -> {
                    val dialog = BaseDialog.newInstance(
                        title = getString(R.string.app_name),
                        description = getString(R.string.username_already_used),
                        showBtnPositive = true,
                        showBtnNegative = false,
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBarsInsets.left, systemBarsInsets.top, systemBarsInsets.right, systemBarsInsets.bottom)
            insets
        }

        binding.apply {

            btnBack.setOnClickListener {
                finish()
            }
            btnSignUp.setOnClickListener {

                val name = etName.text.toString()
                val lastName = etLastName.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()

                if (name.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty()) {
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
                    return@setOnClickListener
                } else if (!cbTerms.isChecked) {
                    val dialog = BaseDialog.newInstance(
                        getString(R.string.terms_error),
                        "",
                        showBtnPositive = true,
                        textBtnPositive = getString(R.string.accept)
                    )
                    dialog.onClickAccept = {
                        dialog.dismiss()
                    }
                    dialog.show(supportFragmentManager, null)
                } else {
                    authViewModel.signUp(username,name, lastName , password)
                }
            }
        }
    }
}
