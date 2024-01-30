package com.example.employeecrm.base

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.employeecrm.auth.Login

open class BaseActivity:AppCompatActivity() {


    // Function to get the authentication token from SharedPreferences
    fun getAuthToken(): String {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("authToken", null).toString()
    }


     fun logout() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("authToken")
        editor.apply()

        startActivity(Intent(this, Login::class.java))
        finish()
    }

}