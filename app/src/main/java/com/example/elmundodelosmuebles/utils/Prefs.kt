package com.example.elmundodelosmuebles.utils

import android.content.Context
import com.example.elmundodelosmuebles.utils.Constants.KEEP_SESSION
import com.example.elmundodelosmuebles.utils.Constants.LOGIN_PROVIDER
import com.example.elmundodelosmuebles.utils.Constants.SHARED_NAME_MYDB
import com.example.elmundodelosmuebles.utils.Constants.TOKEN_GOOGLE
import com.example.elmundodelosmuebles.utils.Constants.USER_EMAIL
import com.example.elmundodelosmuebles.utils.Constants.USER_PASSWORD

class Prefs (val context: Context){
    val storage= context.getSharedPreferences(SHARED_NAME_MYDB,0)

    fun setUserEmail(name:String){
        storage.edit().putString(USER_EMAIL,name).apply()
    }

    fun setUserPassword(password:String){
        storage.edit().putString(USER_PASSWORD,password).apply()
    }
    fun setKeepSession(keepSession: Boolean){
        storage.edit().putBoolean(KEEP_SESSION,keepSession).apply()
    }

    fun setLoginProvider(provider:String){
        storage.edit().putString(LOGIN_PROVIDER,provider).apply()
    }

    fun setTokenGoogle(token: String){
        storage.edit().putString(TOKEN_GOOGLE,token).apply()
    }
    fun getUserEmail():String{
        return storage.getString(USER_EMAIL,"")?:""
    }

    fun getUserPassword():String{
        return storage.getString(USER_PASSWORD,"")?:""
    }
    fun getKeepSession():Boolean{
        return storage.getBoolean(KEEP_SESSION,false)
    }

    fun getLoginProvider():String{
        return storage.getString(LOGIN_PROVIDER,"")?:""
    }
    fun getTokenGoogle():String{
        return storage.getString(TOKEN_GOOGLE,"")?:""
    }

}