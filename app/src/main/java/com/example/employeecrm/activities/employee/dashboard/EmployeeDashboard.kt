package com.example.employeecrm.activities.employee.dashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.employeecrm.R
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.databinding.ActivityEmployeeDashboardBinding
import com.example.employeecrm.model.LoginManager
import com.example.employeecrm.model.LoginResponse

class EmployeeDashboard : BaseActivity() {
    private lateinit var binding : ActivityEmployeeDashboardBinding
    private lateinit var token :String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployeeDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setUpActionBar()

        val storedLoginResponse = LoginManager.loginResponse

        token = storedLoginResponse?.token ?: getAuthToken()

        updateNavigationUserDetails(storedLoginResponse)

//        navigate to the different pages
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.logout->{
                    logout()
                }
                else->{
                    //
                }
            }

            findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(GravityCompat.START)
            true // R
        }


    }

    private fun updateNavigationUserDetails(storedLoginResponse: LoginResponse?) {
        val header = binding.navigationView.getHeaderView(0)
        val userName = header.findViewById<TextView>(R.id.tv_name)
        val userDesignation = header.findViewById<TextView>(R.id.tv_designation)

        if (storedLoginResponse != null && userName != null) {
            userName.text = "Name: ${storedLoginResponse.user.name}"
            userDesignation.text = "Desination: ${storedLoginResponse.user.designation}"
        }
    }

    private fun setUpActionBar() {
//        set drawer layout
        setSupportActionBar(findViewById<Toolbar>(R.id.my_toolbar))

        findViewById<Toolbar>(R.id.my_toolbar).setNavigationIcon(R.drawable.home)

        findViewById<Toolbar>(R.id.my_toolbar).setNavigationOnClickListener {
            toggleDrawer()
        }
    }

    //    toggle the navigation bar
    private fun toggleDrawer() {
        if (findViewById<DrawerLayout>(R.id.drawerLayout).isDrawerOpen(GravityCompat.START)) {
            findViewById<DrawerLayout>(R.id.drawerLayout).closeDrawer(GravityCompat.START)
        } else {
            findViewById<DrawerLayout>(R.id.drawerLayout).openDrawer(GravityCompat.START)
        }
    }
}
