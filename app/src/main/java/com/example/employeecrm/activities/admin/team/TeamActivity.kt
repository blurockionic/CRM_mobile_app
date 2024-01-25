package com.example.employeecrm.activities.admin.team

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.example.employeecrm.model.NewTeam
import com.example.employeecrm.model.Project
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

    private var selectedEmp :  MutableList<String> = mutableListOf()

    //managerList
    private var managerList : MutableList<Employee> = mutableListOf()

    // for all project
    private val allProject: MutableList<Project> = mutableListOf()

    val list = mutableListOf<Employee>()

    //for storing the token
    private lateinit var token: String

    private lateinit var managerId : String
    private lateinit var projectId : String
    private lateinit var adminId: String

    //base url
    private  var BASE_URL = "http://192.168.1.8:4000/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Checking if a login response is stored and accessing its properties
        val storedLoginResponse = LoginManager.loginResponse

        if (storedLoginResponse != null) {
            token = storedLoginResponse.token
            adminId = storedLoginResponse.user._id
            Log.d("response result", adminId)
        }

        val teamName = intent.getStringExtra("teamName")
        val teamId = intent.getStringExtra("teamId")

        //set the value

        binding.etTeamName.setText(teamName)

        Log.d("teamId", "$teamId")

        if(teamId != null){
            binding.btnSubmit.visibility = View.GONE
            binding.btnUpdateTeamDetails.visibility = View.VISIBLE
            binding.tvTitle.text = "Update Team $teamName"
        }else{
            binding.btnSubmit.visibility = View.VISIBLE
            binding.btnUpdateTeamDetails.visibility = View.GONE
        }

