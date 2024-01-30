package com.example.employeecrm.activities.employee.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.employeecrm.R
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.databinding.ActivityEmployeeDashboardBinding

class EmployeeDashboard : BaseActivity() {
    private lateinit var binding : ActivityEmployeeDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
           logout()
        }
    }
}
