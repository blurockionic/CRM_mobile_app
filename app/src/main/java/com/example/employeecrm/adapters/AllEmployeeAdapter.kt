package com.example.employeecrm.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employeecrm.R
import com.example.employeecrm.model.Employee

class AllEmployeeAdapter(
    private val context: Context,
    private val list: List<Employee>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private var onItemClick: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.employee_details_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            Log.d("employee", "hello ji")
            holder.itemView.findViewById<TextView>(R.id.tv_employeeName).text = "emp: ${model.employeeName}"
            holder.itemView.findViewById<TextView>(R.id.tv_designation).text = model.designation

            holder.itemView.setOnClickListener {
                onItemClick?.onCLick(position, model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        onItemClick = onClickListener
    }

    interface OnClickListener{
        fun onCLick(position: Int, model: Employee)
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}