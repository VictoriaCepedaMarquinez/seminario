package ar.edu.unicen.seminario.ui.comun

import androidx.annotation.StringRes
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.datos.error.ErrorApp

@StringRes
fun ErrorApp.mensaje(): Int = when (this) {
    ErrorApp.SIN_INTERNET -> R.string.error_sin_internet
    ErrorApp.TIEMPO_AGOTADO -> R.string.error_tiempo_agotado
    ErrorApp.RED -> R.string.error_red
    ErrorApp.NO_AUTORIZADO -> R.string.error_no_autorizado
    ErrorApp.NO_ENCONTRADO -> R.string.error_no_encontrado
    ErrorApp.SERVIDOR -> R.string.error_servidor
    ErrorApp.DATOS_INVALIDOS -> R.string.error_datos_invalidos
    ErrorApp.DESCONOCIDO -> R.string.error_desconocido
}
