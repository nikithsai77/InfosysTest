package com.android.infosystest

sealed class Resource {
    data class Success(val todoItem: TodoItem): Resource()
    data class Error(val error: String) : Resource()
    object Loading: Resource()
}
