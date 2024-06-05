package com.jsvera.eldarwallet.ui.generateQr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.base.BaseDialog
import com.jsvera.eldarwallet.base.BaseFragment
import com.jsvera.eldarwallet.data.local.AppPreferences.getUser
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.databinding.FragmentGenerateQrBinding
import com.jsvera.eldarwallet.utils.gone
import com.jsvera.eldarwallet.utils.visible
import com.jsvera.eldarwallet.viewModel.QRViewModel

class GenerateQrFragment : BaseFragment() {
    private var _binding: FragmentGenerateQrBinding? = null
    private val binding get() = _binding!!
    private val qrViewModel by viewModels<QRViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenerateQrBinding.inflate(inflater, container, false)
        binding.btnGenerateQR.setOnClickListener { generateQRCode()  }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observa los cambios en el LiveData del ViewModel
        qrViewModel.qrCodeLiveData.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressQR.visible()
                    binding.titleGenerator.text="Generando tu QR..."
                }

                is Resource.Success -> {
                    binding.titleGenerator.text="!QR generado con éxito!"
                    binding.progressQR.gone()
                    val qrCodeBitmap = resource.data
                    qrCodeBitmap?.let {
                        Glide.with(requireContext())
                            .load(it)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .into(binding.ivQr)
                    }
                }

                is Resource.Error -> {
                    binding.progressQR.gone()
                    binding.titleGenerator.text="Hubo un error al generar tu QR\ninténtalo denuevo"
                    binding.btnGenerateQR.visible()
                }
            }
        }

        // Llama al método para generar el QR cuando sea necesario
        generateQRCode()
    }

    private fun generateQRCode() {
        val user = getUser()
        val userName = user?.name
        val lastName = user?.lastName
        if (userName == null || lastName == null) {
            val dialog = BaseDialog.newInstance(
                getString(R.string.app_name),
                getString(R.string.error_user),
                showBtnPositive = true,
                textBtnPositive = getString(R.string.accept),
                cancelable = false
            )
            dialog.onClickAccept = {
                goToLogin()
            }
            dialog.show(childFragmentManager, null)
        } else
            qrViewModel.generateQR(userName, lastName)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}