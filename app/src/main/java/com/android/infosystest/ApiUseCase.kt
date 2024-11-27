package com.android.infosystest

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class ApiUseCase(private val apiRepository: ApiRepository) {

    operator fun invoke(): Flow<Resource> = flow {
        emit(Resource.Loading)
        try {
            val list = apiRepository.getItem()
            withContext(Dispatchers.Default) {
                list.todos.sortedBy {
                    ensureActive()
                    it.todo.first()
                }
            }
            emit(Resource.Success(todoItem = list))
        } catch (e: Exception) {
            if (e is CancellationException) throw CancellationException()
            e.printStackTrace()
            emit(Resource.Error(error = "SomeThing Went Wrong Try Again Later!"))
        }
    }

}