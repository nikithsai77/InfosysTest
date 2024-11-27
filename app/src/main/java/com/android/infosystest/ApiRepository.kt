package com.android.infosystest

interface ApiRepository {
    suspend fun getItem(): TodoItem
}