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

class PrivacyTermsDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.privacy_terms_dialog)
        setCancelable(false)


        setupClickableTerms()


        findViewById<TextView>(R.id.privacy_policy_link).setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = "https://gana-goza-privacy.vercel.app".toUri()
            context.startActivity(intent)
        }


        findViewById<MaterialButton>(R.id.accept_button).setOnClickListener {
            val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            sharedPreferences.edit() { putBoolean("privacy_accepted", true) }
            dismiss()
        }
    }

    private fun setupClickableTerms() {
        val privacyMessage = findViewById<TextView>(R.id.tv_privacy_message)
        val fullText = context.getString(R.string.al_continuar_aceptas_nuestros_terminos_y_condiciones)

        val termsTextEs = "términos y condiciones"
        val termsTextEn = "terms and conditions"
        val termsTextFr = "termes et conditions"
        val termsTextPt = "termos e condições"


        var startIndex = fullText.toLowerCase(Locale.ROOT).indexOf(termsTextEs.toLowerCase(Locale.ROOT))
        var termsText = termsTextEs

        if (startIndex == -1) {
            startIndex = fullText.toLowerCase(Locale.ROOT).indexOf(termsTextEn.toLowerCase(Locale.ROOT))
            termsText = termsTextEn
        }

        if (startIndex == -1) {
            startIndex = fullText.toLowerCase(Locale.ROOT).indexOf(termsTextFr.toLowerCase(Locale.ROOT))
            termsText = termsTextFr
        }

        if (startIndex == -1) {
            startIndex = fullText.toLowerCase(Locale.ROOT).indexOf(termsTextPt.toLowerCase(Locale.ROOT))
            termsText = termsTextPt
        }

        if (startIndex == -1) {
            privacyMessage.text = fullText
            return
        }

        val endIndex = startIndex + termsText.length
        val spannableString = SpannableString(fullText)


        val clickableSpan = object : ClickableSpan() {
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

        spannableString.setSpan(StyleSpan(Typeface.BOLD), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(ForegroundColorSpan("#FF5722".toColorInt()), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        privacyMessage.text = spannableString
        privacyMessage.movementMethod = LinkMovementMethod.getInstance()
        privacyMessage.highlightColor = Color.TRANSPARENT
    }

    companion object {
        fun shouldShowDialog(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            return !sharedPreferences.getBoolean("privacy_accepted", false)
        }
    }
}