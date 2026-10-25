package ar.edu.unicen.seminario.datos.modelo

import com.google.gson.annotations.SerializedName

data class Pelicula(
    val id: Int = 0,
    @SerializedName("title") val titulo: String = "",
    @SerializedName("overview") val sinopsis: String? = null,
    @SerializedName("poster_path") val rutaPoster: String? = null,
    @SerializedName("release_date") val fechaEstreno: String? = null,
    @SerializedName("vote_average") val puntuacion: Double = 0.0
)

data class RespuestaPeliculas(
    @SerializedName("results") val resultados: List<Pelicula> = emptyList()
)
