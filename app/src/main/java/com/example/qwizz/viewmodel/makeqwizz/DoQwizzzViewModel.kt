package com.example.qwizz.viewmodel.makeqwizz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwizz.data.control.QwizzControl
import com.example.qwizz.data.model.Qwizzz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoQwizzzViewModel: ViewModel() {
    private val qwizzControl = QwizzControl()
    private val _quizList = MutableStateFlow<List<Qwizzz>>(emptyList())
    val quizList: StateFlow<List<Qwizzz>> = _quizList

    private val _doQwizzzState = MutableStateFlow<DoQwizzzState>(DoQwizzzState.Initial)
    val doQwizzzState: StateFlow<DoQwizzzState> = _doQwizzzState


    fun fetchQuizzes() {
        Log.d("DoQwizzzViewModel", "Fetching quizzes...")
        viewModelScope.launch {
            _doQwizzzState.value = DoQwizzzState.Loading

            try {

                val result = qwizzControl.getQwizzz()
                _quizList.value = result

                if (result.isNotEmpty()) {
                    _doQwizzzState.value = DoQwizzzState.Initial
                    Log.d("DoQwizzzViewModel", "Fetched quizzes successfully: ${result.size}")
                } else {
                    _doQwizzzState.value = DoQwizzzState.Error("No quizzes found.")
                    Log.d("DoQwizzzViewModel", "No quizzes found.")
                }

            } catch (e: Exception) {

                _doQwizzzState.value = DoQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("DoQwizzzViewModel", "Error fetching quizzes: ${e.localizedMessage}", e)
            }
        }
        }



}

sealed class DoQwizzzState{
    object Initial: DoQwizzzState()
    object Loading: DoQwizzzState()
    object QwizzzSaved: DoQwizzzState()
    object QwizzzNotSaved: DoQwizzzState()
    data class Error(val message: String): DoQwizzzState()
}
