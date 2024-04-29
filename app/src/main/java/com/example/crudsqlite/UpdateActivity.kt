package com.example.crudsqlite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crudsqlite.databinding.ActivityUpdateBinding

class UpdateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding
    private lateinit var helper: SqliteHelper
    private lateinit var originalMember: Member

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = SqliteHelper(this, "member", 1)

        // DetailActivity에서 넘겨준 memberId 받기
        val memberId = intent.getLongExtra("memberId", -1)

        // memberId로 회원 정보 가져오기
        originalMember = helper.getMemberById(memberId) ?: Member(null, "", 0, "")

        // 가져온 회원 정보를 EditText의 기본값으로 설정
        displayMemberDetails(originalMember)

        binding.updateMember.setOnClickListener {
            updateMember()
        }

        binding.cancelUpdate.setOnClickListener {
            finish()
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

        // EditText의 TextWatcher 설정
        binding.textMobile.addTextChangedListener(object : TextWatcher {
            private var isFormatting: Boolean = false

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // 이전 텍스트 변경 전에 실행되는 부분
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // 텍스트 변경 중에 실행되는 부분
            }

            override fun afterTextChanged(editable: Editable?) {
                if (!isFormatting) {
                    isFormatting = true

                    val text = editable.toString().replace("-", "")

                    // 하이픈 제거 후 번호 형식에 맞게 재구성
                    val formattedText = StringBuilder()
                    for (i in text.indices) {
                        if (i == 3 || i == 7) {
                            formattedText.append("-")
                        }
                        formattedText.append(text[i])
                    }

                    binding.textMobile.setText(formattedText)
                    binding.textMobile.setSelection(formattedText.length)

                    isFormatting = false
                }
            }
        })
    }

    private fun displayMemberDetails(member: Member) {
        binding.textName.setText(member.name)
        binding.textAge.setText(member.age.toString())
        binding.textMobile.setText(member.mobile)
    }

    private fun updateMember() {
        // DetailActivity에서 넘겨준 memberId 받기
        val memberId = intent.getLongExtra("memberId", -1)

        val updatedName = binding.textName.text.toString()
        val updatedAge = binding.textAge.text.toString().toIntOrNull() ?: 0
        val updatedMobile = binding.textMobile.text.toString()

        val updatedMember = Member(originalMember.id, updatedName, updatedAge, updatedMobile)
        helper.updateMember(updatedMember)

        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("memberId", memberId)
        startActivity(intent)
        showToast("회원 정보를 수정하였습니다.")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
