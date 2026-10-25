package ar.edu.unicen.seminario.ui.lista

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ar.edu.unicen.seminario.R
import ar.edu.unicen.seminario.databinding.FragmentoListaPeliculasBinding
import ar.edu.unicen.seminario.datos.modelo.TipoListaPeliculas
import ar.edu.unicen.seminario.ui.comun.mensaje
import ar.edu.unicen.seminario.ui.detalle.FragmentoDetallePelicula
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch

class FragmentoListaPeliculas : Fragment() {

    private var _pantalla: FragmentoListaPeliculasBinding? = null
    private val pantalla get() = _pantalla!!

    private val modelo: ListaPeliculasViewModel by viewModels { ListaPeliculasViewModel.Fabrica }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _pantalla = FragmentoListaPeliculasBinding.inflate(inflater, container, false)
        return pantalla.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        modelo.cambiarIdioma(resources.configuration.locales[0].language)

        val adaptador = AdaptadorPeliculas { pelicula ->
            findNavController().navigate(
                R.id.accionListaADetalle,
                FragmentoDetallePelicula.argumentos(pelicula.id)
            )
        }
        configurarLista(adaptador)
        configurarPestanas()
        configurarBuscador()

        pantalla.vistaError.botonReintentar.setOnClickListener { modelo.cargarPeliculas() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    modelo.estado.collect { mostrarEstado(it, adaptador) }
                }
                launch {
                    modelo.tipoLista.collect { tipo ->
                        pantalla.buscador.isVisible = tipo == TipoListaPeliculas.POPULARES
                    }
                }
            }
        }
    }

    private fun mostrarEstado(estado: EstadoUiLista, adaptador: AdaptadorPeliculas) {
        val vacia = estado is EstadoUiLista.Exito && estado.peliculas.isEmpty()

        pantalla.progresoPeliculas.isVisible = estado is EstadoUiLista.Cargando
        pantalla.listaPeliculas.isVisible = estado is EstadoUiLista.Exito && !vacia
        pantalla.textoVacio.isVisible = vacia
        pantalla.vistaError.root.isVisible = estado is EstadoUiLista.Fallo

        when (estado) {
            is EstadoUiLista.Exito -> adaptador.submitList(estado.peliculas)
            is EstadoUiLista.Fallo -> pantalla.vistaError.textoMensajeError.setText(estado.error.mensaje())
            EstadoUiLista.Cargando -> Unit
        }
    }

    private fun configurarLista(adaptador: AdaptadorPeliculas) {
        val columnas = resources.getInteger(R.integer.columnas_lista_peliculas)
        pantalla.listaPeliculas.layoutManager = GridLayoutManager(requireContext(), columnas)
        pantalla.listaPeliculas.adapter = adaptador
    }

    private fun configurarPestanas() {
        val indice = modelo.tipoLista.value.ordinal
        pantalla.pestanas.getTabAt(indice)?.select()
        pantalla.pestanas.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(pestana: TabLayout.Tab) {
                modelo.cambiarLista(TipoListaPeliculas.entries[pestana.position])
                pantalla.listaPeliculas.scrollToPosition(0)
            }

            override fun onTabUnselected(pestana: TabLayout.Tab) = Unit

            override fun onTabReselected(pestana: TabLayout.Tab) {
                pantalla.listaPeliculas.smoothScrollToPosition(0)
            }
        })
    }

    private fun configurarBuscador() {
        pantalla.buscador.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(texto: String?): Boolean {
                modelo.buscar(texto)
                pantalla.buscador.clearFocus()
                return true
            }

            override fun onQueryTextChange(texto: String?): Boolean {
                if (texto.isNullOrBlank()) modelo.buscar(null)
                return true
            }
        })
    }

    override fun onDestroyView() {
        pantalla.listaPeliculas.adapter = null
        super.onDestroyView()
        _pantalla = null
    }
}
