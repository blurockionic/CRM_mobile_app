package com.example.employeecrm.activities.manager.project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.adapters.managerAdapter.ManagerAllProjectAdapter
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.constant.Constant
import com.example.employeecrm.databinding.ActivityManagerProjectBinding
import com.example.employeecrm.model.AllProject
import com.example.employeecrm.model.AllTeamsData
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ManagerProject : BaseActivity() {
    private lateinit var binding :  ActivityManagerProjectBinding

    // for all project
    private val allProject: MutableList<AllProject> = mutableListOf()

    private val myTeam :MutableList<AllTeamsData> = mutableListOf()

    private val BASE_URL = Constant.server

    private  var  managerId : String = ""

    private lateinit var token: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManagerProjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        token = getAuthToken()


        val employeeId = getEmployeeId()
        Log.d("employeeId", employeeId)


//     get project of specific manager
        getProject(employeeId)

//        invoke to get all the teams
        getAllTeam(employeeId)
    }

//    handle for get all the team
    private fun getAllTeam(employeeId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiServices.getAllTeam("token=$token")
                val responseBody =  response.body()
                if(responseBody != null){
                    Log.d("responseSuccess",employeeId)
                    for(i in responseBody.allTeamsData){
                         Log.d("responseSuccess", i.selectedManager._id)
                         if (i.selectedManager._id == employeeId){
                              myTeam.add(i);
                             Log.d("responseSuccess",myTeam.toString())
                         }
                    }

                }else{
                    Log.e("Error","all tem not fetched successfully")
                }
            }catch (e:Exception){
                Log.e("Error","${e.message}")
            }
        }


    }
//    handle for get all related to manager
    private fun getProject(employeeId: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val apiService = retrofit.create(Apis::class.java)

    lifecycleScope.launch {
        try {
            val response = apiService.getProjectDetails("token=$token")
            if(response.isSuccessful){
                val projectResponse = response.body()
                if (projectResponse != null){
                          Log.d("response project", projectResponse.toString())
//                        val proj = projectResponse.allProject.filter { it -> it.managerId == employeeId }
//                        allProject.addAll(proj)
//                    invoke
                    showManagerProjectList(allProject)
                }else{
                    showToast("response is null")
                }
            }else{
                showToast("response is not successful")
            }
        }catch (e:Exception){
            Log.d("projectError", e.message.toString())
        }
    }
    }

    private fun showManagerProjectList(allProject: MutableList<AllProject>) {
        binding.rvManagerProject.layoutManager = LinearLayoutManager(this@ManagerProject, LinearLayoutManager.VERTICAL, false)
        binding.rvManagerProject.setHasFixedSize(true)

        val adapter = ManagerAllProjectAdapter(this@ManagerProject, allProject)
        binding.rvManagerProject.adapter = adapter

        adapter.setOnClickListener(object : ManagerAllProjectAdapter.OnClickListener{
            override fun onCLick(position: Int, model: AllProject) {
                showToast("clicked on project")
                startActivity(Intent(this@ManagerProject, ManagerProjectDetails::class.java).putExtra("projectName", model.projectName).putExtra("projectDescription", model.description))
            }
        })

        adapter.setOnAssignTaskListener(object : ManagerAllProjectAdapter.OnClickListener{
            override fun onCLick(position: Int, model: AllProject) {
                showToast("clicked on assign task")
                showToast("Clicked on assign task")
                val intent = Intent(this@ManagerProject, AssignTask::class.java)
                intent.putExtra("projectName", model.projectName)
                intent.putExtra("projectId", model._id)
                startActivity(intent)
            }
        })

        adapter.setOnReportListener(object : ManagerAllProjectAdapter.OnClickListener{
            override fun onCLick(position: Int, model: AllProject) {
                showToast("click on report project ")
            }
        })
    }
}