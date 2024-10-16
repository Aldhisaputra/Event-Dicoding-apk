package com.bangkit.eventdicoding


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.eventdicoding.databinding.ListEventBinding
import com.bumptech.glide.Glide

class EventAdapter(
    private val onClickItemListener: (ListEventsItem) -> Unit
) : ListAdapter<ListEventsItem, EventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ListEventBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), onClickItemListener)
    }

    class MyViewHolder(private val binding: ListEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem, onItemClickListener: (ListEventsItem) -> Unit) {
            Glide.with(itemView.context).load(event.mediaCover).into(binding.ivEventPicture)
            binding.tvEventTitle.text = event.name

            itemView.setOnClickListener { onItemClickListener(event) }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem == newItem
            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem) = oldItem == newItem
        }
    }
}
