package ar.edu.unicen.seminario.datos.modelo

import com.google.gson.annotations.SerializedName

data class DetallePelicula(
    val id: Int = 0,
    @SerializedName("title") val titulo: String = "",
    @SerializedName("overview") val sinopsis: String? = null,
    @SerializedName("poster_path") val rutaPoster: String? = null,
    @SerializedName("backdrop_path") val rutaFondo: String? = null,
    @SerializedName("release_date") val fechaEstreno: String? = null,
    @SerializedName("vote_average") val puntuacion: Double = 0.0,
    @SerializedName("vote_count") val cantidadVotos: Int = 0,
    @SerializedName("runtime") val duracion: Int? = null,
    @SerializedName("genres") val generos: List<Genero>? = null
)

data class Genero(
    val id: Int = 0,
    @SerializedName("name") val nombre: String = ""
)
