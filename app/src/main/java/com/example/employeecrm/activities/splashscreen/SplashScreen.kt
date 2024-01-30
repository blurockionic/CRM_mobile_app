package com.example.employeecrm.activities.splashscreen

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.employeecrm.R
import com.example.employeecrm.activities.admin.dashboard.Dashboard
import com.example.employeecrm.activities.employee.dashboard.EmployeeDashboard
import com.example.employeecrm.activities.manager.dashboard.ManagerDashboard
import com.example.employeecrm.auth.Login

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)


        // Check if a valid authentication token exists
        val authToken = getAuthToken()

        if (authToken != null && isValidToken(authToken)) {
            Handler(Looper.getMainLooper()).postDelayed({
                navigateBasedOnDesignation(designationType())
            }, 1000)

        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                // No valid token, navigate to the login screen
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
                finish()
            }, 1000)
        }
    }

    //handle for checking designation type
    private fun navigateBasedOnDesignation(designationType: String?) {
        Log.d("designationType", "$designationType")
        when(designationType){
            "admin"->{
                // Valid token exists, navigate to the home screen
                val intent = Intent(this@SplashScreen, Dashboard::class.java)
                startActivity(intent)
                finish()
            }
            "manager"->{
                val intent = Intent(this@SplashScreen, ManagerDashboard::class.java)
                startActivity(intent)
                finish()
            }
            "employee"->{
                val intent = Intent(this@SplashScreen, EmployeeDashboard::class.java)
                startActivity(intent)
                finish()
            }
            else ->{
                val intent = Intent(this@SplashScreen, Login::class.java)
                startActivity(intent)
                finish()
            }
        }
    }


    // Function to get the authentication token from SharedPreferences
    private fun getAuthToken(): String? {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("authToken", null)
    }

    private fun designationType(): String?{
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("designationType", null)
    }

    // Function to check if the authentication token is valid (you need to implement this)
    private fun isValidToken(token: String): Boolean {
        return try {
            // Replace "your-secret" with your actual JWT secret
            val algorithm = Algorithm.HMAC256("DFKDSFJDFDSLDFLDKFDS")

            // Verify the token
            val verifier = JWT.require(algorithm).build()
            verifier.verify(token)

            // Token is valid
            true
        } catch (e: Exception) {
            // Token verification failed
            false
        }
    }
}