package com.example.sharewhatyoucanproject.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.sharewhatyoucanproject.R
import com.example.sharewhatyoucanproject.models.PostModel

class PostAdapter(
    private val onPostClicked: (PostModel) -> Unit,
) : RecyclerView.Adapter<PostAdapter.PostsViewHolder>() {

    val arrayList = mutableListOf<PostModel>()

    fun setPostsList(list: List<PostModel>) {
        arrayList.clear()
        arrayList.addAll(list)
    }

    inner class PostsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val posttitle: TextView = itemView.findViewById(R.id.posttitle)
        val postdesc: TextView = itemView.findViewById(R.id.postdesc)
        val postimg: ImageView = itemView.findViewById(R.id.postimg)

        fun bind(postModel: PostModel) {
            posttitle.text = postModel.title
            postdesc.text = postModel.desc
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
        holder.postimg.scaleType = ImageView.ScaleType.CENTER_CROP
        holder.postimg.load(arrayList[position].image) {
            transformations(RoundedCornersTransformation(32f))
            holder.bind(arrayList[position])
        }
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
}
