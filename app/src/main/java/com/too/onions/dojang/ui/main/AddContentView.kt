package com.too.onions.dojang.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.too.onions.dojang.viewmodel.MainViewModel

@Composable
fun AddContentView(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    TitleBar(viewModel)
}