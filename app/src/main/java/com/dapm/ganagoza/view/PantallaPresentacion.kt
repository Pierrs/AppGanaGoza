package com.dapm.ganagoza.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dapm.ganagoza.view.viewmodel.JuegoViewModel
import com.dapm.ganagoza.databinding.ActivityPantallaPresentacionBinding

class PantallaPresentacion : AppCompatActivity() {
    private val juegoViewModel: JuegoViewModel by viewModels()
    private lateinit var binding: ActivityPantallaPresentacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPantallaPresentacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        juegoViewModel.pantallaPresentacion(this)
    }
}
