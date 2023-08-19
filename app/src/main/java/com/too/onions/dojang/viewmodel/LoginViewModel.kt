package com.too.onions.dojang.viewmodel

import android.provider.ContactsContract.CommonDataKinds.Nickname
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.too.onions.dojang.db.data.User
import com.too.onions.dojang.db.repo.DojangRepository
import com.too.onions.dojang.service.MainService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: DojangRepository): ViewModel() {


    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _isNeedJoin = MutableLiveData<Boolean?>()
    val isNeedJoin: LiveData<Boolean?> get() = _isNeedJoin

    fun resetInNeedJoin(isNeed: Boolean? = null) {
        _isNeedJoin.postValue(isNeed)
    }

    fun checkUser(user: FirebaseUser) {
        viewModelScope.launch(Dispatchers.IO) {
            val retrievedUser = repository.getUser(user?.email ?: "")

            if (retrievedUser == null) {
                _isNeedJoin.postValue(true)
            } else {
                _isNeedJoin.postValue(false)
                MainService.getInstance()?.setUser(retrievedUser)
            }

            setUser(user)
        }
    }
    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

    fun confirmJoin(nickname: String) {
        val confirmUser = _user.value?.copy(nickname = nickname)

        if (confirmUser != null) {
            _user.postValue(confirmUser)
            insertUser(confirmUser)
        }
    }

    private fun setUser(user: FirebaseUser) {
        val connectedUser = User(
            email = user.email ?: "",
            nickname = "",
            uuid = user.uid,
            stamp = ""
        )

        _user.postValue(connectedUser)
    }

}