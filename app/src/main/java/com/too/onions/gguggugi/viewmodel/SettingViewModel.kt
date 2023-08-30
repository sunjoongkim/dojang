package com.too.onions.gguggugi.viewmodel

import androidx.lifecycle.ViewModel
import com.too.onions.gguggugi.db.repo.DojangRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val repository: DojangRepository): ViewModel() {




}
