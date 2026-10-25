package ar.edu.unicen.seminario.datos.error

import ar.edu.unicen.seminario.datos.remoto.ExcepcionSinConexion
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

enum class ErrorApp {
    SIN_INTERNET,
    TIEMPO_AGOTADO,
    RED,
    NO_AUTORIZADO,
    NO_ENCONTRADO,
    SERVIDOR,
    DATOS_INVALIDOS,
    DESCONOCIDO
}

fun Throwable.convertirError(): ErrorApp = when (this) {
    is ExcepcionSinConexion -> ErrorApp.SIN_INTERNET
    is SocketTimeoutException -> ErrorApp.TIEMPO_AGOTADO
    is UnknownHostException -> ErrorApp.RED
    is IOException -> ErrorApp.RED
    is HttpException -> when (code()) {
        401, 403 -> ErrorApp.NO_AUTORIZADO
        404 -> ErrorApp.NO_ENCONTRADO
        in 500..599 -> ErrorApp.SERVIDOR
        else -> ErrorApp.DESCONOCIDO
    }
    is JsonParseException -> ErrorApp.DATOS_INVALIDOS
    else -> ErrorApp.DESCONOCIDO
}
