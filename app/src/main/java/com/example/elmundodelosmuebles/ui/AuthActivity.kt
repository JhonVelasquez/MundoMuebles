package com.example.elmundodelosmuebles.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.elmundodelosmuebles.R
import com.example.elmundodelosmuebles.databinding.ActivityAuthBinding
import com.example.elmundodelosmuebles.utils.Constants.GOOGLE_SIGN_IN
import com.example.elmundodelosmuebles.utils.Constants.LOGIN_PROVIDER
import com.example.elmundodelosmuebles.utils.Constants.USER_EMAIL
import com.example.elmundodelosmuebles.utils.MundoMueblesApplication.Companion.prefs
import com.example.elmundodelosmuebles.utils.ProviderType
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAuthBinding

    private lateinit var loginProvider:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        title="Autenticación"

        binding.btnRegister.setOnClickListener{
            if(binding.etEmail.text.isNotEmpty()&&binding.etPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener {
                    if(it.isSuccessful){
                        saveLoginCredendtials(ProviderType.BASIC.name)
                        showHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }

        binding.btnSignIn.setOnClickListener {
            if(binding.etEmail.text.isNotEmpty()&&binding.etPassword.text.isNotEmpty()){
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                ).addOnCompleteListener {
                    if(it.isSuccessful){
                        saveLoginCredendtials(ProviderType.BASIC.name)
                        showHome(it.result?.user?.email ?:"",ProviderType.BASIC)
                    }else{
                        showAlert()
                    }
                }
            }
        }
        binding.btnLogInGoogle.setOnClickListener {
            val googleConf=
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id_s))
                    .requestEmail()
                    .build()
            val googleClient = GoogleSignIn.getClient(this,googleConf)
            googleClient.signOut()
            startActivityForResult(googleClient.signInIntent,GOOGLE_SIGN_IN)
        }

        loadLoginCredentials()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode== GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    logInGoogleEmailToken(account.email ?:"",account.idToken)
                }
            } catch (e: ApiException){
                showAlert(e.toString())
            }
        }
    }

    private fun logInGoogleEmailToken(email: String,token:String){
        try{
            if(email.isNotEmpty()&&token.isNotEmpty()) {
                val credential = GoogleAuthProvider.getCredential(token, null)
                FirebaseAuth.getInstance().signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            saveLoginCredendtials(ProviderType.GOOGLE.name, email, token)
                            showHome(email, ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
            }
        } catch (e: ApiException){
        showAlert(e.toString())
        }
    }

    private fun showAlert(message:String=""){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        if(message.isNotEmpty()){
            builder.setMessage(message)
        }else {
            builder.setMessage("Se ha producido un error autenticando al usuario.")
        }
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog=builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType){
        val homeIntent= Intent(this, HomeActivity::class.java).apply {
            putExtra(USER_EMAIL,email)
            putExtra(LOGIN_PROVIDER,provider.name)
        }
        startActivity(homeIntent)
    }

    private fun loadLoginCredentials(){

        val mail=prefs.getUserEmail()
        val pass=prefs.getUserPassword()
        val isSel=prefs.getKeepSession()
        val logProv=prefs.getLoginProvider()

        binding.swRememberPassword.isChecked=isSel

        if(isSel){
            loginProvider=logProv
            when(logProv){
                ProviderType.GOOGLE.name -> {
                    etEmail.setText(prefs.getUserEmail())
                    logInGoogleEmailToken(prefs.getUserEmail(), prefs.getTokenGoogle())
                }
                ProviderType.BASIC.name ->{
                    etEmail.setText(mail)
                    etPassword.setText(pass)
                    binding.btnSignIn.callOnClick()
                }
            }
        }

    }
    private fun saveLoginCredendtials(logProvider: String, googleMail:String="", googleToken:String=""){
        val mail=etEmail.text.toString()
        val pass=etPassword.text.toString()
        val isSel=swRememberPassword.isChecked
        prefs.setKeepSession(isSel)

        if(isSel) {
            when(logProvider){
                ProviderType.GOOGLE.name -> {
                    prefs.setTokenGoogle(googleToken)
                    prefs.setUserEmail(googleMail)
                }
                ProviderType.BASIC.name ->{
                    prefs.setUserPassword(pass)
                    prefs.setUserEmail(mail)
                }
            }
            prefs.setLoginProvider(logProvider)

        }else{
            prefs.setUserEmail("")
            prefs.setUserPassword("")
            prefs.setTokenGoogle("")
        }
    }


}