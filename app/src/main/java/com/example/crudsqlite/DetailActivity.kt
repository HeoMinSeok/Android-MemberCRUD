package com.example.crudsqlite

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.crudsqlite.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var helper: SqliteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = SqliteHelper(this, "member", 1)

        // MainActivity에서 넘겨준 memberId 받기
        val memberId = intent.getLongExtra("memberId", -1)

        // memberId로 회원 정보 가져오기
        val member = helper.getMemberById(memberId)

        // 가져온 회원 정보를 화면에 표시
        displayMemberDetails(member)

        binding.updateMember.setOnClickListener {
            val intent = Intent(this, UpdateActivity::class.java)
            intent.putExtra("memberId", memberId)
            startActivity(intent)
        }

        binding.deleteMember.setOnClickListener {
            if (member != null) {
                deleteConfirm(member)
            }
        }

        binding.listMember.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

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
    }

    private fun displayMemberDetails(member: Member?) {
        member?.let {
            binding.textName.text = it.name
            binding.textAge.text = it.age.toString()
            binding.textMobile.text = it.mobile
        }
    }

    private fun deleteMember(member: Member) {
        val memberId = intent.getLongExtra("memberId", -1)
        helper.deleteMember(member)

        val intent = Intent(this, ListActivity::class.java)
        intent.putExtra("memberId", memberId)
        startActivity(intent)
        showToast("등록된 회원을 삭제하였습니다.")

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun deleteConfirm(member: Member) {
        val builder = AlertDialog.Builder(this)
        builder.apply {
            setTitle("회원 삭제")
            setMessage("${member.name} 회원을 삭제하시겠습니까?")
            setPositiveButton("확인") { dialog, which ->
                deleteMember(member)
            }
            setNegativeButton("취소") { dialog, which ->
                dialog.dismiss()
            }
        }
        val dialog = builder.create()
        dialog.show()
    }
}
