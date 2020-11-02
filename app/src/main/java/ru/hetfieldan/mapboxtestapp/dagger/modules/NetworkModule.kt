package ru.hetfieldan.mapboxtestapp.dagger.modules

import dagger.Module
import dagger.Provides
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.hetfieldan.mapboxtestapp.data.network.CarsAPI
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideCarsAPI(): CarsAPI = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .client(OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .writeTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(5000, TimeUnit.MILLISECONDS)
            .connectionPool(ConnectionPool(5, 5, TimeUnit.SECONDS))
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CarsAPI::class.java)
}
