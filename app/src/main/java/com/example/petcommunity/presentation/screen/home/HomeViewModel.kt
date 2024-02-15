package com.example.petcommunity.presentation.screen.home

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.InstallIn
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {

    var list = mutableStateListOf<Int>()

    var text = mutableStateOf("")

    init {
        viewModelScope.launch {
            delay(2000)

            list.add(2)
            list.add(5)
            list.add(1)
            list.add(3)

        }

    }

    fun test() {
        list.clear()
        list.addAll(listOf(1,2,3,4,5,6))
    }


}