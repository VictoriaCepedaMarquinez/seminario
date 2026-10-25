package ar.edu.unicen.seminario.datos.remoto

import ar.edu.unicen.seminario.datos.modelo.DetallePelicula
import ar.edu.unicen.seminario.datos.modelo.RespuestaPeliculas
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiTmdb {

    @GET("movie/popular")
    suspend fun obtenerPopulares(): RespuestaPeliculas

    @GET("trending/movie/week")
    suspend fun obtenerTendencias(): RespuestaPeliculas

    @GET("search/movie")
    suspend fun buscarPeliculas(
        @Query("query") busqueda: String
    ): RespuestaPeliculas

    @GET("movie/{id_pelicula}")
    suspend fun obtenerDetalle(
        @Path("id_pelicula") idPelicula: Int
    ): DetallePelicula

    companion object {
        const val URL_BASE = "https://api.themoviedb.org/3/"
        const val URL_BASE_POSTER = "https://image.tmdb.org/t/p/w500"
        const val URL_BASE_FONDO = "https://image.tmdb.org/t/p/w780"

        fun urlPoster(rutaPoster: String?): String? =
            rutaPoster?.takeIf { it.isNotBlank() }?.let { URL_BASE_POSTER + it }

        fun urlFondo(rutaFondo: String?): String? =
            rutaFondo?.takeIf { it.isNotBlank() }?.let { URL_BASE_FONDO + it }
    }
}
