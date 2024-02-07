package com.example.employeecrm.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.employeecrm.R
import com.example.employeecrm.model.AllTeamsData

class AllTeamListAdapter(
    private val context: Context,
    private val list: MutableList<AllTeamsData>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClick: OnClickListener? = null
    private var onEditBtnClick: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.team_list_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder) {
            holder.itemView.findViewById<TextView>(R.id.tv_team_name).text = model.teamName

            holder.itemView.setOnClickListener {
                onItemClick?.onCLick(position, model)
            }

            // Assuming you have an edit button in your team_list_card layout
            holder.itemView.findViewById<ImageView>(R.id.btn_edit).setOnClickListener {
                onEditBtnClick?.onCLick(position, model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setOnItemClickListener(onClickListener: OnClickListener) {
        onItemClick = onClickListener
    }

    fun setOnEditBtnClickListener(onClickListener: OnClickListener) {
        onEditBtnClick = onClickListener
    }

    interface OnClickListener {
        fun onCLick(position: Int, model: AllTeamsData)
    }

    private class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
