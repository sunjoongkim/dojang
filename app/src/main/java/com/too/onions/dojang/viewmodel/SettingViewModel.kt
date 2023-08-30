package com.too.onions.dojang.viewmodel

import androidx.lifecycle.ViewModel
import com.too.onions.dojang.db.repo.DojangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repository: DojangRepository): ViewModel() {




}
