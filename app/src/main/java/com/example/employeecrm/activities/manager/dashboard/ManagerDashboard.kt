package com.example.employeecrm.activities.manager.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.employeecrm.R
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.databinding.ActivityManagerDashboardBinding

class ManagerDashboard : BaseActivity() {
    private lateinit var binding: ActivityManagerDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogout.setOnClickListener {
            logout()
        }

    }
}