package com.dapm.ganagoza.view.dialogo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.dapm.ganagoza.databinding.DialogLanguageSelectionBinding
import com.dapm.ganagoza.utils.LanguageManager
import androidx.core.graphics.drawable.toDrawable

class LanguageDialog(private val context: Context) {

    private val binding: DialogLanguageSelectionBinding by lazy {
        DialogLanguageSelectionBinding.inflate(LayoutInflater.from(context))
    }

    private val dialog: Dialog by lazy {
        Dialog(context).apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setBackgroundDrawable(Color.TRANSPARENT.toDrawable())
                setLayout(
                    android.view.WindowManager.LayoutParams.MATCH_PARENT,
                    android.view.WindowManager.LayoutParams.WRAP_CONTENT
                )
            }
            setCancelable(true)
        }
    }

    fun show() {
        setupListeners()
        dialog.show()
    }

    private fun setupListeners() {
        binding.layoutSpanish.setOnClickListener {
            changeLanguage(LanguageManager.SPANISH)
        }

        binding.layoutEnglish.setOnClickListener {
            changeLanguage(LanguageManager.ENGLISH)
        }

        binding.layoutFrench.setOnClickListener {
            changeLanguage(LanguageManager.FRENCH)
        }

        binding.layoutPortuguese.setOnClickListener {
            changeLanguage(LanguageManager.PORTUGUESE)
        }
    }

    private fun changeLanguage(languageCode: String) {
        dialog.dismiss()
        LanguageManager.setLocale(context as AppCompatActivity, languageCode)
    }
}