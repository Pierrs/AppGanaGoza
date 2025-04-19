package com.dapm.ganagoza.interfaz.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.dapm.ganagoza.R
import com.dapm.ganagoza.interfaz.actividad.MainActivity
import com.dapm.ganagoza.interfaz.dialogo.DialogoPrivacidadCondiciones

class PantallaPresentacion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configurarPantalla()
        comprobarTerminosPrivacidad()
    }

    private fun configurarPantalla() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_pantalla_presentacion)
        configurarMargenesDePantalla()
    }

    private fun configurarMargenesDePantalla() {
        val vistaPrincipal = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(vistaPrincipal) { _, insets -> insets }
    }

    private fun comprobarTerminosPrivacidad() {
        if (DialogoPrivacidadCondiciones.debeMostrarDialogo(this)) {
            mostrarDialogoPrivacidad()
        } else {
            continuarAMainActivity()
        }
    }

    private fun mostrarDialogoPrivacidad() {
        val dialogoPrivacidad = DialogoPrivacidadCondiciones(this)
        dialogoPrivacidad.setOnDismissListener {
            continuarAMainActivity()
        }
        dialogoPrivacidad.show()
    }

    private fun continuarAMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}
