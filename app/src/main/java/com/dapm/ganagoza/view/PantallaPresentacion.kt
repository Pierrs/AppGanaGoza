package com.dapm.ganagoza.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.dapm.ganagoza.R
import com.dapm.ganagoza.view.dialogo.DiálogoPrivacidadCondiciones

class PantallaPresentacion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        window.statusBarColor = Color.TRANSPARENT



        setContentView(R.layout.activity_pantalla_presentacion)

        setupWindowInsets()

        checkPrivacyTerms()
    }

    private fun setupWindowInsets() {
        val rootView = findViewById<androidx.constraintlayout.widget.ConstraintLayout>(R.id.main)

        ViewCompat.setOnApplyWindowInsetsListener(rootView) { _, insets ->
            insets
        }
    }

    private fun checkPrivacyTerms() {
        if (DiálogoPrivacidadCondiciones.shouldShowDialog(this)) {
            val privacyDialog = DiálogoPrivacidadCondiciones(this)
            privacyDialog.setOnDismissListener {
                continuarAMainActivity()
            }
            privacyDialog.show()
        } else {
            continuarAMainActivity()
        }
    }

    private fun continuarAMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000)
    }
}