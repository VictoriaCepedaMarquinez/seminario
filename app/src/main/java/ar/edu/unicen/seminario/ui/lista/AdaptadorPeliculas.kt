package ar.edu.unicen.seminario.ui.lista

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.ItemPeliculaBinding
import ar.edu.unicen.seminario.datos.modelo.Pelicula
import ar.edu.unicen.seminario.ui.comun.obtenerAnio
import ar.edu.unicen.seminario.ui.comun.cargarPoster

class AdaptadorPeliculas(private val alTocar: (Pelicula) -> Unit) :
    ListAdapter<Pelicula, AdaptadorPeliculas.VistaPelicula>(ComparadorPeliculas) {

    override fun onCreateViewHolder(padre: ViewGroup, tipoVista: Int): VistaPelicula {
        val item = ItemPeliculaBinding.inflate(LayoutInflater.from(padre.context), padre, false)
        return VistaPelicula(item)
    }

    override fun onBindViewHolder(vista: VistaPelicula, posicion: Int) {
        val pelicula = getItem(posicion)
        vista.mostrar(pelicula)
        vista.itemView.setOnClickListener { alTocar(pelicula) }
    }

    class VistaPelicula(private val item: ItemPeliculaBinding) :
        RecyclerView.ViewHolder(item.root) {

        fun mostrar(pelicula: Pelicula) {
            val contexto = item.root.context
            item.textoTitulo.text = pelicula.titulo
            item.textoPuntuacion.text =
                contexto.getString(R.string.puntuacion_corta, pelicula.puntuacion)
            val anio = obtenerAnio(pelicula.fechaEstreno)
            item.textoAnio.isVisible = anio != null
            item.textoAnio.text = anio
            item.textoSinopsis.text = pelicula.sinopsis
            item.imagenPoster.contentDescription =
                contexto.getString(R.string.descripcion_poster, pelicula.titulo)
            item.imagenPoster.cargarPoster(pelicula.rutaPoster)
        }
    }

    object ComparadorPeliculas : DiffUtil.ItemCallback<Pelicula>() {
        override fun areItemsTheSame(anterior: Pelicula, nueva: Pelicula) = anterior.id == nueva.id
        override fun areContentsTheSame(anterior: Pelicula, nueva: Pelicula) = anterior == nueva
    }
}