//        get employee
        getEmployee()

        //invoke
        getProjects()

        createTeam(adminId)
    }

    private fun createTeam(adminId: String) {
        binding.btnSubmit.setOnClickListener {
            Toast.makeText(this@TeamActivity, "Working on this module", Toast.LENGTH_SHORT).show()
            val teamName = binding.etTeamName.text.toString()
            val teamDescription =  binding.etTeamDescription.text.toString()

            Log.d("alldata", "$teamName, $teamDescription, $projectId, $managerId, $selectedEmp, $adminId")

            handleOnCreateTeam(teamName, teamDescription, adminId, projectId, managerId, selectedEmp)

        }
    }

    private fun handleOnCreateTeam(
        teamName: String,
        teamDescription: String,
        adminId: String,
        projectId: String,
        managerId: String,
        selectedEmp: MutableList<String>
    ) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiServices.createNewTeam(
                    NewTeam(
                        "$teamName",
                        "$teamDescription",
                        "$adminId",
                        "$managerId",
                        "$projectId",
                        "$selectedEmp"
                    ),
                    "token=$token"
                )

                if (response.isSuccessful){
                    val success = response.body()
                    if (success != null){
                        Log.d("alldata", "team created successfully!")
                        startActivity(Intent(this@TeamActivity, TeamList::class.java))
                    }else{
                        //Handle scenario where response body is null
                        Log.d("alldata error new project ", "Empty response body")
                    }
                }else{
                    val errorBody = response.errorBody()?.string()
                    Log.d("alldata error new team ", "Error: $errorBody")
                }
            }catch (e: Exception){
                Log.e("alldata", e.message.toString())
            }
        }
    }

    private fun getProjects() {

        //for api call
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiServices.getProjectDetails("token=$token")
                if (response.isSuccessful) {
                    val projectResponse = response.body()
                    if (projectResponse != null) {
                        for (project in projectResponse.allProject) {
//                            push all the project
                            allProject.add(project)
                        }

                        // Extract manager names from the list of employees
                        val projectName = allProject
                            .map { it.projectName }.toTypedArray()
                        // Create an ArrayAdapter using the managerNames array and a default spinner layout
                        val adapter = ArrayAdapter(
                            this@TeamActivity,
                            android.R.layout.simple_spinner_item,
                            projectName
                        )

                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Apply the adapter to the spinner
                        binding.spinnerProjectNName.adapter = adapter

                        binding.spinnerProjectNName.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val projName =
                                        projectName[position] // Get the selected priority
                                    // Handle the selected priority as needed
                                    val projectID =
                                        allProject.filter { it.projectName == projName }
                                            .joinToString { it._id }
                                    Log.d("idies", projectID)
                                        projectId =  projectID
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Handle case when nothing is selected (if needed)
                                }
                            }





                    } else {
                        // Handle scenario where response body is null
                        Log.d("project error", "Empty response body")
                    }
                } else {
                    // Handle unsuccessful login (e.g., invalid credentials, server errors)
                    val errorBody = response.errorBody()?.string()
                    Log.d("project error", "Error: $errorBody")
                }
            } catch (e: IOException) {
                // Handle other exceptions
                Log.d("project error", "Error: ${e.message}")
            }
        }

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

                        for (i in employeeResponse.data) {
                            if(i.designationType == "employee"){
                                employeeDetails.add(i)
                            }
                            if(i.designationType == "manager"){
                                managerList.add(i)
                            }
                        }



                        //load manager list in spinner

                        // Extract manager names from the list of employees
                        val managerNames = managerList.filter { it.designationType == "manager" }
                            .map { it.employeeName }.toTypedArray()
                        // Create an ArrayAdapter using the managerNames array and a default spinner layout
                        val adapter = ArrayAdapter(
                            this@TeamActivity,
                            android.R.layout.simple_spinner_item,
                            managerNames
                        )

                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        // Apply the adapter to the spinner
                        binding.spinnerManagerName.adapter = adapter

                        binding.spinnerManagerName.onItemSelectedListener =
                            object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(
                                    parent: AdapterView<*>?,
                                    view: View?,
                                    position: Int,
                                    id: Long
                                ) {
                                    val managerName =
                                        managerNames[position] // Get the selected priority
                                    // Handle the selected priority as needed
                                    Log.d("msg", managerName)
                                    val managerID =
                                        managerList.filter { it.employeeName == managerName }
                                            .joinToString { it._id }
                                     managerId = managerID
                                    Log.d("msg", managerID)
                                }

                                override fun onNothingSelected(parent: AdapterView<*>?) {
                                    // Handle case when nothing is selected (if needed)
                                }
                            }

                        //invoke
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
         employeeDetailsDup = employeeDetails.toMutableList()
        val recyclerView = findViewById<RecyclerView>(R.id.rv_employee_list)
        recyclerView.layoutManager = LinearLayoutManager(this@TeamActivity, LinearLayoutManager.VERTICAL, false)
        recyclerView.setHasFixedSize(true)

        val adapter = TeamMembersAdapter(this@TeamActivity, employeeDetailsDup)
        recyclerView.adapter = adapter

        adapter.setOnClickListener(object : TeamMembersAdapter.OnClickListener {
            override fun onCLick(position: Int, model: Employee) {
                Toast.makeText(this@TeamActivity, "clicked ${model.employeeName}", Toast.LENGTH_LONG).show()
                list.add(model)
                selectedEmp.add(model._id)
                selectedMembers(list, employeeDetailsDup)
                // Create a mutable copy of the original list to avoid modifying the input parameter
                // Remove the clicked item from the copy
                employeeDetailsDup.removeAt(position)
                // Update the adapter with the modified list
                adapter.notifyDataSetChanged()
            }
        })
    }

    private fun selectedMembers(list: MutableList<Employee>, employeeDetailsDup: MutableList<Employee>) {
        binding.rvSelectedMembers.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSelectedMembers.setHasFixedSize(true)

        val adapter = SelectedTeamMembersAdapter(this@TeamActivity, list)

        adapter.setOnClickListener(object :SelectedTeamMembersAdapter.OnClickListener{
            override fun onCLick(position: Int, model: Employee) {
                // Remove the clicked item from the selectedEmp and list
                if (position < selectedEmp.size) {
                    selectedEmp.removeAt(position)
                }
                if (position < list.size) {
                    list.removeAt(position)
                }

                // Notify the adapter that the data set has changed
                adapter.notifyDataSetChanged()

                // Add the removed item back to employeeDetailsDup
                if (!employeeDetailsDup.contains(model)) {
                    employeeDetailsDup.add(model)
                    showEmpList(employeeDetailsDup)
                }
            }
        })

        binding.rvSelectedMembers.adapter = adapter
    }


}