package com.dapm.ganagoza.view.fragment

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.FragmentCuentaBinding
import com.dapm.ganagoza.utils.Constantes
import com.dapm.ganagoza.view.CambiarPassword
import com.dapm.ganagoza.view.EditarPerfil
import com.dapm.ganagoza.view.OpcionesRegistro
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentCuenta : Fragment() {
    private lateinit var binding: FragmentCuentaBinding
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var mContext :Context
    private lateinit var progressDialog: ProgressDialog

    override fun onAttach(context:Context) {
        mContext=context
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCuentaBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(mContext)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth=FirebaseAuth.getInstance()

        leerInfo()
        binding.BotonEditarPerfil.setOnClickListener {
            startActivity(Intent(mContext,EditarPerfil::class.java))
        }
        binding.BotonCambiarPass.setOnClickListener {
            startActivity(Intent(mContext,CambiarPassword::class.java))
        }

        binding.BotonVerificarCuenta.setOnClickListener {
            verificarCuenta()
        }


        binding.BotonCerrarSesion.setOnClickListener{
            firebaseAuth.signOut()
            startActivity(Intent(mContext,OpcionesRegistro::class.java))
            activity?.finishAffinity()
        }

    }

    private fun leerInfo() {
        val referencia=FirebaseDatabase.getInstance().getReference("Usuarios")
        referencia.child("${firebaseAuth.uid}")
            .addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres= "${snapshot.child("nombres").value}"
                    val email="${snapshot.child("email").value}"
                    val imagen = "${snapshot.child("urlImagenPerfil").value}"
                    val fecha_nac = "${snapshot.child("fecha_nacimiento").value}"
                    var tiempo = "${snapshot.child("tiempo").value}"
                    val telefono = "${snapshot.child("telefono").value}"
                    val codigoTelefono = "${snapshot.child("codigoTelefono").value}"
                    val proveedor = "${snapshot.child("proveedor").value}"

                    val cod_tel = codigoTelefono + telefono

                    if(tiempo=="null"){
                        tiempo="0"
                    }
                    val formato_tiempo = Constantes.obtenerFecha(tiempo.toLong())

                    //SETEAR INFORMACIÓN
                    binding.TvEmail.text=email
                    binding.TvNombres.text=nombres
                    binding.TvNacimiento.text=fecha_nac
                    binding.TvTelefono.text=cod_tel
                    binding.TvMiembro.text=formato_tiempo

                    //SETEAR IMAGEN

                    try {
                        Glide.with(mContext)
                            .load(imagen)
                            .placeholder(R.drawable.img_perfil)
                            .into(binding.IvPerfil)

                    }catch (e:Exception){
                        Toast.makeText(
                            mContext,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    if(proveedor=="Email"){
                        val esVerficado = firebaseAuth.currentUser!!.isEmailVerified
                        if(esVerficado){
                            binding.BotonVerificarCuenta.visibility= View.GONE
                            binding.TvEstadoCuenta.text="Verficado"
                        }else{
                            binding.BotonVerificarCuenta.visibility=View.VISIBLE
                            binding.TvEstadoCuenta.text="No verificado"
                        }
                    }else{
                        binding.BotonVerificarCuenta.visibility=View.GONE
                        binding.TvEstadoCuenta.text="Verficado"
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })


    }
    private fun verificarCuenta(){
        progressDialog.setMessage(("Enviado la verificación a su correo electronico "))
        progressDialog.show()

        firebaseAuth.currentUser!!.sendEmailVerification()
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(
                    mContext,
                    "Las instrucciones fueron enviadas a su correo registrado",
                    Toast.LENGTH_SHORT
                ).show()

            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(
                    mContext,
                    "${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }

    }

}
