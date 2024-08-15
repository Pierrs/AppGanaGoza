package com.dapm.ganagoza.view.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dapm.ganagoza.R
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.repository.RetoRepository
import com.dapm.ganagoza.utils.Constantes
import com.dapm.ganagoza.utils.Constantes.TIEMPO
import com.dapm.ganagoza.view.MainActivity
import com.dapm.ganagoza.view.OpcionesRegistro
import com.dapm.ganagoza.view.dialogo.DialogoMostrarReto.showDialogMostrarReto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random


class JuegoViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>()
    private val retoRepository = RetoRepository(context)

    private val _estadoRotacionBotella = MutableLiveData(false)
    val estadoRotacionBotella: LiveData<Boolean> get() = _estadoRotacionBotella

    private val _rotacionBotella = MutableLiveData<RotateAnimation>()
    val rotacionBotella: LiveData<RotateAnimation> get() = _rotacionBotella

    private val _habilitarBoton = MutableLiveData(true)
    val habilitarBoton: LiveData<Boolean> get() = _habilitarBoton

    private val _isCerpentina = MutableLiveData(false)
    val isCerpentina: LiveData<Boolean> get() = _isCerpentina

    private val _statusShowDialog = MutableLiveData(false)
    val statusShowDialog: LiveData<Boolean> get() = _statusShowDialog

    private val _habilitarSonido = MutableLiveData(false)
    val habilitarSonido: LiveData<Boolean> get() = _habilitarSonido

    private val _listaReto = MutableLiveData<MutableList<Reto>>()
    val listaReto: LiveData<MutableList<Reto>> get() = _listaReto

    private val _progresSstate = MutableLiveData(false)
    val progresSstate: LiveData<Boolean> get() = _progresSstate

    //recomepnsa

    private var contadorJuegos = MutableLiveData<Int>(0)


    fun pantallaPresentacion(activity: Activity) {
        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.schedule({
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }, TIEMPO, TimeUnit.MILLISECONDS)
    }

    fun girarBotella() {
        _estadoRotacionBotella.value = true
        val grados = (Math.random() * 3600) + 1000
        val rotacion = RotateAnimation(
            0f, grados.toFloat(), Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotacion.fillAfter = true
        rotacion.duration = 3600
        rotacion.interpolator = DecelerateInterpolator()
        rotacion.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                _habilitarBoton.value = false
                _isCerpentina.value = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                _isCerpentina.value = false
                _habilitarBoton.value = true
                _statusShowDialog.value = true
                _estadoRotacionBotella.value = false
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }
        })
        _rotacionBotella.value = rotacion
    }

    fun dialogoMostrarReto(context: Context, audioFondo: MediaPlayer, mensajeReto: String) {
        showDialogMostrarReto(context, audioFondo, mensajeReto)
    }

    suspend fun esperar(tiempo: Int) {
        delay(tiempo * 1000L)
    }

    fun setHabilitarSonido(habilitar: Boolean) {
        _habilitarSonido.value = habilitar
    }

    fun agregarReto(reto: Reto) {
        viewModelScope.launch {
            _progresSstate.value = true
            try {
                retoRepository.agregarReto(reto)
                obtenerListaReto()
                _progresSstate.value = false
            } catch (e: Exception) {
                _progresSstate.value = false
            }
        }
    }

    fun obtenerListaReto() {
        viewModelScope.launch {
            _progresSstate.value = true
            try {
                _listaReto.value = retoRepository.obtenerListaReto()
                _progresSstate.value = false
            } catch (e: Exception) {
                _progresSstate.value = false
            }
        }
    }

    fun eliminarReto(reto: Reto) {
        viewModelScope.launch {
            _progresSstate.value = true
            try {
                retoRepository.eliminarReto(reto)
                _progresSstate.value = false
            } catch (e: Exception) {
                _progresSstate.value = false
            }
        }
    }

    fun actualizarReto(reto: Reto) {
        viewModelScope.launch {
            _progresSstate.value = true
            try {
                retoRepository.actualizarReto(reto)
                _progresSstate.value = false
            } catch (e: Exception) {
                _progresSstate.value = false
            }
        }
    }

    fun obtenerDescripcionReto(listaReto: MutableList<Reto>): String {
        var descripcion = ""
        return if (listaReto.isNotEmpty()) {
            val tamanio = listaReto.size
            val randomReto = Random.nextInt(0, tamanio)
            descripcion = listaReto[randomReto].descripcionReto
            descripcion
        } else {
            val emptyReto = Constantes.EMPTY_RETO
            emptyReto
        }
    }

    fun compartir(audioFondo: MediaPlayer, activity: Activity) {
        audioFondo.pause()
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name))
        val nombrePaquete = activity.packageName
        val eslogan = "App Gana Goza.\nHecho por Pieer's Del Aguila !! "
        val urlApp = "https://play.google.com/store/games?hl=es_419&pli=${nombrePaquete}"
        val compartir = eslogan + urlApp
        intent.putExtra(Intent.EXTRA_TEXT, compartir)
        activity.startActivity(intent)
    }


    fun incrementarContadorJuegos() {
        val nuevoValor = (contadorJuegos.value ?: 0) + 1
        contadorJuegos.value = nuevoValor
    }

    fun obtenerContadorJuegos(): Int {
        return contadorJuegos.value ?: 0
    }
}




