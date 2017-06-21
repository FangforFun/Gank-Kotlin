package com.gkzxhn.gank_kotlin.di.module

import com.gkzxhn.gank_kotlin.api.GankApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wingsofts.gankclient.di.module.AppModule
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers

/**
 *
 * Created by 方 on 2017/6/20.
 */

@Module(includes = arrayOf(AppModule::class))
class ApiModule {

    @Provides fun provideRetrofit(baseUrl: HttpUrl, client: OkHttpClient, gson: Gson) =
            Retrofit.Builder()
                    .client(client)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()

    @Provides fun provideBaseUrl() = HttpUrl.parse("http://gank.io/api/")

    @Provides fun provideGson() = GsonBuilder().create()

    @Provides fun provideApi(retrofit: Retrofit) = retrofit.create(GankApi::class.java)
}
