package com.dapm.ganagoza.interfaz.vista

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityReglaJuegoBinding
import com.dapm.ganagoza.utilidades.publicidad.ConfiguracionAnuncios
import com.dapm.ganagoza.utilidades.publicidad.GestorPublicidad
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.AdListener

private const val TAG = "ReglaJuego"

class ReglaJuego : AppCompatActivity() {

    private lateinit var binding: ActivityReglaJuegoBinding
    private lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityReglaJuegoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        setupUI()
        configurarEnlaces()
        configurarBannerPublicitario()
    }

    private fun setupListeners() {
        binding.icEncabezadoPrincipal.ivRegresar.setOnClickListener { finish() }
    }

    private fun setupUI() {
        binding.icEncabezadoPrincipal.tvTituloEncabezado.text = getString(R.string.name_reglas)
    }

    private fun configurarEnlaces() {
        binding.tvTerminos.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://gana-goza-terms.vercel.app".toUri())
            startActivity(intent)
        }

        binding.tvPrivacidad.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, "https://gana-goza-privacy.vercel.app".toUri())
            startActivity(intent)
        }
    }

    private fun configurarBannerPublicitario() {
        GestorPublicidad.obtenerInstancia().inicializarAdMob(this)

        adView = AdView(this).apply {
            adUnitId = ConfiguracionAnuncios.ID_BANNER
            setAdSize(GestorPublicidad.obtenerInstancia().obtenerTamañoBanner(this@ReglaJuego))
            adListener = object : AdListener() {
                override fun onAdLoaded() {
                    Log.d(TAG, "Banner cargado con éxito")
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    Log.e(TAG, "Error al cargar banner: ${error.message}")
                }
            }
        }

        binding.adContainer.addView(adView)

        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    override fun onPause() {
        adView.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        adView.resume()
    }

    override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }
}