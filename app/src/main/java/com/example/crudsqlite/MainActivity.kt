package com.example.crudsqlite

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.crudsqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: RecyclerAdapter
    private lateinit var helper: SqliteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        helper = SqliteHelper(this, "member", 1)

        binding.registerMember.setOnClickListener {
            registerDialog()
        }

        binding.homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.listBtn.setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }

        // TextView를 보이게 설정
        binding.memberCountTextView.visibility = View.VISIBLE

        // 회원 수를 가져와서 TextView에 설정
        updateMemberCount()
    }

    private fun saveMemberToDataBase(name: String, age: Int, mobile: String) {
        val member = Member(
            null,
            name,
            age,
            mobile
        )
        helper.insertMember(member)

        // 회원이 등록될 때마다 회원 수 업데이트
        updateMemberCount()
    }

    private fun registerDialog() {
        val dialogView = layoutInflater.inflate(R.layout.register_dialog_layout, null)
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("회원 등록")
            .setView(dialogView)
            .setPositiveButton("확인") { dialog, which ->
                // 확인 버튼을 눌렀을 때의 동작
                val editTextName = dialogView.findViewById<EditText>(R.id.editTextName)
                val editTextAge = dialogView.findViewById<EditText>(R.id.editTextAge)
                val editTextMobile = dialogView.findViewById<EditText>(R.id.editTextMobile)

                val name = editTextName.text.toString()
                val age = editTextAge.text.toString().toIntOrNull() ?: 0
                val mobile = editTextMobile.text.toString()

                if (name.isNotEmpty() && age > 0 && mobile.isNotEmpty()) {
                    // 입력이 유효한 경우에만 데이터를 저장하고 갱신
                    saveMemberToDataBase(name, age, mobile)
                    showToast("회원이 등록되었습니다.")
                } else {
                    showToast("모든 필드를 입력해주세요.")
                }
            }
            .setNegativeButton("취소", null)

        val dialog = dialogBuilder.create()
        dialog.show()

        val editTextMobile = dialogView.findViewById<EditText>(R.id.editTextMobile) // editTextMobile 정의
        editTextMobile.addTextChangedListener(object : TextWatcher {
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

                    editTextMobile.setText(formattedText)
                    editTextMobile.setSelection(formattedText.length)

                    isFormatting = false
                }
            }
        })
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // 등록된 회원 수를 가져와 TextView에 설정하는 함수
    private fun updateMemberCount() {
        val memberCount = helper.getMemberCount()
        binding.memberCountTextView.text = "등록된 회원: ${memberCount}명"
    }
}
