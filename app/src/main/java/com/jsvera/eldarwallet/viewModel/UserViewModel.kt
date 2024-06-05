package com.jsvera.eldarwallet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsvera.eldarwallet.data.remote.Resource
import com.jsvera.eldarwallet.data.local.entities.User
import com.jsvera.eldarwallet.di.EldarWalletApp
import com.jsvera.eldarwallet.utils.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val _userLiveData = MutableLiveData<Resource<User>>()
    val userLiveData: LiveData<Resource<User>> get() = _userLiveData

    fun loadUser(userName: String) {
        viewModelScope.launch {
            try {
                _userLiveData.postValue(Resource.Loading())

                withContext(Dispatchers.IO) {
                    val db = EldarWalletApp().getDatabase(getApplication())
                    val userEntity = db.getItemsDao().getUserByUsername(userName)
                        ?: throw Exception("User not found")

                    val user = userEntity.toUser()
                    _userLiveData.postValue(Resource.Success(user))
                }
            } catch (e: Exception) {
                _userLiveData.postValue(Resource.Error(e))
                Log.e("LOAD_USER:", e.toString())
            }
        }
    }
}
