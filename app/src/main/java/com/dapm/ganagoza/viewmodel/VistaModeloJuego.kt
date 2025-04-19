package com.dapm.ganagoza.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dapm.ganagoza.R
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.repositorio.RepositorioRetos
import com.dapm.ganagoza.utilidades.Constantes
import com.dapm.ganagoza.interfaz.dialogo.DialogoMostrarReto.mostrarDialogoReto
import kotlinx.coroutines.launch
import kotlin.random.Random

class VistaModeloJuego(application: Application) : AndroidViewModel(application) {
    private val repositorioRetos = RepositorioRetos(getApplication())


    private val _estadoGiroBotella = MutableLiveData(false)
    val estadoGiroBotella: LiveData<Boolean> = _estadoGiroBotella

    private val _giroBotella = MutableLiveData<RotateAnimation>()
    val giroBotella: LiveData<RotateAnimation> = _giroBotella

    private val _activarBoton = MutableLiveData(true)
    val activarBoton: LiveData<Boolean> = _activarBoton

    private val _mostrarEfectoConfeti = MutableLiveData(false)
    val mostrarEfectoConfeti: LiveData<Boolean> = _mostrarEfectoConfeti

    private val _estadoMostrarDialogo = MutableLiveData(false)
    val estadoMostrarDialogo: LiveData<Boolean> = _estadoMostrarDialogo

    private val _activarSonido = MutableLiveData(false)
    val activarSonido: LiveData<Boolean> = _activarSonido

    private val _retosDisponibles = MutableLiveData<List<Reto>>()
    val retosDisponibles: LiveData<List<Reto>> = _retosDisponibles

    private val _estadoDeProgreso = MutableLiveData(false)
    val estadoDeProgreso: LiveData<Boolean> = _estadoDeProgreso


    fun girarBotella() {
        _estadoGiroBotella.value = true
        val giroBotella = crearAnimacionGiro()
        giroBotella.setAnimationListener(crearEscuchadorAnimacion())
        _giroBotella.value = giroBotella
    }


    private fun crearAnimacionGiro(): RotateAnimation {
        val grados = (Math.random() * GRADOS_MAXIMOS) + GRADOS_MINIMOS

        return RotateAnimation(
            0f, grados.toFloat(),
            Animation.RELATIVE_TO_SELF, VALOR_PIVOTE,
            Animation.RELATIVE_TO_SELF, VALOR_PIVOTE
        ).apply {
            fillAfter = true
            duration = DURACION_ANIMACION
            interpolator = DecelerateInterpolator()
        }
    }

    private fun crearEscuchadorAnimacion() = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {
            manejarInicioAnimacion()
        }

        override fun onAnimationEnd(animation: Animation?) {
            manejarFinAnimacion()
        }

        override fun onAnimationRepeat(animation: Animation?) {
        }
    }

    private fun manejarInicioAnimacion() {
        _activarBoton.value = false
        _mostrarEfectoConfeti.value = true
    }


    private fun manejarFinAnimacion() {
        _mostrarEfectoConfeti.value = false
        _activarBoton.value = true
        _estadoMostrarDialogo.value = true
        _estadoGiroBotella.value = false
    }


    fun dialogoMostrarReto(context: Context, audioFondo: MediaPlayer, mensajeReto: String) {
        mostrarDialogoReto(context, audioFondo, mensajeReto)
    }

    fun setHabilitarSonido(habilitar: Boolean) {
        _activarSonido.value = habilitar
    }

    fun agregarReto(reto: Reto) {
        ejecutarOperacionRepositorio {
            repositorioRetos.agregarReto(reto)
            obtenerTodosLosRetos()
        }
    }

    fun obtenerTodosLosRetos() {
        ejecutarOperacionRepositorio {
            _retosDisponibles.value = repositorioRetos.obtenerTodosLosRetos()
        }
    }

    fun eliminarReto(reto: Reto) {
        ejecutarOperacionRepositorio {
            repositorioRetos.eliminarReto(reto)
            obtenerTodosLosRetos()
        }
    }

    fun actualizarReto(reto: Reto) {
        ejecutarOperacionRepositorio {
            repositorioRetos.actualizarReto(reto)
            obtenerTodosLosRetos()
        }
    }

    private fun ejecutarOperacionRepositorio(operacion: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                _estadoDeProgreso.value = true
                operacion()
            } catch (e: Exception) {
            } finally {
                _estadoDeProgreso.value = false
            }
        }
    }

    fun obtenerDescripcionReto(listaReto: List<Reto>): String {
        return if (listaReto.isNotEmpty()) {
            val indiceAleatorio = Random.nextInt(listaReto.size)
            listaReto[indiceAleatorio].descripcionReto
        } else {
            Constantes.MENSAJE_SIN_RETO
        }
    }

    fun compartir(audioFondo: MediaPlayer, activity: Activity) {
        audioFondo.pause()
        val intentCompartir = crearIntentCompartir(activity)
        activity.startActivity(intentCompartir)
    }

    private fun crearIntentCompartir(activity: Activity): Intent {
        val nombrePaquete = activity.packageName
        val eslogan = "App Gana Goza.\nHecho por Pieer's Del Aguila !! "
        val urlApp = "https://play.google.com/store/games?hl=es_419&pli=${nombrePaquete}"
        val contenidoCompartir = eslogan + urlApp

        return Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
            putExtra(Intent.EXTRA_TEXT, contenidoCompartir)
        }
    }
    companion object {
        private const val GRADOS_MAXIMOS = 3600
        private const val GRADOS_MINIMOS = 1000
        private const val VALOR_PIVOTE = 0.5f
        private const val DURACION_ANIMACION = 3600L
    }
}