package com.example.employeecrm.activities.admin.team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.employeecrm.R
import com.example.employeecrm.databinding.ActivityTeamBinding

class TeamActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTeamBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.btnAddMember.setOnClickListener {
            Toast.makeText(this@TeamActivity, "Working on this module", Toast.LENGTH_SHORT).show()
        }
    }
}