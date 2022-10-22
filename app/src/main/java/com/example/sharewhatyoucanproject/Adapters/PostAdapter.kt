package com.example.sharewhatyoucanproject.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.models.PostModel

class PostAdapter(
    private val context: Context,
    private val onPostClicked: (PostModel) -> Unit,
) : RecyclerView.Adapter<PostAdapter.PostsViewHolder>() {

    private val arrayList = mutableListOf<PostModel>()

    fun setPostsList(list: MutableList<PostModel>) {
        arrayList.clear()
        arrayList.addAll(list)
        notifyDataSetChanged()
    }

    inner class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var posttitle: TextView = itemView.findViewById(R.id.posttitle)
        var postdesc: TextView = itemView.findViewById(R.id.postdesc)
        var postimg: ImageView = itemView.findViewById(R.id.postimg)

        fun bind(postModel: PostModel) {
            posttitle.text = postModel.title
            postdesc.text = postModel.desc
            Glide.with(context)
                .load(postModel.image)
                .into(postimg)

            itemView.setOnClickListener {
                onPostClicked(postModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.postitem, parent, false)
        return PostsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostAdapter.PostsViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
