package com.dapm.ganagoza.view.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.FragmentJuegoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.AgregarReto
import com.dapm.ganagoza.view.ReglaJuego
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel
import kotlinx.coroutines.runBlocking


class FragmentJuego : Fragment() {
    private lateinit var listaReto:MutableList<Reto>
    private lateinit var audioFondo: MediaPlayer
    private lateinit var audioGiroBotella:MediaPlayer
    private lateinit var audioMostrarReto:MediaPlayer
    private lateinit var audioBoton:MediaPlayer
    private lateinit var audioSuspenso:MediaPlayer
    private val juegoViewModel: JuegoViewModel by viewModels()
    private lateinit var binding: FragmentJuegoBinding
    var sonido = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentJuegoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        controladores(view)
        observadorViewModel()
        controladoresMultimedia()
    }

    private fun controladoresMultimedia() {
        audioFondo = MediaPlayer.create(context, R.raw.musicafondo)
        audioGiroBotella=MediaPlayer.create(context,R.raw.audiobotella)
        audioMostrarReto=MediaPlayer.create(context,R.raw.audioreto)
        audioBoton=MediaPlayer.create(context,R.raw.audioboton)
        audioSuspenso=MediaPlayer.create(context,R.raw.audiosuspenso)
        audioFondo.start()
    }

    private fun controladores(view: View) {
        binding.botonGirar.setOnClickListener {
            juegoViewModel.girarBotella()
        }

        binding.icContentedorMenuJuego.idImgReglas.setOnClickListener {
            audioFondo.pause()
            val intent = Intent(requireContext(), ReglaJuego::class.java)
            startActivity(intent)
        }

        binding.icContentedorMenuJuego.clVolumen.setOnClickListener{
            sonido=!sonido
            juegoViewModel.setHabilitarSonido(sonido)
        }
        binding.icContentedorMenuJuego.idImgAgregarReto.setOnClickListener {
            audioFondo.pause()
            val intent = Intent(requireContext(),AgregarReto::class.java)
            startActivity(intent)
        }

    }

    private fun observadorViewModel() {
        observadorRotacionBotella()
        observadorCerpentinaOn()
        observadorHabilitarBoton()
        observadorDialogoReto()
        observadorSonido()
        observadorListaReto()
    }

    private fun observadorListaReto() {
        juegoViewModel.obtenerListaReto()
        juegoViewModel.listaReto.observe(viewLifecycleOwner){ lista ->
            listaReto=lista
        }
    }

    private fun observadorSonido() {
        juegoViewModel.habilitarSonido.observe(viewLifecycleOwner){
            habilitar->
            if(habilitar){
                audioFondo.setVolume(0f,0f)
                binding.icContentedorMenuJuego.idImgVolume.isVisible= !habilitar
                binding.icContentedorMenuJuego.idImgNoVolumen.isVisible=habilitar
            }else{
                audioFondo.setVolume(1f,1f)
                binding.icContentedorMenuJuego.idImgVolume.isVisible=!habilitar
                binding.icContentedorMenuJuego.idImgNoVolumen.isVisible=habilitar
            }
        }
    }

    private fun observadorDialogoReto() {
        juegoViewModel.statusShowDialog.observe(viewLifecycleOwner) { status ->
            if (status){
                val countDwnTimer=object:CountDownTimer(4000,1000){
                    override fun onTick(millisUntilFinished: Long) {
                        audioSuspenso.start()
                        binding.tvCuentaRegresiva.text=(millisUntilFinished/1000).toString()

                    }

                    override fun onFinish() {
                        audioSuspenso.pause()
                        audioMostrarReto.start()
                        juegoViewModel.dialogoMostrarReto(
                            requireContext(),
                            audioFondo,
                            juegoViewModel.obtenerDescripcionReto(listaReto)
                        )
                        audioGiroBotella.pause()
                        audioBoton.pause()
                        binding.tvCuentaRegresiva.text=""
                    }

                }
                countDwnTimer.start()
            }
        }
    }

    private fun observadorCerpentinaOn() {
        juegoViewModel.isCerpentina.observe(viewLifecycleOwner) { estadoCerpentina ->
            binding.lottieCerpentina.isVisible = estadoCerpentina
            binding.lottieCerpentina.playAnimation()
        }
    }

    private fun observadorHabilitarBoton() {
        juegoViewModel.habilitarBoton.observe(viewLifecycleOwner) { estadoHabilitarBoton ->
            binding.botonGirar.isVisible = estadoHabilitarBoton
        }
    }

    private fun observadorRotacionBotella() {
        juegoViewModel.estadoRotacionBotella.observe(viewLifecycleOwner) { giroBotella ->
            if (giroBotella) {
                audioBoton.start()
                audioFondo.pause()
                audioGiroBotella.start()
                juegoViewModel.rotacionBotella.observe(viewLifecycleOwner) { rotacion ->
                    binding.ivBotella.startAnimation(rotacion)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        observadorListaReto()
        // Reanuda la música si no está reproduciéndose
        if (!audioFondo.isPlaying) {
            audioFondo.start()
        }
    }

    override fun onPause() {
        super.onPause()
        // Pausa la música cuando el fragmento ya no está visible
        if (audioFondo.isPlaying) {
            audioFondo.pause()
        }

    }
    override fun onStop() {
        super.onStop()
        audioFondo.pause()
    }
}



