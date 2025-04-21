package com.dapm.ganagoza.utilidades.publicidad

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class GestorPublicidad private constructor() {

    private var interstitialAd: InterstitialAd? = null
    private var ultimoTiempoMostrado: Long = 0
    private var contadorRetosAgregados: Int = 0
    private var ultimoEvento: String = ""
    private var onAnuncioCerradoCallback: (() -> Unit)? = null

    companion object {
        private const val TAG = "GestorPublicidad"
        private const val EVENTO_CAMBIO_IDIOMA = "cambio_idioma"

        @Volatile
        private var instancia: GestorPublicidad? = null

        fun obtenerInstancia(): GestorPublicidad {
            return instancia ?: synchronized(this) {
                instancia ?: GestorPublicidad().also { instancia = it }
            }
        }
    }

    fun inicializarAdMob(context: Context) {
        MobileAds.initialize(context) {
            Log.d(TAG, "AdMob inicializado correctamente")
        }

        cargarAnuncioIntersticial(context)
    }

    fun cargarAnuncioIntersticial(context: Context) {
        val solicitudAnuncio = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            ConfiguracionAnuncios.ID_INTERSTICIAL,
            solicitudAnuncio,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Error al cargar intersticial: ${error.message}")
                    interstitialAd = null
                }

                override fun onAdLoaded(interstitial: InterstitialAd) {
                    Log.d(TAG, "Intersticial cargado correctamente")
                    interstitialAd = interstitial

                    interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                        override fun onAdDismissedFullScreenContent() {

                            onAnuncioCerradoCallback?.invoke()

                            onAnuncioCerradoCallback = null


                            interstitialAd = null
                            cargarAnuncioIntersticial(context)
                        }

                        override fun onAdFailedToShowFullScreenContent(error: AdError) {
                            Log.e(TAG, "Error al mostrar intersticial: ${error.message}")

                            onAnuncioCerradoCallback?.invoke()
                            onAnuncioCerradoCallback = null

                            interstitialAd = null
                        }
                    }
                }
            }
        )
    }

    fun registrarEvento(activity: Activity, tipoEvento: String): Boolean {
        val tiempoActual = System.currentTimeMillis()
        val tiempoTranscurrido = (tiempoActual - ultimoTiempoMostrado) / 1000

        when (tipoEvento) {
            ConfiguracionAnuncios.EVENTO_AGREGAR_RETO -> {
                contadorRetosAgregados++

                if (contadorRetosAgregados >= 2 &&
                    tiempoTranscurrido >= ConfiguracionAnuncios.INTERVALO_MINIMO_SEGUNDOS &&
                    ultimoEvento != ConfiguracionAnuncios.EVENTO_AGREGAR_RETO) {

                    contadorRetosAgregados = 0
                    return mostrarAnuncioIntersticial(activity, tipoEvento)
                }
            }
            else -> {
                if (tiempoTranscurrido >= ConfiguracionAnuncios.INTERVALO_MINIMO_SEGUNDOS &&
                    ultimoEvento != tipoEvento) {

                    return mostrarAnuncioIntersticial(activity, tipoEvento)
                }
            }
        }

        return false
    }

    private fun mostrarAnuncioIntersticial(activity: Activity, tipoEvento: String): Boolean {
        return if (interstitialAd != null) {
            interstitialAd?.show(activity)
            ultimoTiempoMostrado = System.currentTimeMillis()
            ultimoEvento = tipoEvento
            true
        } else {
            Log.d(TAG, "El anuncio intersticial no estaba listo")
            cargarAnuncioIntersticial(activity)
            false
        }
    }

    fun mostrarAnuncioObligatorioCerrarGuardar(activity: Activity): Boolean {
        if (interstitialAd != null) {
            interstitialAd?.show(activity)
            ultimoTiempoMostrado = System.currentTimeMillis()
            return true
        } else {
            cargarAnuncioIntersticial(activity)
            return false
        }
    }

    fun mostrarAnuncioObligatorioIdioma(activity: Activity, callback: () -> Unit): Boolean {
        onAnuncioCerradoCallback = callback

        if (interstitialAd != null) {
            interstitialAd?.show(activity)
            ultimoTiempoMostrado = System.currentTimeMillis()
            ultimoEvento = EVENTO_CAMBIO_IDIOMA
            return true
        } else {
            Log.d(TAG, "El anuncio intersticial para cambio de idioma no estaba listo")
            callback.invoke()
            cargarAnuncioIntersticial(activity)
            return false
        }
    }

    private fun obtenerTamañoBanner(activity: Activity): AdSize {
        val displayMetrics = activity.resources.displayMetrics
        val anchoPixeles = displayMetrics.widthPixels
        val densidad = displayMetrics.density
        val anchoDp = (anchoPixeles / densidad).toInt()

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, anchoDp)
    }
}