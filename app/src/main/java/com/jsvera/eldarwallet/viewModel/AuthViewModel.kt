package com.jsvera.eldarwallet.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.jsvera.eldarwallet.EldarWalletApp
import com.jsvera.eldarwallet.data.Resource
import com.jsvera.eldarwallet.data.local.AppPreferences
import com.jsvera.eldarwallet.data.local.database.UserEntity
import com.jsvera.eldarwallet.data.local.entities.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthViewModel(application: Application) : AndroidViewModel(application) {
    //LOGIN
    private val _loginLiveData = MutableLiveData<Resource<User>>()
    val loginLiveData: LiveData<Resource<User>>
        get() = _loginLiveData

    //SIGN UP
    private val _signUpLiveData = MutableLiveData<Resource<User>>()
    val signUpLiveData: LiveData<Resource<User>>
        get() = _signUpLiveData

    fun login(userName: String, password: String) {
        viewModelScope.launch {
            try {
                _loginLiveData.postValue(Resource.Loading())

                withContext(Dispatchers.IO) {
                    val db = EldarWalletApp().getDatabase(getApplication())
                    val userEntity = db.getItemsDao().getUser(userName, password)?.firstOrNull()
                        ?: throw Exception("User not found")

                    val user = userEntity.toUser()
                    AppPreferences.setUser(user)
                    _loginLiveData.postValue(Resource.Success(user))
                }
            } catch (e: Exception) {
                _loginLiveData.postValue(Resource.Error(e))
                Log.e("LOGIN:", e.toString())
            }
        }
    }


    fun signUp(userName: String, name: String, lastName: String, password: String) {
        viewModelScope.launch {
            try {
                _signUpLiveData.postValue(Resource.Loading())

                withContext(Dispatchers.IO) {

                    val db = EldarWalletApp().getDatabase(getApplication())
                    val userInDatabase = db.getItemsDao().getUserByUsername(userName)

                    if (userInDatabase != null) {
                        throw Exception("User already exists")
                    }

                    val userId = db.getItemsDao().insertUser(
                        UserEntity(
                            userId = 0,
                            userName = userName,
                            name = lastName,
                            lastName = name,
                            password = password,
                            balance = 0
                        )
                    )

                    val user = User(
                        name = userName,
                        lastName = name,
                        userName = lastName,
                        password = password
                    ).apply {
                        balance = 0
                        id = userId
                    }
                    AppPreferences.setUser(user)
                    _signUpLiveData.postValue(Resource.Success(user))
                }
            } catch (e: Exception) {
                _signUpLiveData.postValue(Resource.Error(e))
            }
        }
    }


}
