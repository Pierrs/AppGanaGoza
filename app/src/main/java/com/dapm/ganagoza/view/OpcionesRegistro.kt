package com.dapm.ganagoza.view

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityOpcionesRegistroBinding
import com.dapm.ganagoza.utils.Constantes
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception

class OpcionesRegistro : AppCompatActivity() {
    private lateinit var binding:ActivityOpcionesRegistroBinding

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleAccesoDirecto: GoogleSignInClient
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityOpcionesRegistroBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth=FirebaseAuth.getInstance()
        comprobarRegistro()

        val googleOpcionesDirecta = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleAccesoDirecto= GoogleSignIn.getClient(this,googleOpcionesDirecta)

        binding.IngresarCuenta.setOnClickListener{
            startActivity(Intent(this@OpcionesRegistro, CuentaExistente::class.java))
        }
        binding.IngresarEmail.setOnClickListener{
            googleAccesoInmediato()
        }
    }

    private fun googleAccesoInmediato() {
        val googleSignIntent = googleAccesoDirecto.signInIntent
        googleSignInARL.launch(googleSignIntent)
    }

    private val googleSignInARL = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) { resultado ->
        if(resultado.resultCode== RESULT_OK){
            val data = resultado.data
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val cuenta = task.getResult(ApiException::class.java)
                autenticacionGoogle(cuenta.idToken)
            }catch (e:Exception){
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun autenticacionGoogle(idToken: String?) {
        val credential=GoogleAuthProvider.getCredential(idToken,null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {resultadoAuth->
                if(resultadoAuth.additionalUserInfo!!.isNewUser){
                    llenarInfoDB()
                }else{
                    startActivity(Intent(this,MainActivity::class.java))
                }
            }
            .addOnFailureListener{e->
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun llenarInfoDB() {
        progressDialog.setMessage("Guardando informaciòn")
        val tiempo= Constantes.obtenerTiempoDispositivo()
        val emailUsuario=firebaseAuth.currentUser!!.email
        val uidUsuario = firebaseAuth.uid
        val nombreUsuario=firebaseAuth.currentUser?.displayName

        val hashMap=HashMap<String,Any>()
        hashMap["nombres"]="${nombreUsuario}"
        hashMap["codigoTelefono"]=""
        hashMap["telefono"]=""
        hashMap["urlImagenPerfil"]=""
        hashMap["proveedor"]="Google"
        hashMap["escribiendo"]=""
        hashMap["tiempo"]=tiempo
        hashMap["online"]=true
        hashMap["email"]="${emailUsuario}"
        hashMap["uid"]="${uidUsuario}"
        hashMap["fecha_nacmiento"]=""

        val referencias = FirebaseDatabase.getInstance().getReference("Usuarios")
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


    private fun comprobarRegistro(){
        if(firebaseAuth.currentUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finishAffinity()
        }
    }
}
