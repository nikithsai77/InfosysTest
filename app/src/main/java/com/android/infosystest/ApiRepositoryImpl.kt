package com.android.infosystest

class ApiRepositoryImpl(private val apiInterface: ApiInterface) : ApiRepository{

    override suspend fun getItem(): TodoItem {
        return apiInterface.getItem()
    }

}
