package ar.edu.unicen.seminario.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ar.edu.unicen.seminario.SeminarioApp
import ar.edu.unicen.seminario.datos.modelo.TipoListaPeliculas
import ar.edu.unicen.seminario.datos.repositorio.RepositorioPeliculas
import ar.edu.unicen.seminario.datos.repositorio.ResultadoDatos
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaPeliculasViewModel(private val repositorio: RepositorioPeliculas) : ViewModel() {

    private var busqueda: String? = null
    private var idioma: String? = null
    private var carga: Job? = null

    private val _tipoLista = MutableStateFlow(TipoListaPeliculas.POPULARES)
    val tipoLista: StateFlow<TipoListaPeliculas> = _tipoLista.asStateFlow()

    private val _estado = MutableStateFlow<EstadoUiLista>(EstadoUiLista.Cargando)
    val estado: StateFlow<EstadoUiLista> = _estado.asStateFlow()

    fun cambiarLista(tipo: TipoListaPeliculas) {
        if (_tipoLista.value == tipo) return
        _tipoLista.value = tipo
        cargarPeliculas()
    }

    fun cambiarIdioma(nuevoIdioma: String) {
        if (idioma == nuevoIdioma) return
        idioma = nuevoIdioma
        cargarPeliculas()
    }

    fun buscar(texto: String?) {
        val nuevaBusqueda = texto?.trim()?.takeIf { it.isNotEmpty() }
        if (busqueda == nuevaBusqueda) return
        busqueda = nuevaBusqueda
        cargarPeliculas()
    }

    fun cargarPeliculas() {
        carga?.cancel()
        _estado.value = EstadoUiLista.Cargando
        carga = viewModelScope.launch {
            _estado.value = when (val resultado = repositorio.obtenerPeliculas(_tipoLista.value, busqueda)) {
                is ResultadoDatos.Exito -> EstadoUiLista.Exito(resultado.datos)
                is ResultadoDatos.Fallo -> EstadoUiLista.Fallo(resultado.error)
            }
        }
    }

    companion object {
        val Fabrica = viewModelFactory {
            initializer {
                val aplicacion = this[APPLICATION_KEY] as SeminarioApp
                ListaPeliculasViewModel(aplicacion.repositorioPeliculas)
            }
        }
    }
}
