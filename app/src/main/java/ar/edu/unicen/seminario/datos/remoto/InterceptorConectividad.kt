package ar.edu.unicen.seminario.datos.remoto

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ExcepcionSinConexion : IOException("Sin conexión a internet")

class InterceptorConectividad(contexto: Context) : Interceptor {

    private val conectividad =
        contexto.applicationContext.getSystemService(ConnectivityManager::class.java)

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!hayConexion()) throw ExcepcionSinConexion()
        return chain.proceed(chain.request())
    }

    private fun hayConexion(): Boolean {
        val red = conectividad?.activeNetwork ?: return false
        val capacidades = conectividad.getNetworkCapabilities(red) ?: return false
        return capacidades.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}
