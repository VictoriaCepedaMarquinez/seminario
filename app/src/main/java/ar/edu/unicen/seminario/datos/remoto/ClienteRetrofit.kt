package ar.edu.unicen.seminario.datos.remoto

import android.content.Context
import ar.edu.unicen.seminario.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Locale
import java.util.concurrent.TimeUnit

object ClienteRetrofit {

    fun crear(contexto: Context): ApiTmdb {
        val cliente = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(InterceptorConectividad(contexto))
            .addInterceptor { peticion ->
                val direccion = peticion.request().url.newBuilder()
                    .addQueryParameter("language", idiomaActual())
                    .build()
                val nuevaPeticion = peticion.request().newBuilder()
                    .url(direccion)
                    .addHeader("Authorization", "Bearer ${BuildConfig.TMDB_READ_ACCESS_TOKEN}")
                    .addHeader("accept", "application/json")
                    .build()
                peticion.proceed(nuevaPeticion)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            })
            .build()

        return Retrofit.Builder()
            .baseUrl(ApiTmdb.URL_BASE)
            .client(cliente)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiTmdb::class.java)
    }

    private fun idiomaActual(): String =
        if (Locale.getDefault().language == "es") "es-ES" else "en-US"
}
