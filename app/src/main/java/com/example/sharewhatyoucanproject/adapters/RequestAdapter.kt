package com.example.sharewhatyoucanproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.models.RequestModel
import com.google.firebase.auth.FirebaseAuth

class RequestAdapter(
    private val onPostClicked: (RequestModel, String) -> Unit,
) : RecyclerView.Adapter<RequestAdapter.RequestsViewHolder>() {

    private val arrayList = mutableListOf<RequestModel>()

    fun setRequestList(list: List<RequestModel>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    fun updateRequestListStatus(uid: String) {
        arrayList.findLast { it.uid == uid }?.apply { status = 1 }
        notifyDataSetChanged()
    }

    lateinit var auth: FirebaseAuth

    inner class RequestsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rname: TextView = itemView.findViewById(R.id.rname)
        val approvebtn: Button = itemView.findViewById(R.id.approvebtn)

        fun bind(requestModel: RequestModel) {
            rname.text = requestModel.name

            approvebtn.setOnClickListener {
                onPostClicked(requestModel, approvebtn.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.requestitem, parent, false)
        return RequestsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestAdapter.RequestsViewHolder, position: Int) {
        holder.rname.text = arrayList[position].name
        holder.bind(arrayList[position])

        if (arrayList[position].status == 1) {
            holder.approvebtn.text = "Deliver"
            holder.approvebtn.setBackgroundResource(R.drawable.graycurve)
        } else if (arrayList[position].status == 2) {
            holder.approvebtn.text = "Completed"
            holder.approvebtn.setBackgroundResource(R.drawable.greencurve)
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
