package com.example.crudsqlite

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.crudsqlite.databinding.ItemRecyclerBinding

class RecyclerAdapter(private val helper: SqliteHelper) : RecyclerView.Adapter<RecyclerAdapter.Holder>() {
    val listData = mutableListOf<Member>()

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listData[position])

    }

    inner class Holder(private val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        private val divider: View = binding.divider

        fun bind(member: Member) {
            binding.textId.text = "${member.id}"
            binding.textName.text = member.name

            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra("memberId", member.id)
                context.startActivity(intent)
            }

            if (adapterPosition != listData.size - 1) {
                divider.visibility = View.VISIBLE
            } else {
                divider.visibility = View.GONE
            }
        }
    }
}
