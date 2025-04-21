package com.dapm.ganagoza.interfaz.fragmento
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
import com.dapm.ganagoza.databinding.FragmentoJuegoBinding
import com.dapm.ganagoza.modelo.Reto
import com.dapm.ganagoza.interfaz.vista.AgregarReto
import com.dapm.ganagoza.interfaz.vista.ReglaJuego
import com.dapm.ganagoza.interfaz.dialogo.DialogoIdioma
import com.dapm.ganagoza.utilidades.GestorIdioma
import com.dapm.ganagoza.viewmodel.VistaModeloJuego

class FragmentoJuego : Fragment() {
    private lateinit var retosDisponibles: MutableList<Reto>
    private lateinit var musicaAmbienteJuego: MediaPlayer
    private lateinit var efectoSonidoGiro: MediaPlayer
    private lateinit var efectoSonidoRelevarReto: MediaPlayer
    private lateinit var efectoSonidoPresionarBoton: MediaPlayer
    private lateinit var efectoSonidoTension: MediaPlayer
    private val vistaModeloJuego: VistaModeloJuego by viewModels()
    private lateinit var binding: FragmentoJuegoBinding
    private var sonido = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentoJuegoBinding.inflate(inflater, container, false)
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
        musicaAmbienteJuego = MediaPlayer.create(context, R.raw.musica_fondo)
        efectoSonidoGiro = MediaPlayer.create(context, R.raw.audio_botella_girando)
        efectoSonidoRelevarReto = MediaPlayer.create(context, R.raw.audio_mostrar_reto)
        efectoSonidoPresionarBoton = MediaPlayer.create(context, R.raw.audio_presionando_boton)
        efectoSonidoTension = MediaPlayer.create(context, R.raw.audio_revelador)
        musicaAmbienteJuego.start()
    }

    private fun inicializarControladores() {
        binding.botonGirar.setOnClickListener {
            vistaModeloJuego.girarBotella()
        }

        binding.menuJuegoEncabezado.apply {
            llReglasDelJuego.setOnClickListener { mostrarPantallaReglas() }
            llVolumenDelJuego.setOnClickListener { cambiarEstadoSonido() }
            llRetosDelJuego.setOnClickListener { mostrarPantallaAgregarReto() }
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
        observadorActivarBoton()
        observadorDialogoReto()
        observadorSonido()
        observadorRetosDisponibles()
    }

    private fun observadorRetosDisponibles() {
        vistaModeloJuego.obtenerTodosLosRetos()
        vistaModeloJuego.retosDisponibles.observe(viewLifecycleOwner) { listarRetos ->
            retosDisponibles = listarRetos.toMutableList()
        }
    }

    private fun observadorSonido() {
        vistaModeloJuego.activarSonido.observe(viewLifecycleOwner) { activar ->
            if (activar) {
                musicaAmbienteJuego.setVolume(0f, 0f)
                binding.menuJuegoEncabezado.idImgConVolumen.isVisible = !activar
                binding.menuJuegoEncabezado.idImgSinVolumen.isVisible = activar
            } else {
                musicaAmbienteJuego.setVolume(1f, 1f)
                binding.menuJuegoEncabezado.idImgConVolumen.isVisible = !activar
                binding.menuJuegoEncabezado.idImgSinVolumen.isVisible = activar
            }
        }
    }

    private fun observadorDialogoReto() {
        vistaModeloJuego.estadoMostrarDialogo.observe(viewLifecycleOwner) { estado ->
            if (estado) {
                binding.botonGirar.isEnabled = false
                iniciarCuentaRegresiva()
            }
        }
    }

    private fun iniciarCuentaRegresiva() {
        val countDwnTimer = object : CountDownTimer(5000, 1000) {
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

        vistaModeloJuego.completarProcesoDeMostrarReto()
    }


    private fun pausarSonidosJuego() {
        efectoSonidoGiro.pause()
        efectoSonidoPresionarBoton.pause()
    }

    private fun observadorActivarBoton() {
        vistaModeloJuego.activarBoton.observe(viewLifecycleOwner) { estadoActivarBoton ->
            binding.botonGirar.isVisible = estadoActivarBoton
            binding.botonGirar.isEnabled = estadoActivarBoton
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
        vistaModeloJuego.giroBotella.observe(viewLifecycleOwner) { girar ->
            binding.ivBotellin.startAnimation(girar)
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
        GestorIdioma.reiniciarIdioma()
    }

    private fun liberarRecursosMultimedia() {
        if (::musicaAmbienteJuego.isInitialized) musicaAmbienteJuego.release()
        if (::efectoSonidoGiro.isInitialized) efectoSonidoGiro.release()
        if (::efectoSonidoRelevarReto.isInitialized) efectoSonidoRelevarReto.release()
        if (::efectoSonidoPresionarBoton.isInitialized) efectoSonidoPresionarBoton.release()
        if (::efectoSonidoTension.isInitialized) efectoSonidoTension.release()
    }
}