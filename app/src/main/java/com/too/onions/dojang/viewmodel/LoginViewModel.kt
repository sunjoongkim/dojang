package com.too.onions.dojang.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.too.onions.dojang.db.data.User
import com.too.onions.dojang.db.repo.DojangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: DojangRepository): ViewModel() {

    var currentUser: MutableState<User> = mutableStateOf(User())


    // ==== User ====
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    fun getUser(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(repository.getUser(email))
        }
    }
    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUser(user)
        }
    }

}
