package com.example.crudsqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SqliteHelper(context: Context, name: String, version: Int) :
    SQLiteOpenHelper(context, name, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        val create = "create table member (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "age INTEGER," +
                "mobile TEXT" +
                ")"

        db?.execSQL(create)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun insertMember(member: Member): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("name", member.name)
            put("age", member.age)
            put("mobile", member.mobile)
        }

        val regi = db.insert("member", null, contentValues)
        db.close()

        return regi
    }

    fun selectMember(): MutableList<Member> {
        val list = mutableListOf<Member>()

        val select = "select * from member"
        val rdMember = readableDatabase
        val cursor = rdMember.rawQuery(select, null)
        while (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex("id")
            val nameIndex = cursor.getColumnIndex("name")
            val ageIndex = cursor.getColumnIndex("age")
            val mobileIndex = cursor.getColumnIndex("mobile")

            val memberId = cursor.getLong(idIndex)
            val memberName = cursor.getString(nameIndex)
            val memberAge = cursor.getInt(ageIndex)
            val memberMobile = cursor.getString(mobileIndex)

            val member = Member(memberId, memberName, memberAge, memberMobile)
            list.add(member)
        }
        cursor.close()
        rdMember.close()
        return list
    }

    fun updateMember(member: Member) {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("name", member.name)
            put("age", member.age)
            put("mobile", member.mobile)
        }

        db.update("member", contentValues, "id=?", arrayOf(member.id.toString()))
        db.close()
    }

    fun deleteMember(member: Member) {
        val db = writableDatabase
        db.delete("member", "id=?", arrayOf(member.id.toString()))
        db.close()
    }

    fun getMemberById(memberId: Long): Member? {
        val select = "SELECT * FROM member WHERE id = ?"
        val rdMember = readableDatabase
        val cursor = rdMember.rawQuery(select, arrayOf(memberId.toString()))
        var member: Member? = null
        if (cursor.moveToNext()) {
            val idIndex = cursor.getColumnIndex("id")
            val nameIndex = cursor.getColumnIndex("name")
            val ageIndex = cursor.getColumnIndex("age")
            val mobileIndex = cursor.getColumnIndex("mobile")

            val memberId = cursor.getLong(idIndex)
            val memberName = cursor.getString(nameIndex)
            val memberAge = cursor.getInt(ageIndex)
            val memberMobile = cursor.getString(mobileIndex)

            member = Member(memberId, memberName, memberAge, memberMobile)
        }
        cursor.close()
        rdMember.close()
        return member
    }

    fun getMemberCount(): Int {
        val db = readableDatabase
        val query = "SELECT COUNT(*) FROM member"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()
        val count = cursor.getInt(0)
        cursor.close()
        db.close()
        return count
    }

}

data class Member(
    val id: Long?,
    val name: String,
    val age: Int,
    val mobile: String
)