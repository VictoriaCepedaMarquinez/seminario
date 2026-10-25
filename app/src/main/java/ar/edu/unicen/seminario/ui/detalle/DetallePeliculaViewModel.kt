package ar.edu.unicen.seminario.ui.detalle

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import ar.edu.unicen.seminario.SeminarioApp
import ar.edu.unicen.seminario.datos.error.ErrorApp
import ar.edu.unicen.seminario.datos.repositorio.RepositorioPeliculas
import ar.edu.unicen.seminario.datos.repositorio.ResultadoDatos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetallePeliculaViewModel(
    datosGuardados: SavedStateHandle,
    private val repositorio: RepositorioPeliculas
) : ViewModel() {

    private val idPelicula: Int? = datosGuardados[FragmentoDetallePelicula.ARG_ID_PELICULA]

    private val _estado = MutableStateFlow<EstadoUiDetalle>(EstadoUiDetalle.Cargando)
    val estado: StateFlow<EstadoUiDetalle> = _estado.asStateFlow()

    private var idioma: String? = null

    fun cambiarIdioma(nuevoIdioma: String) {
        if (idioma == nuevoIdioma){
            return
        }else {
            idioma = nuevoIdioma
            cargarPelicula()
        }
    }

    fun cargarPelicula() {
        val id = idPelicula
        if (id == null) {
            _estado.value = EstadoUiDetalle.Fallo(ErrorApp.NO_ENCONTRADO)
            return
        }
        _estado.value = EstadoUiDetalle.Cargando
        viewModelScope.launch {
            _estado.value = when (val resultado = repositorio.obtenerDetalle(id)) {
                is ResultadoDatos.Exito -> EstadoUiDetalle.Exito(resultado.datos)
                is ResultadoDatos.Fallo -> EstadoUiDetalle.Fallo(resultado.error)
            }
        }
    }

    companion object {
        val Fabrica = viewModelFactory {
            initializer {
                val aplicacion = this[APPLICATION_KEY] as SeminarioApp
                DetallePeliculaViewModel(createSavedStateHandle(), aplicacion.repositorioPeliculas)
            }
        }
    }
}
