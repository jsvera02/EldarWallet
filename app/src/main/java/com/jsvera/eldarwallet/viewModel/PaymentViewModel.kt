package com.jsvera.eldarwallet.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jsvera.eldarwallet.data.local.entities.Card
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class PaymentViewModel : ViewModel() {

    private val _selectedCard = MutableLiveData<Card?>()
    val selectedCard: LiveData<Card?> get() = _selectedCard

    private val _paymentCompleted = MutableLiveData<Boolean>()
    val paymentCompleted: LiveData<Boolean> get() = _paymentCompleted

    fun selectCard(card: Card) {
        _selectedCard.value = card
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
}

