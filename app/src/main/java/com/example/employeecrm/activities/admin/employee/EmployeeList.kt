package com.example.employeecrm.activities.admin.employee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.R
import com.example.employeecrm.adapters.AllEmployeeAdapter
import com.example.employeecrm.model.Employee
import com.example.employeecrm.model.EmployeeDetails
import com.example.employeecrm.model.LoginManager
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class EmployeeList : AppCompatActivity() {

    private val employeeDetails: MutableList<Employee> = mutableListOf()
    private lateinit var token: String
    private val BASE_URL = "http://192.168.1.21:4000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_employee)


        val storedLoginResponse = LoginManager.loginResponse

        if (storedLoginResponse != null) {
            token = storedLoginResponse.token
            Log.d("response result", token)
        }

        initializeUI()
        getEmployee()
    }

    private fun initializeUI() {


        findViewById<Button>(R.id.btn_add_new_employee).setOnClickListener {
            Toast.makeText(this, "add", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@EmployeeList, AddNewEmployee::class.java))
        }
    }

    private fun getEmployee() {
        val retrofit = buildRetrofit()
        val apiService = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiService.getEmployeeDetails("token=$token")
                if (response.isSuccessful) {
                    val res = response.body()
                    handleSuccessfulResponse(res)
                } else {
                    handleUnsuccessfulResponse(response.errorBody()?.string())
                }
            } catch (e: IOException) {
                handleException(e)
            }
        }
    }

    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun handleSuccessfulResponse(employeeResponse: EmployeeDetails?) {
        if (employeeResponse != null) {
            val transformedEmployeeDetails = employeeResponse.data.map { it.copy() }
            logDetailsAndInvokeShowList(transformedEmployeeDetails)
        } else {
            Log.d("employee error", "Empty response body")
        }
    }

    private fun logDetailsAndInvokeShowList(employeeDetails: List<Employee>) {
        Log.d("hello", "$employeeDetails")
        showEmpList(employeeDetails)
    }

    private fun handleUnsuccessfulResponse(errorBody: String?) {
        Log.d("employee error", "Error: $errorBody")
    }

    private fun handleException(e: IOException) {
        Log.d("employee error", "Error: ${e.message}")
    }

    private fun showEmpList(employeeDetails: List<Employee>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_employee)
        recyclerView.layoutManager = LinearLayoutManager(this@EmployeeList, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        val adapter = AllEmployeeAdapter(this@EmployeeList, employeeDetails)
        recyclerView.adapter = adapter

        adapter.setOnClickListener(object : AllEmployeeAdapter.OnClickListener {
            override fun onCLick(position: Int, model: Employee) {
                Toast.makeText(this@EmployeeList, "clicked ${model.employeeName}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
