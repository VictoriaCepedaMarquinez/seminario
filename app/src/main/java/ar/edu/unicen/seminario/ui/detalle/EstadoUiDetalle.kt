package ar.edu.unicen.seminario.ui.detalle

import ar.edu.unicen.seminario.datos.error.ErrorApp
import ar.edu.unicen.seminario.datos.modelo.DetallePelicula

sealed interface EstadoUiDetalle {
    data object Cargando : EstadoUiDetalle
    data class Exito(val pelicula: DetallePelicula) : EstadoUiDetalle
    data class Fallo(val error: ErrorApp) : EstadoUiDetalle
}
