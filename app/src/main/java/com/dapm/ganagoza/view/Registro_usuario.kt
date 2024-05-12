package com.dapm.ganagoza.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dapm.ganagoza.databinding.ActivityRegistroUsuarioBinding
import com.dapm.ganagoza.utils.Constantes
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Registro_usuario : AppCompatActivity() {
    private lateinit var binding: ActivityRegistroUsuarioBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegistroUsuarioBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)


        firebaseAuth=FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.BotonIngresar.setOnClickListener{
            validarInformacion()
        }
    }
    private var email=""
    private var password=""
    private var r_password=""
    private fun validarInformacion(){
        email=binding.EtEmail.text.toString().trim()
        password=binding.EtPassword.text.toString().trim()
        r_password=binding.TextoRepetirPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.EtEmail.error="Email inválido"
            binding.EtEmail.requestFocus()
        }
        else if(email.isEmpty()){
            binding.EtEmail.error="Ingrese email"
            binding.EtEmail.requestFocus()
        }
        else if(password.isEmpty()){
            binding.EtPassword.error="Ingrese contraseña"
            binding.EtPassword.requestFocus()
        }
        else if(password.length < 6){
            binding.EtPassword.error="La contraseña debe tener al menos 6 caracteres"
            binding.EtPassword.requestFocus()
        }
        else if(r_password.isEmpty()){
            binding.TextoRepetirPassword.error="Repetir contraseña"
            binding.TextoRepetirPassword.requestFocus()
        }
        else if(password!=r_password){
            binding.TextoRepetirPassword.error="No coinciden"
            binding.TextoRepetirPassword.requestFocus()
        }
        else{
            registrarUsuario()
        }
    }
    private fun registrarUsuario(){
        progressDialog.setMessage("creando cuenta")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                        llenarInformacionBD()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,
                    "No se registro el usuario debido a ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun llenarInformacionBD()  {
        progressDialog.setMessage("Guardando informaciòn")
        val tiempo=Constantes.obtenerTiempoDispositivo()
        val emailUsuario=firebaseAuth.currentUser!!.email
        val uidUsuario = firebaseAuth.uid

        val hashMap=HashMap<String,Any>()
        hashMap["nombres"]=""
        hashMap["codigoTelefono"]=""
        hashMap["telefono"]=""
        hashMap["urlImagenPerfil"]=""
        hashMap["proveedor"]="Email"
        hashMap["escribiendo"]=""
        hashMap["tiempo"]=tiempo
        hashMap["online"]=true
        hashMap["email"]="${emailUsuario}"
        hashMap["uid"]="${uidUsuario}"
        hashMap["fecha_nacimiento"]=""

        val referencias =FirebaseDatabase.getInstance().getReference("Usuarios")
        referencias.child(uidUsuario!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                startActivity(Intent(this,MainActivity::class.java))
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(this,
                    "No se registró debido a ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

}
