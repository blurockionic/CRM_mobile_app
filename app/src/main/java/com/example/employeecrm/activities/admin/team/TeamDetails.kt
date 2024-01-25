package com.example.employeecrm.activities.admin.team

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.employeecrm.R
import com.example.employeecrm.databinding.ActivityTeamDetailsBinding

class TeamDetails : AppCompatActivity() {
    private lateinit var binding: ActivityTeamDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityTeamDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val teamName = intent.getStringExtra("teamName")
        val teamDescription = intent.getStringExtra("teamDescription")


        binding.tvTeamName.text =  teamName
        binding.descriptionTextView.text = teamDescription

    }
}