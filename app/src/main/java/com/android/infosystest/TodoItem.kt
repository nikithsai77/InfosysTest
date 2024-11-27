package com.android.infosystest

data class TodoItem(
    val limit: Int,
    val skip: Int,
    val todos: List<Todo>,
    val total: Int
)