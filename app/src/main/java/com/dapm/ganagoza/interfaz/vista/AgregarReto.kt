package com.dapm.ganagoza.interfaz.vista

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dapm.ganagoza.databinding.ActivityAgregarRetoBinding
import com.dapm.ganagoza.adaptador.AdaptadorDeRetos
import com.dapm.ganagoza.interfaz.dialogo.DialogoAgregarReto.mostrarDialogoAgregarReto
import com.dapm.ganagoza.utilidades.publicidad.ConfiguracionAnuncios
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad
import com.dapm.ganagoza.viewmodel.VistaModeloJuego
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError

class AgregarReto : AppCompatActivity() {

    private lateinit var vista: ActivityAgregarRetoBinding
    private val modeloVista: VistaModeloJuego by viewModels()
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        vista = ActivityAgregarRetoBinding.inflate(layoutInflater)
        setContentView(vista.root)
        configurarUI()
        observarModeloVista()
        configurarBannerPublicitario()
    }

    private fun configurarUI() = with(vista) {
        icEncabezadoPrincipal.ivRegresar.setOnClickListener { finish() }

        btnFlotanteAgregarReto.setOnClickListener {
            mostrarDialogoAgregarReto(this@AgregarReto, modeloVista) {
                cargarListaDeRetos()
            }
        }
        listaElementos.layoutManager = LinearLayoutManager(this@AgregarReto).apply {
            reverseLayout = false
            stackFromEnd = false
        }
    }

    private fun observarModeloVista() {
        cargarListaDeRetos()
        modeloVista.estadoDeProgreso.observe(this) { estaCargando ->
            vista.progress.isVisible = estaCargando
        }
    }

    private fun cargarListaDeRetos() {
        modeloVista.obtenerTodosLosRetos()
        modeloVista.retosDisponibles.observe(this) { retos ->
            vista.listaElementos.adapter = AdaptadorDeRetos(retos, modeloVista)
            vista.listaElementos.scrollToPosition(0)
        }
    }
    private fun configurarBannerPublicitario() {
        GestorPublicidad.obtenerInstancia().inicializarAdMob(this)

        if (vista.adContainer != null) {
            adView = AdView(this).apply {
                adUnitId = ConfiguracionAnuncios.ID_BANNER
                setAdSize(GestorPublicidad.obtenerInstancia().obtenerTamañoBanner(this@AgregarReto))
                adListener = object : AdListener() {
                    override fun onAdLoaded() {
                        Log.d(TAG, "Banner cargado con éxito")
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        Log.e(TAG, "Error al cargar banner: ${error.message}")
                    }
                }
            }

            vista.adContainer.addView(adView)

            val adRequest = AdRequest.Builder().build()
            adView.loadAd(adRequest)
        } else {
            Log.e(TAG, "No se encontró el contenedor para el anuncio (adContainer)")
        }
    }

    override fun onPause() {
        if (::adView.isInitialized) {
            adView.pause()
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (::adView.isInitialized) {
            adView.resume()
        }
    }

    override fun onDestroy() {
        if (::adView.isInitialized) {
            adView.destroy()
        }
        super.onDestroy()
    }
}
