package com.dapm.ganagoza.view.fragment
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.FragmentJuegoBinding
import com.dapm.ganagoza.model.Reto
import com.dapm.ganagoza.view.AgregarReto
import com.dapm.ganagoza.view.ReglaJuego
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class FragmentJuego : Fragment() {
    private lateinit var listaReto: MutableList<Reto>
    private lateinit var audioFondo: MediaPlayer
    private lateinit var audioGiroBotella: MediaPlayer
    private lateinit var audioMostrarReto: MediaPlayer
    private lateinit var audioBoton: MediaPlayer
    private lateinit var audioSuspenso: MediaPlayer
    private val juegoViewModel: JuegoViewModel by viewModels()
    private lateinit var binding: FragmentJuegoBinding
    var sonido = false

    private val recompensas = listOf(
        "¡Felicidades! Has ganado un 10% de descuento en tu próxima bebida en Paradero 21.",
        "¡Felicidades! Has ganado una cerveza gratis en Paradero 21.",
        "¡Felicidades! Has ganado un 2x1 en tragos en Paradero 21.",
        "¡Felicidades! Has ganado una entrada gratis a un evento en Paradero 21.",
        "¡Felicidades! Has ganado un 50% de descuento en tu próxima compra en Paradero 21.",
        "¡Felicidades! Has ganado un trago gratis en Paradero 21.",
        "¡Felicidades! Has ganado una gaseosa gratis en Paradero 21.",
        "¡Felicidades! Has ganado una botella de agua gratis en Paradero 21.",
        "¡Felicidades! Has ganado un cóctel gratis en Paradero 21.",
        "¡Felicidades! Has ganado una entrada VIP en Paradero 21."
    )

    private val umbralesRecompensas = listOf(2, 5, 15)
    private var indiceUmbralActual = 0

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
        audioGiroBotella = MediaPlayer.create(context, R.raw.audiobotella)
        audioMostrarReto = MediaPlayer.create(context, R.raw.audioreto)
        audioBoton = MediaPlayer.create(context, R.raw.audioboton)
        audioSuspenso = MediaPlayer.create(context, R.raw.audiosuspenso)
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

        binding.icContentedorMenuJuego.clVolumen.setOnClickListener {
            sonido = !sonido
            juegoViewModel.setHabilitarSonido(sonido)
        }
        binding.icContentedorMenuJuego.idImgAgregarReto.setOnClickListener {
            audioFondo.pause()
            val intent = Intent(requireContext(), AgregarReto::class.java)
            startActivity(intent)
        }
        binding.icContentedorMenuJuego.idImgEstrella.setOnClickListener {
            val juegosJugados = juegoViewModel.obtenerContadorJuegos()
            if (juegosJugados >= umbralesRecompensas[indiceUmbralActual]) {
                mostrarRecompensa()
            } else {
                val juegosNecesarios = umbralesRecompensas[indiceUmbralActual] - juegosJugados
                Toast.makeText(requireContext(), "Necesitas jugar al menos $juegosNecesarios veces más para recibir una recompensa", Toast.LENGTH_SHORT).show()
            }
        }
        binding.icContentedorMenuJuego.idBotonCompartir.setOnClickListener {
            juegoViewModel.compartir(audioFondo, requireActivity())
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
        juegoViewModel.listaReto.observe(viewLifecycleOwner) { lista ->
            listaReto = lista
        }
    }

    private fun observadorSonido() {
        juegoViewModel.habilitarSonido.observe(viewLifecycleOwner) { habilitar ->
            if (habilitar) {
                audioFondo.setVolume(0f, 0f)
                binding.icContentedorMenuJuego.idImgVolume.isVisible = !habilitar
                binding.icContentedorMenuJuego.idImgNoVolumen.isVisible = habilitar
            } else {
                audioFondo.setVolume(1f, 1f)
                binding.icContentedorMenuJuego.idImgVolume.isVisible = !habilitar
                binding.icContentedorMenuJuego.idImgNoVolumen.isVisible = habilitar
            }
        }
    }

    private fun observadorDialogoReto() {
        juegoViewModel.statusShowDialog.observe(viewLifecycleOwner) { status ->
            if (status) {
                incrementarContadorJuegos()
                val countDwnTimer = object : CountDownTimer(4000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        audioSuspenso.start()
                        binding.tvCuentaRegresiva.text = (millisUntilFinished / 1000).toString()
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
                        binding.tvCuentaRegresiva.text = ""
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
        if (::audioFondo.isInitialized && !audioFondo.isPlaying) {
            audioFondo.start()
        }
    }

    override fun onPause() {
        super.onPause()
        // Pausa la música cuando el fragmento se detiene
        if (::audioFondo.isInitialized) {
            audioFondo.pause()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Libera los recursos de MediaPlayer cuando el fragmento se detiene
        if (::audioFondo.isInitialized) {
            audioFondo.release()
        }
        if (::audioGiroBotella.isInitialized) {
            audioGiroBotella.release()
        }
        if (::audioMostrarReto.isInitialized) {
            audioMostrarReto.release()
        }
        if (::audioBoton.isInitialized) {
            audioBoton.release()
        }
        if (::audioSuspenso.isInitialized) {
            audioSuspenso.release()
        }
    }

    private fun incrementarContadorJuegos() {
        juegoViewModel.incrementarContadorJuegos()
    }

    private fun mostrarRecompensa() {
        val recompensa = recompensas.random()

        // Inflar la vista del diálogo primero
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_recompensa, null)

        // Luego obtener la referencia al TextView para el mensaje de recompensa
        val mensajeRecompensa = dialogView.findViewById<TextView>(R.id.mensajeRecompensa)
        mensajeRecompensa.text = recompensa

        // Obtener la referencia al ImageView para el código QR
        val qrImageView = dialogView.findViewById<ImageView>(R.id.qrImageView)

        // Generar el código QR
        val qrCode = generarCodigoQR("Tu texto o URL aquí")
        qrImageView.setImageBitmap(qrCode)

        // Mostrar el diálogo
        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Cerrar") { dialog, _ ->
                dialog.dismiss()
                mostrarDialogoExito()
                actualizarUmbralRecompensa() // Llama al método para actualizar el umbral
            }
            .create()
            .show()
    }
    private fun actualizarUmbralRecompensa() {
        if (indiceUmbralActual < umbralesRecompensas.size - 1) {
            indiceUmbralActual++
        }
    }
    private fun mostrarDialogoExito() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("¡A SEGUIR CHUPANDO!!")
        builder.setMessage("Tú recompensa se ha canjeado correctamente!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun generarCodigoQR(texto: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(texto, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)

        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bitmap
    }
}
