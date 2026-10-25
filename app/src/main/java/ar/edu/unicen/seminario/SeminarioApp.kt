package ar.edu.unicen.seminario

import android.app.Application
import ar.edu.unicen.seminario.datos.remoto.ClienteRetrofit
import ar.edu.unicen.seminario.datos.repositorio.RepositorioPeliculas

class SeminarioApp : Application() {

    val repositorioPeliculas: RepositorioPeliculas by lazy {
        RepositorioPeliculas(ClienteRetrofit.crear(this))
    }
}
