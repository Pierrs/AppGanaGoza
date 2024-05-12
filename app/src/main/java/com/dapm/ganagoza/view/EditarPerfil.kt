package com.dapm.ganagoza.view

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dapm.ganagoza.R
import com.dapm.ganagoza.databinding.ActivityEditarPerfilBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class EditarPerfil : AppCompatActivity() {

    private lateinit var binding: ActivityEditarPerfilBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var imageUri: Uri?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditarPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Espere por favor")
        progressDialog.setCanceledOnTouchOutside(false)

        cargarInfo()
        binding.BotonActualizar.setOnClickListener {
            validarInfo()
        }
        binding.FABCambiarImg.setOnClickListener {
            selecionarImagen_de()
        }

    }

    private var nombres=""
    private var fecha_nacimiento=""
    private var codigoTelefono=""
    private var telefono=""
    private fun validarInfo() {
        nombres=binding.EtNombres.text.toString().trim()
        fecha_nacimiento=binding.EtFechaNacimiento.text.toString().trim()
        codigoTelefono=binding.selectorCodigo.selectedCountryCodeWithPlus
        telefono=binding.EtTelefono.text.toString().trim()

        if (nombres.isEmpty()){
            Toast.makeText(this,"Ingrese su nombre completo",Toast.LENGTH_SHORT).show()
        }else if(fecha_nacimiento.isEmpty()){
            Toast.makeText(this,"Ingrese su fecha nacimiento",Toast.LENGTH_SHORT).show()

        }else if(codigoTelefono.isEmpty()){
            Toast.makeText(this,"Seleccione un codigo",Toast.LENGTH_SHORT).show()

        }else if(telefono.isEmpty()){
            Toast.makeText(this,"Ingresar su telefono",Toast.LENGTH_SHORT).show()

        }else{
            actualizarInfo()
        }
    }

    private fun actualizarInfo() {
        progressDialog.setMessage("Actualizando información")

        val hashMap : HashMap<String, Any> = HashMap()

        if (nombres.isNotEmpty()) {
            hashMap["nombres"] = nombres
        }
        if (fecha_nacimiento.isNotEmpty()) {
            hashMap["fecha_nacimiento"] = fecha_nacimiento
        }
        if (codigoTelefono.isNotEmpty()) {
            hashMap["codigoTelefono"] = codigoTelefono
        }
        if (telefono.isNotEmpty()) {
            hashMap["telefono"] = telefono
        }

        val referencia = FirebaseDatabase.getInstance().getReference("Usuarios")
        referencia.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this,"Se actualizó su información",Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener{
                    e->
                progressDialog.dismiss()
                Toast.makeText(this,"${e.message}",Toast.LENGTH_SHORT).show()
            }
    }


    private fun cargarInfo() {
        val referencia = FirebaseDatabase.getInstance().getReference("Usuarios")
        referencia.child("${firebaseAuth.uid}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val nombres = "${snapshot.child("nombres").value}"
                    val email = "${snapshot.child("email").value}"
                    val imagen = "${snapshot.child("urlImagenPerfil").value}"
                    val fecha_nac = "${snapshot.child("fecha_nacimiento").value}"
                    val telefono = "${snapshot.child("telefono").value}"
                    val codigoTelefono = "${snapshot.child("codigoTelefono").value}"


                    binding.EtNombres.setText(nombres)
                    binding.EtFechaNacimiento.setText(fecha_nac)
                    binding.EtTelefono.setText(telefono)

                    try {
                        Glide.with(applicationContext)
                            .load(imagen)
                            .placeholder(R.drawable.img_perfil)
                            .into(binding.imgPerfil)

                    } catch (e: Exception) {
                        Toast.makeText(
                            this@EditarPerfil,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    try {
                        val codigo = codigoTelefono.replace("+", "").toInt()
                        binding.selectorCodigo.setCountryForPhoneCode(codigo)

                    } catch (e: Exception) {
                        /*Toast.makeText(
                            this@EditarPerfil,
                            "${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()*/
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun selecionarImagen_de() {
        val popupMenu = PopupMenu(this, binding.FABCambiarImg)
        popupMenu.menu.add(Menu.NONE, 1, 1, "Camara")
        popupMenu.menu.add(Menu.NONE, 2, 2, "Galería")

        popupMenu.show()
        popupMenu.setOnMenuItemClickListener { item ->
            val itemId = item.itemId
            if (itemId == 1) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                    concederPermisoCamara.launch(arrayOf(android.Manifest.permission.CAMERA))
                }else{
                    concederPermisoCamara.launch(arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ))
                }

            } else if (itemId == 2) {
                if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                    imagenGaleria()
                }else{
                    concederPermisoAlmacenamiento.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }
            return@setOnMenuItemClickListener true

        }

    }

    private val concederPermisoCamara =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { resultado ->
            var concedidoTodos = true
            for (seConcede in resultado.values) {
                concedidoTodos = concedidoTodos && seConcede
            }
            if (concedidoTodos) {
                imageCamara()
            } else {
                Toast.makeText(
                    this,
                    "El permiso de la cámara o almacenamiento ha sido denegado, o ambas fueron denegadas",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun imageCamara() {
        val contentValues=ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE,"Titulo_imagen")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Descripcion_imagen")
        imageUri=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)
        resultadoCamara_ARL.launch(intent)
    }
    private val resultadoCamara_ARL=
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado->
            if (resultado.resultCode== Activity.RESULT_OK){
                subirImagenStorage()
                /*try {
                    Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.img_perfil)
                        .into(binding.imgPerfil)
                }catch (e:Exception){

                }*/
            }else{
                Toast.makeText(this,
                    "Cancelado",
                    Toast.LENGTH_SHORT).show()
            }
        }

    private val concederPermisoAlmacenamiento=
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
            esConcedido->
            if(esConcedido){
                imagenGaleria()
            }else{
                Toast.makeText(
                    this,
                    "El permiso de alcenamiento ha sido denegada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    private fun imagenGaleria() {
        val intent=Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        resultadoGaleriaARL.launch(intent)
    }
    private val resultadoGaleriaARL=
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            resultado->
            if(resultado.resultCode==Activity.RESULT_OK){
                val data =resultado.data
                imageUri=data?.data
                subirImagenStorage()

                /*try {
                    Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.img_perfil)
                        .into(binding.imgPerfil)
                }catch (e:Exception){

                }*/
            }else{
                Toast.makeText(
                    this,
                    "Selecionar Imagen Cancelada",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    private fun subirImagenStorage(){
        progressDialog.setMessage("Subiendo imagen a Storage")
        progressDialog.show()
        val rutaImagen ="imagenesPerfil/" + firebaseAuth.uid
        val  referencia = FirebaseStorage.getInstance().getReference(rutaImagen)
        referencia.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                val uriTask = taskSnapshot.storage.downloadUrl
                uriTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val urlImagenCargada = task.result.toString()
                        actualizarImagenDB(urlImagenCargada)
                    } else {
                        // Manejar el error
                    }
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_SHORT).show()
            }


    }

    private fun actualizarImagenDB(urlImagenCargada: String) {
        progressDialog.setMessage("Actualizando imagen")
        progressDialog.show()

        val hashMap : HashMap<String,Any> = HashMap()
        if (imageUri!=null){
            hashMap["urlImagenPerfil"]=urlImagenCargada
        }
        val referencia = FirebaseDatabase.getInstance().getReference("Usuarios")
        referencia.child(firebaseAuth.uid!!)
            .updateChildren(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"Su imagen de perfil se ha actualizado",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{e->
                progressDialog.dismiss()
                Toast.makeText(applicationContext,"${e.message}",Toast.LENGTH_SHORT).show()
            }
    }
}

