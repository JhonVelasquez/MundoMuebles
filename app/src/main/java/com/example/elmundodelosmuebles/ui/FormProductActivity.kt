package com.example.elmundodelosmuebles.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.elmundodelosmuebles.databinding.ActivityFormProductBinding
import com.google.firebase.firestore.FirebaseFirestore

class FormProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFormProductBinding
    private val db=FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityFormProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
    }

    fun setupUI(){

        binding.etCodigo.setText("L7TiKfUKnd8Azu67aPrB")
        binding.btnLoad.setOnClickListener {
            val cod =binding.etCodigo.text.toString()

            db.collection("productos")
                .document(cod)
                .get()
                .addOnFailureListener {
                    showAlert(it.message.toString())
                }
                .addOnSuccessListener {
                    try {
                        binding.etAltura.setText((it.get("alturaCM") as Long ?: "").toString())
                        binding.etAncho.setText((it.get("anchoCM") as Long ?: "").toString())
                        binding.etColor.setText(it.get("color") as String ?: "")
                        binding.etCostoMin.setText((it.get("costoMinimo") as Double ?: "").toString())
                        binding.etCostoNormal.setText((it.get("costoNormal") as Long ?: "").toString())
                        binding.etDescripciN.setText(it.get("descripcion") as String ?: "")
                        binding.etLargo.setText((it.get("largoCM") as Long ?: "").toString())
                        binding.etNombre.setText(it.get("nombre") as String ?: "")
                        binding.etTipo.setText(it.get("tipo") as String ?: "")

                        Glide.with(this).load(it.get("imageURL") as String ?: "").into(binding.imProduct)
                    }catch(e: Exception){
                        showAlert(e.toString())
                    }
                }
            getAllDocuments()
        }

        binding.btnComplete.setOnClickListener {
            try {

                db.collection("productos")
                    .document()
                    //.document("producto1")
                    .set(
                        hashMapOf(
                            "alturaCM" to binding.etAltura.text.toString().toLong(),
                            "anchoCM" to binding.etAncho.text.toString().toLong(),
                            "color" to binding.etColor.text.toString(),
                            "costoMinimo" to binding.etCostoMin.text.toString().toDouble(),
                            "costoNormal" to binding.etCostoNormal.text.toString().toLong(),
                            "descripcion" to binding.etDescripciN.text.toString(),
                            "largoCM" to binding.etLargo.text.toString().toLong(),
                            "nombre" to binding.etNombre.text.toString(),
                            "tipo" to binding.etTipo.text.toString(),
                            "imageURL" to "https://i.ibb.co/vkQjKCF/Logo-casita-square-banner.jpg"

                        )
                    )
            }catch (e: Exception){
                showAlert(e.toString())
            }
        }

    }

    private fun getAllDocuments(){
        db.collection("productos").get().addOnSuccessListener {
            it.documents.forEach {
                Log.println(Log.INFO, "IDSSSSSSSSSSS:",it.get("color") as String ?:"")
            }
        }
    }

    private fun showAlert(message:String=""){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        if(message.isNotEmpty()){
            builder.setMessage(message)
        }else {
            builder.setMessage("Se ha producido un error.")
        }
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog =builder.create()
        dialog.show()
    }
}