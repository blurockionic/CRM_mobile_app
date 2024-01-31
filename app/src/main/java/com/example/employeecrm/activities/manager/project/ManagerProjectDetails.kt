package com.example.employeecrm.activities.manager.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.employeecrm.R
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.databinding.ActivityManagerProjectDetailsBinding

class ManagerProjectDetails : BaseActivity() {
    private lateinit var binding: ActivityManagerProjectDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =  ActivityManagerProjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val projectName = intent.getStringExtra("projectName")
        val projectDescription = intent.getStringExtra("projectDescription")

        Log.d("project", "$projectDescription , $projectName")

        binding.tvProjectName.text = "Project Name:- $projectName"
        binding.tvProjectDescription.text = "Description:- \n $projectDescription"
    }
}