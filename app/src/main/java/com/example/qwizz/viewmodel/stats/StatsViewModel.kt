package com.example.qwizz.viewmodel.stats

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwizz.data.control.StatsControl
import com.example.qwizz.data.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class StatsViewModel: ViewModel() {
    private val statsControl = StatsControl()

    private val _userStats = MutableStateFlow<User?>(null)
    val userStats: StateFlow<User?> = _userStats

    val isLoading = mutableStateOf(false)


    private var _state = MutableStateFlow<StatsState>(StatsState.Initial)
    val state: StateFlow<StatsState> = _state

    fun getDataUser(userId: String){
        isLoading.value = true
        _state.value = StatsState.Loading
        viewModelScope.launch {
            try {
                val user = statsControl.getDataUser(userId)
                _userStats.value = user
                isLoading.value = false
                _state.value = StatsState.StatsSaved
            } catch (e: Exception) {
                isLoading.value = false
                _state.value = StatsState.Error(e.message ?: "Unknown error")
            }
        }
    }






}
sealed class StatsState{
    object Initial: StatsState()
    object Loading: StatsState()
    object StatsSaved: StatsState()
    object StatsNotSaved: StatsState()
    data class Error(val message: String): StatsState()
}