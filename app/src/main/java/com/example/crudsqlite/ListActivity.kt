package com.example.crudsqlite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.crudsqlite.databinding.ActivityListBinding

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var helper: SqliteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener {
            onBackPressed() // 이전 페이지로 이동
        }

        binding.listBtn.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        helper = SqliteHelper(this, "member", 1)
        adapter = RecyclerAdapter(helper)

        binding.recyclerMember.adapter = adapter
        binding.recyclerMember.layoutManager = LinearLayoutManager(this)

        loadData()
    }

    private fun loadData() {
        val memberList = helper.selectMember()
        if (memberList.isEmpty()) {
            binding.registerMember.visibility = View.VISIBLE
            binding.noMemberText.visibility = View.VISIBLE
        } else {
            binding.registerMember.visibility = View.GONE
            binding.noMemberText.visibility = View.GONE

            adapter.listData.clear()
            adapter.listData.addAll(memberList)
            adapter.notifyDataSetChanged()
        }
    }
}
