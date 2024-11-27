package com.android.infosystest

import retrofit2.http.GET

interface ApiInterface {
    @GET("todos")
    suspend fun getItem(): TodoItem
}