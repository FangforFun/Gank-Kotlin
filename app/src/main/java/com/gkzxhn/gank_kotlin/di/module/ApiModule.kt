package com.gkzxhn.gank_kotlin.di.module

import android.content.Context
import android.util.Log
import com.gkzxhn.gank_kotlin.api.ApiService
import com.gkzxhn.gank_kotlin.api.GankApi
import com.gkzxhn.gank_kotlin.api.RetrofitClient
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.wingsofts.gankclient.di.module.AppModule
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.schedulers.Schedulers
import java.io.File

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

    @Provides fun provideKaiyanApi(context: Context) = RetrofitClient
            .getInstance(context, ApiService.BASE_URL)
            .retrofit!!.create(ApiService::class.java)

    @Provides fun provideOkhttp(context: Context, interceptor: HttpLoggingInterceptor): OkHttpClient {
        val cacheSize = 1024 * 1024 * 10L
        val cacheDir = File(context.cacheDir, "http")
        val cache = Cache(cacheDir, cacheSize)
        return OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(interceptor).build()
    }
    @Provides fun provideInterceptor() : HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor{
            msg -> Log.d("okhttp",msg)
        }
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}
