package com.example.employeecrm.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity:AppCompatActivity() {


    // Function to get the authentication token from SharedPreferences
    fun getAuthToken(): String {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("authToken", null).toString()
    }

}