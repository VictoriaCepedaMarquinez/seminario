package ar.edu.unicen.seminario.ui.comun

import android.widget.ImageView
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.datos.remoto.ApiTmdb
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.cargarPoster(rutaPoster: String?) {
    Glide.with(this)
        .load(ApiTmdb.urlPoster(rutaPoster))
        .placeholder(R.drawable.ic_pelicula_generica)
        .error(R.drawable.ic_pelicula_generica)
        .fallback(R.drawable.ic_pelicula_generica)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.cargarFondo(rutaFondo: String?, rutaPoster: String?) {
    val direccion = ApiTmdb.urlFondo(rutaFondo) ?: ApiTmdb.urlPoster(rutaPoster)
    Glide.with(this)
        .load(direccion)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}
