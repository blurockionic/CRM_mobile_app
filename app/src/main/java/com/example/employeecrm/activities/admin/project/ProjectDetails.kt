package com.example.employeecrm.activities.admin.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.employeecrm.R
import com.example.employeecrm.databinding.ActivityProjectDetailsBinding

class ProjectDetails : AppCompatActivity() {
    private lateinit var binding: ActivityProjectDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val projectName = intent.getStringExtra("projectName")
        val projectDescription = intent.getStringExtra("projectDescription")

        Log.d("project", "$projectDescription , $projectName")

        binding.tvProjectName.text = "Project Name:- $projectName"
        binding.tvProjectDescription.text = "Description:- \n $projectDescription"
    }
}