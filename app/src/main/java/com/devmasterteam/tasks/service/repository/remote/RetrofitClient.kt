package com.devmasterteam.tasks.service.repository.remote

import com.devmasterteam.tasks.service.model.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor(){


      //email: joao@emial.com
      //name: joao
      //password: 123456
/*    {
        "name": "joao",
        "token": "CrxcCyOaBGm5BVPZ9FlvY4EV0Tmq3vufmIQofaH/UFTziIRh4QIVVQ==",
        "personKey": "6rLlT9THdpiBFdE5qt77n5iEKH2h/1BU84iEYeECFVU="
    }*/

    companion object {

        private lateinit var INSTANCE:Retrofit
        private var token: String = ""
        private var personKey: String = ""

        private fun getRetrofitClient(): Retrofit{

            val httpClient = OkHttpClient.Builder()

            httpClient.addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {

                    val request = chain.request()
                        .newBuilder()
                        .addHeader(TaskConstants.HEADER.TOKEN_KEY, token)
                        .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                        .build()

                    return chain.proceed(request)
                }

            })

            if (!::INSTANCE.isInitialized){
                synchronized(RetrofitClient::class.java){
                    INSTANCE = Retrofit.Builder()
                        .baseUrl("http://devmasterteam.com/CursoAndroid/API/")
                        .client(httpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                }
            }
            return INSTANCE
        }

        fun <T>getService(serviceClass: Class<T>): T{
            return getRetrofitClient().create(serviceClass)
        }

        fun addHeaders(tokenStr: String, personKeyStr: String) {
            token = tokenStr
            personKey = personKeyStr
        }
    }

}