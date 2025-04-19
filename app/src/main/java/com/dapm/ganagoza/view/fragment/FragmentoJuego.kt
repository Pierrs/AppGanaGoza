package com.dapm.ganagoza.view.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.FragmentJuegoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.AgregarReto
import com.dapm.ganagoza.view.ReglaJuego
import com.dapm.ganagoza.view.dialogo.DialogoIdioma
import com.dapm.ganagoza.view.viewmodel.VistaModeloJuego

class FragmentoJuego : Fragment() {
    private lateinit var retosDisponibles: MutableList<Reto>
    private lateinit var musicaAmbienteJuego: MediaPlayer
    private lateinit var efectoSonidoGiro: MediaPlayer
    private lateinit var efectoSonidoRelevarReto: MediaPlayer
    private lateinit var efectoSonidoPresionarBoton: MediaPlayer
    private lateinit var efectoSonidoTension: MediaPlayer
    private val vistaModeloJuego: VistaModeloJuego by viewModels()
    private lateinit var binding: FragmentJuegoBinding
    private var sonido = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentJuegoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configurarAjustesVentana()
        inicializarControladores()
        configurarObservadores()
        cargarRecursosMultimedia()
    }

    private fun configurarAjustesVentana() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val navigationInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())

            binding.clContenedorBoton.updatePadding(bottom = navigationInsets.bottom)

            insets
        }
    }

    private fun cargarRecursosMultimedia() {
        musicaAmbienteJuego = MediaPlayer.create(context, R.raw.musicafondo)
        efectoSonidoGiro = MediaPlayer.create(context, R.raw.audiobotella)
        efectoSonidoRelevarReto = MediaPlayer.create(context, R.raw.audioreto)
        efectoSonidoPresionarBoton = MediaPlayer.create(context, R.raw.audioboton)
        efectoSonidoTension = MediaPlayer.create(context, R.raw.audiosuspenso)
        musicaAmbienteJuego.start()
    }

    private fun inicializarControladores() {
        binding.botonGirar.setOnClickListener {
            vistaModeloJuego.girarBotella()
        }

        binding.menuJuegoEncabezado.apply {
            llReglasDelJuego.setOnClickListener { mostrarPantallaReglas() }
            llVolumenDelJuego.setOnClickListener { cambiarEstadoSonido() }
            llReglasDelJuego.setOnClickListener { mostrarPantallaAgregarReto() }
            llCompartirDelJuego.setOnClickListener { compartirJuego() }
            llIdiomaDelJuego.setOnClickListener { mostrarDialogoIdioma() }
        }
    }

    private fun mostrarPantallaReglas() {
        pausarMusicaAmbiente()
        startActivity(Intent(requireContext(), ReglaJuego::class.java))
    }

    private fun mostrarPantallaAgregarReto() {
        pausarMusicaAmbiente()
        startActivity(Intent(requireContext(), AgregarReto::class.java))
    }

    private fun pausarMusicaAmbiente() {
        musicaAmbienteJuego.pause()
    }

    private fun cambiarEstadoSonido() {
        sonido = !sonido
        vistaModeloJuego.setHabilitarSonido(sonido)
    }

    private fun compartirJuego() {
        vistaModeloJuego.compartir(musicaAmbienteJuego, requireActivity())
    }

    private fun mostrarDialogoIdioma() {
        pausarMusicaAmbiente()
        val dialogoIdioma = DialogoIdioma(requireContext())
        dialogoIdioma.mostrar()
    }

    private fun configurarObservadores() {
        observadorGiroBotella()
        observadorEfectoConfetiActivado()
        observadorActivarBoton()
        observadorDialogoReto()
        observadorSonido()
        observadorRetosDisponibles()
    }

    private fun observadorRetosDisponibles() {
        vistaModeloJuego.obtenerTodosLosRetos()
        vistaModeloJuego.retosDisponibles.observe(viewLifecycleOwner) { lista ->
            retosDisponibles = lista.toMutableList()
        }
    }

    private fun observadorSonido() {
        vistaModeloJuego.activarSonido.observe(viewLifecycleOwner) { habilitar ->
            if (habilitar) {
                musicaAmbienteJuego.setVolume(0f, 0f)
                binding.menuJuegoEncabezado.idImgVolume.isVisible = !habilitar
                binding.menuJuegoEncabezado.idImgNoVolumen.isVisible = habilitar
            } else {
                musicaAmbienteJuego.setVolume(1f, 1f)
                binding.menuJuegoEncabezado.idImgVolume.isVisible = !habilitar
                binding.menuJuegoEncabezado.idImgNoVolumen.isVisible = habilitar
            }
        }
    }

    private fun observadorDialogoReto() {
        vistaModeloJuego.estadoMostrarDialogo.observe(viewLifecycleOwner) { status ->
            if (status) {
                iniciarCuentaRegresiva()
            }
        }
    }

    private fun iniciarCuentaRegresiva() {
        val countDwnTimer = object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                efectoSonidoTension.start()
                binding.tvCuentaRegresiva.text = (millisUntilFinished / 1000).toString()
            }

            override fun onFinish() {
                efectoSonidoTension.pause()
                efectoSonidoRelevarReto.start()
                mostrarReto()
            }
        }
        countDwnTimer.start()
    }

    private fun mostrarReto() {
        vistaModeloJuego.dialogoMostrarReto(
            requireContext(),
            musicaAmbienteJuego,
            vistaModeloJuego.obtenerDescripcionReto(retosDisponibles)
        )
        pausarSonidosJuego()
        binding.tvCuentaRegresiva.text = ""
    }

    private fun pausarSonidosJuego() {
        efectoSonidoGiro.pause()
        efectoSonidoPresionarBoton.pause()
    }

    private fun observadorEfectoConfetiActivado() {
        vistaModeloJuego.mostrarEfectoConfeti.observe(viewLifecycleOwner) { estadoCerpentina ->
            binding.lottieCerpentina.isVisible = estadoCerpentina
            binding.lottieCerpentina.playAnimation()
        }
    }

    private fun observadorActivarBoton() {
        vistaModeloJuego.activarBoton.observe(viewLifecycleOwner) { estadoHabilitarBoton ->
            binding.botonGirar.isVisible = estadoHabilitarBoton
        }
    }

    private fun observadorGiroBotella() {
        vistaModeloJuego.estadoGiroBotella.observe(viewLifecycleOwner) { giroBotella ->
            if (giroBotella) {
                iniciarGiroBotella()
            }
        }
    }

    private fun iniciarGiroBotella() {
        efectoSonidoPresionarBoton.start()
        musicaAmbienteJuego.pause()
        efectoSonidoGiro.start()
        vistaModeloJuego.giroBotella.observe(viewLifecycleOwner) { rotacion ->
            binding.ivBotella.startAnimation(rotacion)
        }
    }

    override fun onResume() {
        super.onResume()
        observadorRetosDisponibles()
        if (::musicaAmbienteJuego.isInitialized && !musicaAmbienteJuego.isPlaying) {
            musicaAmbienteJuego.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (::musicaAmbienteJuego.isInitialized) {
            musicaAmbienteJuego.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        liberarRecursosMultimedia()
    }

    private fun liberarRecursosMultimedia() {
        if (::musicaAmbienteJuego.isInitialized) musicaAmbienteJuego.release()
        if (::efectoSonidoGiro.isInitialized) efectoSonidoGiro.release()
        if (::efectoSonidoRelevarReto.isInitialized) efectoSonidoRelevarReto.release()
        if (::efectoSonidoPresionarBoton.isInitialized) efectoSonidoPresionarBoton.release()
        if (::efectoSonidoTension.isInitialized) efectoSonidoTension.release()
    }
}
