package com.dapm.ganagoza.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityCambiarPasswordBinding
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class CambiarPassword : AppCompatActivity() {

    private lateinit var binding:ActivityCambiarPasswordBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCambiarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        progressDialog=ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth=FirebaseAuth.getInstance()
        firebaseUser=firebaseAuth.currentUser!!

        binding.IbRegresar.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
        binding.BotonActualizarPass.setOnClickListener {
            validarInformacion()

        }

    }

    private var password_actual=""
    private var password_nueva=""
    private var password_nueva_r=""
    private fun validarInformacion() {
        password_actual = binding. EtPasswordActual.text. toString().trim()
        password_nueva = binding. EtPasswordNueva.text. toString().trim()
        password_nueva_r =binding.EtPasswordNuevaR.text.toString().trim()

        if(password_actual.isEmpty()){
            binding.EtPasswordActual.error="Ingresar la contraseña actual"
            binding.EtPasswordActual.requestFocus()
        }else if (password_nueva.isEmpty()){
            binding.EtPasswordNueva.error="Ingresar la nueva contraseña"
            binding.EtPasswordNueva.requestFocus()
        }else if(password_nueva_r.isEmpty()){
            binding.EtPasswordNuevaR.error="Confirmar nuevo contraseña"
            binding.EtPasswordNuevaR.requestFocus()
        }else if(password_nueva!=password_nueva_r){
            binding.EtPasswordNuevaR.error="Las contraseñas no coinciden"
            binding.EtPasswordNuevaR.requestFocus()

        }else{
            aunteticarUsuarioCamPass()
        }


    }

    private fun aunteticarUsuarioCamPass() {
        progressDialog.setMessage("Autenticando usuario")
        progressDialog.show()

        val autoCredencial=EmailAuthProvider.getCredential(firebaseUser.email.toString(),password_actual)
        firebaseUser.reauthenticate(autoCredencial)
            .addOnSuccessListener {
                progressDialog.dismiss()
                actualizarContraseña()
            }
            .addOnFailureListener{
                e->
                progressDialog.dismiss()
                Toast.makeText(this,
                    "${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun actualizarContraseña(){
        progressDialog.setMessage("Cambiando contraseña")
        progressDialog.show()

        firebaseUser.updatePassword(password_nueva)
            .addOnSuccessListener {

                progressDialog.dismiss()
                Toast.makeText(this,
                    "Su contraseña ha sido actualizada",
                    Toast.LENGTH_SHORT
                ).show()
                firebaseAuth.signOut()
                startActivity(Intent(this,CuentaExistente::class.java))
                finishAffinity()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,
                    "${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }


}