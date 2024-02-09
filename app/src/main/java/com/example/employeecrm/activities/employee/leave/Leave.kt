package com.example.employeecrm.activities.employee.leave

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.example.employeecrm.R
import com.example.employeecrm.databinding.ActivityLeaveBinding

class Leave : AppCompatActivity() {
    private lateinit var binding: ActivityLeaveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaveBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Populate the spinner with leave types
        val leaveTypes = arrayOf("Vacation", "Sick Leave", "Personal Leave", "Other")
        val leaveTypeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, leaveTypes)
        leaveTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.leaveTypeSpinner.adapter = leaveTypeAdapter

        // handle for apply leave button
        binding.submitButton.setOnClickListener {
            // invoke apply leave
            applyLeave()
        }
    }

    // handle for apply leave
    private fun applyLeave() {

        val leaveType = binding.leaveTypeSpinner.selectedItem.toString()
        val fromDate = binding.fromDateEditText.text.toString()
        val toDate = binding.toDateEditText.text.toString()
        val reason = binding.reasonEditText.text.toString()

        Log.d("leave apply", "Leave Type: $leaveType")
        Log.d("leave apply", "From Date: $fromDate")
        Log.d("leave apply", "To Date: $toDate")
        Log.d("leave apply", "Reason: $reason")

       //send request ot the server
    }
}
