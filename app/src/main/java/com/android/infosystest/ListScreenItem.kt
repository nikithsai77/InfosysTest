@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.android.infosystest

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun TopAppBar() {
    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(MaterialTheme.colorScheme.primary)
        .padding(16.dp)
    ) {
        Text(text = LocalContext.current.getString(R.string.app_name), modifier = Modifier.fillMaxWidth(), color = MaterialTheme.colorScheme.onPrimary)
    }
}

@Composable
fun ListItem(resource: Resource, item: () -> Unit, add: (Todo) -> Unit) {
    when(resource) {
        is Resource.Success -> DisplayData(resource.todoItem, add)
        is Resource.Error -> Failed(resource.error, item)
        is Resource.Loading -> Loading()
    }
}

@Composable
fun DisplayData(todoItem: TodoItem, add: (Todo) -> Unit) {
    Scaffold(topBar = { TopAppBar() }) { contentPadding ->
        val controller = rememberNavController()
        Column(modifier = Modifier.fillMaxSize().padding(contentPadding)) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                item(key = 3) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { controller.navigate(route = "a") }) {
                        Text(text = "Todo")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { controller.navigate(route = "b") }) {
                        Text(text = "Display Pending Task")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { controller.navigate(route = "c") }) {
                        Text(text = "Add New Task")
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            NavHost(navController = controller, startDestination = "a", modifier = Modifier.fillMaxSize()) {
                composable(route = "a") {
                    Success(todoItem.todos)
                }
                composable(route = "b") {
                    Success(list = todoItem.todos.filter { item ->
                        !item.completed
                    })
                }
                composable(route = "c") {
                    AddText(add, todoItem.todos.size)
                }
            }
        }
    }
}

@Composable
fun AddText(add: (Todo) -> Unit, size: Int) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = CenterHorizontally) {
        var state by remember {
            mutableStateOf(EnterDetail(completed="", todo = "", userId = ""))
        }
        OutlinedTextField(value = state.todo, onValueChange = {
            state = state.copy(todo = it)
        }, maxLines = 1, label = {
            Text(text = "Enter The Description")
        })
        OutlinedTextField(value = state.completed, onValueChange = {
            state = state.copy(completed = it)
        }, maxLines = 1, label = {
            Text(text = "Fill Either True or False")
        }, singleLine = true)
        OutlinedTextField(value = state.userId, onValueChange = {
            state = state.copy(userId = it)
        }, maxLines = 1, label = {
            Text(text = "Enter UserId")
        }, singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = {
            if (state.completed.isNotBlank() && state.todo.isNotBlank() && state.userId.isNotBlank()) {
                add.invoke(
                    Todo(
                        completed = state.completed.toBoolean(),
                        todo = state.todo,
                        id = size + 1,
                        userId = state.userId.toInt()
                    )
                )
                state = state.copy(completed = "", todo = "", userId = "")
            }
            else Toast.makeText(context, "All field are compulsory", Toast.LENGTH_SHORT).show()
        }) {
            Text(text = "Submit")
        }
    }
}

@Composable
fun Success(list: List<Todo>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(list, key = { it.id }) {
            Text(text = "Id: ${it.id}")
            Text(text = "todo: ${it.todo}", maxLines = 1, overflow = TextOverflow.Ellipsis)
            Text(text = "completed: ${it.completed}")
            Text(text = "userId: ${it.userId}")
            Divider()
        }
    }
}

@Composable
fun Loading() {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Please wait it's Loading...")
    }
}

@Composable
fun Failed(error: String, item: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = CenterHorizontally) {
        Text(text = error)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { item() }) {
            Text(text = "Retry")
        }
    }
}