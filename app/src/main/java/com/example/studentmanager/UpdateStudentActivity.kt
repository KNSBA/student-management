package com.example.studentmanager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class UpdateStudentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_student)

        val student = intent.getSerializableExtra("student") as Student
        val index = intent.getIntExtra("index", -1)

        val etName = findViewById<EditText>(R.id.etName)
        val etId = findViewById<EditText>(R.id.etId)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPhone = findViewById<EditText>(R.id.etPhone)
        val btnSave = findViewById<Button>(R.id.btnSave)

        etName.setText(student.name)
        etId.setText(student.mssv)
        etEmail.setText(student.email)
        etPhone.setText(student.phone)

        btnSave.setOnClickListener {
            val updatedStudent = Student(
                etName.text.toString(),
                etId.text.toString(),
                etEmail.text.toString(),
                etPhone.text.toString()
            )
            val intent = Intent()
            intent.putExtra("student", updatedStudent)
            intent.putExtra("index", index)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
