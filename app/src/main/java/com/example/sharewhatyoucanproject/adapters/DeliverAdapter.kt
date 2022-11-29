package com.example.sharewhatyoucanproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.models.PostModel

class DeliverAdapter(
    private val onPostClicked: (PostModel) -> Unit,
) : RecyclerView.Adapter<DeliverAdapter.DeliverViewHolder>() {
    val arrayList = mutableListOf<PostModel>()

    fun setDeliverList(list: List<PostModel>) {
        arrayList.clear()
        arrayList.addAll(list)
    }

    inner class DeliverViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postTitle: TextView = itemView.findViewById(R.id.posttitle)
        val postDesc: TextView = itemView.findViewById(R.id.postdesc)
        val postImg: ImageView = itemView.findViewById(R.id.postimg)

        fun bind(postModel: PostModel) {
            postTitle.text = postModel.title
            postDesc.text = postModel.desc
            postImg.load(arrayList[position].image)
            itemView.setOnClickListener {
                onPostClicked(postModel)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): DeliverAdapter.DeliverViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.postitem, parent, false)
        return DeliverViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliverAdapter.DeliverViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
