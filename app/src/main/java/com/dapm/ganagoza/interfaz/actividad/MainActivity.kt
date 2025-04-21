package com.dapm.ganagoza.interfaz.actividad

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityMainBinding
import com.dapm.ganagoza.interfaz.fragmento.FragmentoJuego
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fragmento = FragmentoJuego()
        supportFragmentManager.beginTransaction()
            .replace(R.id.FragmentoJuego, fragmento)
            .commit()

        GestorPublicidad.obtenerInstancia().inicializarAdMob(this)
    }

}
