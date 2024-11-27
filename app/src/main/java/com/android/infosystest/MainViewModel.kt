package com.android.infosystest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val apiUseCase: ApiUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow<Resource>(Resource.Loading)
    val uiState: StateFlow<Resource> = _uiState.asStateFlow()

    init {
        getItems()
    }

   private fun getItems() {
       apiUseCase().onEach { newState ->
           _uiState.update {
               newState
           }
       }.launchIn(viewModelScope)
   }

   fun setItem(onClick: OnClick) {
       when(onClick) {
           is OnClick.Retry -> getItems()
           is OnClick.Insert -> insert(onClick.todo)
           else -> {}
       }
   }

    private fun insert(todo: Todo) {
        _uiState.update {
            if (it is Resource.Success) {
                val al = it.todoItem.todos as ArrayList<Todo>
                al.add(todo)
            }
            it
        }
    }

}