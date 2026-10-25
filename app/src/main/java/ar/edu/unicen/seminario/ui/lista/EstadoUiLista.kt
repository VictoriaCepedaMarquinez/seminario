package ar.edu.unicen.seminario.ui.lista

import ar.edu.unicen.seminario.datos.error.ErrorApp
import ar.edu.unicen.seminario.datos.modelo.Pelicula

sealed interface EstadoUiLista {
    data object Cargando : EstadoUiLista
    data class Exito(val peliculas: List<Pelicula>) : EstadoUiLista
    data class Fallo(val error: ErrorApp) : EstadoUiLista
}
