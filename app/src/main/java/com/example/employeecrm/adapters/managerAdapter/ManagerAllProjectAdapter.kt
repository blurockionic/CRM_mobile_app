package com.example.employeecrm.adapters.managerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employeecrm.R
import com.example.employeecrm.model.AllProject

class ManagerAllProjectAdapter(
    private val context: Context,
    private val list: MutableList<AllProject>
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onItemClick: OnClickListener? = null

    private var onAssignTaskClick: OnClickListener? = null

    private var onReportBtnClick: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.manager_all_project_card,
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


            holder.itemView.setOnClickListener {
                onItemClick?.onCLick(position, model)
            }

            //handle on assign task
            holder.itemView.findViewById<ImageView>(R.id.btn_assign_task).setOnClickListener {
                onAssignTaskClick?.onCLick(position, model)
            }

            //  handle on report task
            holder.itemView.findViewById<ImageView>(R.id.btn_report_project).setOnClickListener {
                onReportBtnClick?.onCLick(position, model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    fun setOnClickListener(onClickListener: OnClickListener){
        onItemClick = onClickListener
    }

    fun setOnAssignTaskListener(onClickListener: OnClickListener){
        onAssignTaskClick = onClickListener
    }

    fun setOnReportListener(onClickListener: OnClickListener){
        onReportBtnClick = onClickListener
    }


    interface OnClickListener{
        fun onCLick(position: Int, model: AllProject)
    }

    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}