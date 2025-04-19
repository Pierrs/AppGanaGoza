package com.dapm.ganagoza.view.dialogo

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.Window
import android.widget.TextView
import com.dapm.ganagoza.R
import com.google.android.material.button.MaterialButton
import androidx.core.net.toUri
import androidx.core.content.edit
import java.util.Locale
import androidx.core.graphics.toColorInt

class DialogoPrivacidadCondiciones(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialogo_terminos_privacidad)
        setCancelable(false)

        configurarTerminosClicables()

        findViewById<TextView>(R.id.link_politica_privacidad).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://gana-goza-privacy.vercel.app".toUri()
            context.startActivity(intent)
        }

        findViewById<MaterialButton>(R.id.boton_aceptar).setOnClickListener {
            val preferenciasCompartidas = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            preferenciasCompartidas.edit() { putBoolean("privacy_accepted", true) }
            dismiss()
        }
    }

    private fun configurarTerminosClicables() {
        val mensajePrivacidad = findViewById<TextView>(R.id.tv_mensaje_privacidad)
        val textoCompleto = context.getString(R.string.al_continuar_aceptas_nuestros_terminos_y_condiciones)

        val textoTerminosEs = "términos y condiciones"
        val textoTerminosEn = "terms and conditions"
        val textoTerminosFr = "termes et conditions"
        val textoTerminosPt = "termos e condições"

        var indiceInicio = textoCompleto.toLowerCase(Locale.ROOT).indexOf(textoTerminosEs.toLowerCase(Locale.ROOT))
        var textoTerminos = textoTerminosEs

        if (indiceInicio == -1) {
            indiceInicio = textoCompleto.toLowerCase(Locale.ROOT).indexOf(textoTerminosEn.toLowerCase(Locale.ROOT))
            textoTerminos = textoTerminosEn
        }

        if (indiceInicio == -1) {
            indiceInicio = textoCompleto.toLowerCase(Locale.ROOT).indexOf(textoTerminosFr.toLowerCase(Locale.ROOT))
            textoTerminos = textoTerminosFr
        }

        if (indiceInicio == -1) {
            indiceInicio = textoCompleto.toLowerCase(Locale.ROOT).indexOf(textoTerminosPt.toLowerCase(Locale.ROOT))
            textoTerminos = textoTerminosPt
        }

        if (indiceInicio == -1) {
            mensajePrivacidad.text = textoCompleto
            return
        }

        val indiceFin = indiceInicio + textoTerminos.length
        val textoFormateado = SpannableString(textoCompleto)

        val spanClicable = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = "https://gana-goza-terms.vercel.app/".toUri()
                context.startActivity(intent)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.color = "#FF5722".toColorInt()
                ds.isUnderlineText = false
            }
        }

        textoFormateado.setSpan(StyleSpan(Typeface.BOLD), indiceInicio, indiceFin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textoFormateado.setSpan(ForegroundColorSpan("#FF5722".toColorInt()), indiceInicio, indiceFin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        textoFormateado.setSpan(spanClicable, indiceInicio, indiceFin, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        mensajePrivacidad.text = textoFormateado
        mensajePrivacidad.movementMethod = LinkMovementMethod.getInstance()
        mensajePrivacidad.highlightColor = Color.TRANSPARENT
    }

    companion object {
        fun debeMostrarDialogo(context: Context): Boolean {
            val preferenciasCompartidas = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            return !preferenciasCompartidas.getBoolean("privacy_accepted", false)
        }
    }
}