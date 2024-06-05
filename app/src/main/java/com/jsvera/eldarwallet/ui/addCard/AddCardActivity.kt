package com.jsvera.eldarwallet.ui.addCard

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.jsvera.eldarwallet.R
import com.jsvera.eldarwallet.base.BaseActivity
import com.jsvera.eldarwallet.base.BaseDialog
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.data.local.AppPreferences.getUser
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.databinding.ActivityAddCardBinding
import com.jsvera.eldarwallet.ui.main.MainActivity
import com.jsvera.eldarwallet.utils.AppConstants.KEY_AMEX
import com.jsvera.eldarwallet.utils.AppConstants.KEY_MASTER
import com.jsvera.eldarwallet.utils.AppConstants.KEY_VISA
import com.jsvera.eldarwallet.utils.EncryptionUtil
import com.jsvera.eldarwallet.utils.addAutoFormatExpirationDateWatcher
import com.jsvera.eldarwallet.utils.gone
import com.jsvera.eldarwallet.utils.visible
import com.jsvera.eldarwallet.viewModel.CardViewModel
import java.util.Calendar
import java.util.Locale

class AddCardActivity : BaseActivity() {

    private val cardViewModel by viewModels<CardViewModel>()
    private val binding by lazy { ActivityAddCardBinding.inflate(layoutInflater) }

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
            binding.btnBack.setOnClickListener { finish() }
            setCardTypeDrawable(etCardNumber)
            addAutoFormatExpirationDateWatcher(etExpirationDate)
            btnSaveCard.setOnClickListener {
                val name = etCardHolderName.text.toString()
                val cardNumber = etCardNumber.text.toString()
                val cardHolderName = etCardHolderName.text.toString()
                val expirationDate = etExpirationDate.text.toString()
                val cvv = etCVV.text.toString()
                val dni = etDNI.text.toString()
                val nameUser = getUser()?.name
                val lastNameUser = getUser()?.lastName
                var validateName: Boolean = false
                if (nameUser.isNullOrEmpty() || lastNameUser.isNullOrEmpty()) {
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
                    dialog.show(supportFragmentManager, null)
                } else
                    validateName = isFullNameMatch(nameUser, lastNameUser, name)

                if (name.isEmpty() || cardNumber.isEmpty() || cardHolderName.isEmpty() || expirationDate.isEmpty() || cvv.isEmpty() || dni.isEmpty()) {
                    showErrorDialog("Error", "Complete todos los campos")
                } else if (!isValidCardNumber(cardNumber)) {
                    showErrorDialog("Error", "Número de tarjeta inválido")
                } else if (cvv.length !in 3..4 || !cvv.all { it.isDigit() }) {
                    showErrorDialog("Error", "El CVV debe ser de 3 o 4 dígitos")
                } else if (!isValidExpirationDate(expirationDate)) {
                    showErrorDialog(
                        "Error",
                        "Fecha de expiración o formato MM/YY inválidos"
                    )
                } else if (!validateName) {
                    showErrorDialog(
                        "Error",
                        "No puedes agregar una tarjeta de otra persona,\ncorrobora los datos e inténtalo nuevamente"
                    )
                } else {
                    val encryptedCardNumber = EncryptionUtil.encrypt(cardNumber)
                    val encryptedCvv = EncryptionUtil.encrypt(cvv)
                    val cardType = detectCardType(cardNumber)

                    cardViewModel.addCard(
                        CardEntity(
                            name = name,
                            card_number = encryptedCardNumber,
                            card_holder_name = cardHolderName,
                            expiration_date = expirationDate,
                            cvv = encryptedCvv,
                            card_type = cardType,
                            dni = dni,
                            user_id = AppPreferences.getUser()?.userId ?: 0,
                            isSelected = false
                        )
                    )
                }
            }
        }
    }

    private fun isFullNameMatch(firstName: String, lastName: String, fullName: String): Boolean {
        val fullNameWords = fullName.trim().lowercase(Locale.ROOT).split(" ")
        val firstNameMatch = fullNameWords.contains(firstName.trim().lowercase(Locale.ROOT))
        val lastNameMatch = fullNameWords.contains(lastName.trim().lowercase(Locale.ROOT))
        return firstNameMatch && lastNameMatch
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        val firstDigit = cardNumber.firstOrNull()?.toString()?.toIntOrNull()
        return firstDigit in listOf(3, 4, 5)
    }

    private fun setCardTypeDrawable(editText: EditText) {
        val cardDrawableVisa = R.drawable.ic_visa
        val cardDrawableMastercard = R.drawable.ic_master
        val cardDrawableAmex = R.drawable.ic_amex

        editText.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                isFormatting = true
                val formattedText = formatCardNumber(s.toString())
                editText.setText(formattedText)
                editText.setSelection(formattedText.length)
                isFormatting = false

                val cardNumber = formattedText.replace(" ", "")
                val cardTypeDrawable = when {
                    cardNumber.startsWith("5") -> cardDrawableMastercard
                    cardNumber.startsWith("4") -> cardDrawableVisa
                    cardNumber.startsWith("3") -> cardDrawableAmex
                    else -> 0
                }

                editText.setCompoundDrawablesWithIntrinsicBounds(0, 0, cardTypeDrawable, 0)
            }
        })
    }


    private fun detectCardType(cardNumber: String): String {
        return when (cardNumber.first()) {
            '3' -> KEY_AMEX
            '4' -> KEY_VISA
            '5' -> KEY_MASTER
            else -> "Unknown"
        }
    }

    private fun formatCardNumber(cardNumber: String): String {
        val digitsOnly = cardNumber.replace("\\s".toRegex(), "")
        val formatted = StringBuilder()

        when {
            digitsOnly.startsWith("3") -> {
                // Formatting cards start with 3
                for (i in digitsOnly.indices) {
                    if (i == 4 || i == 10) {
                        formatted.append(" ")
                    }
                    formatted.append(digitsOnly[i])
                }
            }

            digitsOnly.startsWith("4") || digitsOnly.startsWith("5") -> {
                // Formatting cards start with 4 or 5
                for (i in digitsOnly.indices) {
                    if (i > 0 && i % 4 == 0) {
                        formatted.append(" ")
                    }
                    formatted.append(digitsOnly[i])
                }
            }

            else -> {
                return digitsOnly
            }
        }

        return formatted.toString()
    }

    private fun isValidExpirationDate(expirationDate: String): Boolean {
        val regex = Regex("""^(0[1-9]|1[0-2])/([0-9]{2})$""")
        if (!regex.matches(expirationDate)) return false

        val (month, year) = expirationDate.split("/").map { it.toInt() }
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR) % 100
        val currentMonth = calendar.get(Calendar.MONTH) + 1

        return if (year > currentYear) {
            true
        } else if (year == currentYear) {
            month >= currentMonth
        } else {
            false
        }
    }

    override fun initViewModel() {
        cardViewModel.addCardLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBar.visible()
                    binding.btnSaveCard.gone()
                }

                is Resource.Success -> {
                    binding.progressBar.gone()
                    binding.btnSaveCard.visible()
                    val i = Intent(applicationContext, MainActivity::class.java)
                    i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(i)
                }

                is Resource.Error -> {
                    binding.progressBar.gone()
                    binding.btnSaveCard.visible()
                    showErrorDialog("Error", resource.e.toString() ?: "An error occurred")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}