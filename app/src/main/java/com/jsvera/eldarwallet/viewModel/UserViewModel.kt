package com.jsvera.eldarwallet.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsvera.eldarwallet.data.local.entities.User
import com.jsvera.eldarwallet.di.EldarWalletApp
import com.jsvera.eldarwallet.utils.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application)  {
    private val _userLiveData = MutableLiveData<User>()
    val userLiveData: LiveData<User> get() = _userLiveData

    fun loadUser(userName: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val db = EldarWalletApp().getDatabase(getApplication())
                val userEntity = db.getItemsDao().getUserByUsername(userName)
                userEntity?.let {
                    _userLiveData.postValue(it.toUser())
                }
            }
        }
    }
}