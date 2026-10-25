package ar.edu.unicen.seminario.ui.detalle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.FragmentoDetallePeliculaBinding
import ar.edu.unicen.seminario.datos.modelo.DetallePelicula
import ar.edu.unicen.seminario.ui.comun.cargarFondo
import ar.edu.unicen.seminario.ui.comun.cargarPoster
import ar.edu.unicen.seminario.ui.comun.formatearFecha
import ar.edu.unicen.seminario.ui.comun.mensaje
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class FragmentoDetallePelicula : Fragment() {

    private var _pantalla: FragmentoDetallePeliculaBinding? = null
    private val pantalla get() = _pantalla!!

    private val modelo: DetallePeliculaViewModel by viewModels { DetallePeliculaViewModel.Fabrica }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _pantalla = FragmentoDetallePeliculaBinding.inflate(inflater, container, false)
        return pantalla.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelo.cambiarIdioma(resources.configuration.locales[0].language)

        pantalla.vistaError.botonReintentar.setOnClickListener { modelo.cargarPelicula() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                modelo.estado.collect { mostrarEstado(it) }
            }
        }
    }

    private fun mostrarEstado(estado: EstadoUiDetalle) {
        pantalla.progresoDetalle.isVisible = estado is EstadoUiDetalle.Cargando
        pantalla.contenidoDetalle.isVisible = estado is EstadoUiDetalle.Exito
        pantalla.vistaError.root.isVisible = estado is EstadoUiDetalle.Fallo

        when (estado) {
            is EstadoUiDetalle.Exito -> mostrarPelicula(estado.pelicula)
            is EstadoUiDetalle.Fallo -> pantalla.vistaError.textoMensajeError.setText(estado.error.mensaje())
            EstadoUiDetalle.Cargando -> Unit
        }
    }

    private fun mostrarPelicula(pelicula: DetallePelicula) {
        val fechaEstreno = formatearFecha(pelicula.fechaEstreno)
            ?: getString(R.string.no_disponible)
        val duracion = pelicula.duracion ?: 0

        pantalla.textoTituloDetalle.text = pelicula.titulo
        pantalla.textoPuntuacionDetalle.text = getString(R.string.puntuacion_detalle, pelicula.puntuacion)
        pantalla.textoVotosDetalle.text = resources.getQuantityString(
            R.plurals.cantidad_votos, pelicula.cantidadVotos, pelicula.cantidadVotos
        )
        pantalla.textoFechaEstrenoDetalle.text = fechaEstreno
        pantalla.textoDuracionDetalle.isVisible = duracion > 0
        pantalla.textoDuracionDetalle.text = formatearDuracion(duracion)
        mostrarGeneros(pelicula.generos.orEmpty().map { it.nombre }.filter { it.isNotBlank() })
        pantalla.textoSinopsisDetalle.text =
            pelicula.sinopsis?.takeIf { it.isNotBlank() } ?: getString(R.string.sinopsis_no_disponible)
        pantalla.imagenPosterDetalle.contentDescription =
            getString(R.string.descripcion_poster, pelicula.titulo)
        pantalla.imagenPosterDetalle.cargarPoster(pelicula.rutaPoster)
        pantalla.imagenFondo.cargarFondo(pelicula.rutaFondo, pelicula.rutaPoster)
    }

    private fun formatearDuracion(minutos: Int): String {
        val horas = minutos / 60
        return if (horas > 0) {
            getString(R.string.duracion, horas, minutos % 60)
        } else {
            getString(R.string.duracion_minutos, minutos)
        }
    }

    private fun mostrarGeneros(generos: List<String>) {
        pantalla.grupoGeneros.removeAllViews()
        pantalla.grupoGeneros.isVisible = generos.isNotEmpty()
        generos.forEach { nombre ->
            val etiqueta = Chip(requireContext()).apply {
                text = nombre
                isClickable = false
                isCheckable = false
                setEnsureMinTouchTargetSize(false)
            }
            pantalla.grupoGeneros.addView(etiqueta)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _pantalla = null
    }

    companion object {
        const val ARG_ID_PELICULA = "idPelicula"

        fun argumentos(idPelicula: Int): Bundle = bundleOf(ARG_ID_PELICULA to idPelicula)
    }
}
