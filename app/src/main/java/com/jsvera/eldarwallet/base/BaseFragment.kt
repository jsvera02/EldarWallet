package com.jsvera.eldarwallet.base

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.ui.login.LoginActivity
import com.jsvera.eldarwallet.ui.main.MainActivity


open class BaseFragment() : Fragment() {
    private var hasErrorDialogBeenShown = false

    fun goToLogin() {
        val i = Intent(requireContext(), LoginActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }

    fun Context.showMessage(error: String?, manager: FragmentManager, viewModel: Unit) {
        val dialog2 = BaseDialog.newInstance(
            "Revisa tu conexión a internet",
            null,
            textBtnPositive = "Reintentar",
            showBtnNegative = false,
            showDrawable = false
        )
        dialog2.onClickAccept = { viewModel }
        dialog2.show(manager, null)
    }

    fun goToHome(frag_id: Int? = null, finish: Boolean = false) {
        val i = Intent(requireContext(), MainActivity::class.java)
        if (finish) ActivityCompat.finishAffinity(requireActivity())
        requireContext().startActivity(i)
    }

    fun checkNetworkConnection(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val currentNetwork = connectivityManager.activeNetwork
        return currentNetwork != null
    }

    fun showErrorDialog(title: String, message: String) {
        if (!hasErrorDialogBeenShown) {
            hasErrorDialogBeenShown = true
            val dialog = BaseDialog.newInstance(
                title,
                message,
                showBtnPositive = true,
                textBtnPositive = getString(R.string.accept)
            )
            dialog.onClickAccept = {
                dialog.dismiss()
            }
            dialog.show(childFragmentManager, null)
        }
    }
}