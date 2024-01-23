package com.example.employeecrm.activities.admin.team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.R
import com.example.employeecrm.adapters.SelectedTeamMembersAdapter
import com.example.employeecrm.adapters.TeamMembersAdapter
import com.example.employeecrm.databinding.ActivityTeamBinding
import com.example.employeecrm.model.Employee
import com.example.employeecrm.model.LoginManager
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class TeamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeamBinding

    //    for employee details
    private val employeeDetails: MutableList<Employee> = mutableListOf()
    //employee copy details
    private var employeeDetailsDup: MutableList<Employee> = mutableListOf()

    val list = mutableListOf<Employee>()

    //for storing the token
    private lateinit var token: String

    //base url
    private  var BASE_URL = "http://192.168.1.21:4000/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Checking if a login response is stored and accessing its properties
        val storedLoginResponse = LoginManager.loginResponse

        if (storedLoginResponse != null) {
            token = storedLoginResponse.token
            Log.d("response result", token)
        }



        binding.btnAddMember.setOnClickListener {
            Toast.makeText(this@TeamActivity, "Working on this module", Toast.LENGTH_SHORT).show()
        }

//        get employee
        getEmployee()

    }


    //    handle for get the employee details
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
                        Log.d("msg", "$employeeResponse")
                        for (i in employeeResponse.data) {
                            employeeDetails.add(i)
                        }

//                        invoke
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

    private fun showEmpList(employeeDetails: List<Employee>) {
        val mutableList = employeeDetails.toMutableList()
        val recyclerView = findViewById<RecyclerView>(R.id.rv_employee_list)
        recyclerView.layoutManager = LinearLayoutManager(this@TeamActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        val adapter = TeamMembersAdapter(this@TeamActivity, mutableList)
        recyclerView.adapter = adapter

        adapter.setOnClickListener(object : TeamMembersAdapter.OnClickListener {
            override fun onCLick(position: Int, model: Employee) {
                Toast.makeText(this@TeamActivity, "clicked ${model.employeeName}", Toast.LENGTH_LONG).show()
                list.add(model)
                selectedMembers(list, mutableList)
                // Create a mutable copy of the original list to avoid modifying the input parameter
                // Remove the clicked item from the copy
                mutableList.removeAt(position)

                // Update the adapter with the modified list
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun selectedMembers(list: MutableList<Employee>, mutableList: MutableList<Employee>) {
        binding.rvSelectedMembers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedMembers.setHasFixedSize(true)

        val adapter = SelectedTeamMembersAdapter(this@TeamActivity, list)

        adapter.setOnClickListener(object :SelectedTeamMembersAdapter.OnClickListener{
            override fun onCLick(position: Int, model: Employee) {
                list.removeAt(position)
                // Notify the adapter that the data set has changed
                mutableList.add(model)
                showEmpList(mutableList)
                adapter.notifyDataSetChanged()
            }
        })

        binding.rvSelectedMembers.adapter = adapter
    }


}