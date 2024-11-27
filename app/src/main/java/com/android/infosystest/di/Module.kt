package com.android.infosystest.di

import com.android.infosystest.ApiUseCase
import com.android.infosystest.ApiInterface
import com.android.infosystest.ApiRepository
import com.android.infosystest.ApiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideRetrofitInstance(): ApiInterface {
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }


    @Singleton
    @Provides
    fun provideApiRepository(apiInterface: ApiInterface) : ApiRepository {
        return ApiRepositoryImpl(apiInterface)
    }

    @Singleton
    @Provides
    fun provideAPiUSeCase(apiRepository: ApiRepository) : ApiUseCase {
        return ApiUseCase(apiRepository)
    }

}