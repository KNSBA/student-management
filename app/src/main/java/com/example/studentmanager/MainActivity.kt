package com.example.studentmanager

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var studentAdapter: StudentAdapter
    private lateinit var dbHelper: StudentDatabaseHelper
    private val students = mutableListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = StudentDatabaseHelper(this)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        students.addAll(dbHelper.getAllStudents())

        studentAdapter = StudentAdapter(students, ::onStudentMenuClick)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = studentAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            val intent = Intent(this, AddStudentActivity::class.java)
            startActivityForResult(intent, 100)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK) {
            val student = data?.getSerializableExtra("student") as Student
            dbHelper.addStudent(student)
            students.add(0, student)
            studentAdapter.notifyItemInserted(0)

        } else if (requestCode == 101 && resultCode == RESULT_OK) {
            val index = data?.getIntExtra("index", -1) ?: -1
            val updatedStudent = data?.getSerializableExtra("student") as Student
            if (index in students.indices) {
                students[index] = updatedStudent
                studentAdapter.notifyItemChanged(index)
            }
        }
    }

    private fun onStudentMenuClick(view: View, position: Int) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_student, popup.menu)
        popup.setOnMenuItemClickListener {
            val student = students[position]
            when (it.itemId) {
                R.id.menu_update -> {
                    val intent = Intent(this, UpdateStudentActivity::class.java)
                    intent.putExtra("student", student)
                    intent.putExtra("index", position)
                    startActivityForResult(intent, 101)
                }
                R.id.menu_delete -> {
                    AlertDialog.Builder(this)
                        .setTitle("Xác nhận xóa")
                        .setMessage("Bạn có chắc muốn xóa sinh viên này?")
                        .setPositiveButton("Xóa") { _, _ ->
                            students.removeAt(position)
                            studentAdapter.notifyItemRemoved(position)
                        }
                        .setNegativeButton("Hủy", null)
                        .show()
                }
                R.id.menu_call -> {
                    val intent = Intent(Intent.ACTION_DIAL)
                    intent.data = Uri.parse("tel:${student.phone}")
                    startActivity(intent)
                }
                R.id.menu_email -> {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    intent.data = Uri.parse("mailto:${student.email}")
                    startActivity(intent)
                }
            }
            true
        }
        popup.show()
    }
}
