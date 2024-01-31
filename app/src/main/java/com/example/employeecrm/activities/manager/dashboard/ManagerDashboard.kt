package com.example.employeecrm.activities.manager.dashboard

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.R
import com.example.employeecrm.activities.admin.employee.EmployeeList
import com.example.employeecrm.activities.manager.project.ManagerProject
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.constant.Constant
import com.example.employeecrm.databinding.ActivityManagerDashboardBinding
import com.example.employeecrm.model.LoginManager
import com.example.employeecrm.model.LoginResponse
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.Exception

class ManagerDashboard : BaseActivity() {
    private lateinit var binding: ActivityManagerDashboardBinding
    private lateinit var token: String
    private var BASE_URL = Constant.server
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpActionBar()


        val storedLoginResponse = LoginManager.loginResponse

        token = storedLoginResponse?.token ?: getAuthToken()

        updateNavigationUserDetails(storedLoginResponse)

//        navigate to the different pages
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.employee->{
                    startActivity(Intent(this, EmployeeList::class.java))
                }
                R.id.project->{
                    startActivity(Intent(this, ManagerProject::class.java))
                }
                R.id.logout->{
                    logout()
                }
                else->{
                    //
                }
            }

            findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(GravityCompat.START)
            true // R
        }

        //get profile
        getProfile()


    }

    private fun updateNavigationUserDetails(storedLoginResponse: LoginResponse?) {
        val header = binding.navigationView.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_name)
        val userDesignation = header.findViewById<TextView>(R.id.tv_designation)

        if (storedLoginResponse != null && userName != null) {
            userName.text = "Name: ${storedLoginResponse.user.name}"
            userDesignation.text = "Desination: ${storedLoginResponse.user.designation}"
        }
    }

    private fun getProfile() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.myProfile("token=$token")
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Handle successful login response
                    } else {
                        // Handle scenario where response body is null
                        Log.d("LoginError", "Empty response body")
                    }
                } else {
                    // Handle unsuccessful login (e.g., invalid credentials, server errors)
                    val errorBody = response.errorBody()?.string()
                    Log.d("LoginError", "Error: $errorBody")
                }
            } catch (e: IOException) {
                // Handle network issues or I/O problems
                Log.d("LoginError", "Network error: ${e.message}")
            } catch (e: Exception) {
                // Handle other exceptions
                Log.d("LoginError", "Error: ${e.message}")
            }
        }
    }

    private fun setUpActionBar() {
//        set drawer layout
        setSupportActionBar(findViewById<Toolbar>(R.id.my_toolbar))

        findViewById<Toolbar>(R.id.my_toolbar).setNavigationIcon(R.drawable.home)

        findViewById<Toolbar>(R.id.my_toolbar).setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    //    toggle the navigation bar
    private fun toggleDrawer() {
        if (findViewById<DrawerLayout>(R.id.drawerLayout).isDrawerOpen(GravityCompat.START)) {
            findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(GravityCompat.START)
        } else {
            findViewById<DrawerLayout>(R.id.drawerLayout).openDrawer(GravityCompat.START)
        }
    }
}