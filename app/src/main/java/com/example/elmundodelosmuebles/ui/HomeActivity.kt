package com.example.elmundodelosmuebles.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.elmundodelosmuebles.databinding.ActivityHomeBinding
import com.example.elmundodelosmuebles.utils.Constants.LOGIN_PROVIDER
import com.example.elmundodelosmuebles.utils.Constants.USER_EMAIL
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle=intent.extras!!
        setupUI(bundle.getString(USER_EMAIL) ?:"",bundle.getString(LOGIN_PROVIDER)?:"")
    }

    private fun setupUI(email:String, provider:String) {

        title="Inicio"
        binding.tvEmail.text=email
        binding.tvProvider.text=provider

        binding.btnLogOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            onBackPressed()
        }

        binding.btnProductForm.setOnClickListener {
            val homeIntent= Intent(this, FormProductActivity::class.java)
            startActivity(homeIntent)
        }

    }

}