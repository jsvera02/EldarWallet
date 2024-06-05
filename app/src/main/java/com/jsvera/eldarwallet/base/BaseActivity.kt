package com.jsvera.eldarwallet.base

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.ui.login.LoginActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()

    }

    abstract fun initViewModel()

    fun showErrorDialog(title: String, message: String) {
        val dialog = BaseDialog.newInstance(
            title,
            message,
            showBtnPositive = true,
            textBtnPositive = getString(R.string.accept)
        )
        dialog.onClickAccept = {
            dialog.dismiss()
        }
        dialog.show(supportFragmentManager, null)
    }

    fun goToLogin(/*shareContentId:String?*/) {
        val i = Intent(this, LoginActivity::class.java)
        /*    if (shareContentId!=null) {
                i.putExtra("shareContentId", shareContentId)
            }*/
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

}