package com.rakha.pert5_pmob

import android.net.Uri
import android.view.*
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

data class Postt(
    val username: String,
    val caption: String,
    val profileImage: Int = R.drawable.asset1,
    val postImage: Int = 0,
    val imageUri: String? = null
)

class PostAdapter(
    private var posts: MutableList<Post>,
    private val onEditClick: (Int, Post) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPostProfile: ImageView = itemView.findViewById(R.id.ivProfile)
        val tvPostUsername: TextView = itemView.findViewById(R.id.tvPostUsername)
        val ivPostImage: ImageView = itemView.findViewById(R.id.ivPostImage)
        val tvPostCaption: TextView = itemView.findViewById(R.id.tvPostCaption)
        val ibMenu: ImageButton = itemView.findViewById(R.id.ibMenu)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val postt = posts[position]
        holder.tvPostUsername.text = postt.username
        holder.tvPostCaption.text = postt.caption

        if (postt.imageUri != null) {
            holder.ivPostImage.setImageURI(Uri.parse(postt.imageUri))
        } else if (postt.postImage != 0) {
            holder.ivPostImage.setImageResource(postt.postImage)
        } else {
            holder.ivPostImage.setImageDrawable(null)
        }

        holder.ibMenu.setOnClickListener { view ->
            val popup = PopupMenu(view.context, view)
            popup.menuInflater.inflate(R.menu.add, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                val currentPos = holder.bindingAdapterPosition
                if (currentPos == RecyclerView.NO_POSITION) return@setOnMenuItemClickListener false

                when (item.itemId) {
                    R.id.menu_edit -> {
                        val currentPost = posts[currentPos]
                        onEditClick(currentPos, currentPost)
                        true
                    }
                    R.id.menu_delete -> {
                        onDeleteClick(currentPos)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    override fun getItemCount(): Int = posts.size

    fun updatePost(position: Int, updatedPost: Post) {
        if (position in 0 until posts.size) {
            posts[position] = updatedPost
            notifyItemChanged(position)
        }
    }

    fun deletePost(position: Int) {
        if (position in 0 until posts.size) {
            posts.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, posts.size - position)
        }
    }
    fun getPostUsername(position: Int): String {
        return if (position in 0 until posts.size) posts[position].username else "unknown"
    }
}


