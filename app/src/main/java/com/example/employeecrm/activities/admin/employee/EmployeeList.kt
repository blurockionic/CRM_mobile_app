package com.example.employeecrm.activities.admin.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.adapters.AllEmployeeAdapter
import com.example.employeecrm.databinding.ActivityEmployeeBinding
import com.example.employeecrm.model.Employee
import com.example.employeecrm.model.LoginManager
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class EmployeeList : AppCompatActivity() {
    private lateinit var binding :ActivityEmployeeBinding
    //    for employee details
    private val employeeDetails: MutableList<Employee> = mutableListOf()
    //for storing the token
    private lateinit var token: String
    // Base URL of your API
    private val BASE_URL = "http://192.168.26.40:4000/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Checking if a login response is stored and accessing its properties
        val storedLoginResponse = LoginManager.loginResponse

        if (storedLoginResponse != null) {
            token = storedLoginResponse.token
            Log.d("response result", token)
        }

        getEmployee()

        //on click
        binding.btnAddNewEmployee.setOnClickListener {
            Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EmployeeList, AddNewEmployee::class.java))
        }
    }



    private fun getEmployee() {


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(Apis::class.java)
        lifecycleScope.launch {
            try {
                val response = apiService.getEmployeeDetails("token=$token")
                if (response.isSuccessful) {
                    val employeeResponse = response.body()
                    if (employeeResponse != null) {
                        // Handle successful login response
                        Log.d("msg","$employeeResponse")
                        for (i in employeeResponse.employee) {
                            employeeDetails.add(i)
                        }
//                        invoke showEmpList function
                        showEmpList(employeeDetails)


                    } else {
                        // Handle scenario where response body is null
                        Log.d("employee error", "Empty response body")
                    }
                } else {
                    // Handle unsuccessful login (e.g., invalid credentials, server errors)
                    val errorBody = response.errorBody()?.string()
                    Log.d("employee error", "Error: $errorBody")
                }
            } catch (e: IOException) {
                // Handle other exceptions
                Log.d("employee error", "Error: ${e.message}")
            }
        }

    }

    //admin@gmail.com
    //1234567890

    private fun showEmpList(employeeDetails: MutableList<Employee>) {
        binding.rvEmployee.layoutManager = LinearLayoutManager(this@EmployeeList, LinearLayoutManager.VERTICAL, false)
        binding.rvEmployee.setHasFixedSize(true)

        val adapter = AllEmployeeAdapter(this@EmployeeList, employeeDetails)

        binding.rvEmployee.adapter = adapter

        adapter.setOnClickListener(object :
            AllEmployeeAdapter.OnClickListener{
            override fun onCLick(position: Int, model: Employee) {
                Toast.makeText(this@EmployeeList, "clicked ${model.employeeName}", Toast.LENGTH_LONG).show()
            }
        })
    }
}