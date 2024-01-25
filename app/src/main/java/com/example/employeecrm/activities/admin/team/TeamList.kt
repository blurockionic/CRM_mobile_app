package com.example.employeecrm.activities.admin.team

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.adapters.AllTeamListAdapter
import com.example.employeecrm.databinding.ActivityTeamListBinding
import com.example.employeecrm.model.LoginManager
import com.example.employeecrm.model.Team
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TeamList : AppCompatActivity() {
    private lateinit var binding: ActivityTeamListBinding

    private  var allTeam: MutableList<Team> = mutableListOf()

    private lateinit var token : String

    private  var BASE_URL = "http://192.168.1.8:4000/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeamListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Checking if a login response is stored and accessing its properties
        val storedLoginResponse = LoginManager.loginResponse

        if (storedLoginResponse != null) {
            token = storedLoginResponse.token
        }

        binding.btnCreateNewTeam.setOnClickListener {
            startActivity(Intent(this, TeamActivity::class.java))
        }

        getAllTeam()
    }

    private fun getAllTeam() {
        val retrofit =Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiServices.allTeam("token=$token")
                if(response.isSuccessful){
                    val teamResponse = response.body()
                    if (teamResponse !=null){
                        for (team in teamResponse.allTeams){
                            allTeam.add(team)
                        }

                        handleOnShowTeamList(allTeam)
                    }
                }else{
                    Log.d("error", "error in else block")
                }
            }catch (e:Exception){
                Log.d("error", e.message.toString())
            }
        }
    }

    private fun handleOnShowTeamList(allTeam: MutableList<Team>) {
        Log.d("allteam", allTeam.toString())
        binding.rvTeamList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false
        )
        binding.rvTeamList.setHasFixedSize(true)

        val adapter = AllTeamListAdapter(this@TeamList, allTeam)

        binding.rvTeamList.adapter = adapter

        //function on click team
        adapter.setOnItemClickListener(object : AllTeamListAdapter.OnClickListener{
            override fun onCLick(position: Int, model: Team) {
                Toast.makeText(this@TeamList, "clicked ${model.teamName}", Toast.LENGTH_LONG).show()
                val intent = Intent(this@TeamList, TeamDetails::class.java)
                intent.putExtra("teamName", model.teamName)
                intent.putExtra("teamDescription", model.teamDescription)
                startActivity(intent)
            }
        })

        //function on click on edit
        adapter.setOnEditBtnClickListener(object : AllTeamListAdapter.OnClickListener{
            override fun onCLick(position: Int, model: Team) {
                Toast.makeText(this@TeamList, "edit ${model.teamName}", Toast.LENGTH_LONG).show()
                val intent = Intent(this@TeamList, TeamActivity::class.java)
                intent.putExtra("teamName", model.teamName)
                intent.putExtra("teamId", model._id)

                startActivity(intent)
            }
        })
    }
}