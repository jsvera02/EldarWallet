package com.jsvera.eldarwallet.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsvera.eldarwallet.base.BaseViewModel
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.data.local.entities.CardEntity
import com.jsvera.eldarwallet.di.EldarWalletApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CardViewModel(application: Application) : BaseViewModel(application) {

    private val _addCardLiveData = MutableLiveData<Resource<Unit>>()
    val addCardLiveData: LiveData<Resource<Unit>> get() = _addCardLiveData

    private val _cardsLiveData = MutableLiveData<Resource<List<CardEntity>>>()
    val cardsLiveData: LiveData<Resource<List<CardEntity>>> get() = _cardsLiveData

    fun addCard(cardEntity: CardEntity) {
        viewModelScope.launch {
            try {
                _addCardLiveData.postValue(Resource.Loading())

                withContext(Dispatchers.IO) {
                    val db = EldarWalletApp().getDatabase(getApplication())
                    db.cardDao().insertCard(cardEntity)
                    _addCardLiveData.postValue(Resource.Success(Unit))
                }
            } catch (e: Exception) {
                _addCardLiveData.postValue(Resource.Error(e))
            }
        }
    }

    fun loadCards(userId: Long) {
        viewModelScope.launch {
            _cardsLiveData.postValue(Resource.Loading())
            try {
                val db = EldarWalletApp().getDatabase(getApplication())
                val cards = db.cardDao().getCardsByUserId(userId)

                _cardsLiveData.postValue(Resource.Success(cards))
            } catch (e: Exception) {
                _cardsLiveData.postValue(Resource.Error(e))
            }
        }
    }
}
