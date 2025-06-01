package com.example.qwizz.viewmodel.editqwizz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.qwizz.data.control.QwizzControl
import com.example.qwizz.data.model.AnswerOption
import com.example.qwizz.data.model.QuizQuestion
import com.example.qwizz.data.model.Qwizzz
import com.example.qwizz.ui.editqwizz.EditQwizzz
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EditQwizzzViewModel : ViewModel() {
    private val qwizzControl = QwizzControl()

    private val _editQwizzzState = MutableStateFlow<EditQwizzzState>(EditQwizzzState.Initial)
    val editQwizzzState: StateFlow<EditQwizzzState> = _editQwizzzState

    private val _quizList = MutableStateFlow<List<Qwizzz>>(emptyList())
    val quizList: StateFlow<List<Qwizzz>> = _quizList

    private val _currentQwizzz = MutableStateFlow<Qwizzz?>(null)
    val currentQwizzz: StateFlow<Qwizzz?> = _currentQwizzz

    init {
        observeQuizList()
    }

    private fun observeQuizList() {
        viewModelScope.launch {
            qwizzControl.observeQwizzz()
                .collect { qwizzz ->
                    _quizList.value = qwizzz.sortedBy { it.title }
                }
        }
    }


    fun fetchQuizzesAvailables() {
        viewModelScope.launch {
            _editQwizzzState.value = EditQwizzzState.Loading
            try {
                val result = qwizzControl.getQwizzzAvailable()
                _quizList.value = result.sortedBy { it.title }
                if (result.isNotEmpty()) {
                    _editQwizzzState.value = EditQwizzzState.QwizzzSaved
                    Log.d("EditQwizzzViewModel", "Fetched quizzes successfully: ${result.size}")
                } else {
                    _editQwizzzState.value = EditQwizzzState.QwizzzNotSaved
                    Log.d("EditQwizzzViewModel", "No quizzes found.")
                }
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("EditQwizzzViewModel", "Error fetching quizzes: ${e.localizedMessage}", e)
            }
        }

    }


    fun updateCurrentAvailableQwizzz(qwizzz: Qwizzz) {
        viewModelScope.launch {
            try {
                _currentQwizzz.value = qwizzz
                Log.d("EditQwizzzViewModel", "Current qwizzz updated successfully: ${qwizzz.title}")
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e(
                    "EditQwizzzViewModel",
                    "Error updating current qwizzz: ${e.localizedMessage}",
                    e
                )
            }
        }

    }

    fun updateTitleQwizzz(title: String) {
        viewModelScope.launch {
            try {
                val currentQwizzzValue = _currentQwizzz.value
                if (currentQwizzzValue == null) {
                    Log.w("EditQwizzzViewModel", "Current Qwizzz is null")
                    return@launch
                }
                _currentQwizzz.value = currentQwizzzValue.copy(title = title)
                Log.d("EditQwizzzViewModel", "Title updated successfully: $title")
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("EditQwizzzViewModel", "Error updating title: ${e.localizedMessage}", e)

            }
        }
    }

    fun updateQwizQuestion(questionIndex: Int, question: String) {
        viewModelScope.launch {
            try {
                val currentList = _currentQwizzz.value?.question?.toMutableList() ?: mutableListOf()
                val updatedQuestion = currentList[questionIndex].copy(questionText = question)
                currentList[questionIndex] = updatedQuestion
                _currentQwizzz.value = _currentQwizzz.value?.copy(question = currentList)
                Log.d("EditQwizzzViewModel", "Question updated successfully: $question")
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("EditQwizzzViewModel", "Error updating question: ${e.localizedMessage}", e)
            }

        }
    }

    fun updateAnswerText(questionIndex: Int, answerIndex: Int, answer: String) {
        viewModelScope.launch {
            try {
                val currentList = _currentQwizzz.value?.question?.toMutableList() ?: mutableListOf()
                val updatedQuestion = currentList[questionIndex]
                val updatedOptions = updatedQuestion.options.toMutableList()
                updatedOptions[answerIndex] = updatedOptions[answerIndex].copy(text = answer)
                val updatedCurrentQuestion = updatedQuestion.copy(options = updatedOptions)
                currentList[questionIndex] = updatedCurrentQuestion
                _currentQwizzz.value = _currentQwizzz.value?.copy(question = currentList)
                Log.d("EditQwizzzViewModel", "Answer updated successfully: $answer")
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.d("EditQwizzzViewModel", "Error updating answer: ${e.localizedMessage}", e)

            }
        }
    }

    fun updateAnswerCorrect(questionIndex: Int, selectedOptionIndex: Int) {
        viewModelScope.launch {
            try {
                val currentQwizzzValue = _currentQwizzz.value
                if (currentQwizzzValue == null) {
                    Log.w("EditQwizzzViewModel", "Current Qwizzz is null")
                    return@launch
                }

                val updatedQuestions = currentQwizzzValue.question.toMutableList()
                if (questionIndex !in updatedQuestions.indices) {
                    Log.w("EditQwizzzViewModel", "Invalid question index")
                    return@launch
                }

                val question = updatedQuestions[questionIndex]
                val updatedOptions = question.options.mapIndexed { index, option ->
                    option.copy(correct = index == selectedOptionIndex)
                }

                val updatedQuestion = question.copy(options = updatedOptions)
                updatedQuestions[questionIndex] = updatedQuestion

                _currentQwizzz.value = currentQwizzzValue.copy(question = updatedQuestions)

                Log.d(
                    "EditQwizzzViewModel",
                    "Correct answer updated for question[$questionIndex]: option[$selectedOptionIndex]"
                )
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.d("EditQwizzzViewModel", "Error updating correct: ${e.localizedMessage}", e)
            }
        }
    }

    fun deleteQuestionAt(quizIndex: Int, questionIndex: Int) {
        viewModelScope.launch {
            try {
                val current = _currentQwizzz.value
                if (current == null) {
                    Log.w("EditQwizzzViewModel", "Current quiz is null")
                    return@launch
                }

                val updatedQuestions = current.question.toMutableList()

                if (questionIndex !in updatedQuestions.indices) {
                    Log.w("EditQwizzzViewModel", "Invalid question index: $questionIndex")
                    return@launch
                }

                updatedQuestions.removeAt(questionIndex)

                _currentQwizzz.value = current.copy(question = updatedQuestions)

                Log.d(
                    "EditQwizzzViewModel",
                    "Question at index $questionIndex deleted from current quiz"
                )
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.d("EditQwizzzViewModel", "Error deleting question: ${e.localizedMessage}", e)
            }
        }
    }

    fun addEmptyQwizzzQuestion() {
        viewModelScope.launch {
            try {
                val current = _currentQwizzz.value

                if (current == null) {
                    Log.w("EditQwizzzViewModel", "Current quiz is null, cannot add question")
                    return@launch
                }

                val emptyOptions = List(4) { AnswerOption() }

                val newQuestion = QuizQuestion(
                    questionText = "",
                    options = emptyOptions
                )

                val updatedQuestions = current.question.toMutableList().apply {
                    add(newQuestion)
                }

                _currentQwizzz.value = current.copy(question = updatedQuestions)

                Log.d("EditQwizzzViewModel", "Question added successfully to current Qwizzz")
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.d("EditQwizzzViewModel", "Error adding question: ${e.localizedMessage}", e)
            }
        }
    }

    fun deletedQwizzz(qwizzz: Qwizzz?) {
        viewModelScope.launch {
            try {
                val id = qwizzControl.getIdQwizzz(qwizzz)
                val result = qwizzControl.deleteQwizzzById(id)
                if (result) {
                    _editQwizzzState.value = EditQwizzzState.QwizzzSaved
                    Log.d("EditQwizzzViewModel", "Qwizzz deleted successfully")
                } else {
                    _editQwizzzState.value = EditQwizzzState.QwizzzNotSaved
                    Log.d("EditQwizzzViewModel", "Qwizzz not deleted")
                }
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("EditQwizzzViewModel", "Error deleting qwizzz: ${e.localizedMessage}", e)
            }
        }
    }

    fun updateTime(time : Int){
        viewModelScope.launch {
            _currentQwizzz.value = _currentQwizzz.value?.copy(timeQuiz = time)
        }
    }

    fun updateQwizzz(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val currentQwizzzValue = _currentQwizzz.value
                if (currentQwizzzValue == null) {
                    Log.w("EditQwizzzViewModel", "Current Qwizzz is null")
                    _editQwizzzState.value = EditQwizzzState.QwizzzNotSaved
                    onResult(false)
                    return@launch
                }
                val id = qwizzControl.getIdQwizzz(currentQwizzzValue)
                val result = qwizzControl.updateQwizzz(id, currentQwizzzValue)
                if (result) {
                    _editQwizzzState.value = EditQwizzzState.QwizzzSaved
                    Log.d("EditQwizzzViewModel", "Qwizzz updated successfully")
                    onResult(true)
                } else {
                    _editQwizzzState.value = EditQwizzzState.QwizzzNotSaved
                    Log.d("EditQwizzzViewModel", "Qwizzz not updated")
                    onResult(false)
                }
            } catch (e: Exception) {
                _editQwizzzState.value =
                    EditQwizzzState.Error(e.localizedMessage ?: "Unknown error")
                Log.e("EditQwizzzViewModel", "Error updating qwizzz: ${e.localizedMessage}", e)
                onResult(false)
            }
        }
    }


    fun isQuizEmpty(): Boolean {
        val questions = _currentQwizzz.value?.question ?: return true
        return questions.none { question ->
            question.questionText.isNotBlank() || question.options.any { it.text.isNotBlank() }
        }
    }

    fun isValidQuiz(): Boolean {
        val quiz = _currentQwizzz.value ?: return false
        for ((index, question) in quiz.question.withIndex()) {
            if (question.questionText.isBlank()) {
                Log.d("VALIDASI", "Pertanyaan ke-$index kosong")
                return false
            }
            if (question.options.map { it.text }.distinct().size != question.options.size) {
                Log.d("VALIDASI", "Opsi jawaban di pertanyaan ke-$index ada yang duplikat")
                return false
            }
            if (!question.options.any { it.correct }) {
                Log.d("VALIDASI", "Tidak ada jawaban yang benar di pertanyaan ke-$index")
                return false
            }
        }
        return true
    }

}

sealed class EditQwizzzState {
    object Initial : EditQwizzzState()
    object Loading : EditQwizzzState()
    object QwizzzSaved : EditQwizzzState()
    object QwizzzNotSaved : EditQwizzzState()
    data class Error(val message: String) : EditQwizzzState()
}