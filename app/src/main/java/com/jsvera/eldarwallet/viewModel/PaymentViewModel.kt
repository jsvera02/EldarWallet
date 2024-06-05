package com.jsvera.eldarwallet.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsvera.eldarwallet.base.BaseViewModel
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.data.local.entities.Card
import com.jsvera.eldarwallet.di.EldarWalletApp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PaymentViewModel(application: Application) : BaseViewModel(application) {

    private val _selectedCard = MutableLiveData<Card?>()
    val selectedCard: LiveData<Card?> get() = _selectedCard

    private val _paymentCompleted = MutableLiveData<Boolean>()
    val paymentCompleted: LiveData<Boolean> get() = _paymentCompleted

  /*  init {
        val cardId = AppPreferences.getSelectedCardId()
        if (cardId != -1L) {
            viewModelScope.launch {
                _selectedCard.value = getCardById(cardId)
            }
        }
    }*/

    fun selectCard(card: Card) {
        _selectedCard.value = card
     //   card.cardId?.let { AppPreferences.setSelectedCardId(it) }
    }

    fun simulatePayment() {
        val selectedCard = _selectedCard.value
        if (selectedCard != null) {
            viewModelScope.launch {
                delay(2000)
                _paymentCompleted.value = true
            }
        } else {
            _paymentCompleted.value = false
        }
    }

    private suspend fun getCardById(cardId: Long): Card? {
        val db = EldarWalletApp().getDatabase(getApplication())
        return db.cardDao().getCardById(cardId)?.toCard()
    }
}
