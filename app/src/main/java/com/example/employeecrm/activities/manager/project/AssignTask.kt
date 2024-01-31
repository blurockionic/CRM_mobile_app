package com.example.employeecrm.activities.manager.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.employeecrm.R
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.databinding.ActivityAssignTaskBinding

class AssignTask : BaseActivity() {
    private lateinit var binding: ActivityAssignTaskBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}