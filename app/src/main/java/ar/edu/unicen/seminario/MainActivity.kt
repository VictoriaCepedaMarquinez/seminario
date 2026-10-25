package ar.edu.unicen.seminario

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import ar.edu.unicen.seminario.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var pantalla: ActivityMainBinding
    private lateinit var navegador: NavController
    private lateinit var configBarra: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        pantalla = ActivityMainBinding.inflate(layoutInflater)
        setContentView(pantalla.root)
        setSupportActionBar(pantalla.barraHerramientas)

        ViewCompat.setOnApplyWindowInsetsListener(pantalla.root) { vista, espacios ->
            val barras = espacios.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout() or
                    WindowInsetsCompat.Type.ime()
            )
            vista.setPadding(barras.left, barras.top, barras.right, barras.bottom)
            WindowInsetsCompat.CONSUMED
        }

        val contenedor = supportFragmentManager
            .findFragmentById(R.id.contenedorNavegacion) as NavHostFragment
        navegador = contenedor.navController
        configBarra = AppBarConfiguration(navegador.graph)
        setupActionBarWithNavController(navegador, configBarra)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navegador.navigateUp(configBarra) || super.onSupportNavigateUp()
    }
}
