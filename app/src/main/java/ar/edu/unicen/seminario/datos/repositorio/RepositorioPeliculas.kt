package ar.edu.unicen.seminario.datos.repositorio

import ar.edu.unicen.seminario.datos.error.ErrorApp
import ar.edu.unicen.seminario.datos.error.convertirError
import ar.edu.unicen.seminario.datos.modelo.DetallePelicula
import ar.edu.unicen.seminario.datos.modelo.Pelicula
import ar.edu.unicen.seminario.datos.modelo.TipoListaPeliculas
import ar.edu.unicen.seminario.datos.remoto.ApiTmdb
import kotlinx.coroutines.CancellationException

sealed interface ResultadoDatos<out T> {
    data class Exito<T>(val datos: T) : ResultadoDatos<T>
    data class Fallo(val error: ErrorApp) : ResultadoDatos<Nothing>
}

class RepositorioPeliculas(private val api: ApiTmdb) {

    suspend fun obtenerPeliculas(
        tipo: TipoListaPeliculas,
        busqueda: String?
    ): ResultadoDatos<List<Pelicula>> =
        try {
            val respuesta = when {
                tipo == TipoListaPeliculas.TENDENCIAS -> api.obtenerTendencias()
                busqueda.isNullOrBlank() -> api.obtenerPopulares()
                else -> api.buscarPeliculas(busqueda = busqueda)
            }
            ResultadoDatos.Exito(respuesta.resultados)
        } catch (excepcion: CancellationException) {
            throw excepcion
        } catch (excepcion: Exception) {
            ResultadoDatos.Fallo(excepcion.convertirError())
        }

    suspend fun obtenerDetalle(idPelicula: Int): ResultadoDatos<DetallePelicula> =
        try {
            ResultadoDatos.Exito(api.obtenerDetalle(idPelicula))
        } catch (excepcion: CancellationException) {
            throw excepcion
        } catch (excepcion: Exception) {
            ResultadoDatos.Fallo(excepcion.convertirError())
        }
}
