package com.jordyf15.storyapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jordyf15.storyapp.R
import com.jordyf15.storyapp.data.remote.response.Story
import com.jordyf15.storyapp.databinding.ItemRowStoryBinding
import com.jordyf15.storyapp.utils.Utils

class ListStoryAdapter : PagingDataAdapter<Story, ListStoryAdapter.ListViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemRowStoryBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val story = getItem(position) ?: return

        holder.binding.tvItemName.text = story.name
        if (story.description.length > 80) {
            holder.binding.tvItemDescription.text = holder.itemView.context.getString(
                R.string.more_description,
                story.description.take(80)
            )
        } else {
            holder.binding.tvItemDescription.text = story.description
        }
        holder.binding.tvItemTimestamp.text = Utils.createdAtToNow(story.createdAt)
        Glide.with(holder.itemView.context)
            .load(story.photoUrl)
            .into(holder.binding.imgItemPhoto)

        holder.itemView.setOnClickListener {
            val optionsCompat: ActivityOptionsCompat =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                    holder.itemView.context as Activity,
                    Pair(holder.binding.tvItemDescription, "description"),
                    Pair(holder.binding.tvItemName, "name"),
                    Pair(holder.binding.tvItemTimestamp, "timestamp"),
                    Pair(holder.binding.imgProfilePic, "profilePicture"),
                    Pair(holder.binding.imgItemPhoto, "photo")
                )
            onItemClickCallback.onItemClicked(story, optionsCompat)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Story, optionsCompat: ActivityOptionsCompat)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}