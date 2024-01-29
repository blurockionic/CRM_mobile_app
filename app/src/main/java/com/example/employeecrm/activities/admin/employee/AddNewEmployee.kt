package com.example.employeecrm.activities.admin.employee

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.R
import com.example.employeecrm.activities.project.Projects
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.databinding.ActivityAddNewEmployeeBinding
import com.example.employeecrm.model.LoginManager
import com.example.employeecrm.model.NewEmployee
import com.example.employeecrm.model.ProjectRequest
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AddNewEmployee : BaseActivity() {
    private lateinit var binding :ActivityAddNewEmployeeBinding
    //selected gender
    private lateinit var selectedGender: String
    //selected dob
    private lateinit var selectedDOB:String
//    token
    private lateinit var token:String

    //base url
    private val BASE_URL = "http://192.168.1.5:4000/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Checking if a login response is stored and accessing its properties
        val storedLoginResponse = LoginManager.loginResponse

        token = storedLoginResponse?.token ?: getAuthToken()

        initializeUI()
    }

    private fun initializeUI() {




        //  gender
        val radioGroup = binding.radioGroupGender

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonMale -> {
                    // Call your function for Male
                    val gender = "male"
                    handleOnSelectedGender(gender)
                }
                R.id.radioButtonFemale -> {
                    // Call your function for Female
                    val gender = "female"
                    handleOnSelectedGender(gender)
                }
                R.id.radioButtonTransgender -> {
                    // Call your function for Transgender
                    val gender = "transgender"
                    handleOnSelectedGender(gender)
                }
            }
        }

        //btn submit
        binding.buttonSubmit.setOnClickListener {
            val employeeName = binding.etEmployeeName.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val dateOfBirth = binding.etDateOfBirth.text.toString()
            val email= binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val department = binding.etDepartment.text.toString()
            val designation =  binding.etDesignation.text.toString()
            val designationType = binding.etDesignationType.text.toString()
            val village = binding.etVillage.text.toString()
            val city = binding.etCity.text.toString()
            val address = binding.etAddress.text.toString()
            val pinCode = binding.etPin.text.toString()
            val policeStation = binding.etPoliceStation.text.toString()
            val state = binding.etState.text.toString()
            // Log the values
            Log.d("YourTag", "Employee Name: $employeeName")
            Log.d("YourTag", "Phone Number: $phoneNumber")
            Log.d("YourTag", "Date of Birth: $dateOfBirth")
            Log.d("YourTag", "Email: $email")
            Log.d("YourTag", "Password: $password")
            Log.d("YourTag", "Department: $department")
            Log.d("YourTag", "Designation: $designation")
            Log.d("YourTag", "Designation Type: $designationType")
            Log.d("YourTag", "Village: $village")
            Log.d("YourTag", "City: $city")
            Log.d("YourTag", "Address: $address")
            Log.d("YourTag", "Pin Code: $pinCode")
            Log.d("YourTag", "Police Station: $policeStation")
            Log.d("YourTag", "State: $state")
            Log.d("YourTag", "gender: $selectedGender")

            //handle on submit the data
            handleOnSubmitEmpDetails(
                employeeName,
                phoneNumber,
                dateOfBirth,
                email,
                password,
                department,
                designation,
                designationType,
                village,
                city,
                address,
                pinCode,
                policeStation,
                state,
                selectedGender
                )
        }
    }

    private fun handleOnSubmitEmpDetails(
        employeeName: String,
        phoneNumber: String,
        dateOfBirth: String,
        email: String,
        password: String,
        department: String,
        designation: String,
        designationType: String,
        village: String,
        city: String,
        address: String,
        pinCode: String,
        policeStation: String,
        state: String,
        selectedGender: String
    ) {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiServices = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiServices.addNewEmployee( NewEmployee(
                    "$employeeName",
                    "$selectedGender",
                    "$email",
                    "$password",
                    "$phoneNumber",
                    "$dateOfBirth",
                    "$address",
                    "tantnagar",
                    "$policeStation",
                    "$city",
                    "$state",
                    "$pinCode",
                    "$designation",
                    "$designationType",
                    "$department"
                    ),
                    "token=$token")
                if (response.isSuccessful) {
                    val success = response.body()

                    if (success != null) {
                        // Handle successful login response
                        Toast.makeText(this@AddNewEmployee, "Successfully Registered! ${response.message()}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@AddNewEmployee, EmployeeList::class.java))
                    } else {
                        //Handle scenario where response body is null
                        Log.d("error on submission ", "Empty response body")
                    }
                } else {
                    // Handle unsuccessful login (e.g., invalid credentials, server errors)
                    val errorBody = response.errorBody()?.string()
                    Log.d("error on submission ", "Error: $errorBody")
                    Toast.makeText(this@AddNewEmployee, response.message(), Toast.LENGTH_SHORT).show()
                }
            }catch (e: Exception){
                Log.d(e.message, e.message.toString())
            }
        }
    }



    private fun handleOnSelectedGender(gender: String) {
        selectedGender = gender
        Toast.makeText(this, "$selectedGender", Toast.LENGTH_SHORT).show()
    }

    fun showStartDatePickerDialog(view: View) {
        val startDate = findViewById<EditText>(R.id.et_date_of_birth)

        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

        picker.addOnPositiveButtonClickListener { selection ->
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val formattedDate = sdf.format(Date(selection))
            startDate.setText(formattedDate)

        }

        picker.show(supportFragmentManager, picker.toString())

    }

}