package com.example.employeecrm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employeecrm.R
import com.example.employeecrm.model.Project

open class AllProjectAdapter (
    private val context: Context,
    private val list: MutableList<Project>
):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    private val onClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
      return MyViewHolder(
          LayoutInflater.from(context).inflate(
              R.layout.project_card,
              parent,
              false
          )
      )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            holder.itemView.findViewById<TextView>(R.id.tv_projectName).text = model.projectName
            holder.itemView.findViewById<TextView>(R.id.tv_created_at).text = model.projectStartDate
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface OnClickListener{
        fun onCLick(position: Int, model: Project)
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}