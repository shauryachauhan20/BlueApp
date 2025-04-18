package com.example.neosofttechtestapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neosofttechtestapp.dataSource.model.ContentListItem
import com.example.neosofttechtestapp.databinding.VerticalListItemLayoutBinding

class ContentAdapter(private val items: List<ContentListItem>) :
    RecyclerView.Adapter<ContentAdapter.ContentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentViewHolder {
        val binding = VerticalListItemLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ContentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ContentViewHolder, position: Int) {
        holder.binding.dataItem = items[position]
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = items.size

    class ContentViewHolder(val binding: VerticalListItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)
}